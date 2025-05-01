/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.library_system.Utils;

import javafx.event.Event;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * Verktygsklass som visar en popup-fönster i JavaFX baserat på en FXML-fil.
 * Används för att öppna nya fönster som modala dialogrutor i gränssnittet.
 * 
 * Exempel: popup för att lägga till användare, visa information etc.
 * 
 * @author Linus, Emil, Oliver, Viggo
 */
public class PopUpWindow {

    /**
     * Visar ett popup-fönster baserat på en angiven FXML-fil.
     * Fönstret är modal, vilket innebär att användaren måste stänga det
     * innan hen kan återgå till huvudfönstret.
     * 
     * @param event Eventet som triggar popupen (ex. knapptryckning)
     * @param fxmlFil Namnet på FXML-filen (inklusive .fxml), ex: "AddCustomer.fxml"
     */
    public void popUp(Event event, String fxmlFil) {
        try {
            // Laddar FXML-filen och skapar root-noden
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/mycompany/library_system/" + fxmlFil));
            Parent root = loader.load();

            // Skapar ett nytt popup-fönster (Stage)
            Stage stage = new Stage();
            stage.setTitle("Popup-fönster");

            // Sätter innehållet (Scene) till popupen
            stage.setScene(new Scene(root));

            // Visar popupen och blockerar tills användaren stänger det
            stage.showAndWait();

        } catch (Exception e) {
            // Skriver ut felmeddelande vid laddningsfel
            System.err.println("Fel vid öppning av popup: " + fxmlFil);
            e.printStackTrace();
        }
    }
}
