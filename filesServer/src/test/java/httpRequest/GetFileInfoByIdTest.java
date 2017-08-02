package httpRequest;

import java.io.IOException;
import com.squareup.okhttp.*;
import org.json.JSONException;
import util.JSONGenerate;

/**
 * Created by scream on 2017/7/13.
 */
public class GetFileInfoByIdTest {
    private static final OkHttpClient client = new OkHttpClient();
    public static void main(String[] args) throws Exception {
        getFileInfoById();
    }
    //通过id获取文件meta信息
    public static void getFileInfoById() throws IOException, JSONException {
        MediaType JSON = MediaType.parse("application/json; charset=utf-8");
        final String FORM_ACTION = "http://127.0.0.1:8080/getFileMetaById";//文件info发送url
        //构造请求json参数
        String json = JSONGenerate.getFileMetaById();
        System.out.println("get the root json:" + json );
        RequestBody requestBody = RequestBody.create(JSON, json);
        //创建一个请求对象
        Request request = new Request.Builder()
                .url(FORM_ACTION)
                .post(requestBody)
                .build();
         Response response = client.newCall(request).execute();
         if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);
         System.out.println(response.body().string());
         System.out.println("长度：" + request.body().contentLength());

       // Request request = new  Request.Builder().url("https://api.github.com/markdown/raw").post(RequestBody.create(MEDIA_TYPE_MARKDOWN, file)).build();
       // Response response = client.newCall(request).execute();
       // if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);
        // System.out.println(response.body().string());

    }

}
