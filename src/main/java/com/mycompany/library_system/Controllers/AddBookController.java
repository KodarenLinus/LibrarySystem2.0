package com.mycompany.library_system.Controllers;

import com.mycompany.library_system.Logic.ItemManagement.AddBook;
import com.mycompany.library_system.Logic.ItemManagement.GetCategories;
import com.mycompany.library_system.Logic.ItemManagement.GetGenres;
import com.mycompany.library_system.Models.Book;
import com.mycompany.library_system.Models.Category;
import com.mycompany.library_system.Models.Genre;
import com.mycompany.library_system.Utils.AlertHandler;
import com.mycompany.library_system.Utils.ChangeWindow;
import com.mycompany.library_system.Utils.PopUpWindow;
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
 * Hanterar användarinput, validering och inmatning till databasen.
 * 
 * @author Linus, Emil, Oliver, Viggo
 */
public class AddBookController {

    private AlertHandler alertHandler = new AlertHandler();
    private String title;
    private String header; 
    private String content;
    
    @FXML
    private ComboBox<Category> Category;

    @FXML
    private ComboBox<Genre> Genre;
    
    @FXML
    private TextField ISBN;

    @FXML
    private TextField Location;

    @FXML
    private TextField Title;
   
    
    @FXML
    private ToggleButton addNewBook;
    
    /**
    * Navigerar användaren till föregående vy där man väljer typ av objekt att lägga till.
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
    * Försöker lägga till en ny bok efter att ha validerat användarens input.
    * Visar popup-fönster vid framgång, eller felmeddelande vid ogiltig input.
    *
    * @param event ActionEvent från användarens knapptryckning
    */
    @FXML
    void addBook(ActionEvent event) {

        // Kontrollera att alla fält är ifyllda
        if (!isFormValid()) {
            title = "Alla fält är inte ifyllda";
            header ="Du måste fylla i alla fälten"; 
            content = "Du har missat att fylla i ett eller flera fält, "
                    + "se till att alla fält är ifyllda innan du klickar dig vidare";
            
            alertHandler.createAlert(title, header, content);
            return;
        }
        
        try {
            // Hämta och konvertera data från formuläret
            int isbn = Integer.parseInt(ISBN.getText());
            Category selectedCategory = Category.getValue();
            Genre selectedGenre = Genre.getValue();
            
            // Borde kanske vara en alert
            title = "lyckades";
            header ="Du har har lagt till bok"; 
            content = "Grattis du har lagt till en bok";
            
            alertHandler.createAlert(title, header, content);
            
            
            // Skapa bok-objekt och spara i databasen
            Book book = new Book(
                    Title.getText(), 
                    Location.getText(), 
                    isbn, selectedCategory.getCategoryID(), 
                    selectedCategory.getCategoryName(), 
                    selectedGenre.getGenreID(), 
                    selectedGenre.getGenreName()
            );
            AddBook addBook = new AddBook();
            addBook.insertBook(book);
        } catch (NumberFormatException e){
            // ISBN måste vara ett heltal – visa felmeddelande
            title = "Ej tillåten input";
            header ="Enbart heltal i ISBN fältet"; 
            content = "Du skrev in tecken som inte är siffror. Försök igen med ett giltigt ISBN.";
            AlertHandler alertHandler = new AlertHandler();
            alertHandler.createAlert(title, header, content);
        }
       
    }
    
    @FXML
    public void initialize() throws SQLException  {
        
        Category.setCellFactory(list -> new ListCell<Category>() {
            @Override
            protected void updateItem(Category category, boolean empty) {
                super.updateItem(category, empty);
                if (empty || category == null) {
                    setText(null);
                } else {
                    setText(category.toString()); 
                }
            }
        });
        
        int categoryIDForDVD = 2; // sätt variabelns värde till dvd kategorin
        
        // Initiera kategori-dropdown och ta bort DVD från listan
        GetCategories getCategories = new GetCategories();
        ArrayList<Category> allCategories = getCategories.getAllCategories();
        // Ta bort DVD-kategorin från listan eftersom detta formulär endast är avsett för böcker
        allCategories.removeIf(category -> category.getCategoryID() == categoryIDForDVD);
        Category.getItems().setAll(allCategories);
        
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
        
        // Initiera genre-dropdown
        GetGenres getGenres = new GetGenres();
        ArrayList<Genre> allGenres = getGenres.getAllGenres();
        Genre.getItems().setAll(allGenres);
     
    }
    
    /**
    * Validerar att alla fält i formuläret är korrekt ifyllda.
    *
    * @return true om alla fält är ifyllda, annars false
    */
    private boolean isFormValid() {
        return !(Title.getText().isEmpty() || Location.getText().isEmpty() || ISBN.getText().isEmpty()
             || Category.getValue() == null || Genre.getValue() == null);
    }
}
