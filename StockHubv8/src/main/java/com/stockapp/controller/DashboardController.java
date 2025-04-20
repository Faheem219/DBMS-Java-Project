package com.stockapp.controller;

import com.stockapp.model.User;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class DashboardController implements UserAware {
    @FXML private Label titleLabel;

    @Override
    public void initData(User user) {
        titleLabel.setText("Hello, " + user.getName() + "! Welcome to your Dashboard.");
    }
}
