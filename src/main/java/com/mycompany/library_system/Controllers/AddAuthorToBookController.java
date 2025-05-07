/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.library_system.Controllers;

import com.mycompany.library_system.Logic.AuthorMangement.AddAuthorToBook;
import com.mycompany.library_system.Models.Author;
import com.mycompany.library_system.Models.Book;
import com.mycompany.library_system.Search.SearchAuthor;
import com.mycompany.library_system.Utils.AlertHandler;
import com.mycompany.library_system.Utils.ObjectSession;
import com.mycompany.library_system.Utils.PopUpWindow;
import java.util.ArrayList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

/**
 *
 * @author Linus, Emil, Oliver, Viggo
 */
public class AddAuthorToBookController {
    
    String title;
    String header; 
    String content;
    AlertHandler alertHandler = new AlertHandler();
    private Object item = ObjectSession.getInstance().getCurrentItem();
    
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
            AddAuthorToBook addAuthorToBook = new AddAuthorToBook();
            ArrayList<Author> authors = new ArrayList<>(AuthorsToAddToBookList.getItems());

            boolean success = addAuthorToBook.insertToBookAuthor((Book)item, authors);

            if (success) {
                title = "Författare tillagda till " + item.toString();
                header = "Följande författare har lagts till:";
                content = authors.stream()
                        .map(a -> a.getFirstname() + " " + a.getLastname())
                        .reduce("", (s1, s2) -> s1 + "\n" + s2).trim();

                // Töm listan och uppdatera
                AuthorsToAddToBookList.getItems().clear();
                applyFilter();
            } else {
                title = "Misslyckades lägga till författare";
                header = "Databasfel uppstod";
                content = "Ingen författare kunde kopplas till boken.";
            }

            alertHandler.createAlert(title, header, content);

        } catch (ClassCastException e) {
            title = "Fel objekt valt";
            header = "Du försökte lägga till författare till något som inte är en bok.";
            content = "Vänligen välj en bok innan du lägger till författare.";
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
        // Ändrar label texten så användaren vett vilken bok de lägger till författare på
        LabelAddAuthorToBook.setText("Lägg till författare till " + item.toString());
        
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
        
        try {
            // Laddar in alla objekt vid start
            SearchAuthor searchAuthor = new SearchAuthor();
            ArrayList<Author> allAuthors = searchAuthor.search("", (Book)item);
            //allAuthors.removeAll(AuthorsToAddToBookList.getItems());
            AuthorList.getItems().setAll(allAuthors);
        } catch (ClassCastException e){
            title = "ClassCastExeption";
            header ="Du försökte casta ett objekt till book som inte är book"; 
            content = "Vänligen se till att koden gör det den ska";
            alertHandler.createAlert(title, header, content);
        }
        // Söker efter objekt i realtid och visar matchningar
        SearchAuthor.textProperty().addListener((observable, oldValue, newValue) -> {
            applyFilter();
        });
    }
    
    @FXML
    public void handleClose() {
        Stage stage = (Stage) SearchAuthor.getScene().getWindow();
        ObjectSession.getInstance().clear();
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

       ArrayList<Author> allAuthors = searchAuthor.search(searchTerm, (Book)item);

       // Viktigt: Filtrera bort redan tillagda författare
       allAuthors.removeAll(AuthorsToAddToBookList.getItems());

       AuthorList.getItems().setAll(allAuthors);
    }
}
