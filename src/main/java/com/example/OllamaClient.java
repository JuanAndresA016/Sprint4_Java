package com.example;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class OllamaClient {

    private static final String BASE_URL = "http://localhost:11434/api/chat";
    private static final String MODEL = "qwen2.5:3b";

    private final HttpClient http = HttpClient.newHttpClient();
    private final ObjectMapper mapper = new ObjectMapper();

    public String chat(String userMessage) throws Exception {

        Map<String, Object> body = Map.of(
                "model", MODEL,
                "stream", false,
                "messages", List.of(
                        Map.of(
                                "role", "user",
                                "content", userMessage)));

        String json = mapper.writeValueAsString(body);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .build();

        HttpResponse<String> response = http.send(
                request,
                HttpResponse.BodyHandlers.ofString());

        JsonNode root = mapper.readTree(response.body());

        return root.get("message").get("content").asText();
    }
}