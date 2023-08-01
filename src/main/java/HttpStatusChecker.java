import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import java.io.IOException;

public class HttpStatusChecker {
    private final OkHttpClient client = new OkHttpClient();

    public String getStatusImage(int code) throws IOException, HttpStatusNotFoundException {
        String url = "https://http.cat/" + code + ".jpg";
        Request request = new Request.Builder()
                .url(url)
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new HttpStatusNotFoundException("HTTP status code not found: " + response.code());
            }
            return url;
        }
    }


    public static void main(String[] args) {
        HttpStatusChecker checker = new HttpStatusChecker();

        try {
            String imageUrl = checker.getStatusImage(200);
            System.out.println("Status 200 Image URL: " + imageUrl);

            imageUrl = checker.getStatusImage(404);
            System.out.println("Status 404 Image URL: " + imageUrl);

            imageUrl = checker.getStatusImage(10000);
            System.out.println("Status 10000 Image URL: " + imageUrl);
        } catch (IOException e) {
            System.err.println("Error connecting to the server: " + e.getMessage());
        } catch (HttpStatusNotFoundException e) {
            System.err.println(e.getMessage());
        }
    }
}

class HttpStatusNotFoundException extends Exception {
    public HttpStatusNotFoundException(String message) {
        super(message);
    }
}