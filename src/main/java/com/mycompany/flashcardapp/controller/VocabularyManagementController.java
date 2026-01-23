package com.mycompany.flashcardapp.controller;

import com.mycompany.flashcardapp.database.FlashcardDAO;
import com.mycompany.flashcardapp.database.TopicDAO;
import com.mycompany.flashcardapp.model.Flashcard;
import com.mycompany.flashcardapp.model.Topic;
import com.mycompany.flashcardapp.model.User;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.List;
import java.util.Optional;


public class VocabularyManagementController {

    @FXML
    private Button backButton;
    @FXML
    private Label titleLabel;
    @FXML
    private StackPane contentPane;// cái này dùng để chứa cả khung topic và vocab, chuyển đổi 2 cái dễ

    @FXML
    private VBox topicsView;
    @FXML
    private TextField newTopicField;
    @FXML
    private Label topicCountLabel;
    @FXML
    private FlowPane topicsFlowPane;
    @FXML
    private HBox topicActionsPane;
    @FXML
    private Label selectedTopicLabel;
    @FXML
    private TextField editTopicField;

    @FXML
    private VBox flashcardsView;
    @FXML
    private Label currentTopicLabel;
    @FXML
    private Label flashcardCountLabel;
    @FXML
    private TextField vocabField;
    @FXML
    private TextField defField;
    @FXML
    private TextField searchField;
    @FXML
    private TableView<Flashcard> flashcardTable;
    @FXML
    private TableColumn<Flashcard, Integer> idColumn;
    @FXML
    private TableColumn<Flashcard, String> vocabularyColumn;
    @FXML
    private TableColumn<Flashcard, String> definitionColumn;

    private User currentUser;
    private Topic selectedTopic;
    private Topic currentViewingTopic;
    private Flashcard selectedFlashcard;

    private final TopicDAO topicDAO = new TopicDAO();
    private final FlashcardDAO flashcardDAO = new FlashcardDAO();

    private ObservableList<Topic> topicsList = FXCollections.observableArrayList();
    private ObservableList<Flashcard> flashcardsList = FXCollections.observableArrayList();
    private FilteredList<Flashcard> filteredFlashcards;

    private boolean isInFlashcardsView = false;



    @FXML
    public void initialize() {
        setupTableColumns();
        setupTableSelection();
        setupSearchFilter();

    }

    // cai nay sau se duoc goi o MainMenuController
    public void setUser(User user) {
        this.currentUser = user;
        loadTopics();
        showTopicsView();
    }

    private void showTopicsView() {
        isInFlashcardsView = false;
        titleLabel.setText("Chủ đề");

        topicsView.setVisible(true);
        flashcardsView.setVisible(false);
        flashcardsView.setManaged(false);// cai nay de toan bo HBox chi co topic
        clearTopicSelection();
    }
    private void loadTopics() {
        if (currentUser == null)
            return;

        List<Topic> topics = topicDAO.getAllTopics(currentUser.getId());
        topicsList.setAll(topics);

        topicCountLabel.setText(topics.size() + " chủ đề");
        renderTopicCards();
    }

    /*
    * Hàm này để hiển thị dsach ban đầu (chưa có topic),
    * khi có thao tác thì cập nhật dsach ngay lập tức
    * */
    private void renderTopicCards() {
        topicsFlowPane.getChildren().clear();

        for (Topic topic : topicsList) {
            VBox card = createTopicCard(topic);
            topicsFlowPane.getChildren().add(card);
        }

        if (topicsList.isEmpty()) {
            Label placeholder = new Label("📭 Chưa có chủ đề nào.\nHãy tạo chủ đề mới để bắt đầu!");
            placeholder.setStyle("-fx-font-size: 16; -fx-text-fill: #95a5a6; -fx-text-alignment: center;");
            placeholder.setWrapText(true);
            topicsFlowPane.getChildren().add(placeholder);
        }
    }


    private void showFlashcardsView(Topic topic) {
        if (topic == null)
            return;

        isInFlashcardsView = true;
        currentViewingTopic = topic;
        titleLabel.setText("Từ vựng - " + topic.getName());
        currentTopicLabel.setText(topic.getName());

        topicsView.setVisible(false);
        topicsView.setManaged(false);
        flashcardsView.setVisible(true);
        flashcardsView.setManaged(true);

        loadFlashcards(topic);
    }

