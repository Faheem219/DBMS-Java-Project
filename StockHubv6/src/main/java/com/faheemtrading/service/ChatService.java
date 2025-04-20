package com.faheemtrading.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.faheemtrading.util.DBUtil;
import javafx.util.Pair;

import java.net.URI;
import java.net.http.*;
import java.nio.charset.StandardCharsets;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ChatService {

    /** explicit HTTP/1.1 to avoid h2c upgrade that uvicorn cannot parse */
    private static final HttpClient client = HttpClient.newBuilder()
            .version(HttpClient.Version.HTTP_1_1)
            .build();

    private static final ObjectMapper mapper = new ObjectMapper();
    private static final String BASE_URL = "http://localhost:8080/chat";

    /* ------------ REST call ------------ */
    public static String ask(int userId, String prompt) throws Exception {

        ObjectNode payload = mapper.createObjectNode()
                .put("user_id", userId)
                .put("prompt",  prompt);

        HttpRequest req = HttpRequest.newBuilder(new URI(BASE_URL))
                .header("Content-Type", "application/json; charset=utf-8")
                .POST(HttpRequest.BodyPublishers.ofString(
                        payload.toString(), StandardCharsets.UTF_8))
                .build();

        HttpResponse<String> res = client.send(
                req, HttpResponse.BodyHandlers.ofString(StandardCharsets.UTF_8));

        if (res.statusCode() != 200) {
            throw new RuntimeException("Chat service error: " + res.body());
        }
        return mapper.readTree(res.body()).get("answer").asText();
    }

    /* ------------ load saved chat history ------------ */
    public static List<Pair<String,String>> loadHistory(int userId) {
        List<Pair<String,String>> list = new ArrayList<>();
        String sql = """
                SELECT prompt, response
                  FROM chat_history
                 WHERE user_id = ?
              ORDER BY id
              """;
        try (Connection c = DBUtil.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setInt(1, userId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                list.add(new Pair<>(rs.getString("prompt"),
                        rs.getString("response")));
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return list;
    }
}
