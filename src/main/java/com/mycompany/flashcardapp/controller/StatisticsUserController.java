package com.mycompany.flashcardapp.controller;

import com.mycompany.flashcardapp.database.FlashcardDAO;
import com.mycompany.flashcardapp.database.StreakDAO;
import com.mycompany.flashcardapp.database.TopicDAO;
import com.mycompany.flashcardapp.model.Flashcard;
import com.mycompany.flashcardapp.model.Streak;
import com.mycompany.flashcardapp.model.Topic;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.List;

public class StatisticsUserController {

    @FXML
    private Label lblTotalWords;
    @FXML
    private Label lblLearnedWords;
    @FXML
    private Label lblTotalTopics;
    @FXML
    private Label lblAvgScore;

    @FXML
    private Label lblCurrentStreak;
    @FXML
    private Label lblLongestStreak;
    @FXML
    private Label lblFreezeCount;

    private final FlashcardDAO flashcardDAO = new FlashcardDAO();
    private final TopicDAO topicDAO = new TopicDAO();
    private final StreakDAO streakDAO = new StreakDAO();

    @FXML
    public void initialize() {
        int userId = SessionManager.getInstance().getCurrentUser().getId();
        loadStatistics(userId);
        loadStreak(userId);
    }

    private void loadStatistics(int userId) {
        List<Flashcard> allFlashcards = flashcardDAO.getAllFlashcards(userId);
        int total = allFlashcards.size();
        long learned = allFlashcards.stream().filter(Flashcard::isLearned).count();

        lblTotalWords.setText(String.valueOf(total));
        lblLearnedWords.setText(String.valueOf(learned));

        List<Topic> topics = topicDAO.getAllTopics(userId);
        lblTotalTopics.setText(String.valueOf(topics.size()));

        lblAvgScore.setText("--");
    }

    private void loadStreak(int userId) {
        Streak streak = streakDAO.getUserStreak(userId);
        if (streak != null) {
            lblCurrentStreak.setText(String.valueOf(streak.getCurrentStreak()));
            lblLongestStreak.setText(String.valueOf(streak.getLongestStreak()));
            lblFreezeCount.setText(String.valueOf(streak.getFreezeCount()));
        } else {
            lblCurrentStreak.setText("0");
            lblLongestStreak.setText("0");
            lblFreezeCount.setText("0");
        }
    }

    @FXML
    void handleBack(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/MainMenu.fxml"));
            Parent root = loader.load();

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setMaximized(false);
            stage.setScene(new Scene(root, 1280, 720));
            stage.setMaximized(false);
            stage.setTitle("Flashcard Learning - Menu chính");
            stage.setMaximized(true);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Không thể quay về MainMenu");
        }
    }
}
