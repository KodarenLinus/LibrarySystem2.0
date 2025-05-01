/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.library_system.Utils;

import javafx.scene.control.Alert;

/**
 *
 * @author Linus, Emil, Oliver, Viggo
 */
public class AlertHandler {
    
    /**
     * En metod för att skapa alerts
     * 
     * @param title
     * @param header
     * @param content 
     */
    public void createAlert(String title, String header, String content) {
        // Visa alert-varning
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
