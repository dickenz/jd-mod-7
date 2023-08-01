import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import java.io.File;
import java.io.*;

public class HttpStatusImageDownloader {
    private final OkHttpClient client = new OkHttpClient();

    public void downloadStatusImage(int code) throws IOException, HttpStatusNotFoundException {
        HttpStatusChecker checker = new HttpStatusChecker();
        String imageUrl = checker.getStatusImage(code);

        Request request = new Request.Builder()
                .url(imageUrl)
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new IOException("Failed to download image: " + response);
            }

            String fileName = "http_cat_" + code + ".jpg";
            File outputFile = new File(fileName);

            try (OutputStream os = new FileOutputStream(outputFile)) {
                assert response.body() != null;
                os.write(response.body().bytes());
            }
            System.out.println("Image downloaded and saved to: " + outputFile.getAbsolutePath());
        }
    }

    public static void main(String[] args) {
        HttpStatusImageDownloader downloader = new HttpStatusImageDownloader();

        try {
            downloader.downloadStatusImage(200);
            downloader.downloadStatusImage(404);

            downloader.downloadStatusImage(10000);
        } catch (IOException e) {
            System.err.println("Error downloading image: " + e.getMessage());
        } catch (HttpStatusNotFoundException e) {
            System.err.println("HTTP status code not found: " + e.getMessage());
        }
    }
}

