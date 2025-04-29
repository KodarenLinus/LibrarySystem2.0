/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.library_system.Controllers;

import com.mycompany.library_system.Logic.AddAuthorToBook;
import com.mycompany.library_system.Models.Author;
import com.mycompany.library_system.Models.Book;
import com.mycompany.library_system.Models.Items;
import com.mycompany.library_system.Search.SearchAuthor;
import com.mycompany.library_system.Search.SearchItems;
import com.mycompany.library_system.Utils.AlertHandler;
import com.mycompany.library_system.Utils.ObjectSession;
import com.mycompany.library_system.Utils.PopUpWindow;
import java.util.ArrayList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

/**
 *
 * @author Linus
 */
public class AddAuthorToBookController {
    
    @FXML
    private ListView<Author> AuthorList;

    @FXML
    private ListView<Author> AuthorsToAddToBookList;
    
    @FXML
    private TextField SearchAuthor;

    @FXML
    private Label LabelAddAuthorToBook;

    @FXML
    void AddAuthor(ActionEvent event) {
        String fmxlf = "CreateAuthor.fxml";
        PopUpWindow popUpWindow = new PopUpWindow();
        popUpWindow.popUp(event, fmxlf);
    }
    
    @FXML
    void AddAuthorsToBook(ActionEvent event) {
        try {
            Object item = ObjectSession.getCurrentItem();
            AddAuthorToBook addAuthorToBook = new AddAuthorToBook();
            ArrayList<Author> authors = new ArrayList<>(AuthorsToAddToBookList.getItems());
            addAuthorToBook.insertToBookAuthor((Book)item, authors);
        } catch (ClassCastException e) {
            // Berättar om vi castar ett objekt som inte är en book!!
            String title = "Du försökte lägga till en Författare på ett objekt som inte är en book";
            String header ="Du kan enbart lägga till författare på böcker"; 
            String content = "Vänligen gå tillbaka och testa välja ett nytt object.";
            AlertHandler alertHandler = new AlertHandler();
            alertHandler.createAlert(title, header, content);
        }
        

    }
    
        @FXML
    void addToBookAuthorList(MouseEvent event) {
        Author selectedItem = AuthorList.getSelectionModel().getSelectedItem();

        if (selectedItem != null) {
            AuthorList.getItems().remove(selectedItem);                  
            AuthorsToAddToBookList.getItems().add(selectedItem);         
            applyFilter();                                               
        }
    }

    @FXML
    void RemoveFromBookAuthorList(MouseEvent event) {
         Author selectedItem = AuthorsToAddToBookList.getSelectionModel().getSelectedItem();

        if (selectedItem != null) {
            AuthorsToAddToBookList.getItems().remove(selectedItem);
            applyFilter();
        }
    }

    
    @FXML
    public void initialize()  {
        
        // Visar titel för varje objekt i listan
        AuthorList.setCellFactory(list -> new ListCell<Author>() {
            @Override
            protected void updateItem(Author item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(item.toString()); 
                }
            }
        });
        
        // Laddar in alla objekt vid start
        SearchAuthor searchAuthor = new SearchAuthor();
        ArrayList<Author> allAuthors = searchAuthor.search("");
        //allAuthors.removeAll(AuthorsToAddToBookList.getItems());
        AuthorList.getItems().setAll(allAuthors);
        
        // Söker efter objekt i realtid och visar matchningar
        SearchAuthor.textProperty().addListener((observable, oldValue, newValue) -> {
            applyFilter();
        });
    }
    
    @FXML
    public void handleClose() {
        Stage stage = (Stage) SearchAuthor.getScene().getWindow();
        ObjectSession.clear();
        stage.close();
    }
    
    
    /**
    * 
    * kollar om filter är applicerad och uppdaterar listan med items.
    * 
    */
    private void applyFilter() {
       String searchTerm = SearchAuthor.getText();
       SearchAuthor searchAuthor = new SearchAuthor();

       ArrayList<Author> allAuthors = searchAuthor.search(searchTerm);

       // Viktigt: Filtrera bort redan tillagda författare
       allAuthors.removeAll(AuthorsToAddToBookList.getItems());

       AuthorList.getItems().setAll(allAuthors);
    }
}
