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

    @FXML
    private TextField usernameField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private PasswordField confirmPasswordField;

    @FXML
    private Button registerButton;

    @FXML
    private Button backToLoginButton;

    @FXML
    private Label messageLabel;

    private final UserDAO userDAO = new UserDAO();

    /**
     * Handle register button click
     */
    @FXML
    private void handleRegister() {
        String username = usernameField.getText().trim();
        String password = passwordField.getText();
        String confirmPassword = confirmPasswordField.getText();

        // Validation
        if (username.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
            showError("Vui lòng nhập đầy đủ thông tin!");
            return;
        }

        if (username.length() < 3) {
            showError("Tên đăng nhập phải có ít nhất 3 ký tự!");
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

        // Check if username already exists
        if (userDAO.isUsernameExists(username)) {
            showError("Tên đăng nhập đã tồn tại!");
            return;
        }

        // Attempt registration
        boolean success = userDAO.register(username, password);
        if (success) {
            showSuccess("Đăng ký thành công! Đang chuyển đến trang đăng nhập...");

            // Navigate back to login after 1.5 seconds
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
            stage.setScene(new Scene(root));
            stage.setTitle("Flashcard Learning - Đăng nhập");
        } catch (Exception e) {
            e.printStackTrace();
            showError("Không thể quay lại màn hình đăng nhập!");
        }
    }


    private void showError(String message) {
        if (messageLabel != null) {
            messageLabel.setText(message);
            messageLabel.setStyle("-fx-text-fill: #e74c3c;");
        }
    }


    private void showSuccess(String message) {
        if (messageLabel != null) {
            messageLabel.setText(message);
            messageLabel.setStyle("-fx-text-fill: #27ae60;");
        }
    }
}