    private VBox createTopicCard(Topic topic) {
        // Tạo card container
        VBox card = new VBox(10);
        card.setAlignment(Pos.CENTER);
        card.setPrefWidth(200);
        card.setPrefHeight(150);
        card.setPadding(new Insets(20));
        card.setStyle("-fx-background-color: white; -fx-background-radius: 15; " +
                "-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.1), 10, 0, 0, 3); " +
                "-fx-cursor: hand;");

        // Icon
        Label icon = new Label("📁");
        icon.setStyle("-fx-font-size: 36;");

        // Tên topic
        Label name = new Label(topic.getName());
        name.setStyle("-fx-font-size: 15; -fx-font-weight: bold; -fx-text-fill: #2c3e50;");
        name.setWrapText(true);
        name.setMaxWidth(170);

        // Số lượng thẻ
        Label count = new Label(topic.getFlashcardCount() + " thẻ");
        count.setStyle("-fx-font-size: 12; -fx-text-fill: #7f8c8d;");

        card.getChildren().addAll(icon, name, count);

        // === CLICK VÀO CARD → MỞ DANH SÁCH TỪ VỰNG ===
        card.setOnMouseClicked(e -> showFlashcardsView(topic));

        // Hiệu ứng hover đơn giản
        card.setOnMouseEntered(e -> card.setStyle(
                "-fx-background-color: #667eea; -fx-background-radius: 15; " +
                        "-fx-effect: dropshadow(three-pass-box, rgba(102,126,234,0.4), 12, 0, 0, 4); " +
                        "-fx-cursor: hand;"));
        card.setOnMouseExited(e -> card.setStyle(
                "-fx-background-color: white; -fx-background-radius: 15; " +
                        "-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.1), 10, 0, 0, 3); " +
                        "-fx-cursor: hand;"));

        return card;
    }

    private void selectTopic(Topic topic) {
        this.selectedTopic = topic;
        selectedTopicLabel.setText("Đang chọn: " + topic.getName());
        editTopicField.setText(topic.getName());

        topicActionsPane.setVisible(true);
        topicActionsPane.setManaged(true);

        renderTopicCards();
    }

    private void clearTopicSelection() {
        this.selectedTopic = null;
        editTopicField.clear();
        topicActionsPane.setVisible(false);
        topicActionsPane.setManaged(false);
        renderTopicCards();
    }

    @FXML
    void handleCreateTopic(ActionEvent event) {
        String topicName = newTopicField.getText().trim();

        if (topicName.isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Thiếu thông tin", "Vui lòng nhập tên chủ đề!");
            return;
        }

        if (topicDAO.topicExists(currentUser.getId(), topicName)) {
            showAlert(Alert.AlertType.WARNING, "Trùng tên", "Chủ đề '" + topicName + "' đã tồn tại!");
            return;
        }

        boolean success = topicDAO.addTopic(currentUser.getId(), topicName);
        if (success) {
            newTopicField.clear();
            loadTopics();
        } else {
            showAlert(Alert.AlertType.ERROR, "Lỗi", "Không thể tạo chủ đề. Vui lòng thử lại!");
        }
    }

    @FXML
    void handleEditTopic(ActionEvent event) {
        if (selectedTopic == null) {
            showAlert(Alert.AlertType.WARNING, "Chưa chọn", "Vui lòng nhấn vào một chủ đề để chọn!");
            return;
        }

        String newName = editTopicField.getText().trim();
        if (newName.isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Thiếu thông tin", "Vui lòng nhập tên mới cho chủ đề!");
            return;
        }

        if (newName.equals(selectedTopic.getName())) {
            showAlert(Alert.AlertType.INFORMATION, "Không thay đổi", "Tên mới giống tên cũ!");
            return;
        }

        boolean success = topicDAO.updateTopic(selectedTopic.getId(), newName);
        if (success) {
            clearTopicSelection();
            loadTopics();
        } else {
            showAlert(Alert.AlertType.ERROR, "Lỗi", "Không thể cập nhật chủ đề!");
        }
    }

