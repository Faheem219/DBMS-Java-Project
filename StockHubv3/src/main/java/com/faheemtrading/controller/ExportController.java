package com.faheemtrading.controller;

import javafx.fxml.FXML;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DialogPane;

public class ExportController {

    @FXML private ChoiceBox<String> formatChoice;
    @FXML private DialogPane dialogPane; // injected automatically
    private ButtonType okBtn;

    @FXML
    private void initialize(){
        formatChoice.getItems().addAll("CSV","PDF");
        formatChoice.setValue("CSV");
        // grab okBtn for later
        okBtn = dialogPane.getButtonTypes().filtered(b -> b.getButtonData().isDefaultButton()).get(0);
    }

    public ButtonType getOkButton(){ return okBtn; }
    public String getFormat(){ return formatChoice.getValue(); }
}
