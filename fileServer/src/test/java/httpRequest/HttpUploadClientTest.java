package httpRequest;

import com.squareup.okhttp.*;
import util.TestConfiguration;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLSession;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * 测试文件上传
 */
public class HttpUploadClientTest implements Runnable {

    private static final OkHttpClient client = new OkHttpClient();
    private static final String IMGUR_CLIENT_ID = "...";

    //private static String localfilename = "C:\\Users\\scream\\Desktop\\fileSource\\test.txt";
   //private String localfilename = "C:\\Users\\scream\\Desktop\\只传路径接口设计.docx";
   // private String localfilename = "D:\\大数据Hadoop基础实战班\\培训视频.rar";
    // private String localfilename = "D:\\大数据Hadoop基础实战班\\data\\jd.data.500w.sql";
     private String localfilename = "C:\\Users\\scream\\Desktop\\gradle-4.0-all.zip";
   // private String localfilename = "C:\\Users\\scream\\Desktop\\logo.txt";
    //private String localfilename = "D:\\CODE\\LCMT Concept Third Edition - 副本 (2) - 副本 - 副本.pdf";
    //private String localfilename =  "D:\\CODE\\Sublime Text 2.0.1 x64 Setup.exe";
    //private String localfilename = "D:\\temp\\mgfileservice.log";
    // private String localfilename = "C:\\Users\\scream\\Desktop\\spring4.x企业应用开发实战\\精通Spring+4.x企业应用开发实战_陈雄华著_北京：电子工业出版社，2017.01.01_P820.pdf";
    //private String localfilename = "D:\\CODE\\testpackage.zip";
    //private String localfilename = "F:\\aspnet项目整理文件\\化院项目.rar";


    private String description = null;
    private String fileName = null;
    private String filePath = null;
    private String id = "5";

    public HttpUploadClientTest() {
    }

    public static void main(String[] args) throws IOException {
        HttpUploadClientTest tuc = new HttpUploadClientTest();
        List<String> fileNameList = new ArrayList<String>();
        fileNameList.add(tuc.localfilename);
        //tuc.sendFileInfo2(fileNameList,tuc.creatorID,batchid);
        //TestSendFileInfo.sendFileInfo(batchid);

        for (int i = 0; i < 1; i++) {
            new Thread(new HttpUploadClientTest()).start();
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void run() {
        client.setConnectTimeout(6, TimeUnit.SECONDS); //设置连接超时时间为600秒
        client.setWriteTimeout(600, TimeUnit.SECONDS);   //设置写入超时时间为600秒
        client.setReadTimeout(600, TimeUnit.SECONDS);    //设置读取超时时间为600秒
        client.setHostnameVerifier(new MyHostnameVerifier());
        try {
            upload();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void upload() throws IOException {
        final String FORM_ACTION = "http://" + TestConfiguration.HOST_IP + ":8080/fileImport";//文件上传路径url
        final File file = new File(localfilename);

        String fileName = file.getName();
        this.fileName = fileName;
        //description = "desc_" + fileName;     //设置description为文件名
        //filePath = localfilename;            //设置path为文件名
        filePath = "/";
        MediaType mediaType = MediaType.parse(contentType(file.getAbsolutePath()));
        RequestBody requestBody = new MultipartBuilder()
                .type(MultipartBuilder.FORM)
                .addPart(
                        Headers.of("Content-Disposition", "form-data; name=\"parentDirId\""),
                        RequestBody.create(null, "1"))
                .addPart(
                        Headers.of("Content-Disposition", "form-data; name=\"filePath\""),
                        RequestBody.create(null, filePath))
                .addPart(
                        Headers.of("Content-Disposition", "form-data; name=\"fileupload\";filename=\"" + fileName + "\""),
                        RequestBody.create(mediaType, file))
                .build();
        //System.out.println("batchID:" + batchid);
        Request request = new Request.Builder()
                .header("Authorization", "Client-ID " + IMGUR_CLIENT_ID)
               //.header("Connection", "Close")
                //.header("cookie","user_id="+creatorID)
                .header("Transfer-Encoding","chunked")
                .url(FORM_ACTION)
                .post(requestBody)
                .build();

        System.out.println(request.headers().toString());
        System.out.println(request.toString());
        System.out.println("--------------------");

        Response response = client.newCall(request).execute();
        if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);

        System.out.println(response.body().string());
    }

    //判断需要上传文件的类型
    private String contentType(String path) {
        if (path.endsWith(".png")) return "image/png";
        if (path.endsWith(".jpg")) return "image/jpeg";
        if (path.endsWith(".jpeg")) return "image/jpeg";
        if (path.endsWith(".gif")) return "image/gif";
        if (path.endsWith(".html")) return "text/html; charset=utf-8";
        if (path.endsWith(".txt")) return "text/plain; charset=utf-8";
        return "application/octet-stream";
    }

    static class MyHostnameVerifier implements HostnameVerifier {
        @Override
        public boolean verify(String hostname, SSLSession session) {
            return true;
        }

    }
}
