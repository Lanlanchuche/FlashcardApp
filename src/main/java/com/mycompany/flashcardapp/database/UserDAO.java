package com.mycompany.flashcardapp.database;

import com.mycompany.flashcardapp.model.User;

import java.sql.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import com.mycompany.flashcardapp.constant.*;

public class UserDAO {
    private final Connection connection;
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public UserDAO() {

        this.connection = DatabaseConnection.getInstance().getConnection();
        ensureOtpColumns();
    }
    // 1. Tự động thêm cột cho bảng users nếu chưa có

    private void ensureOtpColumns() {
        try {
            Statement stmt = connection.createStatement();
            stmt.executeQuery("SELECT otp_code, otp_expiry FROM users LIMIT 1");
        } catch (SQLException e) {
            // Nếu lỗi tức là chưa có cột, ta thêm vào
            try {
                Statement stmt = connection.createStatement();
                stmt.execute("ALTER TABLE users ADD COLUMN otp_code TEXT");
                stmt.execute("ALTER TABLE users ADD COLUMN otp_expiry TEXT");
                System.out.println("Đã thêm cột OTP vào bảng users.");
            } catch (SQLException ex) {
                // Có thể cột đã tồn tại hoặc lỗi khác, bỏ qua
            }
        }
    }
    //2.Kiểm tra email có tồn tại không
    public boolean isEmailExists(String email) {
        String sql = "SELECT COUNT(*) FROM users WHERE email = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, email);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) return rs.getInt(1) > 0;
        } catch (SQLException e) { e.printStackTrace(); }
        return false;
    }
    // 3.Lưu mã OTP vào dâtbase
    public boolean saveOTP(String email,String otpCode) throws SQLException {
        // Cập nhật mã OTP và thời gian hết hạn (5 phút sau hiện tại)
        String sql = "UPDATE user SET otp_code = ?, otp_expiry = ? WHERE email = ?";
        try(PreparedStatement pstmt = connection.prepareStatement(sql)) {
            LocalDateTime expiryTime = LocalDateTime.now().plusMinutes(5);
            pstmt.setString(1, otpCode);
            pstmt.setString(1, expiryTime.format(DATE_FORMATTER));
            pstmt.setString(3, email);
            return pstmt.executeUpdate() > 0;


        }catch(SQLException e){
            e.printStackTrace();
            return false;
        }
    }
    // 4. Xác thực mã OTP
    public boolean verifyOTP(String email, String inputOtp) {
        String sql = "SELECT otp_code, otp_expiry FROM users WHERE email = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, email);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                String dbOtp = rs.getString("otp_code");
                String dbExpiryStr = rs.getString("otp_expiry");

                if (dbOtp == null || dbExpiryStr == null) return false;

                LocalDateTime expiry = LocalDateTime.parse(dbExpiryStr, DATE_FORMATTER);

                // Kiểm tra: Mã khớp VÀ chưa hết hạn
                if (dbOtp.equals(inputOtp) && LocalDateTime.now().isBefore(expiry)) {
                    return true;
                }
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return false;
    }

    // 5. Cập nhật mật khẩu mới sau khi xác thực thành công
    public boolean updatePassword(String email, String newPassword) {
        // Đổi mật khẩu và xóa OTP để không dùng lại được
        String sql = "UPDATE users SET password = ?, otp_code = NULL, otp_expiry = NULL WHERE email = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, newPassword);
            pstmt.setString(2, email);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }


    public boolean isUsernameExists(String username) {
        String sql = "SELECT COUNT(*) FROM users WHERE username = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, username);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            System.err.println("Failed to check username existence!");
            e.printStackTrace();
        }
        return false;
    }

    public boolean register(String username, String password) {
        // Kiểm tra trùng username
        if (isUsernameExists(username)) {
            return false;
        }

        String sql = "INSERT INTO users (username, password) VALUES (?, ?)";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, username);
            pstmt.setString(2, password);
            pstmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.err.println(ErrorMessage.REGISTER_FAILED);
            e.printStackTrace();
            return false;
        }
    }

    public User login(String username, String password) {
        String sql = "SELECT * FROM users WHERE username = ? AND password = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, username);
            pstmt.setString(2, password);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                return new User(
                        rs.getInt("id"),
                        rs.getString("username"),
                        rs.getString("password"));
            }
        } catch (SQLException e) {
            System.err.println(ErrorMessage.LOGIN_FAILED);
            e.printStackTrace();
        }
        return null;
    }
}