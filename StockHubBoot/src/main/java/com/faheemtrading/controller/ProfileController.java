package com.faheemtrading.controller;

import com.faheemtrading.model.User;
import com.faheemtrading.service.ProfileService;
import com.faheemtrading.util.AlertUtil;
import com.faheemtrading.util.Session;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

import java.math.BigDecimal;

public class ProfileController {

    @FXML private Label          nameLabel, emailLabel;
    @FXML private PasswordField  pwdField;
    @FXML private TextField      contactField, incomeField;
    @FXML private ChoiceBox<String> typeChoice;

    private final ProfileService svc = new ProfileService();
    private User user;

    @FXML public void initialize() {
        user = svc.load(Session.getUser().getUserId());

        nameLabel .setText(user.getName());
        emailLabel.setText(user.getEmail());
        pwdField.setText(user.getPassword());
        contactField.setText(user.getContactInfo());
        incomeField.setText(user.getAnnualIncome().toPlainString());

    }

    @FXML private void save() {
        try {
            user.setPassword( pwdField.getText() );
            user.setContactInfo( contactField.getText() );
            user.setAnnualIncome( new BigDecimal(incomeField.getText()) );

            if (svc.save(user)) {
                AlertUtil.info("Profile updated", "Changes saved!");
            } else {
                AlertUtil.error("DB error", "Could not save profile.");
            }
        } catch (Exception ex) {
            AlertUtil.error("Validation", "Enter a valid number for income.");
        }
    }
}
