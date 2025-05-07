package com.mycompany.library_system.Controllers;

import com.mycompany.library_system.Logic.AuthorMangement.AddAuthor;
import com.mycompany.library_system.Models.Author;
import com.mycompany.library_system.Utils.AlertHandler;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;

/**
 * Controllerklass för att skapa nya författare i bibliotekssystemet.
 * Hanterar inmatning av förnamn och efternamn och lagrar dem i databasen via AddAuthor-logik.
 * 
 * Samverkar med tillhörande FXML-fil där användaren kan skriva in ny författarinformation.
 * 
 * @author Linus, Emil, Oliver, Viggo
 */
public class CreateAuthorController {
    
     // Instans av AlertHandler för att visa meddelanden
    private AlertHandler alertHandler = new AlertHandler();
    
    // Texter till popup-meddelanden
    private String title;
    private String header; 
    private String content;
    
    // FXML-kopplingar till gränssnittets komponenter
    @FXML
    private TextField AuthorFirstname;

    @FXML
    private TextField AuthorLastname;

    /**
     * Skapar en ny författare baserat på användarens inmatade namn.
     * Skickar vidare informationen till AddAuthor för databasinmatning.
     * 
     * @param event ActionEvent som triggas vid knapptryckning
     */
    @FXML
    void CreateAuthor(ActionEvent event) {
         if (!isFormValid()) {
            title = "Alla fält är inte ifyllda";
            header = "Du måste fylla i alla fälten"; 
            content = "Du har missat att fylla i ett eller flera fält, "
                    + "se till att alla fält är ifyllda innan du klickar dig vidare";
            alertHandler.createAlert(title, header, content);
            return;
        }
        String firstname = AuthorFirstname.getText();
        String lastname = AuthorLastname.getText();
        Author author = new Author(firstname, lastname);
        AddAuthor addAuthor = new AddAuthor();
        addAuthor.insertAuthor(author);
        
        title = "Lyckades";
        header = "Customer har lagts till"; 
        content = "Grattis, du har lagt till en ny kund i systemet.";
        alertHandler.createAlert(title, header, content);
    }
    
     /**
     * Kontrollerar att alla formulärfält är ifyllda.
     *
     * @return true om alla fält är ifyllda, annars false.
     */
    private boolean isFormValid() {
        return !(AuthorFirstname.getText().isEmpty() || AuthorLastname.getText().isEmpty());
    }
}
