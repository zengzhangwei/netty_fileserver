package common.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.nio.ByteBuffer;

/**
 * Created by scream on 2017/7/20.
 */
public class FileInputStreamTest {
    public static void main(String[] args) throws FileNotFoundException {
       byte[] bytes = "hello world".getBytes();
       System.out.println("string bytes length :"+ bytes.length);
        ByteBuffer buf = ByteBuffer.wrap(bytes,2,3);
        bytes[2] = '2';
        System.out.println("the offser is :" + buf.arrayOffset()+ " and the data is :" + new String(buf.array()) );
        System.out.println("the length :"+buf.array().length);
    }
}
