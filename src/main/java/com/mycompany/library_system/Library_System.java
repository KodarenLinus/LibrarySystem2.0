/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package com.mycompany.library_system;

import javafx.application.Application;
import static javafx.application.Application.launch;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 *
 * Library System
 * 
 * Detta program simulerar ett bibliotekssystem där användare kan låna och återlämna objekt.
 * Systemet hanterar användare, lånebara objekt och håller koll på lån och återlämning.
 * Programmet kommunicerar med en relationsdatabas (MySQL).
 *
 * Startpunkten för programmet är denna klass.
 * 
 * @author Linus, Emil, Oliver, Viggo
 * @version 1.0
 * @since 2025-05-20
 * 
 */
public class Library_System extends Application {

   @Override
    public void start(Stage stage) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("LoginView.fxml"));
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.setTitle("Library System");
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
    
}
