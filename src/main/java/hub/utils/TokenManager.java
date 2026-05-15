package hub.utils;

import org.example.Main;

import javax.swing.*;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.file.Files;
import java.nio.file.Path;

public class TokenManager {
    private static final String TOKEN_FILE = "token.txt";

    public static String getToken() {
        if (!hasToken()) {
            return null; // Или пустую строку "", если вам так удобнее
        }

        try {
            return Files.readString(Path.of(TOKEN_FILE)).trim();
        } catch (IOException e) {
            System.err.println("Ошибка при чтении токена!");
            e.printStackTrace();
            return null;
        }
    }

    public static boolean hasToken() {
        File f = new File(TOKEN_FILE);
        return f.exists() && f.length() > 0;
    }

    public static String registration(String login, String password) {
        try {
            String jsonBody = String.format("{\"username\":\"%s\",\"password\":\"%s\"}", login, password);
            System.out.println(URI.create((Main.dotenv.get("BASE_URL") == null ? "https://jgames-server.onrender.com/" : Main.dotenv.get("BASE_URL")) + "api/auth/register"));
            System.out.println(jsonBody);
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create((Main.dotenv.get("BASE_URL") == null ? "https://jgames-server.onrender.com/" : Main.dotenv.get("BASE_URL")) + "api/auth/register"))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(jsonBody))
                    .build();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            System.out.println(response.body());
            if(response.statusCode() != 200){
                return response.body();
            }
            try (FileWriter w = new FileWriter(TOKEN_FILE)) {
                w.write(response.body());
                System.out.println("Файл успешно сохранен: " + new File(TOKEN_FILE).getAbsolutePath());
            } catch (IOException e) {
                System.err.println("Ошибка записи в файл!");
                e.printStackTrace();
                return "WRITE_ERROR";
            }
            return "SUCCESS";

        } catch (Exception e) {
            e.printStackTrace();
        }
        return "Ошибка при авторизации";
    }

    public static String login(String login, String password) {
        try {
            String jsonBody = String.format("{\"username\":\"%s\",\"password\":\"%s\"}", login, password);
            System.out.println(URI.create((Main.dotenv.get("BASE_URL") == null ? "https://jgames-server.onrender.com/" : Main.dotenv.get("BASE_URL")) + "api/auth/register"));
            System.out.println(jsonBody);
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create((Main.dotenv.get("BASE_URL") == null ? "https://jgames-server.onrender.com/" : Main.dotenv.get("BASE_URL")) + "api/auth/login"))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(jsonBody))
                    .build();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            System.out.println(response.body());
            if(response.statusCode() != 200){
                return response.body();
            }
            try (FileWriter w = new FileWriter(TOKEN_FILE)) {
                w.write(response.body());
                System.out.println("Файл успешно сохранен: " + new File(TOKEN_FILE).getAbsolutePath());
            } catch (IOException e) {
                System.err.println("Ошибка записи в файл!");
                e.printStackTrace();
                return "Ошибка записи токена";
            }
            return "SUCCESS";

        } catch (Exception e) {
            e.printStackTrace();
        }
        return "Ошибка при авторизации";
    }

    public static void deleteToken() {
        new File(TOKEN_FILE).delete();
    }
}
