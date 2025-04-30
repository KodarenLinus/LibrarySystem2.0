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
 *
 * @author Linus, Emil, Oliver Viggo
 */
public class PopUpWindow {
    
    /**
     * 
     * @param event
     * @param fmxlf 
     */
    public void popUp(Event event, String fmxlf) {
         try {
            // Ladda FXML-filen för popupen
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/mycompany/library_system/" + fmxlf));
            Parent root = loader.load();  // Här laddas rootkomponenten från FXML-filen

            // Skapa en ny Stage (popup)
            Stage stage = new Stage();
            stage.setTitle("Popup Title");

            // Sätt scenen för popupen
            stage.setScene(new Scene(root));

            // Visa popupen och vänta på att den stängs
            stage.showAndWait();
            
        } catch (Exception e) {
            
            e.printStackTrace();
        }
    }
}
