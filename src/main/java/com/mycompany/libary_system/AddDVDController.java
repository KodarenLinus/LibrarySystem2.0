/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.libary_system;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.stage.Stage;

/**
 *
 * @author Linus
 */
public class AddDVDController {

    @FXML
    private TextField Location;

    @FXML
    private TextField Title;

    @FXML
    private ToggleButton addNewDVD;

    @FXML
    void GoToItem(ActionEvent event) {
          
        String fxmlf = "AddItemsChoice.fxml";
        ChangeWindow changeWindow = new ChangeWindow();
        changeWindow.windowChange(event, fxmlf);

    }

    @FXML
    void addDVD(ActionEvent event) {
        
        try {
        // Ladda FXML-filen för popupen
        FXMLLoader loader = new FXMLLoader(getClass().getResource("newDVDPop.fxml"));
        Parent root = loader.load();  // Här laddas rootkomponenten från FXML-filen

        // Skapa en ny Stage (popup)
        Stage stage = new Stage();
        stage.setTitle("Popup Title");

        // Sätt scenen för popupen
        stage.setScene(new Scene(root));

        // Visa popupen och vänta på att den stängs
        stage.showAndWait();

        AddDVD addDVD = new AddDVD();
        DVD dvd = new DVD(Title.getText(), Location.getText());
        addDVD.insertDVD(dvd);
        } catch (Exception e) {
        e.printStackTrace();
    }
    }

}
