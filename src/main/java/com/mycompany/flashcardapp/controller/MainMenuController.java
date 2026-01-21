package com.mycompany.flashcardapp.controller;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import java.io.IOException;

public class MainMenuController {
    @FXML
    private Label welcomeLabel;


    @FXML
    public void initialize() {
        String username = "Người dùng";
        welcomeLabel.setText("Xin chào, " + username + "!");
    }

    @FXML
    void handleLogout(ActionEvent event) {

    }

    @FXML
    void openVocabularyManagement(MouseEvent event) {

    }

    @FXML
    void openStudyMode(MouseEvent event) {

    }

    @FXML
    void openTestMode(MouseEvent event) {

    }

    @FXML
    void openStatistics(MouseEvent event) {

    }

    private void switchScene(Object event, String fxmlPath, String title) {
    }


}
