package com.xinghai.fileServer.FileHandle.impl;

import com.datastax.driver.core.Row;
import com.datastax.driver.core.Session;
import com.xinghai.fileServer.common.exception.FileServerErrorEnum;
import com.xinghai.fileServer.common.exception.FileServerException;
import com.xinghai.fileServer.dao.cassandraDao.CassandraManager;
import com.xinghai.fileServer.domain.BO.FileMetaBO;
import com.xinghai.fileServer.FileHandle.HandleFile;
import com.xinghai.fileServer.service.fileMetaService.FileMetaService;
import com.xinghai.fileServer.service.fileMetaService.impl.FileMetaServiceImpl;
import com.xinghai.fileServer.service.nettyInit.FileServerConfig;
import io.netty.buffer.ByteBuf;
import io.netty.handler.codec.http.multipart.InterfaceHttpData;
import io.netty.handler.codec.http.multipart.MixedFileUpload;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.util.Date;
import java.util.UUID;

/**
 * Created by scream on 2017/7/26.
 * cassandra存储处理文件
 */
public class CassandraHandleFileImpl implements HandleFile {
    @Override
    public void saveFile(FileMetaBO fileMetaBO, InterfaceHttpData data) {
        long startTime = new Date().getTime();
        MixedFileUpload curData = (MixedFileUpload) data;
        UUID chunkId = UUID.randomUUID();
        fileMetaBO.setFileId(chunkId.toString());//设置文件fileId
        FileServerConfig.logger.info("data length: " + curData.length() + " and the uuid is" + chunkId);
        try {
            //每次都拿10兆
            ByteBuf bytebuf = curData.getChunk(FileServerConfig.CASSANDRABLOCK);
           // FileServerConfig.logger.info("first get data length:" + bytebuf.readableBytes());
            long totalLength = curData.length();
            long currLength = 0;
            System.out.print("insert file chunks to db..");
            //从第一块开始插入
            int blockCount = 1;
            while (currLength < totalLength) {
                currLength += bytebuf.capacity();  //这里直接用capactiy即可，根据测试，获得的长度就是数据长度
                int start = bytebuf.readerIndex();
                int end = start;
                int end_target = bytebuf.readerIndex() + bytebuf.readableBytes();
                Session session = CassandraManager.getSession();
                while (end < end_target) {
                    System.out.print(".");
                    int currLen = Math.min(end_target - start, FileServerConfig.CHUNKSIZE);
                    end = end + currLen;
                    byte[] byteArr = new byte[currLen];
                    bytebuf.readBytes(byteArr, 0, currLen);
                    start += currLen;
                    ByteBuffer bb = ByteBuffer.wrap(byteArr, 0, byteArr.length);
                    // if (writer != null) writer.write(bb.array());
                    //FileServerConfig.logger.info("read the byte is :" + new String(byteArr));
                    String insertFileChunk = "insert into file.file_chunk (file_id , block_count , content) values(? , ? , ?)";
                    session.execute(insertFileChunk, chunkId, blockCount++, bb);
                }
                bytebuf.clear();
                bytebuf.release(); //释放
                bytebuf = curData.getChunk(FileServerConfig.CASSANDRABLOCK);
            }
            fileMetaBO.setBlockCount(--blockCount);
            //将meta信息存进mysql
            try {
                FileMetaService fms = new FileMetaServiceImpl();
                int result = fms.createFile(fileMetaBO);
            }catch (Exception e){
                FileServerConfig.logger.error(e.getMessage());
                //将Cassandra中数据清除
                deleteFileDataInCassandra(chunkId);
                throw new FileServerException(FileServerErrorEnum.FILE_META_INSERT_ERROR);
            }
            FileServerConfig.logger.info("\ninsert file chunks completed!");
            bytebuf.release();
            long endTime = new Date().getTime();
            FileServerConfig.logger.info("the total time:" + (endTime - startTime) * 1.0/1000);
        } catch (IOException e) {
            String exceptionMsg = "Get file chunk exception";
            FileServerConfig.logger.error(exceptionMsg + ": " + e.getMessage());
            throw new FileServerException(FileServerErrorEnum.INSERT_CHUNK_ERROR);
        }
    }

