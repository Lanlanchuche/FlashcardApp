package com.mycompany.flashcardapp.controller;

import com.mycompany.flashcardapp.database.UserDAO;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;


public class RegisterController {

    // FXML Components
    @FXML
    private TextField usernameField;

    @FXML
    private TextField emailField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private PasswordField confirmPasswordField;

    @FXML
    private Button registerButton;

    @FXML
    private Button backToLoginButton;

    @FXML
    private Label registerErrorLabel;

    private final UserDAO userDAO = new UserDAO();


    @FXML
    private void handleRegister() {
        String username = usernameField.getText().trim();
        String email = emailField.getText().trim();
        String password = passwordField.getText();
        String confirmPassword = confirmPasswordField.getText();

        if (username.isEmpty() || email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
            showError("Vui lòng nhập đầy đủ thông tin!");
            return;
        }

        if (username.length() < 3) {
            showError("Tên đăng nhập phải có ít nhất 3 ký tự!");
            return;
        }

        if (!email.contains("@") || !email.contains(".")) {
            showError("Email không hợp lệ!");
            return;
        }

        if (password.length() < 4) {
            showError("Mật khẩu phải có ít nhất 4 ký tự!");
            return;
        }

        if (!password.equals(confirmPassword)) {
            showError("Mật khẩu xác nhận không khớp!");
            return;
        }

        if (userDAO.isUsernameExists(username)) {
            showError("Tên đăng nhập đã tồn tại!");
            return;
        }

        boolean success = userDAO.register(username, password);
        if (success) {
            showSuccess("Đăng ký thành công! Đang chuyển đến trang đăng nhập...");
            new Thread(() -> {
                try {
                    Thread.sleep(1500);
                    javafx.application.Platform.runLater(this::handleBackToLogin);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }).start();
        } else {
            showError("Đăng ký thất bại! Vui lòng thử lại.");
        }
    }

    @FXML
    private void handleBackToLogin() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/Login.fxml"));
            Parent root = loader.load();

            Stage stage = (Stage) backToLoginButton.getScene().getWindow();
            stage.setMaximized(false);
            stage.setScene(new Scene(root, 1280, 720));
            stage.setTitle("Flashcard Learning - Đăng nhập");
            stage.setMaximized(true);
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
            showError("Không thể quay lại màn hình đăng nhập!");
        }
    }

    @FXML
    void backToWelcome(javafx.event.ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/Welcome.fxml"));
            Parent root = loader.load();

            Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
            stage.setMaximized(false);
            stage.setScene(new Scene(root, 1280, 720));
            stage.setTitle("Flashcard Learning - Welcome");
            stage.setMaximized(true);
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
            showError("Không thể quay lại màn hình Welcome!");
        }
    }

    @FXML
    void goToLogin(javafx.event.ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/Login.fxml"));
            Parent root = loader.load();

            Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
            stage.setMaximized(false);
            stage.setScene(new Scene(root, 1280, 720));
            stage.setTitle("Flashcard Learning - Đăng nhập");
            stage.setMaximized(true);
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
            showError("Không thể mở màn hình đăng nhập!");
        }
    }

    private void showError(String message) {
        if (registerErrorLabel != null) {
            registerErrorLabel.setText(message);
            registerErrorLabel.setStyle("-fx-text-fill: #e74c3c;");
        }
    }

    private void showSuccess(String message) {
        if (registerErrorLabel != null) {
            registerErrorLabel.setText(message);
            registerErrorLabel.setStyle("-fx-text-fill: #27ae60;");
        }
    }
}
