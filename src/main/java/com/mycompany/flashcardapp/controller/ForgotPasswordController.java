package com.mycompany.flashcardapp.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.regex.Pattern;

public class ForgotPasswordController {

    @FXML
    private TextField emailField;

    @FXML
    private PasswordField passwordField; // Ô ẩn (Mật khẩu mới)
    @FXML
    private TextField passwordTextField; // Ô hiện (Mật khẩu mới)

    @FXML
    private PasswordField confirmPasswordField; // Ô ẩn (Xác nhận)
    @FXML
    private TextField confirmTextField; // Ô hiện (Xác nhận)

    @FXML
    private CheckBox showPasswordCheckbox;

    @FXML
    private Button nextButton;

    @FXML
    private Hyperlink backLink;

    // --- CẤU HÌNH REGEX ---
    // 1. Email: Theo yêu cầu của bạn (Gmail format)
    private static final String EMAIL_PATTERN = "^[a-z0-9]((\\.|\\+)?[a-z0-9]){5,}@g(oogle)?mail\\.com$";

    // 2. Mật khẩu: Ít nhất 1 chữ hoa, 1 số, 1 ký tự đặc biệt
    // Giải thích:
    // (?=.*[A-Z]) : Phải có ít nhất 1 chữ in hoa
    // (?=.*[0-9]) : Phải có ít nhất 1 số
    // (?=.*[^a-zA-Z0-9]): Phải có ít nhất 1 ký tự đặc biệt (không phải chữ hoặc số)
    private static final String PASSWORD_PATTERN = "^(?=.*[A-Z])(?=.*[0-9])(?=.*[^a-zA-Z0-9]).+$";

    /**
     * Xử lý khi tick vào "Hiện mật khẩu"
     */
    @FXML
    void handleShowPassword(ActionEvent event) {
        if (showPasswordCheckbox.isSelected()) {
            // Chế độ HIỆN: Lấy dữ liệu từ ô ẩn -> gán sang ô hiện
            passwordTextField.setText(passwordField.getText());
            confirmTextField.setText(confirmPasswordField.getText());

            // Ẩn PasswordField, Hiện TextField
            passwordField.setVisible(false);
            passwordField.setManaged(false);
            passwordTextField.setVisible(true);
            passwordTextField.setManaged(true);

            confirmPasswordField.setVisible(false);
            confirmPasswordField.setManaged(false);
            confirmTextField.setVisible(true);
            confirmTextField.setManaged(true);

        } else {
            // Chế độ ẨN: Lấy dữ liệu từ ô hiện -> gán sang ô ẩn
            passwordField.setText(passwordTextField.getText());
            confirmPasswordField.setText(confirmTextField.getText());

            // Hiện PasswordField, Ẩn TextField
            passwordField.setVisible(true);
            passwordField.setManaged(true);
            passwordTextField.setVisible(false);
            passwordTextField.setManaged(false);

            confirmPasswordField.setVisible(true);
            confirmPasswordField.setManaged(true);
            confirmTextField.setVisible(false);
            confirmTextField.setManaged(false);
        }
    }

    /**
     * Nút "Tiếp theo" -> Kiểm tra Validate -> Chuyển sang OTP.fxml
     */
    @FXML
    void handleNext(ActionEvent event) {
        // 1. Lấy dữ liệu chuẩn (dù đang ẩn hay hiện)
        String email = emailField.getText().trim();
        String password = showPasswordCheckbox.isSelected() ? passwordTextField.getText() : passwordField.getText();
        String confirm = showPasswordCheckbox.isSelected() ? confirmTextField.getText()
                : confirmPasswordField.getText();

        // 2. KIỂM TRA ĐẦU VÀO (VALIDATION)

        // Kiểm tra trống
        if (email.isEmpty() || password.isEmpty() || confirm.isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Thiếu thông tin", "Vui lòng nhập đầy đủ email và mật khẩu mới!");
            return;
        }

        // Kiểm tra định dạng Email
        if (!Pattern.matches(EMAIL_PATTERN, email)) {
            showAlert(Alert.AlertType.WARNING, "Email không hợp lệ",
                    "Vui lòng nhập đúng định dạng Gmail!\n(Ví dụ: example@gmail.com)");
            return;
        }

        // Kiểm tra độ mạnh Mật khẩu (Hoa, Số, Đặc biệt)
        if (!Pattern.matches(PASSWORD_PATTERN, password)) {
            showAlert(Alert.AlertType.WARNING, "Mật khẩu yếu",
                    "Mật khẩu phải bao gồm:\n- Ít nhất 1 chữ in hoa\n- Ít nhất 1 số\n- Ít nhất 1 ký tự đặc biệt (@, #, $, ...)");
            return;
        }

        // Kiểm tra độ dài tối thiểu (ví dụ 6 ký tự)
        if (password.length() < 6) {
            showAlert(Alert.AlertType.WARNING, "Mật khẩu quá ngắn", "Mật khẩu phải có ít nhất 6 ký tự!");
            return;
        }

        // Kiểm tra mật khẩu xác nhận
        if (!password.equals(confirm)) {
            showAlert(Alert.AlertType.ERROR, "Lỗi xác nhận", "Mật khẩu xác nhận không trùng khớp!");
            return;
        }

        // 3. Chuyển cảnh sang OTP (Nếu tất cả hợp lệ)
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/OTP.fxml"));
            Parent root = loader.load();

            // Nếu bạn có OTPController, bạn có thể truyền email sang đây để gửi mã
            // OTPController otpController = loader.getController();
            // otpController.setEmail(email);

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setMaximized(false);
            stage.setScene(new Scene(root, 1280, 720));
            stage.setTitle("Flashcard Learning - Xác thực OTP");
            stage.setMaximized(true);
            stage.show();

            System.out.println("DEBUG: Validation OK. Chuyển sang OTP. Email: " + email);

        } catch (IOException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Lỗi hệ thống", "Không thể mở màn hình OTP!");
        }
    }

    /**
     * Nút "Quay lại" -> Chuyển về Login.fxml
     */
    @FXML
    void handleBack(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/Login.fxml"));
            Parent root = loader.load();

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setMaximized(false);
            stage.setScene(new Scene(root, 1280, 720));
            stage.setTitle("Flashcard Learning - Đăng nhập");
            stage.setMaximized(true);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Lỗi", "Không thể quay lại màn hình đăng nhập!");
        }
    }

    private void showAlert(Alert.AlertType type, String title, String content) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}