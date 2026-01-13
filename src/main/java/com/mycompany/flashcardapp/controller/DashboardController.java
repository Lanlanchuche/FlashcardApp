package com.mycompany.flashcardapp.controller;

import com.mycompany.flashcardapp.database.FlashcardDAO;
import com.mycompany.flashcardapp.model.Flashcard;
import com.mycompany.flashcardapp.model.User;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.util.Collections;
import java.util.List;

public class DashboardController {
    // --- KHAI BÁO BIẾN ---
    @FXML private TableView<Flashcard> flashcardTable;
    @FXML private TableColumn<Flashcard, String> vocabularyColumn;
    @FXML private TableColumn<Flashcard, String> definitionColumn;
    @FXML private TableColumn<Flashcard, Boolean> learnedColumn;
    @FXML private TextField vocabField, defField;
    @FXML private Label welcomeLabel;

    @FXML private StackPane flashcardPane;
    @FXML private VBox frontCard, backCard;
    @FXML private Label reviewVocabLabel, reviewDefLabel;

    @FXML private Label testVocabLabel, testResultLabel;
    @FXML private TextField testAnswerField;
    @FXML private Button submitTestButton, nextTestButton;

    @FXML private TextField timerMinutesField;
    @FXML private Label timerDisplayLabel;
    @FXML private Button startTimerButton, stopTimerButton;

    private final FlashcardDAO flashcardDAO = new FlashcardDAO();
    private User currentUser;
    private Flashcard selectedFlashcard;
    private List<Flashcard> studyCards, testCards;
    private Flashcard currentStudyCard, currentTestCard;
    private boolean isFrontVisible = true;
    private Timeline timeline;
    private int remainingSeconds = 0;

