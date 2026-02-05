package com.mycompany.flashcardapp.controller;

import com.mycompany.flashcardapp.model.User;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;

public class MainMenuController {

    @FXML
    private VBox TestBox;

    @FXML
    private Button logoutButton;

    @FXML
    private VBox reviewBox;

    @FXML
    private VBox statisticsBox;

    @FXML
    private VBox topicManagementBox;

    @FXML
    private Label welcomeLabel;

    @FXML
    public void initialize() {
        User currentUser = SessionManager.getInstance().getCurrentUser();
        if (currentUser != null && welcomeLabel != null) {
            welcomeLabel.setText("Xin chào, " + currentUser.getUsername() + "!");
        }
    }

    @FXML
    void handleLogout(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/Welcome.fxml"));
            Parent root = loader.load();

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setMaximized(false);
            stage.setScene(new Scene(root, 1280, 720));
            stage.setTitle("Flashcard Learning - Welcome");
            stage.setMaximized(true);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Không thể quay về màn hình Welcome");
        }
    }

    @FXML
    void openVocabularyManagement(MouseEvent event) {
        User currentUser = SessionManager.getInstance().getCurrentUser();
        System.out.println("=== MainMenuController.openVocabularyManagement() called ===");
        System.out.println("DEBUG: currentUser = "
                + (currentUser != null ? currentUser.getUsername() + " (ID: " + currentUser.getId() + ")" : "NULL"));

        if (currentUser == null) {
            System.err.println("ERROR: currentUser is NULL! Cannot open VocabularyManagement.");
            return;
        }

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/VocabularyManagement.fxml"));
            Parent root = loader.load();

            System.out.println("DEBUG: User already set in SessionManager: " + currentUser.getUsername());

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setMaximized(false);
            stage.setScene(new Scene(root, 1280, 720));
            stage.setTitle("Flashcard Learning - Quản lý từ vựng");
            stage.setMaximized(true);
            stage.show();
        } catch (IOException e) {
            System.err.println("ERROR: Failed to open VocabularyManagement screen");
            e.printStackTrace();
            System.err.println("Không thể mở màn hình Quản lý từ vựng");
        }
    }

    @FXML
    void openStudyMode(MouseEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/StudyMode.fxml"));
            Parent root = loader.load();

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setMaximized(false);
            stage.setScene(new Scene(root, 1280, 720));
            stage.setTitle("Flashcard Learning - Học tập");
            stage.setMaximized(true);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Không thể mở màn hình Học tập");
        }
    }

    @FXML
    void openTestMode(MouseEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/TestMode.fxml"));
            Parent root = loader.load();

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setMaximized(false);
            stage.setScene(new Scene(root, 1280, 720));
            stage.setTitle("Flashcard Learning - Kiểm tra");
            stage.setMaximized(true);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Không thể mở màn hình Kiểm tra");
        }
    }

    @FXML
    void openStatistics(MouseEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/Statistics.fxml"));
            Parent root = loader.load();

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setMaximized(false);
            stage.setScene(new Scene(root, 1280, 720));
            stage.setTitle("Flashcard Learning - Thống kê");
            stage.setMaximized(true);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Không thể mở màn hình Thống kê");
        }
    }
}
