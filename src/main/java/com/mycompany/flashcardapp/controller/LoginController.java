package com.mycompany.flashcardapp.controller;

import com.mycompany.flashcardapp.database.UserDAO;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;

public class LoginController {


    @FXML
    private Label messageLabel;

    @FXML
    private PasswordField passwordField;

    @FXML
    private TextField usernameField;



    @FXML
    void handleGoToRegister(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/Register.fxml"));
            Parent root = loader.load();

            Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Flashcard Learning - Đăng ký");
        } catch (Exception e) {
            e.printStackTrace();
            showError("Không thể mở màn hình đăng ký!");
        }
    }


    @FXML
    void handleLogin(ActionEvent event) {
        String username = usernameField.getText();
        String password = passwordField.getText();

        UserDAO userDAO = new UserDAO();

        //Kiểm tra tài khoản
        com.mycompany.flashcardapp.model.User loggedInUser = userDAO.login(username, password);

        if (loggedInUser != null) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/Dashboard.fxml"));
                Parent dashboardRoot = loader.load();

                // LẤY CONTROLLER CỦA DASHBOARD VÀ TRUYỀN DỮ LIỆU
                DashboardController dashboardController = loader.getController();
                dashboardController.setUser(loggedInUser);

                Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();

                // Tăng kích thước, hiển thị đẹp hơn
                Scene scene = new Scene(dashboardRoot, 800, 500);
                stage.setScene(scene);
                stage.setTitle("Flashcard Learning - Dashboard");
                stage.centerOnScreen();
                stage.show();

            } catch (IOException e) {
                e.printStackTrace();
                messageLabel.setText("❌ Lỗi: Không thể nạp giao diện Dashboard!");
            }
        } else {
            messageLabel.setText("❌ Tên đăng nhập hoặc mật khẩu không đúng!");
        }
    }

    private void showError(String message) {
        if (messageLabel != null) {
            messageLabel.setText(message);
            messageLabel.setStyle("-fx-text-fill: #e74c3c;");
        }
    }

}
