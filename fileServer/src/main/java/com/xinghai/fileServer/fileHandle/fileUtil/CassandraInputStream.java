package com.xinghai.fileServer.fileHandle.fileUtil;

import com.datastax.driver.core.Row;
import com.datastax.driver.core.Session;
import com.xinghai.fileServer.common.exception.FileServerErrorEnum;
import com.xinghai.fileServer.common.exception.FileServerException;
import com.xinghai.fileServer.config.FileServerConfig;
import com.xinghai.fileServer.dao.cassandraDao.CassandraManager;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;

/**
 * Created by scream on 2017/8/4.
 * 从cassandra中一块一块拿数据
 */
public class CassandraInputStream extends InputStream {
    private byte[] dataContent;
    private int position;
    private int blockCount;
    private int length;
    private int mark;
    private String fileId;

    public CassandraInputStream(String fileId,int blockConut){
        this.fileId = fileId;
        this.blockCount = blockConut;
    }

    public boolean hasNextBlock(){
        return blockCount > 0;
    }

    /**
     *读取一块数据
     * @return byte[] 返回一块数据
     */
    public byte[] nextBlock(){
        if(blockCount <= 0){
            throw new FileServerException(FileServerErrorEnum.CAN_NOT_GET_MORE_BLOCK);
        }
        Session session = CassandraManager.getSession();
        String query = "select * from file.file_chunk where file_id = " + fileId + " and block_count = " + blockCount;
        Row row = session.execute(query).one();
        if (row == null) {
            throw new FileServerException(FileServerErrorEnum.READ_FILE_CHUNK_ERROR);
        }
        ByteBuffer bb = row.getBytes("content");
//        System.out.println("limit: " + bb.limit());
//        System.out.println("capacity: " + bb.capacity());
//        System.out.println("position: " + bb.position());
//        System.out.println("remaining: " + bb.remaining());
        blockCount -- ;
        dataContent = new byte[bb.remaining()];
        bb.get(dataContent);
        length = dataContent.length;
        position = 0;
        FileServerConfig.logger.info("get the byte to string: " + new String(dataContent));
        return dataContent;
    }


    @Override
    public int read() throws IOException {
        int current = position;
        if(current <= length){
            position ++;
        }else{
            return -1;
        }
        return dataContent[current];
    }

    @Override
    public int available(){
        return  length - position;
    }

    @Override
    public synchronized void  mark(int readlimit) {
        this.mark = readlimit;
    }

    @Override
    public void reset() throws IOException {
        if (this.mark<0 || this.mark >= length) {
            throw new IOException("标识不对");
        }
        //指针重新指到mark位置，让流可以重新读取
        position = mark;
    }

}
