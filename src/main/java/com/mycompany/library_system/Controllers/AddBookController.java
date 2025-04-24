package com.mycompany.library_system.Controllers;

import com.mycompany.library_system.Logic.AddBook;
import com.mycompany.library_system.Logic.GetCategories;
import com.mycompany.library_system.Logic.GetGenres;
import com.mycompany.library_system.Models.Book;
import com.mycompany.library_system.Models.Category;
import com.mycompany.library_system.Models.Genre;
import com.mycompany.library_system.Models.Items;
import com.mycompany.library_system.Search.SearchItems;
import com.mycompany.library_system.Utils.ChangeWindow;
import com.mycompany.library_system.Utils.PopUpWindow;
import java.sql.SQLException;
import java.util.ArrayList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListCell;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

public class AddBookController {

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
    
    
    @FXML
    void GoToItem(ActionEvent event) {
       
        String fxmlf = "AddItemsChoice.fxml";
        ChangeWindow changeWindow = new ChangeWindow();
        changeWindow.windowChange(event, fxmlf);

    }

    @FXML
    void addBook(ActionEvent event) {
        try {
            int isbn = Integer.parseInt(ISBN.getText());
            Category selectedCategory = Category.getValue();
            Genre selectedGenre = Genre.getValue();
            
            PopUpWindow popUpWindow = new PopUpWindow();
            String fxmlf = "newBookPop.fxml";
            popUpWindow.popUp(event, fxmlf);

            // Efter att popupen har stängts, lägg till boken i databasen
            AddBook addBook = new AddBook();
            Book book = new Book(Title.getText(), Location.getText(), isbn, selectedCategory.getCategoryID(), selectedCategory.getCategoryName(), selectedGenre.getGenreID(), selectedGenre.getGenreName());
            addBook.insertBook(book);
        } catch (NumberFormatException e){
            // En pop-up som säger att vi måste skiva enbart heltal
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
        
        GetCategories getCategories = new GetCategories();
        ArrayList<Category> allCategories = getCategories.getAllCategories();
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
        
        GetGenres getGenres = new GetGenres();
        ArrayList<Genre> allGenres = getGenres.getAllGenres();
        Genre.getItems().setAll(allGenres);
     
    }
}
