/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.library_system.Controllers;

import com.mycompany.library_system.Logic.AddDVD;
import com.mycompany.library_system.Logic.GetCategories;
import com.mycompany.library_system.Logic.GetGenres;
import com.mycompany.library_system.Models.Category;
import com.mycompany.library_system.Utils.ChangeWindow;
import com.mycompany.library_system.Models.DVD;
import com.mycompany.library_system.Models.Genre;
import com.mycompany.library_system.Utils.PopUpWindow;
import java.sql.SQLException;
import java.util.ArrayList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListCell;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

/**
 *
 * @author Linus
 */
public class AddDVDController {

    @FXML
    private ComboBox<Genre> Genre;
    
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
    void SelectGenre(MouseEvent event) {

    }

    @FXML
    void addDVD(ActionEvent event) {
        Genre selectedGenre = Genre.getValue();
        PopUpWindow popUpWindow = new PopUpWindow();
        String fxmlf = "newDVDPop.fxml";
        popUpWindow.popUp(event, fxmlf);
        
        AddDVD addDVD = new AddDVD();
        DVD dvd = new DVD(Title.getText(), Location.getText(), 2, "DVD", selectedGenre.getGenreID(), selectedGenre.getGenreName());
        addDVD.insertDVD(dvd);
    }
    
    @FXML
    public void initialize() throws SQLException  {
        
        Genre.setCellFactory(list -> new ListCell<Genre>() {
            @Override
            protected void updateItem(Genre genre, boolean empty) {
                super.updateItem(genre, empty);
                if (empty || genre == null) {
                    setText(null);
                } else {
                    setText(genre.toString()); 
                }
            }
        });
        
        GetGenres getGenres = new GetGenres();
        ArrayList<Genre> allGenres = getGenres.getAllGenres();
        Genre.getItems().setAll(allGenres);
        
    }

}
