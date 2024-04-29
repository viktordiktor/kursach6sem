package com.nikonenko.kursach6sem.utils;

import lombok.experimental.UtilityClass;
import org.springframework.util.StreamUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import org.json.JSONObject;

public class ImgurUploader {
    public String uploadToImgur(MultipartFile multipartFile, String clientID) throws IOException {
        // Создаем временный файл для сохранения содержимого MultipartFile
        File tempFile = File.createTempFile("temp", null);
        try (OutputStream outputStream = new FileOutputStream(tempFile);
             InputStream inputStream = multipartFile.getInputStream()) {
            StreamUtils.copy(inputStream, outputStream);
        }

        // Загружаем временный файл на Imgur
        String imageUrl = uploadToImgur(tempFile, clientID);

        // Удаляем временный файл
        tempFile.delete();

        return imageUrl;
    }

    public String uploadToImgur(File file, String clientID) throws IOException {
        URL url = new URL("https://api.imgur.com/3/upload");
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setDoOutput(true);
        conn.setRequestMethod("POST");
        conn.setRequestProperty("Authorization", "Client-ID " + clientID);

        // Set the content type as multipart/form-data
        conn.setRequestProperty("Content-Type", "multipart/form-data");

        // Create the request body
        OutputStream outputStream = conn.getOutputStream();
        PrintWriter writer = new PrintWriter(new OutputStreamWriter(outputStream, "UTF-8"), true);
        writer.append("\r\n").append("Content-Disposition: form-data; name=\"image\"; filename=\"")
                .append(file.getName()).append("\"\r\n").append("Content-Type: ")
                .append(HttpURLConnection.guessContentTypeFromName(file.getName()))
                .append("\r\n")
                .append("\r\n");

        // Write the file content
        FileInputStream inputStream = new FileInputStream(file);
        byte[] buffer = new byte[4096];
        int bytesRead;
        while ((bytesRead = inputStream.read(buffer)) != -1) {
            outputStream.write(buffer, 0, bytesRead);
        }

        outputStream.flush();
        inputStream.close();

        writer.append("\r\n")
                .append("------WebKitFormBoundary7MA4YWxkTrZu0gW--\r\n");
        writer.close();

        // Get the response
        int responseCode = conn.getResponseCode();
        if (responseCode == HttpURLConnection.HTTP_OK) {
            BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            StringBuilder response = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                response.append(line);
            }
            reader.close();

            // Parse the JSON response
            JSONObject json = new JSONObject(response.toString());
            JSONObject data = json.getJSONObject("data");
            String imageUrl = data.getString("link");

            return imageUrl;
        } else {
            throw new IOException("Failed to upload image to Imgur. Response code: " + responseCode);
        }
    }
}
