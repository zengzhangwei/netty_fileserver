package httpRequest;

import com.squareup.okhttp.*;

import java.io.File;
import java.io.IOException;

/**
 * Created by scream on 2017/7/14.
 */
public class MutiPart {
    private static final String IMGUR_CLIENT_ID = "...";
    private static final MediaType MEDIA_TYPE_PNG = MediaType.parse("image/png");

    private static final OkHttpClient client = new OkHttpClient();

    public static void main(String[] args) throws Exception {
        run();
    }

    public static void run() throws Exception {
        // Use the imgur image upload API as documented at https://api.imgur.com/endpoints/image
        RequestBody requestBody = new MultipartBuilder()
                .type(MultipartBuilder.FORM)
                .addPart(
                        Headers.of("Content-Disposition", "form-data; name=\"title\""),
                        RequestBody.create(null, "Square Logo"))
                .addPart(
                        Headers.of("Content-Disposition", "form-data; name=\"image\""),
                        //new File("\u202AC:\\Users\\scream\\Desktop\\logo.txt"))
                        RequestBody.create(MEDIA_TYPE_PNG, new File("/logo.txt")))
                .build();

        Request request = new Request.Builder()
                .header("Authorization", "Client-ID " + IMGUR_CLIENT_ID)
                .url("https://api.imgur.com/3/image")
                .post(requestBody)
                .build();
        System.out.println("the request is:" +request.body().toString());

       // Response response = client.newCall(request).execute();
       // if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);
       // System.out.println(response.body().string());
    }
}
