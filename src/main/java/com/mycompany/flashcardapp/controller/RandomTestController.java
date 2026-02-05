package com.mycompany.flashcardapp.controller;

import com.mycompany.flashcardapp.database.FlashcardDAO;
import com.mycompany.flashcardapp.model.Flashcard;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.*;

/**
 * Controller for Random Test Mode with 3 question types:
 * - Fill-in-the-blank (Điền từ)
 * - Multiple choice (Trắc nghiệm)
 * - Word scramble (Xáo từ)
 */
public class RandomTestController {

    // Common UI elements
    @FXML
    private Label questionLabel;
    @FXML
    private Label questionTypeLabel;
    @FXML
    private Label feedbackLabel;
    @FXML
    private Label scoreLabel;
    @FXML
    private Label progressLabel;
    @FXML
    private ProgressBar progressBar;
    @FXML
    private Button submitButton;
    @FXML
    private Button nextButton;

    // Fill-in-the-blank UI
    @FXML
    private TextField answerField;

    // Multiple choice UI
    @FXML
    private VBox multipleChoicePane;
    @FXML
    private Button optionButton1;
    @FXML
    private Button optionButton2;
    @FXML
    private Button optionButton3;

    // Word scramble UI
    @FXML
    private VBox wordScramblePane;
    @FXML
    private Label scrambledLabel;
    @FXML
    private TextField unscrambleField;

    private FlashcardDAO flashcardDAO;
    private List<QuestionData> testQuestions;
    private int currentQuestionIndex = 0;
    private int correctAnswers = 0;
    private int totalQuestions = 0;
    private QuestionData currentQuestion;

    private enum QuestionType {
        DIEN_TU,
        TRAC_NGHIEM,
        XAO_TU
    }

    public class QuestionData {
        Flashcard flashcard;
        QuestionType type;
        List<String> options; //DIEN_TU
        int correctOptionIndex;
        String scrambleWord; //XAO_TU

        QuestionData(Flashcard flashcard, QuestionType type) {
            this.flashcard = flashcard;
            this.type = type;
        }
    }

    @FXML
    private void initialize() {
        flashcardDAO = new FlashcardDAO();
        loadTestFlashcards();
    }

    private void loadTestFlashcards() {
        int userId = SessionManager.getInstance().getCurrentUser().getId();
        List<Flashcard> allFlashcards = flashcardDAO.getAllFlashcards(userId);

        if(allFlashcards.isEmpty()) {
            showAlert("Bạn chưa tạo từ vựng nào để kiểm tra!");
            backtoTestMenu();
            return;
        }
        else{
            Collections.shuffle(allFlashcards);
            int limit = Math.min(10, allFlashcards.size());
            testQuestions = new ArrayList<>();


            for (int i = 0; i < limit; i++) {
                Flashcard flashcard = allFlashcards.get(i);//lay ra chi so
                QuestionType type;

                if(i < 4){
                    type = QuestionType.TRAC_NGHIEM;
                }
                else if(i < 7){
                    type = QuestionType.DIEN_TU;
                }
                else{
                    type = QuestionType.XAO_TU;
                }

                QuestionData questionData = new QuestionData(flashcard, type);
                if(type == QuestionType.TRAC_NGHIEM){
                    prepareMultipleChoiceData(questionData, allFlashcards);

                }
                 else if (type == QuestionType.XAO_TU) {
                    prepareWordScrambleData(questionData);
                 }

                 testQuestions.add(questionData);
            }


        }
        totalQuestions = testQuestions.size();
        currentQuestionIndex = 0;
        correctAnswers = 0;

        displayCurrentQuestion();
        updateProgress();

    }
    private void prepareMultipleChoiceData(QuestionData question, List<Flashcard> allFlashcards) {
        question.options = new ArrayList<>();

        question.options.add(question.flashcard.getDefinition());

        List<Flashcard> otherCards = new ArrayList<>(allFlashcards);
        otherCards.remove(question.flashcard);//bo dap an dung khoi danh sach dap an sai
        Collections.shuffle(otherCards);

        int wrongCount = 0;
        for (Flashcard card : otherCards) {
            if (wrongCount >= 2)
                break;
            if (!card.getDefinition().equals(question.flashcard.getDefinition())) {
                question.options.add(card.getDefinition());
                wrongCount++;
            }
        }
        while (question.options.size() < 3) {
            question.options.add("Đáp án ngẫu nhiên " + question.options.size());
        }

        String correctAnswer = question.flashcard.getDefinition();
        Collections.shuffle(question.options);
        question.correctOptionIndex = question.options.indexOf(correctAnswer);

    }
}
