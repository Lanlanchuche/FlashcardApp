package com.mycompany.flashcardapp.controller;

import com.mycompany.flashcardapp.database.TopicDAO;
import com.mycompany.flashcardapp.model.User;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class AddTopicController {

    @FXML
    private Button continueButton;

    @FXML
    private TextField topicNameField;

    private User currentUser;
    TopicDAO topicDAO = new TopicDAO();

    @FXML
    void addTopicName(ActionEvent event) {
        String topicName = topicNameField.getText();

        if (topicName.isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Thông báo", "Vui lòng nhập tên chủ đề!");
            return;
        }
        topicDAO.addTopic(this.currentUser.getId(),topicName);

        goToVocabularyOfTopic(event);

    }

    public void goToVocabularyOfTopic(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/VocabularyOfTopic.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage)((javafx.scene.Node)event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root, 1280, 720));
            stage.setMaximized(true);
            stage.setTitle("Quản lí từ vựng");
            stage.show();
        }
        catch (Exception e) {
            e.printStackTrace();
            System.err.println("Không thể chuyển sang màn hình Quản lí từ vựng");
        }

    }
    private void showAlert(Alert.AlertType type, String title, String content) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    public void setUser(User user) {
        this.currentUser = user;
    }

}
