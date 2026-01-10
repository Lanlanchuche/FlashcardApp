/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package com.mycompany.flashcardapp;

import com.mycompany.flashcardapp.database.DatabaseConnection;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 *
 * @author Admin
 */
public class MyFlashcardApp extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        try {
            // Initialize database connection
            DatabaseConnection.getInstance();

            // Hiện màn hình đăng nhập
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/Login.fxml"));
            Parent root = loader.load();

            // Setup the scene
            Scene scene = new Scene(root, 500, 600);

            // Configure the stage
            stage.setTitle("Flashcard Learning - Đăng nhập");
            stage.setScene(scene);
            stage.setResizable(false);
            stage.show();

            System.out.println("Application started successfully!");
        } catch (Exception e) {
            System.err.println("Failed to start application!");
            e.printStackTrace();
        }
    }
}
