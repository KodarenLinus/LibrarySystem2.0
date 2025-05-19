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
 * Controller-klass som hanterar vyn för att lägga till författare till en bok.
 * Användaren kan söka, välja och koppla författare till en viss bok.
 * 
 * Fungerar tillsammans med AddBookToAuthor.fxml.
 * 
 * @author Linus, Emil, Oliver, Viggo
 */
public class AddAuthorToBookController {

    // Instans av AlertHandler för att visa meddelanden
    private AlertHandler alert = new AlertHandler();
    
    // Texter till popup-meddelanden
    private String title;
    private String header; 
    private String content;

    // Bokobjekt som författare ska läggas till på
    private Object item = ObjectSession.getInstance().getCurrentItem();

    // FXML-kopplingar till gränssnittets komponenter
    @FXML
    private ListView<Author> AuthorList;

    @FXML
    private ListView<Author> AuthorsToAddToBookList;

    @FXML
    private TextField SearchAuthor;

    @FXML
    private Label LabelAddAuthorToBook;

    /**
     * Öppnar ett nytt fönster för att skapa en ny författare.
     * 
     * @event -> Händelsen som triggas när användaren klickar på "Lägg till författare".
     */
    @FXML
    void AddAuthor(ActionEvent event) {
        String fxmlFile = "CreateAuthor.fxml";
        PopUpWindow popUpWindow = new PopUpWindow();
        popUpWindow.popUp(event, fxmlFile);
    }

    /**
     * Lägger till valda författare till den valda boken i databasen.
     * Visar alert beroende på om åtgärden lyckades eller misslyckades.
     * 
     * @event -> Händelsen som triggas när användaren klickar på "Lägg till författare till book".
     */
    @FXML
    void AddAuthorsToBook(ActionEvent event) {
        try {
            AddAuthorToBook addAuthorToBook = new AddAuthorToBook();
            ArrayList<Author> authors = new ArrayList<>(AuthorsToAddToBookList.getItems());

            boolean success = addAuthorToBook.insertToBookAuthor((Book) item, authors);

            if (success) {
                title = "Författare tillagda till " + item.toString();
                header = "Följande författare har lagts till:";
                content = authors.stream()
                        .map(a -> a.getFirstname() + " " + a.getLastname())
                        .reduce("", (s1, s2) -> s1 + "\n" + s2).trim();

                // Rensar listan efter tillägg
                AuthorsToAddToBookList.getItems().clear();
                applyFilter();
            } else {
                title = "Misslyckades lägga till författare";
                header = "Databasfel uppstod";
                content = "Ingen författare kunde kopplas till boken.";
            }

            alert.createAlert(title, header, content);

        } catch (ClassCastException e) {
            title = "Fel objekt valt";
            header = "Du försökte lägga till författare till något som inte är en bok.";
            content = "Vänligen välj en bok innan du lägger till författare.";
            alert.createAlert(title, header, content);
        }
    }

    /**
     * Flyttar vald författare från listan till listan över författare som ska läggas till.
     * 
     * @event Händelsen som triggas när användaren klickar på ett objekt i listan.
     */
    @FXML
    void addToBookAuthorList(MouseEvent event) {
        Author selectedItem = AuthorList.getSelectionModel().getSelectedItem();

        if (selectedItem != null) {
            AuthorList.getItems().remove(selectedItem);
            AuthorsToAddToBookList.getItems().add(selectedItem);
            applyFilter();
        }
    }

    /**
     * Tar bort en författare från listan över de som ska läggas till.
     * 
     * @event Händelsen som triggas när användaren klickar på ett objekt i listan.
     */
    @FXML
    void RemoveFromBookAuthorList(MouseEvent event) {
        Author selectedItem = AuthorsToAddToBookList.getSelectionModel().getSelectedItem();

        if (selectedItem != null) {
            AuthorsToAddToBookList.getItems().remove(selectedItem);
            applyFilter();
        }
    }

    /**
     * Initialiserar vyn vid öppning.
     * Sätter label för aktuell bok, laddar författarlistan och kopplar filter till sökfältet.
     */
    @FXML
    public void initialize() {
        // Ändrar labeltext så att användaren vet vilken bok författare läggs till på
        LabelAddAuthorToBook.setText("Lägg till författare till " + item.toString());

        // Visar författarens namn i listan
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
            // Laddar in alla författare som inte redan är kopplade till boken
            SearchAuthor searchAuthor = new SearchAuthor();
            ArrayList<Author> allAuthors = searchAuthor.search("", (Book) item);
            AuthorList.getItems().setAll(allAuthors);
        } catch (ClassCastException e) {
            title = "ClassCastException";
            header = "Objektet kunde inte tolkas som en bok";
            content = "Vänligen kontrollera att ett bokobjekt är valt.";
            alert.createAlert(title, header, content);
        }

        // Lyssnar efter ändringar i sökfältet
        SearchAuthor.textProperty().addListener((observable, oldValue, newValue) -> {
            applyFilter();
        });
    }

    /**
     * Stänger fönstret och rensar sessionen.
     */
    @FXML
    public void handleClose() {
        Stage stage = (Stage) SearchAuthor.getScene().getWindow();
        ObjectSession.getInstance().clear();
        stage.close();
    }

    /**
     * Filtrerar listan med författare utifrån användarens sökterm och
     * utesluter redan valda författare.
     */
    private void applyFilter() {
        String searchTerm = SearchAuthor.getText();
        SearchAuthor searchAuthor = new SearchAuthor();

        ArrayList<Author> allAuthors = searchAuthor.search(searchTerm, (Book) item);
        allAuthors.removeAll(AuthorsToAddToBookList.getItems()); // Undvik dubbletter

        AuthorList.getItems().setAll(allAuthors);
    }
}
