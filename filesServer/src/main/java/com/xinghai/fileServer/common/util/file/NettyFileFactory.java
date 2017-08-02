//package com.xinghai.fileServer.common.util.file;
//
//import com.datastax.driver.core.*;
//import com.xinghai.fileServer.service.nettyInit.FileServerConfig;
//import io.netty.buffer.ByteBuf;
//import io.netty.handler.codec.http.multipart.FileUpload;
//import io.netty.handler.codec.http.multipart.InterfaceHttpData;
//import io.netty.handler.codec.http.multipart.MixedFileUpload;
//
//import java.io.File;
//import java.io.FileOutputStream;
//import java.io.IOException;
//import java.io.RandomAccessFile;
//import java.nio.ByteBuffer;
//import java.util.LinkedList;
//import java.util.List;
//import java.util.UUID;
//
//
//public class NettyFileFactory {
//
//    private static int CASS_CHUNKSIZE = FileServerConfig.CHUNKSIZE;
//
//    public static void storeFile(InterfaceHttpData data , FileInfo file) {
//        FileUpload curData = ((MixedFileUpload) data).copy();
//        Configuration.logger.info("data length:" + curData.length()+"\tcontent type:"+curData.getContentType()+"\tin memory:"+curData.isInMemory());
//        UUID chunkId = UUID.randomUUID();
//        file.setChunkId(chunkId.toString());
//        try {
//            ByteBuf bytebuf = curData.getChunk(10 * CASS_CHUNKSIZE);
//            long totalLength = curData.length();
//            long currLength = 0;
//            System.out.print("insert file chunks to db..");
//            file.setSize(totalLength);
//            int blockCount = 0;
//            while (currLength < totalLength) {
//                currLength += bytebuf.capacity();  //这里直接用capactiy即可，根据测试，获得的长度就是数据长度
//
//                int start = bytebuf.readerIndex();
//                int end = start;
//                int end_target = bytebuf.readerIndex() + bytebuf.readableBytes();
//                Session session = DBManager.getSession();
//                while (end < end_target) {
//                    System.out.print(".");
//                    int currLen = Math.min(end_target - start, CASS_CHUNKSIZE);
//                    end = end + currLen;
//                    //HttpFileServerHandler.logger.info("readableBytes:\t" + bytebuf.readableBytes()
//                    //          + "\tstart:\t" + start + "\tend:\t" + end + "\tcurrLen:\t" + currLen);
//
//                    byte[] byteArr = new byte[currLen];
//                    bytebuf.readBytes(byteArr, 0, currLen);
//
//                    start += currLen;
//                    ByteBuffer bb = ByteBuffer.wrap(byteArr, 0, byteArr.length);
//
//                    String insertFileChunk = "insert into mgmeta.filechunks (id , seq , data) values(? , ? , ?)";
//                    session.execute(insertFileChunk, chunkId, blockCount++, bb);
//                }
//
//                bytebuf.clear();
//                bytebuf.release(); //释放
//                bytebuf = curData.getChunk(10 * CASS_CHUNKSIZE);
//            }
//            file.setBlockCount(blockCount);
//            Configuration.logger.info("\ninsert file chunks completed!");
//            bytebuf.release();
//        } catch (IOException e) {
//            String exceptionMsg = "Get file chunk exception";
//            Configuration.logger.error(exceptionMsg+": "+e.getMessage());
//            throw new FileServerException(exceptionMsg);
//        }
//    }
//
//    /**
//     * 将data 保存到 cassandra中，如果 tmpFile 不为null 将再存在本地磁盘中一份
//     * @param data
//     * @param file
//     * @param tmpFile
//     */
//    public static void storeFile(InterfaceHttpData data, MGFile file, File tmpFile) {
//        MixedFileUpload curData = (MixedFileUpload) data;
//        Configuration.logger.info("data length:" + curData.length() + "\tcontent type:" + curData.getContentType() + "\tin memory:" + curData.isInMemory());
//        file.chunkId = UUID.randomUUID();
//
//        try {
//            FileOutputStream writer = null;
//            if (tmpFile != null) {
//                writer = new FileOutputStream(tmpFile);
//            }
//            //ByteBuf bytebuf = curData.getByteBuf();
//            ByteBuf bytebuf = curData.getChunk(10 * CASS_CHUNKSIZE);
//            long totalLength = curData.length();
//            long currLength = 0;
//            file.size = totalLength; //设置文件大小
//            System.out.print("insert file chunks to db..");
//            while (currLength < totalLength) {
//                currLength += bytebuf.capacity();  //这里直接用capactiy即可，根据测试，获得的长度就是数据长度
//
//                int start = bytebuf.readerIndex();
//                int end = start;
//                int end_target = bytebuf.readerIndex() + bytebuf.readableBytes();
//                Session session = DBManager.getSession();
//                while (end < end_target) {
//                    System.out.print(".");
//                    int currLen = Math.min(end_target - start, CASS_CHUNKSIZE);
//                    end = end + currLen;
//                    //HttpFileServerHandler.logger.info("readableBytes:\t" + bytebuf.readableBytes()
//                    //          + "\tstart:\t" + start + "\tend:\t" + end + "\tcurrLen:\t" + currLen);
//
//                    byte[] byteArr = new byte[currLen];
//                    bytebuf.readBytes(byteArr, 0, currLen);
//
//                    start += currLen;
//                    ByteBuffer bb = ByteBuffer.wrap(byteArr, 0, byteArr.length);
//
//                    if (writer != null) writer.write(bb.array());
//
//                    String insertFileChunk = "insert into mgmeta.filechunks (id , seq , data) values(? , ? , ?)";
//                    session.execute(insertFileChunk, file.chunkId, file.blockCount++, bb);
//                }
//
//                bytebuf.clear();
//                bytebuf.release(); //释放
//                bytebuf = curData.getChunk(10 * CASS_CHUNKSIZE);
//            }
//            if (writer != null) writer.close();
//            Configuration.logger.info("\ninsert file chunks completed!");
//            bytebuf.release();
//
//            MGFileFactory.insertMeta(file);
//            //insertMetaWithoutObject(file, session); //在文件块都存到数据库中之后，将文件信息插入file表中
//            //insertUserFile(file, session); //在文件处理完之后，插入文件权限表
//            //attachFileToObject(file, session);
//
//        } catch (IOException e) {
//            String exceptionMsg = "Get file chunk exception";
//            Configuration.logger.error(exceptionMsg + ": " + e.getMessage());
//            throw new FileServerException(exceptionMsg);
//        } catch (IDNotFoundException e) {
//            String exceptionMsg = "Insert file meta exception";
//            Configuration.logger.error(exceptionMsg + ": " + e.getMessage());
//            throw new FileServerException(exceptionMsg);
//        }
//    }
//
//    // insert data
//    public static void insertMetaWithoutObject(MGFile file) throws IDNotFoundException {
//        Session session = DBManager.getSession();
//        String cql = "insert into mgmeta.file (id, chunkid, fileobject, created_on, created_by, label, description, filename, path, filetype, category, block_count, size, hash, content) values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
//        session.execute(cql, file.id, file.chunkId, file.fileObject, file.createdOn,
//                file.createdBy, file.label, file.description,
//                file.fileName, file.path, file.fileType, file.category, file.blockCount,
//                file.size, file.hash, file.content);
//    }
//
//
/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//
//
//    // 判断用户请求的文件是否在file表内
//    public static boolean isInUserFile(int user_id, UUID id) {
//        Session session = DBManager.getSession();
//        String query = "select id from mgmeta.file where created_by = " + user_id + " and id = " + id.toString();
//        Row row = session.execute(query).one();
//        if (row == null) {
//            return false;
//        } else {
//            return true;
//        }
//    }
//
//    // 判断数据库中是否存在相同的文件
//    public static Object[] findSameFile(String md5str, String filename){
//    	Session session = DBManager.getSession();
//        String query = "select hash, filename,id, chunkid ,created_by,size from mgmeta.file where hash = '" + md5str
//                + "' and filename = '"+filename+"'   ALLOW FILTERING ;" ;
//        Row row = session.execute(query).one();
//        if(row == null){
//            return null;
//        } else {
//            Object[] uuid_arr = new Object[3];
//            uuid_arr[0] = row.getUUID("id");
//            uuid_arr[1] = row.getUUID("chunkid");
//            uuid_arr[2] = row.getLong("size");
//            return uuid_arr;
//        }
//    }
//
//    //--------------------------------------------------插入信息--------------------------------------------------------
//
//    // 第一个请求，为文件状态表赋值
//    public static void insertUploadStatus_false(int user_id, UUID batch_id, List<String> filenameList, List<String> filepathList, List<Long> filesizeList, List<Long> timeStampList, String remark) {
//        Session session = DBManager.getSession();
//        String cql = "insert into mgconf.uploadstatus_fail (user_id,batch_id,filename,filepath,submit_time,file_size, remark) values (?,?,?,?,?,?,?)";
//        BatchStatement bStatement = new BatchStatement();
//        int fileNum = filenameList.size();
//        for (int idx = 0; idx < fileNum; idx++) {
//            String filename = filenameList.get(idx);
//            String filepath = filepathList.get(idx);
//            long fileSize = filesizeList.get(idx);
//            long timeStamp = timeStampList.get(idx);
//
//            Configuration.logger.info("insert file info user_id: " + user_id + "\tfilename:" + filename);
//            Statement insertFileInfoStatement = new SimpleStatement(cql, user_id, batch_id, filename, filepath, timeStamp, fileSize, remark);
//            bStatement.add(insertFileInfoStatement);
//        }
//        session.execute(bStatement);
//    }
//
//    //插入batch列表
//    public static void insertBathInfo(int user_id, UUID batch_id, String user_name, String user_ip, boolean status, long sunmit_time) {
//        Session session = DBManager.getSession();
//        String query = "select user_name from mgconf.batch_info where batch_id = " + batch_id + " and status = " + status + " ALLOW FILTERING";
//        ResultSet resultSet = session.execute(query);
//        Row row = resultSet.one();
//        if (row != null) return;
//        String cql = "insert into mgconf.batch_info (user_id,batch_id,user_name,user_ip,status,submit_time) values (?,?,?,?,?,?)";
//        session.execute(cql, user_id, batch_id, user_name, user_ip, status, sunmit_time);
//    }
//
//    // 修改文件状态(上传成功的文件的状态)
//    public static void changeFileStatus(NettyFileStatus fileStatus) {
//        Session session = DBManager.getSession();
//
//        //如果不存在 成功batch（status 为true） 则插入
//        String cql0 = "select batch_id from mgconf.batch_info where user_id = ? and batch_id= ? and status = true limit 1";
//        ResultSet resultSet0 = session.execute(cql0, fileStatus.user_id, fileStatus.batch_id);
//        if (resultSet0.one() == null) {
//            String query = "select * from mgconf.batch_info where batch_id = " + fileStatus.batch_id + " and status = false limit 1";
//            ResultSet resultSet = session.execute(query);
//            Row row = resultSet.one();
//            String cql = "insert into mgconf.batch_info (user_id,batch_id,user_name,user_ip,status,submit_time) values (?,?,?,?,?,?)";
//            session.execute(cql, row.getInt("user_id"), row.getUUID("batch_id"), row.getString("user_name"), row.getString("user_ip"), true, row.getLong("submit_time"));
//        }
//
//        String cql1 = "delete from mgconf.uploadstatus_fail where user_id = ? and filepath = ? and filename = ? and batch_id = ?";
//        String cql2 = "insert into mgconf.uploadstatus_success (user_id,batch_id,id,filename,submit_time,complete_time,file_size,filepath) values(?,?,?,?,?,?,?,?)";
//        session.execute(cql1, fileStatus.user_id, fileStatus.path, fileStatus.filename, fileStatus.batch_id);
//        session.execute(cql2, fileStatus.user_id, fileStatus.batch_id, fileStatus.id, fileStatus.filename,
//                fileStatus.submit_time, fileStatus.complete_time, fileStatus.file_size, fileStatus.path);
//
//        //如果该批次的文件都上传成功，则删除 batch_info status 为 false 的记录
//        String cql3 = "select batch_id from mgconf.uploadstatus_fail where user_id = ? and batch_id= ? limit 1";
//        ResultSet resultSet = session.execute(cql3, fileStatus.user_id, fileStatus.batch_id);
//        if (resultSet.one() == null) {
//            String cql4 = "delete from mgconf.batch_info where user_id = ? and batch_id= ? and status =false";
//            session.execute(cql4, fileStatus.user_id, fileStatus.batch_id);
//        }
//    }
//
//    //更新uploadstatus_fail表的remark
//    public static void updateStatusFailRemark(String remark, NettyFileStatus nettyFileStatus) {
//        Session session = DBManager.getSession();
//        String cql = "insert into mgconf.uploadstatus_fail(batch_id,user_id,filepath,filename,file_size,remark,submit_time) values(?,?,?,?,?,?,?)";
//        session.execute(cql, nettyFileStatus.batch_id, nettyFileStatus.user_id, nettyFileStatus.path,
//                nettyFileStatus.filename, nettyFileStatus.file_size, remark, nettyFileStatus.submit_time);
//    }
//
//    //---------------------------------------------获取信息-------------------------------------------------------------
//
//    //获取指定用户ID指定状态的所有batch信息
//    public static List<BatchInfo> getBatchinfo(boolean status, int user_id) {
//        Session session = DBManager.getSession();
//        List<BatchInfo> binfoList = new LinkedList<BatchInfo>();
//        String query = "select batch_id,user_name,user_ip,status,submit_time from mgconf.batch_info where user_id = " + user_id + " and status = " + status;
//        ResultSet resultSet = session.execute(query);
//        List<Row> resultList = resultSet.all();
//        for (Row row : resultList) {
//            UUID batch_id = row.getUUID("batch_id");
//            String user_name = row.getString("user_name");
//            String user_ip = row.getString("user_ip");
//            long time_stamp = row.getLong("submit_time");
//            BatchInfo bthInfo = new BatchInfo(user_id, batch_id, user_name, user_ip, time_stamp);
//            binfoList.add(bthInfo);
//        }
//        return binfoList;
//    }
//
//    //获取某一批上传成功的所有文件信息
//    public static List<NettyFileStatus> getSuccessBatchFileInfo(UUID batch_id) {
//        Session session = DBManager.getSession();
//        List<NettyFileStatus> fileInfoList = new LinkedList<NettyFileStatus>();
//        String query = "select filepath,filename,file_size,complete_time,id from mgconf.uploadstatus_success where batch_id = " + batch_id;
//        ResultSet resultSet = session.execute(query);
//        List<Row> resultList = resultSet.all();
//        for (Row row : resultList) {
//            String path = row.getString("filepath");
//            String filename = row.getString("filename");
//            long filesize = row.getLong("file_size");
//            long date = row.getLong("complete_time");
//            UUID id = row.getUUID("id"); //文件ID
//
//            NettyFileStatus nfs = new NettyFileStatus(path, filename, filesize, date, id);
//            fileInfoList.add(nfs);
//        }
//        return fileInfoList;
//    }
//
//    //获取某一批上传失败的所有文件信息
//    public static List<NettyFileStatus> getFailBatchFileInfo(UUID batch_id) {
//        Session session = DBManager.getSession();
//        List<NettyFileStatus> fileInfoList = new LinkedList<NettyFileStatus>();
//        String query = "select filepath,filename,file_size,submit_time,remark from mgconf.uploadstatus_fail where batch_id = " + batch_id;
//        ResultSet resultSet = session.execute(query);
//        List<Row> resultList = resultSet.all();
//        for (Row row : resultList) {
//            String path = row.getString("filepath");
//            String filename = row.getString("filename");
//            long filesize = row.getLong("file_size");
//            long date = row.getLong("submit_time");
//            String remark = row.getString("remark");
//
//            NettyFileStatus nfs = new NettyFileStatus(path, filename, filesize, date, remark);
//            fileInfoList.add(nfs);
//        }
//        return fileInfoList;
//    }
//
//    //--------------------------------------------删除记录--------------------------------------------------------------
//
//    //删除指定批次指定状态(上传成功/失败)的信息
//    public static void deleteBatch(UUID batch_id, int user_id, boolean status) {
//        Session session = DBManager.getSession();
//        String cql = "delete from mgconf.batch_info where user_id =" + user_id + " and batch_id = " + batch_id + " and status = " + status;
//        session.execute(cql);
//        if (status == true) {
//            String cql2 = "delete from mgconf.uploadstatus_success where user_id =" + user_id + " and batch_id = " + batch_id;
//            session.execute(cql2);
//        } else {
//            String cql2 = "delete from mgconf.uploadstatus_fail where user_id =" + user_id + " and batch_id = " + batch_id;
//            session.execute(cql2);
//        }
//    }
//
//
//    //删除指定的文件上传成功记录
//    public static void deleteUpLoadStatusSuccess(UUID batch_id, int user_id, UUID fileid) {
//        Session session = DBManager.getSession();
//        String cql = "delete from mgconf.uploadstatus_success where user_id = " + user_id + " and batch_id = " + batch_id + " and id = " + fileid;
//        session.execute(cql);
//    }
//
//    //删除指定的文件上传失败记录
//    public static void deleteUpLoadStatusFail(UUID batch_id, int user_id, String filepath, String filename) {
//        Session session = DBManager.getSession();
//        String cql = "delete from mgconf.uploadstatus_fail where user_id = " + user_id + " and filepath = '" + filepath + "' and batch_id= " + batch_id + "and filename = '" + filename + "'";
//
//        session.execute(cql);
//    }
//
//    /**
//     * delete file bytes in cassandra
//     * @param file
//     */
//    public static void deleteFile(FileInfo file) {
//        final Session session = DBManager.getSession();
//        if (file.getChunkId() != null) {
//            String cql = "delete from mgmeta.filechunks where id = ?";
//            session.execute(cql, UUID.fromString(file.getChunkId()));
//        }
//    }
//
//    //--------------------------------------------获取文件--------------------------------------------------------------
//
//    public static byte[] getFile(FileInfo file) throws IDNotFoundException {
//        Session session = DBManager.getSession();
//        if(file.getSize() > Integer.MAX_VALUE){ // 文件太大
//            Configuration.logger.info("File too large,can not download!! File ID："+file.getId());
//            return new byte[0];
//        }
//        byte[] dst = new byte[file.getSize().intValue()];
//        int offset = 0;
//
//        int blockCount = file.getBlockCount();
//        System.err.print("Getting "+file.getBlockCount()+" file chunks from cassandra.....");
//        for (int i = 0; i < blockCount; i++) {
//            String query = "select data from mgmeta.filechunks where id = " + file.getChunkId() + " and seq = " + i;
//            Row row = session.execute(query).one();
//            if (row == null) {
//                String msg = String.format("id:%s not found in mgmeta.filechunks", file.getChunkId());
//                throw new IDNotFoundException(msg);
//            }
//
//            //System.out.print(".");
//            ByteBuffer bb = row.getBytes("data");
//            int num = bb.remaining();
//            bb.get(dst, offset, num);
//            offset += num;
//        }
//        return dst;
//    }
//
//    // get data to byte array
//    public static byte[] getFile(MGFile file) throws IDNotFoundException {
//
//        Session session = DBManager.getSession();
//        if (file.size > Integer.MAX_VALUE) { // 文件太大
//            Configuration.logger.info("File too large,can not download!! File ID：" + file.getId());
//            return new byte[0];
//        }
//        byte[] dst = new byte[(int) file.size];
//        int offset = 0;
//        System.err.print("Getting " + file.blockCount + " file chunks from cassandra.....");
//        for (int i = 0; i < file.blockCount; i++) {
//            String query = "select data from mgmeta.filechunks where id = " + file.chunkId.toString() + " and seq = " + i;
//            Row row = session.execute(query).one();
//            if (row == null) {
//                String msg = String.format("id:%s not found in mgmeta.filechunks", file.chunkId.toString());
//                throw new IDNotFoundException(msg);
//            }
//
//            //System.out.print(".");
//            ByteBuffer bb = row.getBytes("data");
//            int num = bb.remaining();
//            bb.get(dst, offset, num);
//            offset += num;
//        }
//        return dst;
//    }
//
//    // get data to RandomAccessFile(用于大文件的处理，备用)
//    public static MGFile getFile(UUID id, RandomAccessFile raf) throws IOException, IDNotFoundException {
//        Session session = DBManager.getSession();
//        MGFile file = MGFileFactory.getMeta(id);
//        for (int i = 0; i < file.blockCount; i++) {
//            String query = "select seq,data from mgmeta.filechunks where id = " + id.toString() + " and seq = " + i;
//            Row row = session.execute(query).one();
//            if (row == null) {
//                String msg = String.format("id:%s seq:%s not found in mgmeta.filechunks", id.toString(), i);
//                Configuration.logger.info(msg);
//            }
//
//            ByteBuffer bb = row.getBytes("data");
//            int seq = row.getInt("seq");
//            byte[] dst = new byte[bb.remaining()];
//            bb.get(dst);
//            raf.seek(seq * Configuration.CHUNKSIZE);
//            raf.write(dst, 0, Configuration.CHUNKSIZE);
//        }
//        return file;
//    }
//
//    // 指定范围对文件获取
//    public static byte[] getFileChunk(UUID id, long startDataLoc, long endDataLoc) throws IDNotFoundException {
//        Session session = DBManager.getSession();
//        int stSeq = (int) (startDataLoc / Configuration.CHUNKSIZE);
//        int edSeq = (int) (endDataLoc / Configuration.CHUNKSIZE);
//        byte[] dst = new byte[(int) (endDataLoc - startDataLoc + 1)];
//        int offset = 0;
//
//        // 第一个file chunk要单独处理
//        int seq = stSeq;
//        String query = "select data from mgmeta.filechunks where id = " + id.toString() + " and seq = " + seq;
//        Row firstRow = session.execute(query).one();
//        if (firstRow == null) {
//            String msg = String.format("id:%s and seq:%s not found in mgmeta.filechunks", id.toString(), seq);
//            throw new IDNotFoundException(msg);
//        }
//        ByteBuffer firstbb = firstRow.getBytes("data");
//        firstbb.position((int) (startDataLoc - stSeq * Configuration.CHUNKSIZE));
//        firstbb.limit(Math.min(Configuration.CHUNKSIZE, (int) (endDataLoc - stSeq * Configuration.CHUNKSIZE + 1)));
//        int firstnum = firstbb.remaining();
//        Configuration.logger.info("num:" + firstnum + "\toffset :" + offset + "");
//        firstbb.get(dst, offset, firstnum);
//        offset += firstnum;
//        System.err.println("Getting Chunk " + seq + " completed!");
//        seq++;
//
//
//        // 除第1个和最后一个file chunk，都是完整的
//        for (; seq < edSeq; seq++) {
//            query = "select data from mgmeta.filechunks where id = " + id.toString() + " and seq = " + seq;
//            Row row = session.execute(query).one();
//            if (row == null) {
//                String msg = String.format("id:%s not found in mgmeta.filechunks", id.toString());
//                throw new IDNotFoundException(msg);
//            }
//
//            ByteBuffer bb = row.getBytes("data");
//
//            int num = bb.remaining();
//            Configuration.logger.info("num:" + num + "\toffset :" + offset + "");
//            bb.get(dst, offset, num);
//            offset += num;
//            System.err.println("Getting Chunk " + seq + " completed!");
//        }
//
//        if (seq > edSeq) {
//            return dst;
//        }
//
//        // 最后一个file chunk要单独处理
//        seq = edSeq;
//        query = "select data from mgmeta.filechunks where id = " + id.toString() + " and seq = " + seq;
//        Row lastRow = session.execute(query).one();
//        if (lastRow == null) {
//            String msg = String.format("id:%s not found in mgmeta.filechunks", id.toString());
//            throw new IDNotFoundException(msg);
//        }
//        ByteBuffer lastbb = lastRow.getBytes("data");
//        lastbb.position(Math.max(0, (int) (startDataLoc - edSeq * Configuration.CHUNKSIZE)));
//        lastbb.limit((int) (endDataLoc - edSeq * Configuration.CHUNKSIZE + 1));
//        int lastnum = lastbb.remaining();
//        Configuration.logger.info("num:" + lastnum + "\toffset :" + offset + "");
//        lastbb.get(dst, offset, lastnum);
//        Configuration.logger.info("Getting Chunk " + seq + " completed!");
//
//        return dst;
//    }
//
//}
