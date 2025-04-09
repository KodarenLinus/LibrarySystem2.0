/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package com.mycompany.libary_system;

import javafx.application.Application;
import static javafx.application.Application.launch;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 *
 * @author Linus
 */
public class Libary_System extends Application {

   @Override
    public void start(Stage stage) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("test.fxml"));
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.setTitle("Library System");
            stage.show();
        } catch (Exception e) {
            e.printStackTrace(); // 🔍 Key to debugging FXML load problems
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
    
}
