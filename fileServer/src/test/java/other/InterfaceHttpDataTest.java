package other;

import io.netty.handler.codec.http.multipart.HttpData;
import io.netty.handler.codec.http.multipart.InterfaceHttpData;
import io.netty.handler.codec.http.multipart.MixedFileUpload;

import java.nio.charset.Charset;

/**
 * Created by scream on 2017/7/31.
 */
public class InterfaceHttpDataTest {
    private static String localfilename = "C:\\Users\\scream\\Desktop\\只传路径接口设计.docx";
    public static void main(String[] args){
        MixedFileUpload data = new MixedFileUpload("Hello world", localfilename, "application/octet-stream",null, Charset.defaultCharset(), 20, 10);
        data.getName();
        data.getHttpDataType();
    }
}