    @FXML
    public void initialize() {
        vocabularyColumn.setCellValueFactory(new PropertyValueFactory<>("vocabulary"));
        definitionColumn.setCellValueFactory(new PropertyValueFactory<>("definition"));
        learnedColumn.setCellValueFactory(new PropertyValueFactory<>("isLearned"));

        flashcardTable.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                selectedFlashcard = newVal;
                vocabField.setText(newVal.getVocabulary());
                defField.setText(newVal.getDefinition());
            }
        });
    }

    public void setUser(User user) {
        this.currentUser = user;
        if (currentUser != null) {
            welcomeLabel.setText("Xin chào, " + currentUser.getUsername() + "!");
            refreshAllData();
        }
    }

    private void refreshAllData() {
        loadFlashcards();
        initializeStudyMode();
        initializeTestMode();
    }

    // --- TAB QUẢN LÝ ---
    private void loadFlashcards() {
        if (currentUser == null) return;
        flashcardTable.setItems(FXCollections.observableArrayList(flashcardDAO.getAllFlashcards(currentUser.getId())));
    }

    @FXML private void handleAdd() {
        if (flashcardDAO.addFlashcard(currentUser.getId(), vocabField.getText(), defField.getText())) refreshAllData();
    }

    @FXML private void handleUpdate() {
        if (selectedFlashcard != null && flashcardDAO.updateFlashcard(selectedFlashcard.getId(), vocabField.getText(), defField.getText())) refreshAllData();
    }

    @FXML private void handleDelete() {
        if (selectedFlashcard != null && flashcardDAO.deleteFlashcard(selectedFlashcard.getId())) refreshAllData();
    }

    @FXML private void handleMarkLearned() {
        if (selectedFlashcard != null) {
            flashcardDAO.markAsLearned(selectedFlashcard.getId(), !selectedFlashcard.isLearned());
            refreshAllData();
        }
    }

    // --- TAB HỌC TẬP ---
    private void initializeStudyMode() {
        studyCards = flashcardDAO.getUnlearnedFlashcards(currentUser.getId());
        Collections.shuffle(studyCards);
        loadNextStudyCard();
    }

    private void loadNextStudyCard() {
        if (studyCards == null || studyCards.isEmpty()) {
            reviewVocabLabel.setText("🎉 Hoàn thành!");
            reviewDefLabel.setText("Đã học xong.");
            if (flashcardPane != null) flashcardPane.setDisable(true);
            return;
        }
        flashcardPane.setDisable(false);
        currentStudyCard = studyCards.get(0);
        reviewVocabLabel.setText(currentStudyCard.getVocabulary());
        reviewDefLabel.setText(currentStudyCard.getDefinition());
        resetFlip();
    }

    @FXML private void handleFlipCard() {
        isFrontVisible = !isFrontVisible;
        frontCard.setVisible(isFrontVisible);
        backCard.setVisible(!isFrontVisible);
    }

    @FXML private void handleNextCard() {
        if (studyCards != null && !studyCards.isEmpty()) studyCards.remove(0);
        loadNextStudyCard();
    }

    @FXML private void handleRestartStudy() { initializeStudyMode(); }

    @FXML private void handleMarkLearnedInStudy() {
        if (currentStudyCard != null && flashcardDAO.markAsLearned(currentStudyCard.getId(), true)) refreshAllData();
    }

    private void resetFlip() {
        isFrontVisible = true;
        frontCard.setVisible(true);
        backCard.setVisible(false);
    }

    // --- TAB ÔN TẬP ---
    private void initializeTestMode() {
        testCards = flashcardDAO.getUnlearnedFlashcards(currentUser.getId());
        Collections.shuffle(testCards);
        loadNextTestCard();
    }

    private void loadNextTestCard() {
        if (testVocabLabel == null) return;
        if (testCards == null || testCards.isEmpty()) {
            testVocabLabel.setText("Hết từ!");
            testResultLabel.setText("Hoàn thành bài ôn tập.");
            testAnswerField.setDisable(true);
            if (submitTestButton != null) submitTestButton.setDisable(true);
            return;
        }
        testAnswerField.setDisable(false);
        if (submitTestButton != null) submitTestButton.setDisable(false);
        currentTestCard = testCards.get(0);
        testVocabLabel.setText(currentTestCard.getVocabulary());
        testAnswerField.clear();
        testResultLabel.setText("");
    }

    @FXML private void handleSubmitTestAnswer() {
        if (currentTestCard == null) return;
        if (testAnswerField.getText().trim().equalsIgnoreCase(currentTestCard.getDefinition())) {
            testResultLabel.setText("✓ Chính xác!");
            testResultLabel.setStyle("-fx-text-fill: green;");
        } else {
            testResultLabel.setText("✗ Sai! Đáp án: " + currentTestCard.getDefinition());
            testResultLabel.setStyle("-fx-text-fill: red;");
        }
        if (submitTestButton != null) submitTestButton.setDisable(true);
    }

    @FXML private void handleNextTestCard() {
        if (testCards != null && !testCards.isEmpty()) testCards.remove(0);
        loadNextTestCard();
    }

    @FXML private void handleRestartTest() { initializeTestMode(); }

    @FXML private void handleMarkLearnedInTest() {
        if (currentTestCard != null && flashcardDAO.markAsLearned(currentTestCard.getId(), true)) refreshAllData();
    }

    // --- ĐỒNG HỒ ---
    @FXML private void handleStartTimer() {
        try {
            remainingSeconds = Integer.parseInt(timerMinutesField.getText().trim()) * 60;
            if (timeline != null) timeline.stop();
            timeline = new Timeline(new KeyFrame(Duration.seconds(1), e -> {
                remainingSeconds--;
                timerDisplayLabel.setText(String.format("%02d:%02d", remainingSeconds / 60, remainingSeconds % 60));
                if (remainingSeconds <= 0) {
                    timeline.stop();
                    new Alert(Alert.AlertType.INFORMATION, "Xong! Nghỉ ngơi nhé.").show();
                    handleStopTimer();
                }
            }));
            timeline.setCycleCount(Timeline.INDEFINITE);
            timeline.play();
            startTimerButton.setDisable(true);
        } catch (Exception e) {
            new Alert(Alert.AlertType.ERROR, "Nhập số phút hợp lệ!").show();
        }
    }

    @FXML private void handleStopTimer() {
        if (timeline != null) timeline.stop();
        startTimerButton.setDisable(false);
        timerDisplayLabel.setText("00:00");
    }

    @FXML private void handleLogout(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/view/Login.fxml"));
            ((Stage)((Node)event.getSource()).getScene().getWindow()).setScene(new Scene(root, 600, 400));
        } catch (Exception e) { e.printStackTrace(); }
    }
}