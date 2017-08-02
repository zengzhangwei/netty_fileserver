package httpRequest;

import javax.net.ssl.*;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import util.TestConfiguration;

import java.util.concurrent.TimeUnit;

/**
 * 测试文件下载
 */
public class HttpDownloadTest {
    private static String filename = "download";
    private static String filepath = "C:\\Users\\scream\\Desktop\\fileDst\\" + filename;  //下载好的文件的保存目录
    private static String requestFileURI = "http://"+ TestConfiguration.HOST_IP+":8080/fileDownload?id=" + 47 + "&filePath=" + "/";
    //private static String requestFileURI = "http://"+ TestConfiguration.HOST_IP+":8080/download?uuid=" + uuid;

    private static final OkHttpClient client = new OkHttpClient();
    public static void main(String[] args) {

        try {
            SSLContext sslContext = SSLContext.getInstance("TLS");
            sslContext.init(null, new TrustManager[]{new MyX509TrustManager()}, new java.security.SecureRandom());
            client.setSslSocketFactory(sslContext.getSocketFactory());
        } catch (NoSuchAlgorithmException | KeyManagementException e) {
            e.printStackTrace();
        }

        client.setConnectTimeout(600, TimeUnit.SECONDS); //设置连接超时时间为600秒
        client.setWriteTimeout(600, TimeUnit.SECONDS);   //设置写入超时时间为600秒
        client.setReadTimeout(600, TimeUnit.SECONDS);    //设置读取超时时间为600秒
        client.setHostnameVerifier(new MyHostnameVerifier());
        HttpDownloadTest test = new HttpDownloadTest();
        try {
            test.download();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void download() throws IOException {
        Request requestGet = new  Request.Builder()
                .header("Connection", "Close")
                .url(requestFileURI)
                .build();

        System.out.println(requestGet.headers().toString());
        System.out.println(requestGet.toString());
        System.out.println("--------------------");

        Response response = client.newCall(requestGet).execute();
        if (!response.isSuccessful()) {
            String bodyStr = new String(response.body().bytes(),"utf-8");
            System.out.println(bodyStr);
            //throw new IOException("Unexpected code " + response);
        }else {
            //获取response头中的文件类型来设定后缀名
            String fileType = response.header("Content-Type");
            System.out.println(response.headers());
            InputStream inputStream = response.body().byteStream();
            FileOutputStream fileOutputStream = new FileOutputStream(filepath);

            int len;
            byte[] buffer = new byte[1024];
            while ((len = inputStream.read(buffer)) != -1) {
                fileOutputStream.write(buffer, 0, len);
            }
            inputStream.close();
            fileOutputStream.close();
            System.out.println("Output completed!");
        }
    }


    static class MyX509TrustManager implements X509TrustManager {

        @Override
        public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
            // Log.d(tag, "check client trusted. authType=" + authType);
        }

        @Override
        public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
            // Log.d(tag, "check servlet trusted. authType=" + authType);
        }

        @Override
        public X509Certificate[] getAcceptedIssuers() {
            // Log.d(tag, "get acceptedissuer");
            return null;
        }

    }

    static class MyHostnameVerifier implements HostnameVerifier {
        @Override
        public boolean verify(String hostname, SSLSession session) {
            return true;
        }
    }
}

