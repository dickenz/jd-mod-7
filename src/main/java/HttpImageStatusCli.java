import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class HttpImageStatusCli {
    private static final String INVALID_NUMBER_MESSAGE = "Please enter a valid number";

    public static void main(String[] args) {
        HttpImageStatusCli cli = new HttpImageStatusCli();
        cli.askStatus();
    }

    public void askStatus() {
        System.out.println("Enter HTTP status code:");
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(System.in))) {
            String input = reader.readLine().trim();
            try {
                int statusCode = Integer.parseInt(input);
                downloadImageForStatus(statusCode);
            } catch (NumberFormatException e) {
                System.out.println(INVALID_NUMBER_MESSAGE);
            }
        } catch (IOException e) {
            System.err.println("Error reading input: " + e.getMessage());
        }
    }

    private void downloadImageForStatus(int statusCode) {
        HttpStatusImageDownloader downloader = new HttpStatusImageDownloader();
        try {
            downloader.downloadStatusImage(statusCode);
        } catch (IOException e) {
            System.err.println("Error downloading image: " + e.getMessage());
        } catch (HttpStatusNotFoundException e) {
            System.out.println("There is no image for HTTP status " + statusCode);
        }
    }
}
