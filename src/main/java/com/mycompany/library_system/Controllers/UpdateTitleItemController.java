/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.library_system.Controllers;

import com.mycompany.library_system.Logic.ItemManagement.UpdateItem;
import com.mycompany.library_system.Models.Items;
import com.mycompany.library_system.Utils.AlertHandler;
import com.mycompany.library_system.Utils.ObjectSession;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

/**
 * Controller klass som hanterar uppdatering av titlar för items
 * 
 * Fungerar tillsammans med UpdateTitleItem.fmxl
 * 
 * @author Linus, Emil, Oliver, Viggo
 */
public class UpdateTitleItemController {
    
    // Instans av AlertHandler för att visa meddelanden
    private AlertHandler alert = new AlertHandler();
    
    // Texter till popup-meddelanden
    private String title;
    private String header; 
    private String content;
    
    // Det objekt som för närvarande är valt att uppdateras
    private Object item = ObjectSession.getInstance().getCurrentItem();
    
    // FXML-kopplingar till gränssnittets komponenter
    @FXML
    private Label ItemTitle;

    @FXML
    private TextField Title;

    /**
     * Uppdaterar titeln för det valda objektet.
     * Validerar input och visar meddelande om titel saknas.
     *
     * @param event ActionEvent som triggas när användaren klickar på "Uppdatera"
     */
    @FXML
    void UpdateTitle(ActionEvent event) {
        String newTitle = Title.getText().trim();

        if (newTitle.isEmpty()) {
            title = "Felaktig titel";
            header = "Titel saknas";
            content = "Vänligen fyll i ett nytt titelvärde innan du uppdaterar.";
            alert.createAlert(title, header, content);
            return;
        }

        UpdateItem updateItem = new UpdateItem();
        updateItem.updateItemTitle((Items) item, newTitle);

        title = "Uppdaterad";
        header = "Titel uppdaterad";
        content = "Titeln har uppdaterats till: " + newTitle;
        alert.createAlert(title, header, content);
    }
    
    /**
     * Initialiserar vyn genom att visa aktuell titel för objektet.
     * Denna metod körs automatiskt när fxml-filen laddas.
     */
    @FXML
    public void initialize() {
        ItemTitle.setText("Ändra title på " + item.toString());
    }
}
