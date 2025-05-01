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
    
    /**
     * Startmetoden körs automatiskt vid applikationsstart.
     * Här laddas FXML-filen och det första fönstret visas.
     *
     * @param stage Det primära fönstret för JavaFX-applikationen
     */
    @Override
    public void start(Stage stage) {
        try {
            // Laddar inloggningsvyn från FXML
            Parent root = FXMLLoader.load(getClass().getResource("StartView.fxml"));
            
            // Skapar en scen med root-noden och lägger till i fönstret
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.setTitle("Library System");
            
            // Visar huvudfönstret
            stage.show();
        } catch (Exception e) {
            // Fel vid laddning av FXML visas i konsolen
            System.err.println("Kunde inte ladda LoginView.fxml");
            e.printStackTrace();
        }
    }

    /**
     * Main-metoden startar JavaFX-applikationen.
     *
     * @param args Kommandoradsargument (om några)
     */
    public static void main(String[] args) {
        launch(args);
    }
    
}
