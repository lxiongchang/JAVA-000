package homework;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

import java.io.IOException;

/**
 * 参考gray
 */
public class HttpClientTest {

    public static void main(String[] args) throws IOException {
        OkHttpClient httpClient = new OkHttpClient();

        Request request = new Request.Builder().url("http://localhost:8088/api/hello")
                .get().build();

        try (Response response = httpClient.newCall(request).execute()) {
            ResponseBody body = response.body();
            assert body != null;
            if (response.isSuccessful()) {
                System.out.println("Success: " + body.string());
            } else {
                System.out.println("Error: " + response.code() + "." + body.string());
            }
        }
    }
}