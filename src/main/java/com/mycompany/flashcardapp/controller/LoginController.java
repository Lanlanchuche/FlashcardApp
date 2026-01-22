package com.mycompany.flashcardapp.controller;

import com.mycompany.flashcardapp.database.UserDAO;
import com.mycompany.flashcardapp.model.User;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;

public class LoginController {
    @FXML
    private Label loginErrorLabel;

    @FXML
    private PasswordField passwordField;

    @FXML
    private TextField usernameField;

    private final UserDAO userDAO = new UserDAO();


    @FXML
    private void handleLogin(ActionEvent event) {
        String username = usernameField.getText();
        String password = passwordField.getText();

        if(username.isEmpty() || password.isEmpty()){
            showError("Tên đăng nhập và mật khẩu không được để trống");
            return;
        }

        User user = userDAO.login(username, password);
        if(user != null){
            try {
                // Load MainMenu
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/MainMenu.fxml"));
                Parent root = loader.load();

                // Get controller and pass user data
                MainMenuController controller = loader.getController();
                controller.setUser(user);

                // Navigate to MainMenu
                Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                stage.setMaximized(false);
                stage.setScene(new Scene(root, 1280, 720));
                stage.setTitle("Flashcard Learning - Menu Chính");
                stage.setMaximized(true);
                stage.show();
            } catch (Exception e) {
                e.printStackTrace();
                showError("Không thể mở màn hình menu chính!");
            }

        }
        else{
            showError("Tên đăng nhập hoặc mật khẩu không chính xác");
        }

    }
    @FXML
    void backToWelcome(ActionEvent event) {
        try{
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/Welcome.fxml"));
            Parent root = loader.load();

            Stage stage  = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setMaximized(true);
            stage.setTitle("Flashcard Learning - Welcome");
            stage.show();
        }
        catch(IOException e){
            e.printStackTrace();
            showError("Không thể trở về giao diện Welcome");
        }

    }


     private void showError(String message) {
        if(message != null) {
            loginErrorLabel.setText(message);
            loginErrorLabel.setStyle("-fx-text-fill:  #e74c3c;");
        }
    }

    @FXML
    void goToRegister(ActionEvent event) {
        try{
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/Register.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage)((javafx.scene.Node)event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Flashcard Learning - Đăng ký");
            stage.show();
        }
        catch (Exception e) {
            e.printStackTrace();
            showError("Không thể mở màn hình đăng ký!");
        }

    }

    @FXML
    void handleForgotPassword(ActionEvent event){

    }

}
