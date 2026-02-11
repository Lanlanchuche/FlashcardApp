package com.mycompany.flashcardapp.controller;

import com.mycompany.flashcardapp.database.TopicDAO;
import com.mycompany.flashcardapp.model.Topic;
import com.mycompany.flashcardapp.model.User;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.List;

public class ReviewTopicController {

    @FXML
    private TextField searchField;

    @FXML
    private ListView<Topic> topicListView;

    @FXML
    private Button backButton;


    private final TopicDAO topicDAO = new TopicDAO();
    private final ObservableList<Topic> masterData = FXCollections.observableArrayList();
    private FilteredList<Topic> filteredData;
    private User currentUser;

    @FXML
    public void initialize() {
        // 1. Lấy thông tin người dùng hiện tại
        currentUser = SessionManager.getInstance().getCurrentUser();
        if (currentUser == null) {
            showAlert("Lỗi", "Không tìm thấy thông tin người dùng! Vui lòng đăng nhập lại.");
            return;
        }


        if (backButton != null) {
            backButton.setOnAction(event -> handleBack());
        }


        configureListView();


        loadTopics();
        setupSearchFilter();


        topicListView.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2) {
                Topic selectedTopic = topicListView.getSelectionModel().getSelectedItem();
                if (selectedTopic != null) {
                    openReviewCard(selectedTopic);
                }
            }
        });
    }

    private void configureListView() {
        topicListView.setCellFactory(param -> new ListCell<Topic>() {
            @Override
            protected void updateItem(Topic item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                    setStyle("-fx-background-color: white;");
                } else {
                    // Hiển thị: "Tên Topic (Số lượng từ)"
                    setText(item.getName() + " (" + item.getFlashcardCount() + " từ)");

                    // Style CSS trực tiếp cho từng dòng
                    setStyle("-fx-padding: 10; -fx-font-size: 16px; -fx-cursor: hand; -fx-border-color: transparent transparent #eee transparent;");
                }
            }
        });
    }

    private void loadTopics() {
        List<Topic> topics = topicDAO.getAllTopics(currentUser.getId());
        masterData.setAll(topics);
    }

    private void setupSearchFilter() {

        filteredData = new FilteredList<>(masterData, p -> true);


        topicListView.setItems(filteredData);


        if (searchField != null) {
            searchField.textProperty().addListener((observable, oldValue, newValue) -> {
                filterTopics(newValue);
            });
        }
    }

    private void filterTopics(String keyword) {
        filteredData.setPredicate(topic -> {
            // Nếu từ khóa rỗng -> Hiển thị tất cả
            if (keyword == null || keyword.isEmpty()) {
                return true;
            }


            String lowerCaseFilter = keyword.toLowerCase();
            return topic.getName().toLowerCase().contains(lowerCaseFilter);
        });
    }

    private void handleBack() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/MainMenu.fxml"));
            Parent root = loader.load();

            Stage stage = (Stage) topicListView.getScene().getWindow();
            stage.setMaximized(false);
            stage.setScene(new Scene(root, 1280, 720));
            stage.setTitle("Flashcard Learning - Menu Chính");
            stage.setMaximized(true);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Lỗi", "Không thể quay lại Menu chính.");
        }
    }


    private void openReviewCard(Topic topic) {
        if (topic.getFlashcardCount() == 0) {
            showAlert("Thông báo", "Chủ đề '" + topic.getName() + "' chưa có từ vựng nào để ôn tập!");
            return;
        }

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/ReviewCard.fxml"));
            Parent root = loader.load();



            try {
                // Giả định bạn sẽ đặt tên controller là ReviewCardController
                // Uncomment dòng dưới nếu bạn đã tạo Controller đó
                /* ReviewCardController controller = loader.getController();
                if (controller != null) {
                    controller.setTopic(topic);
                }
                */
            } catch (Exception e) {
                System.err.println("Cảnh báo: Không thể truyền data sang ReviewCardController (có thể do chưa tạo Controller).");
            }


            Stage stage = (Stage) topicListView.getScene().getWindow();
            stage.setMaximized(false);
            stage.setScene(new Scene(root, 1280, 720));
            stage.setTitle("Ôn tập: " + topic.getName());
            stage.setMaximized(true);
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Lỗi", "Không thể mở màn hình ôn tập: " + e.getMessage());
        }
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}