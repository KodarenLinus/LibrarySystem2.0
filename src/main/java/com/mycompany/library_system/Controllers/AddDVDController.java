/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.library_system.Controllers;

import com.mycompany.library_system.Logic.AddDVD;
import com.mycompany.library_system.Logic.GetDirectors;
import com.mycompany.library_system.Logic.GetGenres;
import com.mycompany.library_system.Models.CategoryType;
import com.mycompany.library_system.Utils.ChangeWindow;
import com.mycompany.library_system.Models.DVD;
import com.mycompany.library_system.Models.Director;
import com.mycompany.library_system.Models.Genre;
import com.mycompany.library_system.Utils.AlertHandler;
import com.mycompany.library_system.Utils.PopUpWindow;
import java.sql.SQLException;
import java.util.ArrayList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListCell;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.scene.input.MouseEvent;

/**
 *
 * @author Linus, Emil, Oliver, Viggo
 */
public class AddDVDController {
    
      @FXML
    private ComboBox<Director> Director;

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
    void addDVD(ActionEvent event) {
        
        String title;
        String header; 
        String content;
        
        // Kollar att alla fält är ifyllda!
        if (!isFormValid()) {
            title = "Alla fält är inte ifyllda";
            header ="Du måste fylla i alla fälten"; 
            content = "Du har missat att fylla i ett eller flera fält, "
                    + "se till att alla fält är ifyllda innan du klickar dig vidare";
            AlertHandler alertHandler = new AlertHandler();
            alertHandler.createAlert(title, header, content);
            return;
        }
        
        Genre selectedGenre = Genre.getValue();
        Director selectedDirector = Director.getValue();
        PopUpWindow popUpWindow = new PopUpWindow();
        String fxmlf = "newDVDPop.fxml";
        popUpWindow.popUp(event, fxmlf);
        
        AddDVD addDVD = new AddDVD();
        CategoryType dvd_ = CategoryType.DVD;
        DVD dvd = new DVD(Title.getText(), Location.getText(), dvd_.getId(), dvd_.getDisplayName(), selectedGenre.getGenreID(), selectedGenre.getGenreName(), selectedDirector.getDirectorID());
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
        
        Director.setCellFactory(list -> new ListCell<Director>() {
            @Override
            protected void updateItem(Director director, boolean empty) {
                super.updateItem(director, empty);
                if (empty || director == null) {
                    setText(null);
                } else {
                    setText(director.toString()); 
                }
            }
        });
        
        GetDirectors getDirector = new GetDirectors();
        ArrayList<Director> allDirector = getDirector.getAllDirectors();
        Director.getItems().setAll(allDirector);
        
    }
    
    private boolean isFormValid() {
        return !(Title.getText().isEmpty() || Location.getText().isEmpty() || Genre.getValue() == null);
    }

}
