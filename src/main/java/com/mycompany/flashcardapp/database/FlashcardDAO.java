package com.mycompany.flashcardapp.database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.mycompany.flashcardapp.constant.*;
import com.mycompany.flashcardapp.model.Flashcard;

public class FlashcardDAO {
    private final Connection connection;

    public FlashcardDAO() {
        this.connection = DatabaseConnection.getInstance().getConnection();
    }

    public boolean addFlashcard(int userId, String vocabulary, String definition) {
        String sql = "INSERT INTO flashcards (user_id, vocabulary, definition, is_learned) VALUES (?, ?, ?, 0)";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, userId);
            pstmt.setString(2, vocabulary);
            pstmt.setString(3, definition);
            pstmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.err.println(ErrorMessage.ADD_FLASHCARD_FAILED);
            e.printStackTrace();
            return false;
        }
    }

    public boolean updateFlashcard(int id, String vocabulary, String definition) {
        String sql = "UPDATE flashcards SET vocabulary = ?, definition = ? WHERE id = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, vocabulary);
            pstmt.setString(2, definition);
            pstmt.setInt(3, id);
            pstmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.err.println(ErrorMessage.UPDATE_FLASHCARD_FAILED);
            e.printStackTrace();
            return false;
        }
    }

    public boolean deleteFlashcard(int id) {
        String sql = "DELETE FROM flashcards WHERE id = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            pstmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.err.println(ErrorMessage.DELETE_FLASHCARD_FAILED);
            e.printStackTrace();
            return false;
        }
    }

    public List<Flashcard> getAllFlashcards(int userId) {
        List<Flashcard> flashcards = new ArrayList<>();
        String sql = "SELECT * FROM flashcards WHERE user_id = ? ORDER BY id DESC";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, userId);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                Flashcard flashcard = new Flashcard(
                        rs.getInt("id"),
                        rs.getInt("user_id"),
                        rs.getString("vocabulary"),
                        rs.getString("definition"),
                        rs.getInt("is_learned") == 1);
                flashcards.add(flashcard);
            }
        } catch (SQLException e) {
            System.err.println("Failed to get flashcards!");
            e.printStackTrace();
        }
        return flashcards;
    }

    public boolean markAsLearned(int id, boolean isLearned) {
        String sql = "UPDATE flashcards SET is_learned = ? WHERE id = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, isLearned ? 1 : 0);
            pstmt.setInt(2, id);
            pstmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.err.println("Failed to mark flashcard as learned!");
            e.printStackTrace();
            return false;
        }
    }
    public List<Flashcard> getUnlearnedFlashcards(int userId) {
        List<Flashcard> flashcards = new ArrayList<>();
        String sql = "SELECT * FROM flashcards WHERE user_id = ? AND is_learned = 0";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, userId);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                Flashcard flashcard = new Flashcard(
                        rs.getInt("id"),
                        rs.getInt("user_id"),
                        rs.getString("vocabulary"),
                        rs.getString("definition"),
                        false);
                flashcards.add(flashcard);
            }
        } catch (SQLException e) {
            System.err.println("Failed to get unlearned flashcards!");
            e.printStackTrace();
        }
        return flashcards;
    }


}