    @FXML
    void handleDeleteTopic(ActionEvent event) {
        if (selectedTopic == null) {
            showAlert(Alert.AlertType.WARNING, "Chưa chọn", "Vui lòng nhấn vào một chủ đề để chọn!");
            return;
        }

        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
        confirm.setTitle("Xác nhận xóa");
        confirm.setHeaderText("Xóa chủ đề: " + selectedTopic.getName());
        confirm.setContentText(
                "Tất cả thẻ từ vựng trong chủ đề này sẽ được chuyển sang 'Không có chủ đề'.\nBạn có chắc chắn muốn xóa?");

        Optional<ButtonType> result = confirm.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            boolean success = topicDAO.deleteTopic(selectedTopic.getId());
            if (success) {
                clearTopicSelection();
                loadTopics();
                showAlert(Alert.AlertType.INFORMATION, "Thành công", "Đã xóa chủ đề!");
            } else {
                showAlert(Alert.AlertType.ERROR, "Lỗi", "Không thể xóa chủ đề!");
            }
        }
    }

    @FXML
    void handleCancelSelection(ActionEvent event) {
        clearTopicSelection();
    }

    private void setupTableColumns() {
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        vocabularyColumn.setCellValueFactory(new PropertyValueFactory<>("vocabulary"));
        definitionColumn.setCellValueFactory(new PropertyValueFactory<>("definition"));

        idColumn.setStyle("-fx-alignment: CENTER;");
    }


    private void setupTableSelection() {
        flashcardTable.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            selectedFlashcard = newVal;
            if (newVal != null) {
                vocabField.setText(newVal.getVocabulary());
                defField.setText(newVal.getDefinition());
            }
        });
    }

    private void setupSearchFilter() {
        filteredFlashcards = new FilteredList<>(flashcardsList, p -> true);

        searchField.textProperty().addListener((obs, oldVal, newVal) -> {
            filteredFlashcards.setPredicate(flashcard -> {
                if (newVal == null || newVal.isEmpty()) {
                    return true;
                }
                String lowerCaseFilter = newVal.toLowerCase();
                return flashcard.getVocabulary().toLowerCase().contains(lowerCaseFilter) ||
                        flashcard.getDefinition().toLowerCase().contains(lowerCaseFilter);
            });
            updateFlashcardCount();
        });

        flashcardTable.setItems(filteredFlashcards);
    }


    private void loadFlashcards(Topic topic) {
        if (currentUser == null || topic == null)
            return;

        List<Flashcard> flashcards = flashcardDAO.getFlashcardsByTopic(currentUser.getId(), topic.getId());
        flashcardsList.setAll(flashcards);
        updateFlashcardCount();
    }


    private void updateFlashcardCount() {
        int total = flashcardsList.size();
        int filtered = filteredFlashcards.size();

        if (searchField.getText() != null && !searchField.getText().isEmpty()) {
            flashcardCountLabel.setText(filtered + "/" + total + " thẻ");
        } else {
            flashcardCountLabel.setText(total + " thẻ");
        }
    }

    @FXML
    void handleAddFlashcard(ActionEvent event) {
        String vocab = vocabField.getText().trim();
        String def = defField.getText().trim();

        if (vocab.isEmpty() || def.isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Thiếu thông tin", "Vui lòng nhập đầy đủ từ vựng và nghĩa!");
            return;
        }

        if (currentViewingTopic == null) {
            showAlert(Alert.AlertType.ERROR, "Lỗi", "Không xác định được chủ đề hiện tại!");
            return;
        }

        boolean success = flashcardDAO.addFlashcard(
                currentUser.getId(),
                vocab,
                def,
                currentViewingTopic.getId());

        if (success) {
            clearFlashcardForm();
            loadFlashcards(currentViewingTopic);
            loadTopics(); // Làm mới topic count
        } else {
            showAlert(Alert.AlertType.ERROR, "Lỗi", "Không thể thêm thẻ từ vựng!");
        }
    }

    @FXML
    void handleUpdateFlashcard(ActionEvent event) {
        if (selectedFlashcard == null) {
            showAlert(Alert.AlertType.WARNING, "Chưa chọn", "Vui lòng chọn một thẻ từ vựng trong bảng!");
            return;
        }

        String vocab = vocabField.getText().trim();
        String def = defField.getText().trim();

        if (vocab.isEmpty() || def.isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Thiếu thông tin", "Vui lòng nhập đầy đủ từ vựng và nghĩa!");
            return;
        }

        boolean success = flashcardDAO.updateFlashcard(
                selectedFlashcard.getId(),
                vocab,
                def,
                currentViewingTopic.getId());

        if (success) {
            clearFlashcardForm();
            loadFlashcards(currentViewingTopic);
        } else {
            showAlert(Alert.AlertType.ERROR, "Lỗi", "Không thể cập nhật thẻ từ vựng!");
        }
    }

    @FXML
    void handleDeleteFlashcard(ActionEvent event) {
        if (selectedFlashcard == null) {
            showAlert(Alert.AlertType.WARNING, "Chưa chọn", "Vui lòng chọn một thẻ từ vựng trong bảng!");
            return;
        }

        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
        confirm.setTitle("Xác nhận xóa");
        confirm.setHeaderText("Xóa thẻ: " + selectedFlashcard.getVocabulary());
        confirm.setContentText("Bạn có chắc chắn muốn xóa thẻ từ vựng này?");

        Optional<ButtonType> result = confirm.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            boolean success = flashcardDAO.deleteFlashcard(selectedFlashcard.getId());
            if (success) {
                clearFlashcardForm();
                loadFlashcards(currentViewingTopic);
                loadTopics();
            } else {
                showAlert(Alert.AlertType.ERROR, "Lỗi", "Không thể xóa thẻ từ vựng!");
            }
        }
    }


    private void clearFlashcardForm() {
        vocabField.clear();
        defField.clear();
        selectedFlashcard = null;
        flashcardTable.getSelectionModel().clearSelection();
    }

    @FXML
    void handleBack(ActionEvent event) {
        if (isInFlashcardsView) {
            showTopicsView();
            loadTopics();
        } else {
            navigateToMainMenu(event);
        }
    }

    @FXML
    void handleLogout(ActionEvent event) {
        navigateToWelcome(event);
    }

    private void navigateToMainMenu(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/MainMenu.fxml"));
            Parent root = loader.load();

            MainMenuController controller = loader.getController();
            controller.setUser(currentUser);

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setMaximized(false);
            stage.setScene(new Scene(root, 1280, 720));
            stage.setTitle("Flashcard Learning - Menu Chính");
            stage.setMaximized(true);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Lỗi", "Không thể quay về menu chính!");
        }
    }

    private void navigateToWelcome(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/Welcome.fxml"));
            Parent root = loader.load();

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setMaximized(false);
            stage.setScene(new Scene(root, 1280, 720));
            stage.setTitle("Flashcard Learning - Welcome");
            stage.setMaximized(true);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Lỗi", "Không thể đăng xuất!");
        }
    }

    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }



}