    /**
     * @param file
     * @return byte[] 返回文件内容数组,注意，对于小文件可调用此方法，大文件会导致内存溢出
     */
     @Override
    public  byte[] getFile(FileMetaBO file) {
        Session session = CassandraManager.getSession();
        if (file.getSize() > Integer.MAX_VALUE) { // 文件太大
            FileServerConfig.logger.info("File too large,can not download!! File ID：" + file.getId());
            throw new FileServerException(FileServerErrorEnum.FILE_TOO_LARGE);
        }
        byte[] dst = new byte[Integer.parseInt(String.valueOf(file.getSize()))];
        int offset = 0;
        System.err.print("Getting " + file.getBlockCount() + " file chunks from cassandra.....");
        for (int i = 1; i <= file.getBlockCount(); i++) {
            String query = "select content from file.file_chunk where file_id = " + file.getFileId().toString() + " and block_count = " + i;
            Row row = session.execute(query).one();
            if (row == null) {
                //String msg = String.format("id:%s not found in mgmeta.filechunks", file.chunkId.toString());
                throw new FileServerException(FileServerErrorEnum.READ_FILE_CHUNK_ERROR);
            }
            ByteBuffer bb = row.getBytes("content");
            int num = bb.remaining();
            bb.get(dst, offset, num);
            offset += num;
        }
        return dst;
    }

    // get data to RandomAccessFile(用于大文件的处理,大致思路为在服务端将数据组装为一个文件，再发送给客户端)
    public void getFile(FileMetaBO file, RandomAccessFile raf) {
        Session session = CassandraManager.getSession();
        String fileId = file.getFileId();
        //偏移量
        long position = 0;
        for (int i = 1; i <= file.getBlockCount(); i++) {
            String query = "select * from file.file_chunk where file_id = " + fileId + " and block_count = " + i;
            Row row = session.execute(query).one();
            if (row == null) {
                //String msg = String.format("id:%s seq:%s not found in mgmeta.filechunks", id.toString(), i);
                throw new FileServerException(FileServerErrorEnum.READ_FILE_CHUNK_ERROR);
            }
            ByteBuffer bb = row.getBytes("content");
            int seq = row.getInt("block_count");
            byte[] dst = new byte[bb.remaining()];
            bb.get(dst);
            FileServerConfig.logger.info("the block_count is :" + seq + " and the data length is :" + dst.length + "the position is :" + position);
            try {
                    raf.seek(position);
                    raf.write(dst, 0, dst.length);
            } catch (IOException e) {
                e.printStackTrace();
            }
            position = position + dst.length;
        }
    }

    @Override
    public void deleteFile(FileMetaBO file) {
        final Session session = CassandraManager.getSession();
        if (file.getFileId() != null) {
            String cql = "delete from file.file_chunk where file_id = ?";
            session.execute(cql, UUID.fromString(file.getFileId()));
            FileMetaService fms = new FileMetaServiceImpl();
            fms.deleteFileMetaById(file.getId());
        }else{
            throw new FileServerException(FileServerErrorEnum.FILE_NOT_EXIST);
        }
    }

    /**
     *
     * @param fileId 文件id
     *  只负责从cassandra中删除文件数据,并没有删除meta信息
     */
    private void deleteFileDataInCassandra(UUID fileId){
        final Session session = CassandraManager.getSession();
        if (fileId != null) {
            String cql = "delete from file.file_chunk where file_id = ?";
            session.execute(cql, fileId);
        }else{
            throw new FileServerException(FileServerErrorEnum.FILE_NOT_EXIST);
        }
    }
}
