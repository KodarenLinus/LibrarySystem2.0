package com.mycompany.library_system.Controllers;

import com.mycompany.library_system.Logic.ItemManagement.AddBook;
import com.mycompany.library_system.Logic.ItemManagement.GetCategories;
import com.mycompany.library_system.Logic.ItemManagement.GetGenres;
import com.mycompany.library_system.Logic.ItemManagement.GetPublisher;
import com.mycompany.library_system.Models.Book;
import com.mycompany.library_system.Models.Category;
import com.mycompany.library_system.Models.Genre;
import com.mycompany.library_system.Models.Publisher;
import com.mycompany.library_system.Utils.AlertHandler;
import com.mycompany.library_system.Utils.ChangeWindow;
import java.sql.SQLException;
import java.util.ArrayList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListCell;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.TextField;

/**
 * Controller för att lägga till nya böcker i bibliotekssystemet.
 * Hanterar användarinmatning, validering och databasinmatning.
 * 
 * Fungerar tillsammans med AddBook.fxml.
 * 
 * @author Linus, Emil, Oliver, Viggo
 */
public class AddBookController {

    // Instans av AlertHandler för att visa meddelanden
    private AlertHandler alertHandler = new AlertHandler();
    
    // Texter till popup-meddelanden
    private String title;
    private String header; 
    private String content;
    
    // FXML-kopplingar till gränssnittets komponenter
    @FXML
    private ComboBox<Category> Category;

    @FXML
    private ComboBox<Genre> Genre;
    
    @FXML
    private ComboBox<Publisher> Publisher;
    
    @FXML
    private TextField ISBN;

    @FXML
    private TextField Location;

    @FXML
    private TextField Title;

    @FXML
    private ToggleButton addNewBook;

    /**
     * Navigerar användaren till vyn för att välja objekt att lägga till (t.ex. bok eller DVD).
     *
     * @param event Händelsen som triggas av användarens klick på "Tillbaka"-knappen.
     */
    @FXML
    void GoToItem(ActionEvent event) {
        String fxmlf = "AddItemsChoice.fxml";
        ChangeWindow changeWindow = new ChangeWindow();
        changeWindow.windowChange(event, fxmlf);
    }

    /**
     * Försöker skapa och lägga till en ny bok efter att ha validerat fälten.
     * Visar en alert vid lyckad registrering eller om något fält är felaktigt ifyllt.
     *
     * @param event Händelsen som triggas när användaren klickar på "Lägg till bok".
     */
    @FXML
    void addBook(ActionEvent event) {
        if (!isFormValid()) {
            title = "Alla fält är inte ifyllda";
            header = "Du måste fylla i alla fälten"; 
            content = "Du har missat att fylla i ett eller flera fält. Se till att allt är ifyllt.";
            alertHandler.createAlert(title, header, content);
            return;
        }
        
        try {
            int isbn = Integer.parseInt(ISBN.getText());
            Category selectedCategory = Category.getValue();
            Genre selectedGenre = Genre.getValue();
            Publisher selectedPublisher = Publisher.getValue();

            Book book = new Book(
                Title.getText(), 
                Location.getText(), 
                isbn, 
                selectedCategory.getCategoryID(), 
                selectedCategory.getCategoryName(), 
                selectedGenre.getGenreID(), 
                selectedGenre.getGenreName(),
                selectedPublisher.getPublisherID()
            );
            
            AddBook addBook = new AddBook();
            addBook.insertBook(book);

            title = "Lyckades";
            header = "Boken har lagts till"; 
            content = "Grattis, du har lagt till en ny bok i systemet.";
            alertHandler.createAlert(title, header, content);
        } catch (NumberFormatException e) {
            title = "Ej tillåten input";
            header = "ISBN måste vara heltal"; 
            content = "Du skrev in ogiltiga tecken i ISBN. Använd endast siffror.";
            alertHandler.createAlert(title, header, content);
        }
    }

    /**
     * Initierar vyn när den öppnas.
     * Laddar genrer och kategorier, samt tar bort DVD-kategorin då den ej hör hemma här.
     *
     * @throws SQLException om databasen inte kan nås.
     */
    @FXML
    public void initialize() throws SQLException {
        // Anpassad cellrendering för kategorier
        Category.setCellFactory(list -> new ListCell<Category>() {
            @Override
            protected void updateItem(Category category, boolean empty) {
                super.updateItem(category, empty);
                setText((empty || category == null) ? null : category.toString());
            }
        });

        // Hämta alla kategorier och ta bort DVD (ID 2)
        int categoryIDForDVD = 2;
        GetCategories getCategories = new GetCategories();
        ArrayList<Category> allCategories = getCategories.getAllCategories();
        allCategories.removeIf(category -> category.getCategoryID() == categoryIDForDVD);
        Category.getItems().setAll(allCategories);

        // Anpassad cellrendering för genrer
        Genre.setCellFactory(list -> new ListCell<Genre>() {
            @Override
            protected void updateItem(Genre genre, boolean empty) {
                super.updateItem(genre, empty);
                setText((empty || genre == null) ? null : genre.toString());
            }
        });

        // Hämta och sätt genrer
        GetGenres getGenres = new GetGenres();
        ArrayList<Genre> allGenres = getGenres.getAllGenres();
        Genre.getItems().setAll(allGenres);
        
         // Anpassad cellrendering för genrer
        Publisher.setCellFactory(list -> new ListCell<Publisher>() {
            @Override
            protected void updateItem(Publisher publisher, boolean empty) {
                super.updateItem(publisher, empty);
                setText((empty || publisher == null) ? null : publisher.toString());
            }
        });

        // Hämta och sätt genrer
        GetPublisher getPublisher = new GetPublisher();
        ArrayList<Publisher> allPublisher = getPublisher.getAllPublishers();
        Publisher.getItems().setAll(allPublisher);
    }

    /**
     * Kontrollerar att alla formulärfält är ifyllda.
     *
     * @return true om alla fält är ifyllda, annars false.
     */
    private boolean isFormValid() {
        return !(Title.getText().isEmpty() 
              || Location.getText().isEmpty() 
              || ISBN.getText().isEmpty()
              || Category.getValue() == null 
              || Genre.getValue() == null
              || Publisher.getValue() == null);
    }
}
