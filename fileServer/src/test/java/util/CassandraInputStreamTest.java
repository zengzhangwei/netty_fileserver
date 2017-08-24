package util;

import com.xinghai.fileServer.dao.cassandraDao.CassandraManager;
import com.xinghai.fileServer.fileHandle.fileUtil.CassandraInputStream;
import sun.security.util.Length;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;

/**
 * Created by scream on 2017/8/11.
 */
public class CassandraInputStreamTest {
    public static String newFilePath = "C:\\Users\\scream\\Desktop\\newStreamDst" + File.separator + "aaa";
    public static void main(String[] args) throws IOException {
        CassandraInputStream cassandraIS = new CassandraInputStream("1b2f94e7-a06f-4eea-93b9-439e2101ee5e",1);
        File file = new File(newFilePath);
        if(!file.exists()){
            file.createNewFile();
        }
        RandomAccessFile rf = new RandomAccessFile(file,"rw");
        long position = 0;
        while(cassandraIS.hasNextBlock()){
            System.out.println("position: " +position);
            byte[] data = cassandraIS.nextBlock();
            byte[] fewByte = new byte[10];
            cassandraIS.mark(3);
            cassandraIS.read(fewByte,0,fewByte.length);
            System.out.println("get few byte first time: " + new String(fewByte));
            cassandraIS.reset();
            cassandraIS.read(fewByte,0,fewByte.length);
            System.out.println("get few byte second time: " + new String(fewByte));
            System.out.println("avaiable byte: " + cassandraIS.available());
            int len = data.length;
            rf.seek(position);
            rf.write(data,0,len);
            position += len;
        }
        rf.close();
        CassandraManager.close();
    }
}
