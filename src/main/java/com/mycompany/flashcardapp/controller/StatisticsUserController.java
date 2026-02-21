package com.mycompany.flashcardapp.controller;

import com.mycompany.flashcardapp.database.FlashcardDAO;
import com.mycompany.flashcardapp.database.StreakDAO;
import com.mycompany.flashcardapp.database.TestResultDAO;
import com.mycompany.flashcardapp.database.TopicDAO;
import com.mycompany.flashcardapp.model.Flashcard;
import com.mycompany.flashcardapp.model.Streak;
import com.mycompany.flashcardapp.model.TestResult;
import com.mycompany.flashcardapp.model.Topic;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
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

    @FXML
    private VBox testResultsContainer;
    @FXML
    private Label lblNoResults;

    private final FlashcardDAO flashcardDAO = new FlashcardDAO();
    private final TopicDAO topicDAO = new TopicDAO();
    private final StreakDAO streakDAO = new StreakDAO();
    private final TestResultDAO testResultDAO = new TestResultDAO();

    @FXML
    public void initialize() {
        int userId = SessionManager.getInstance().getCurrentUser().getId();
        loadStatistics(userId);
        loadStreak(userId);
        loadTestHistory(userId);
    }

    private void loadStatistics(int userId) {
        List<Flashcard> allFlashcards = flashcardDAO.getAllFlashcards(userId);
        int total = allFlashcards.size();
        long learned = allFlashcards.stream().filter(Flashcard::isLearned).count();

        lblTotalWords.setText(String.valueOf(total));
        lblLearnedWords.setText(String.valueOf(learned));

        List<Topic> topics = topicDAO.getAllTopics(userId);
        lblTotalTopics.setText(String.valueOf(topics.size()));

        double avg = testResultDAO.getAverageScore(userId);
        if (avg < 0) {
            lblAvgScore.setText("--");
        } else {
            lblAvgScore.setText(String.format("%.0f%%", avg));
        }
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

    private void loadTestHistory(int userId) {
        List<TestResult> results = testResultDAO.getResultsByUser(userId);

        if (results.isEmpty()) {
            if (lblNoResults != null)
                lblNoResults.setVisible(true);
            return;
        }

        if (lblNoResults != null)
            lblNoResults.setVisible(false);

        for (TestResult r : results) {
            HBox row = buildResultRow(r);
            testResultsContainer.getChildren().add(row);
        }
    }

    private HBox buildResultRow(TestResult r) {
        HBox row = new HBox();
        row.setPadding(new Insets(7, 10, 7, 10));
        row.setStyle("-fx-background-color: #F8F8F8; -fx-background-radius: 6;");

        Label lblType = new Label(r.getDisplayName());
        lblType.setPrefWidth(155);
        lblType.setStyle("-fx-font-size: 12;");

        Label lblScore = new Label(r.getScoreText());
        lblScore.setPrefWidth(85);
        lblScore.setStyle("-fx-font-size: 12; -fx-font-weight: bold;");

        String pct = String.format("%.0f%%", r.getPercentage());
        Label lblPct = new Label(pct);
        lblPct.setPrefWidth(65);
        String color = r.getPercentage() >= 80 ? "#2E7D32"
                : r.getPercentage() >= 50 ? "#E65100"
                        : "#C62828";
        lblPct.setStyle("-fx-font-size: 12; -fx-font-weight: bold; -fx-text-fill: " + color + ";");

        Label lblDate = new Label(r.getDateDisplay());
        lblDate.setStyle("-fx-font-size: 11; -fx-text-fill: #757575;");

        row.getChildren().addAll(lblType, lblScore, lblPct, lblDate);
        return row;
    }

    @FXML
    void handleBack(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/MainMenu.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setMaximized(false);
            stage.setScene(new Scene(root, 1280, 720));
            stage.setTitle("Flashcard Learning - Menu chính");
            stage.setMaximized(true);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Không thể quay về MainMenu");
        }
    }
}
