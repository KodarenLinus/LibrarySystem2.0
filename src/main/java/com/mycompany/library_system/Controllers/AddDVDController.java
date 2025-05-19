package com.mycompany.library_system.Controllers;

import com.mycompany.library_system.Logic.ItemManagement.AddDVD;
import com.mycompany.library_system.Logic.ItemManagement.GetDirectors;
import com.mycompany.library_system.Logic.ItemManagement.GetGenres;
import com.mycompany.library_system.Models.CategoryType;
import com.mycompany.library_system.Utils.ChangeWindow;
import com.mycompany.library_system.Models.DVD;
import com.mycompany.library_system.Models.Director;
import com.mycompany.library_system.Models.Genre;
import com.mycompany.library_system.Utils.AlertHandler;
import java.sql.SQLException;
import java.util.ArrayList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListCell;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;

/**
 * Controllerklass för att lägga till nya DVD:er i bibliotekssystemet.
 * Ansvarar för att hantera användarinmatning, validering och databasinmatning.
 * Fungerar tillsammans med AddDVD.fxml.
 * 
 * @author Linus, Emil, Oliver, Viggo
 */
public class AddDVDController {

    // Instans av AlertHandler för att visa meddelanden
    private AlertHandler alert = new AlertHandler();
    
    // Texter till popup-meddelanden
    private String title;
    private String header; 
    private String content;

    // FXML-kopplingar till gränssnittets komponenter
    @FXML
    private ComboBox<Director> Director;

    @FXML
    private ComboBox<Genre> Genre;

    @FXML
    private TextField Location;

    @FXML
    private TextField Title;

    /**
     * Navigerar tillbaka till valfönstret för att lägga till olika typer av objekt.
     * 
     * @param event ActionEvent från knapptryckning
     */
    @FXML
    void GoToItem(ActionEvent event) {
        String fxmlf = "AddItemsChoice.fxml";
        ChangeWindow changeWindow = new ChangeWindow();
        changeWindow.windowChange(event, fxmlf);
    }

    /**
     * Lägger till en ny DVD i databasen efter att ha validerat formuläret.
     * Om något fält saknas visas en varning. Om alla fält är korrekta
     * skapas ett nytt DVD-objekt och skickas till databasen
     *
     * @param event ActionEvent från knapptryckning
     */

    @FXML
    void addDVD(ActionEvent event) {
        if (!isFormValid()) {
            title = "Alla fält är inte ifyllda";
            header = "Du måste fylla i alla fälten"; 
            content = "Du har missat att fylla i ett eller flera fält, "
                    + "se till att alla fält är ifyllda innan du klickar dig vidare";
            alert.createAlert(title, header, content);
            return;
        }
        
        // Hämtar data från genre och category combo box
        Genre selectedGenre = Genre.getValue();
        Director selectedDirector = Director.getValue();

        AddDVD addDVD = new AddDVD();
        CategoryType dvd_ = CategoryType.DVD;
        
        // Skapar ett DVD objekt
        DVD dvd = new DVD(
            Title.getText(),
            Location.getText(),
            dvd_.getId(),
            dvd_.getDisplayName(),
            selectedGenre.getGenreID(),
            selectedGenre.getGenreName(),
            selectedDirector.getDirectorID()
        );
        
        //Skickar DVD objektet till AddDVD för att lägga till den i databasen
        addDVD.insertDVD(dvd);
        
        //En alert som säger att vi lyckas lägga till DVD i databasen
        title = "Lyckades";
        header = "DVD har lagts till"; 
        content = "Grattis, du har lagt till en ny DVD i systemet.";
        alert.createAlert(title, header, content);
       
    }

    /**
     * Initialiserar ComboBox-komponenterna med regissörer och genrer från databasen.
     * Denna metod körs automatiskt av JavaFX när scenen laddas.
     *
     * @throws SQLException om det uppstår ett fel vid hämtning av data från databasen
     */
    @FXML
    public void initialize() throws SQLException {
        Genre.setCellFactory(list -> new ListCell<Genre>() {
            @Override
            protected void updateItem(Genre genre, boolean empty) {
                super.updateItem(genre, empty);
                setText(empty || genre == null ? null : genre.toString());
            }
        });

        GetGenres getGenres = new GetGenres();
        ArrayList<Genre> allGenres = getGenres.getAllGenres();
        Genre.getItems().setAll(allGenres);

        Director.setCellFactory(list -> new ListCell<Director>() {
            @Override
            protected void updateItem(Director director, boolean empty) {
                super.updateItem(director, empty);
                setText(empty || director == null ? null : director.toString());
            }
        });

        GetDirectors getDirector = new GetDirectors();
        ArrayList<Director> allDirector = getDirector.getAllDirectors();
        Director.getItems().setAll(allDirector);
    }

    /**
     * Validerar att alla obligatoriska fält är ifyllda.
     * 
     * @return true om formuläret är korrekt ifyllt, annars false
     */
    private boolean isFormValid() {
        return !(Title.getText().isEmpty() || Location.getText().isEmpty() || Genre.getValue() == null);
    }
}
