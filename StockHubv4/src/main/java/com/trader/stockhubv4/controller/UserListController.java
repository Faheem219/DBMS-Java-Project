package com.trader.stockhubv4.controller;

import com.trader.stockhubv4.dao.UserDAO;
import com.trader.stockhubv4.dao.impl.UserDAOImpl;
import com.trader.stockhubv4.exception.AppException;
import com.trader.stockhubv4.model.User;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public class UserListController {
    @FXML private TableView<User> tableUsers;
    @FXML private TableColumn<User, Integer> colUserId;
    @FXML private TableColumn<User, String>  colName;
    @FXML private TableColumn<User, String>  colEmail;
    @FXML private TableColumn<User, String>  colPassword;

    private final UserDAO userDao = new UserDAOImpl();

    @FXML
    public void initialize() {
        colUserId.setCellValueFactory(data -> new javafx.beans.property.
                SimpleIntegerProperty(data.getValue().getUserId()).asObject());
        colName.setCellValueFactory(data -> new javafx.beans.property.
                SimpleStringProperty(data.getValue().getName()));
        colEmail.setCellValueFactory(data -> new javafx.beans.property.
                SimpleStringProperty(data.getValue().getEmail()));
        colPassword.setCellValueFactory(data -> new javafx.beans.property.
                SimpleStringProperty(data.getValue().getPassword()));
        onRefresh();
    }

    @FXML
    private void onAdd() {
        Dialog<User> dlg = createUserDialog("Add User", null);
        Optional<User> res = dlg.showAndWait();
        res.ifPresent(u -> {
            try {
                userDao.create(u);
                onRefresh();
            } catch (Exception e) {
                showError("Add failed: " + e.getMessage());
            }
        });
    }

    @FXML
    private void onEdit() {
        User sel = tableUsers.getSelectionModel().getSelectedItem();
        if (sel == null) {
            showError("Select a user first");
            return;
        }
        Dialog<User> dlg = createUserDialog("Edit User", sel);
        Optional<User> res = dlg.showAndWait();
        res.ifPresent(u -> {
            try {
                userDao.update(u);
                onRefresh();
            } catch (Exception e) {
                showError("Update failed: " + e.getMessage());
            }
        });
    }

    @FXML
    private void onDelete() {
        User sel = tableUsers.getSelectionModel().getSelectedItem();
        if (sel == null) {
            showError("Select a user first");
            return;
        }
        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION,
                "Delete user " + sel.getName() + "?",
                ButtonType.OK, ButtonType.CANCEL);
        confirm.showAndWait().ifPresent(b -> {
            if (b == ButtonType.OK) {
                try {
                    userDao.delete(sel.getUserId());
                    onRefresh();
                } catch (Exception e) {
                    showError("Delete failed: " + e.getMessage());
                }
            }
        });
    }

    @FXML
    private void onRefresh() {
        try {
            List<User> all = userDao.findAll();
            tableUsers.setItems(FXCollections.observableArrayList(all));
        } catch (Exception e) {
            showError("Load failed: " + e.getMessage());
        }
    }

    // Reusable dialog builder
    private Dialog<User> createUserDialog(String title, User existing) {
        Dialog<User> dlg = new Dialog<>();
        dlg.setTitle(title);
        dlg.getDialogPane().getButtonTypes()
                .addAll(ButtonType.OK, ButtonType.CANCEL);

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);

        TextField idF = new TextField();
        TextField nameF = new TextField();
        TextField emailF = new TextField();
        TextField pwdF = new TextField();

        if (existing != null) {
            idF.setText(String.valueOf(existing.getUserId()));
            idF.setDisable(true);
            nameF.setText(existing.getName());
            emailF.setText(existing.getEmail());
            pwdF.setText(existing.getPassword());
        }

        grid.addRow(0, new Label("ID:"), idF);
        grid.addRow(1, new Label("Name:"), nameF);
        grid.addRow(2, new Label("Email:"), emailF);
        grid.addRow(3, new Label("Password:"), pwdF);

        dlg.getDialogPane().setContent(grid);
        dlg.setResultConverter(bt -> {
            if (bt == ButtonType.OK) {
                User u = new User();
                u.setUserId(Integer.parseInt(idF.getText()));
                u.setName(nameF.getText());
                u.setEmail(emailF.getText());
                u.setPassword(pwdF.getText());
                return u;
            }
            return null;
        });
        return dlg;
    }

    private void showError(String msg) {
        Alert a = new Alert(Alert.AlertType.ERROR, msg, ButtonType.OK);
        a.setHeaderText(null);
        a.showAndWait();
    }
}
