package com.mycompany.flashcardapp.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;

public class VocabularyManagementController {

    @FXML
    private TextField defField;

    @FXML
    private TableColumn<?, ?> definitionColumn;

    @FXML
    private TableView<?> flashcardTable;

    @FXML
    private TableColumn<?, ?> idColumn;

    @FXML
    private Button logoutButton;

    @FXML
    private TextField newTopicField;

    @FXML
    private TextField searchField;

    @FXML
    private Label statsLabel;

    @FXML
    private TableColumn<?, ?> topicColumn;

    @FXML
    private ComboBox<?> topicComboBox;

    @FXML
    private Label topicCountLabel;

    @FXML
    private ListView<?> topicListView;

    @FXML
    private TextField vocabField;

    @FXML
    private TableColumn<?, ?> vocabularyColumn;

    @FXML
    void backToMainMenu(ActionEvent event) {

    }

    @FXML
    void handleAdd(ActionEvent event) {

    }

    @FXML
    void handleClear(ActionEvent event) {

    }

    @FXML
    void handleCreateTopic(ActionEvent event) {

    }

    @FXML
    void handleDelete(ActionEvent event) {

    }

    @FXML
    void handleDeleteTopic(ActionEvent event) {

    }

    @FXML
    void handleEditTopic(ActionEvent event) {

    }

    @FXML
    void handleLogout(ActionEvent event) {

    }

    @FXML
    void handleRefresh(ActionEvent event) {

    }

    @FXML
    void handleUpdate(ActionEvent event) {

    }

}
