package com.mycompany.flashcardapp.controller;

import com.mycompany.flashcardapp.database.UserDAO;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;

public class RegisterController {

    // --- KHAI BÁO CÁC THÀNH PHẦN KHỚP VỚI Register.fxml ---

    @FXML
    private TextField usernameField; // [cite: 1341]

    @FXML
    private TextField emailField; // [cite: 1346]

    @FXML
    private PasswordField passwordField;// [cite: 1351]

    @FXML
    private PasswordField confirmPasswordField;// [cite: 1356]

    @FXML
    private Button registerButton;// [cite: 1359]

    // --- XỬ LÝ DỮ LIỆU ---
    private final UserDAO userDAO = new UserDAO();

    /**
     * Xử lý sự kiện khi nhấn nút "Đăng ký" hoặc nhấn Enter tại ô xác nhận mật khẩu
     */
    @FXML
    private void handleRegister(ActionEvent event) {
        String username = usernameField.getText().trim();
        String email = emailField.getText().trim();
        String password = passwordField.getText();
        String confirmPassword = confirmPasswordField.getText();

        // 1. Kiểm tra dữ liệu đầu vào
        if (username.isEmpty() || email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Thiếu thông tin", "Vui lòng nhập đầy đủ tất cả các trường!");
            return;
        }

        if (username.length() < 3) {
            showAlert(Alert.AlertType.WARNING, "Tên đăng nhập không hợp lệ", "Tên đăng nhập phải có ít nhất 3 ký tự!");
            return;
        }

        if (!email.contains("@") || !email.contains(".")) {
            showAlert(Alert.AlertType.WARNING, "Email không hợp lệ",
                    "Vui lòng nhập đúng định dạng email (vd: user@example.com)!");
            return;
        }

        if (password.length() < 4) {
            showAlert(Alert.AlertType.WARNING, "Mật khẩu quá ngắn", "Mật khẩu phải có ít nhất 4 ký tự!");
            return;
        }

        if (!password.equals(confirmPassword)) {
            showAlert(Alert.AlertType.ERROR, "Mật khẩu không khớp", "Mật khẩu xác nhận không trùng khớp!");
            return;
        }

        // 2. Kiểm tra logic nghiệp vụ (Database)
        if (userDAO.isUsernameExists(username)) {
            showAlert(Alert.AlertType.ERROR, "Lỗi đăng ký", "Tên đăng nhập đã tồn tại! Vui lòng chọn tên khác.");
            return;
        }

        // 3. Thực hiện đăng ký
        // Lưu ý: Nếu UserDAO chưa hỗ trợ lưu email, bạn cần cập nhật UserDAO.
        // Hiện tại hàm register chỉ nhận username/password.
        boolean success = userDAO.register(username, password);

        if (success) {
            // Thông báo thành công và chuyển trang
            showAlert(Alert.AlertType.INFORMATION, "Thành công", "Đăng ký tài khoản thành công!");

            // Chuyển sang màn hình đăng nhập
            navigateToLogin(event);
        } else {
            showAlert(Alert.AlertType.ERROR, "Lỗi hệ thống", "Đăng ký thất bại! Vui lòng thử lại sau.");
        }
    }

    /**
     * Chuyển hướng sang màn hình Đăng nhập (Login)
     * Được gọi bởi Hyperlink "Hỗ trợ thêm thông tin" (mà thực tế là nút Back to
     * Login trong logic cũ)
     */
    @FXML
    void goToLogin(ActionEvent event) {
        navigateToLogin(event);
    }

    /**
     * Hàm dùng chung để chuyển cảnh sang Login.fxml
     */
    private void navigateToLogin(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/Login.fxml"));
            Parent root = loader.load();

            // Lấy Stage từ nguồn sự kiện (Nút hoặc Hyperlink)
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

            // Cấu hình Stage
            stage.setMaximized(false);
            stage.setScene(new Scene(root, 1280, 720));
            stage.setTitle("Flashcard Learning - Đăng nhập");
            stage.setMaximized(true);
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Lỗi ứng dụng", "Không thể mở màn hình đăng nhập!");
        }
    }

    /**
     * Hiển thị thông báo (Thay thế cho Label registerErrorLabel không có trong
     * FXML)
     */
    private void showAlert(Alert.AlertType type, String title, String content) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}