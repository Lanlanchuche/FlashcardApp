package com.mycompany.flashcardapp.controller;

import com.mycompany.flashcardapp.database.UserDAO;
import com.mycompany.flashcardapp.model.User;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;

public class LoginController {

    @FXML
    private Label loginErrorLabel;

    @FXML
    private TextField usernameField;

    @FXML
    private PasswordField passwordField;

    // Đã thêm ID cho Hyperlink theo yêu cầu
    @FXML
    private Hyperlink forgotPasswordLink;

    private final UserDAO userDAO = new UserDAO();

    @FXML
    private void handleLogin(ActionEvent event) {
        String username = usernameField.getText().trim();
        String password = passwordField.getText();

        if (username.isEmpty() || password.isEmpty()) {
            showError("Tên đăng nhập và mật khẩu không được để trống");
            return;
        }

        User user = userDAO.login(username, password);
        if (user != null) {
            try {
                // Lưu user vào session
                SessionManager.getInstance().setCurrentUser(user);

                // Chuyển sang Menu chính
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/MainMenu.fxml"));
                Parent root = loader.load();

                Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                stage.setMaximized(false);
                stage.setScene(new Scene(root, 1280, 720));
                stage.setTitle("Flashcard Learning - Menu Chính");
                stage.setMaximized(true);
                stage.show();
            } catch (IOException e) {
                e.printStackTrace();
                showError("Lỗi hệ thống: Không thể vào màn hình chính!");
            }
        } else {
            showError("Tên đăng nhập hoặc mật khẩu không đúng!");
        }
    }

    /**
     * Chức năng: Chuyển sang màn hình Quên Mật Khẩu
     * Đã liên kết với nút "Quên mật khẩu?" trong FXML
     */
    @FXML
    void handleForgotPassword(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/ForgotPassword.fxml"));
            Parent root = loader.load();

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setMaximized(false);
            stage.setScene(new Scene(root, 1280, 720));
            stage.setTitle("Flashcard Learning - Quên mật khẩu");
            stage.setMaximized(true);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            showError("Không thể mở màn hình Quên mật khẩu!");
        }
    }

    /**
     * Chức năng: Chuyển sang màn hình Đăng ký
     */
    @FXML
    void goToRegister(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/Register.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setMaximized(false);
            stage.setScene(new Scene(root, 1280, 720));
            stage.setTitle("Flashcard Learning - Đăng ký");
            stage.setMaximized(true);
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
            showError("Không thể mở màn hình đăng ký!");
        }
    }

    // Hàm hiển thị lỗi lên Label thay vì Alert (giữ nguyên style cũ của bạn)
    private void showError(String message) {
        if (loginErrorLabel != null) {
            loginErrorLabel.setText(message);
            loginErrorLabel.setStyle("-fx-text-fill: #e74c3c;");
            loginErrorLabel.setVisible(true);
        } else {
            // Fallback dùng Alert nếu Label chưa load được
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText(message);
            alert.show();
        }
    }
}