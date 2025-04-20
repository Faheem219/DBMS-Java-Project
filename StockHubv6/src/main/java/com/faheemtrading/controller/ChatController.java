package com.faheemtrading.controller;

import com.faheemtrading.service.ChatService;
import com.faheemtrading.util.AlertUtil;
import com.faheemtrading.util.Session;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.util.Pair;

public class ChatController {

    @FXML private ListView<Message> chatList;
    @FXML private TextField         inputField;

    private record Message(String text, boolean isUserPrompt) {} // true = AI answer (right)

    @FXML public void initialize() {

        /* ---------- cell renderer ---------- */
        chatList.setCellFactory(lv -> new ListCell<>() {
            @Override protected void updateItem(Message m, boolean empty) {
                super.updateItem(m, empty);
                if (empty || m == null) { setGraphic(null); return; }

                TextFlow bubble = new TextFlow(new Text(m.text()));
                bubble.getStyleClass().add(m.isUserPrompt ? "bubble-right" : "bubble-left");

                HBox box = new HBox(bubble);
                box.setAlignment(m.isUserPrompt ? Pos.CENTER_RIGHT : Pos.CENTER_LEFT);
                setGraphic(box);
            }
        });

        /* ---------- history from DB ---------- */
        int uid = Session.getUser().getUserId();
        for (Pair<String,String> pair : ChatService.loadHistory(uid)) {
            chatList.getItems().add(new Message(pair.getKey(), true));  // prompt  -> left
            chatList.getItems().add(new Message(pair.getValue(), false)); // answer -> right
        }
    }

    @FXML private void sendMessage() {
        String prompt = inputField.getText().strip();
        if (prompt.isEmpty()) return;

        int uid = Session.getUser().getUserId();
        chatList.getItems().add(new Message(prompt, false)); // prompt on left
        inputField.clear();

        new Thread(() -> {
            try {
                String reply = ChatService.ask(uid, prompt);
                Platform.runLater(() ->
                        chatList.getItems().add(new Message(reply, true)));
            } catch (Exception ex) {
                ex.printStackTrace();
                Platform.runLater(() ->
                        AlertUtil.error("Chat error", ex.getMessage()));
            }
        }).start();
    }
}
