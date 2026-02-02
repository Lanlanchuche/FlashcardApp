package com.mycompany.flashcardapp.controller;

import com.mycompany.flashcardapp.database.TopicDAO;
import com.mycompany.flashcardapp.model.Topic;
import com.mycompany.flashcardapp.model.User;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.List;

public class VocabularyManagementController {

    @FXML
    private Button addTopicButton;

    @FXML
    private Button backButton;

    @FXML
    private Button searchButton;

    @FXML
    private TextField searchTopicTextField;

    @FXML
    private ListView<Topic> topicListView;

    private ObservableList<Topic> topicsList = FXCollections.observableArrayList();

    private FilteredList<Topic> filteredTopics;

    private User currentUser;
    private final TopicDAO topicDAO = new TopicDAO();

    public void initialize() {
        filteredTopics = new FilteredList<>(topicsList, p -> true);
        topicListView.setItems(filteredTopics);

        topicListView.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2) {
                Topic selected = topicListView.getSelectionModel().getSelectedItem();
                if (selected != null) {
                    openVocabularyOfTopic(selected);
                }
            }
        });
    }

    public void setUser(User user) {
        this.currentUser = user;
        loadTopics();
    }

    private void loadTopics() {
        if (currentUser == null) return;
        List<Topic> topics = topicDAO.getAllTopics(currentUser.getId());
        topicsList.setAll(topics);

    }

    @FXML
    void handleSearchTopic(ActionEvent event) {
        String searchText = searchTopicTextField.getText().trim().toLowerCase();

        filteredTopics.setPredicate(topic -> {
            if (searchText.isEmpty()) return true;
            return topic.getName().toLowerCase().contains(searchText);
        });

    }

    @FXML
    void handleAddTopic(ActionEvent event) {
        try{
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/topic.fxml"));
            Parent root = loader.load();


            AddTopicController controller = loader.getController();

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setMaximized(false);
            stage.setScene(new Scene(root, 1280, 720));
            stage.setTitle("Thêm chủ đề");
            stage.setMaximized(true);
            stage.show();
        }
        catch (Exception e){
            e.printStackTrace();
            System.err.println("Không thể chuyển qua màn hình Thêm topic");

        }

    }

    @FXML
    void handleBack(ActionEvent event) {
        try{
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/MainMenu.fxml"));
            Parent root = loader.load();


            MainMenuController controller = loader.getController();

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setMaximized(false);
            stage.setScene(new Scene(root, 1280, 720));
            stage.setTitle("Flashcard Learning - Menu Chính");
            stage.setMaximized(true);
            stage.show();
        }
        catch(Exception e){
            e.printStackTrace();
            System.err.println("Không thể quay ve MainMenu");
        }

    }

    @FXML
    private void openVocabularyOfTopic(Topic topic) {
    }



}
