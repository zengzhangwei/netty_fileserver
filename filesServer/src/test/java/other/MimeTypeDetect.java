package other;

import com.xinghai.fileServer.service.nettyInit.FileServerConfig;
import org.apache.tika.Tika;

import java.io.*;

/**
 * Created by scream on 2017/7/28.
 * 检测文件类型
 */
public class MimeTypeDetect {

    //private static String localfilename = "D:\\大数据Hadoop基础实战班\\data\\jd.data.500w.sql";
   // private static String localfilename = "C:\\Users\\scream\\Desktop\\图书资料\\REDIS 入门指南.pdf";
    //private static String localfilename = "D:\\大数据Hadoop基础实战班\\培训视频\\20170207 dashujujichuban 01.mp4";
    //private static String localfilename = "C:\\Users\\scream\\Desktop\\只传路径接口设计.docx";
    private static String localfilename = "C:\\Users\\scream\\Desktop\\fileSource\\test.txt";
    public static void main(String[] args) throws IOException {
        File file = new File(localfilename);
        System.out.println("file length:" + file.length());
        Tika tika = new Tika();
        //detecting the file type using detect method
        //String filetype = tika.detect(file);
        InputStream is = new FileInputStream(file);
        long len = Math.min(file.length(),(long)FileServerConfig.CHUNKSIZE);
        System.out.println("detect length is :" + len);
        byte[] detectContent = new byte[(int)len];
        is.read(detectContent);
        String filetype = tika.detect(detectContent);
        System.out.println(filetype);
    }
}
