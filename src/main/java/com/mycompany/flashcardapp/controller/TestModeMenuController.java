package com.mycompany.flashcardapp.controller;

import com.mycompany.flashcardapp.model.User;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;

public class TestModeMenuController {

    @FXML
    private Button buttonRandomTest;

    @FXML
    private Button buttonTopicTest;

    @FXML
    private Label instructionLabel;

    private User currentUser;

    @FXML
    void handleRandomTest(ActionEvent event) {
        try{
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/RandomTest.fxml"));
            Parent root = loader.load();

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setMaximized(false);
            stage.setScene(new Scene(root, 1280, 720));
            stage.setMaximized(true);
            stage.show();
        }
        catch(Exception e){
            e.printStackTrace();
            System.err.println("Loi chuyen sang muc kiem tra");
        }

    }

    @FXML
    void handleTopicTest(ActionEvent event) {
        try{
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/TopicTest.fxml"));
            Parent root = loader.load();

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setMaximized(false);
            stage.setScene(new Scene(root, 1280, 720));
            stage.setMaximized(true);
            stage.show();
        }
        catch(Exception e){
            e.printStackTrace();
            System.err.println("Loi chuyen sang muc kiem tra");
        }


    }

    public void setUser(User user) {
        this.currentUser = user;
        if (user != null && instructionLabel != null) {
            instructionLabel.setText( user.getUsername() + " hãy chọn hình thức kiểm tra để bắt đầu :3");
        }
    }

}
