package com.example;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class ConversationClient {

    private static final String BASE_URL = "http://localhost:11434/api/chat";
    private static final String MODEL = "qwen2.5:3b";

    private final HttpClient http = HttpClient.newHttpClient();
    private final ObjectMapper mapper = new ObjectMapper();

    private List<Map<String, String>> history = new ArrayList<>();

    public ConversationClient() {

        history.add(Map.of(
                "role", "system",
                "content",
                "You are a programming assistant. "));
    }

    public String chat(String userMessage) throws Exception {

        history.add(Map.of(
                "role", "user",
                "content", userMessage));

        Map<String, Object> body = Map.of(
                "model", MODEL,
                "stream", false,
                "temperature", 0.7,
                "messages", history);

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

        String assistantMessage =
                root.get("message").get("content").asText();

        history.add(Map.of(
                "role", "assistant",
                "content", assistantMessage));

        return assistantMessage;
    }

    public static void main(String[] args) {

        try {

            ConversationClient client = new ConversationClient();

            Scanner scanner = new Scanner(System.in);

            System.out.println("Conversation started.");
            System.out.println("Type 'exit' to quit.");

            while (true) {

                System.out.print("\nYou: ");
                String input = scanner.nextLine();

                if (input.equalsIgnoreCase("exit")) {
                    break;
                }

                String response = client.chat(input);

                System.out.println("\nAssistant:");
                System.out.println(response);
            }

            scanner.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}