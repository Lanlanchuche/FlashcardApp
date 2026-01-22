package com.mycompany.flashcardapp.database;

import com.mycompany.flashcardapp.model.Streak;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class StreakDAO {
    private final Connection connection;

    public StreakDAO() {
        this.connection = DatabaseConnection.getInstance().getConnection();
    }

    public Streak getUserStreak(int userId) {
        String sql = "SELECT * FROM streaks WHERE user_id = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, userId);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                return new Streak(
                        rs.getInt("user_id"),
                        rs.getInt("current_streak"),
                        rs.getInt("longest_streak"),
                        rs.getString("last_completed_at"),
                        rs.getInt("freeze_count"));
            }
        } catch (SQLException e) {
            System.err.println("Failed to get user streak!");
            e.printStackTrace();
        }
        return null;
    }


    public boolean createDefaultStreak(int userId) {
        String sql = "INSERT INTO streaks (user_id, current_streak, longest_streak, last_completed_at, freeze_count) " +
                "VALUES (?, 0, 0, NULL, 0)";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, userId);
            pstmt.executeUpdate();
            System.out.println("✓ Created default streak for user " + userId);
            return true;
        } catch (SQLException e) {
            System.err.println("Failed to create default streak!");
            e.printStackTrace();
            return false;
        }
    }


    public boolean updateStreak(int userId) {
        Streak streak = getUserStreak(userId);
        if (streak == null) {
            System.err.println("Streak not found for user " + userId);
            return false;
        }

        LocalDate today = LocalDate.now();
        LocalDate lastCompleted = null;

        // Parse ngày học cuối cùng
        if (streak.getLastCompletedAt() != null && !streak.getLastCompletedAt().isEmpty()) {
            try {
                lastCompleted = LocalDate.parse(streak.getLastCompletedAt());
            } catch (Exception e) {
                System.err.println("Failed to parse last_completed_at: " + streak.getLastCompletedAt());
                lastCompleted = null;
            }
        }

        // LOGIC TÍNH STREAK THEO NGÀY
        boolean shouldUpdate = false;

        if (lastCompleted == null) {
            // Trường hợp 1: LẦN ĐẦU TIÊN học
            streak.setCurrentStreak(1);
            shouldUpdate = true;
            System.out.println("✓ First study session! Streak = 1");

        } else if (lastCompleted.equals(today)) {
            // Trường hợp 2: ĐÃ HỌC HÔM NAY RỒI
            // Không tăng streak, không cần update database
            System.out.println("ℹ Already studied today. Streak remains: " + streak.getCurrentStreak());
            return true; // Return true vì không phải lỗi, chỉ là không update

        } else {
            // Trường hợp 3: CHƯA HỌC HÔM NAY
            long daysBetween = ChronoUnit.DAYS.between(lastCompleted, today);

            if (daysBetween == 1) {
                // HỌC LIÊN TỤC (hôm qua mới học)
                streak.setCurrentStreak(streak.getCurrentStreak() + 1);
                System.out.println("✓ Consecutive day! Streak increased to: " + streak.getCurrentStreak());

            } else {
                // BỎ LỠ (cách quá 1 ngày) → RESET STREAK
                System.out.println("⚠ Missed " + (daysBetween - 1) + " day(s). Streak reset to 1");
                streak.setCurrentStreak(1);
            }
            shouldUpdate = true;
        }

        // Cập nhật longest_streak nếu cần
        if (shouldUpdate && streak.getCurrentStreak() > streak.getLongestStreak()) {
            streak.setLongestStreak(streak.getCurrentStreak());
            System.out.println("🏆 New record! Longest streak: " + streak.getLongestStreak());
        }

        // Lưu ngày hôm nay
        if (shouldUpdate) {
            streak.setLastCompletedAt(today.toString());
        }

        // Lưu vào database
        return saveStreak(streak);
    }

    /**
     * LƯU THÔNG TIN STREAK VÀO DATABASE
     *
     * @param streak Streak object cần lưu
     * @return true nếu lưu thành công, false nếu thất bại
     */
    private boolean saveStreak(Streak streak) {
        String sql = "UPDATE streaks SET current_streak = ?, longest_streak = ?, last_completed_at = ?, freeze_count = ? "
                +
                "WHERE user_id = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, streak.getCurrentStreak());
            pstmt.setInt(2, streak.getLongestStreak());
            pstmt.setString(3, streak.getLastCompletedAt());
            pstmt.setInt(4, streak.getFreezeCount());
            pstmt.setInt(5, streak.getUserId());
            pstmt.executeUpdate();
            System.out.println("✓ Streak saved to database");
            return true;
        } catch (SQLException e) {
            System.err.println("Failed to save streak!");
            e.printStackTrace();
            return false;
        }
    }
}
