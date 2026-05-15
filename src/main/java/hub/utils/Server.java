package hub.utils;

import com.google.gson.Gson;
import hub.entities.Stat;
import org.example.Main;

import java.io.Console;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class Server {
    static public Stat[] getTop10(String gameName) {
        try {
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create((Main.dotenv.get("BASE_URL") == null ? "https://jgames-server.onrender.com/" : Main.dotenv.get("BASE_URL"))  + "api/stat/top/" + gameName))
                    .GET()
                    .build();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            System.out.println(response);
            if (response.statusCode() != 200) {
                return new Stat[]{};
            }
            Gson gson = new Gson();
            return gson.fromJson(response.body(), Stat[].class);
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    static public void SaveStat(String gameName, Long stat){
        System.out.println("start");
        try {
            String token = TokenManager.getToken();
            String jsonBody = String.format("{\"gameName\":\"%s\",\"stat\":\"%s\"}", gameName, stat);
            System.out.println(URI.create((Main.dotenv.get("BASE_URL") == null ? "https://jgames-server.onrender.com/" : Main.dotenv.get("BASE_URL")) + "api/stat/save"));
            System.out.println(jsonBody);
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create((Main.dotenv.get("BASE_URL") == null ? "https://jgames-server.onrender.com/" : Main.dotenv.get("BASE_URL")) + "api/stat/save"))
                    .header("Content-Type", "application/json")
                    .header("Authorization", token)
                    .POST(HttpRequest.BodyPublishers.ofString(jsonBody))
                    .build();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            System.out.println(response.body());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}


