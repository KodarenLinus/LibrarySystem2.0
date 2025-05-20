package com.mycompany.library_system.Controllers;

import com.mycompany.library_system.Utils.ChangeWindow;
import java.io.IOException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;

/**
 * Controllerklass för vyn där användaren väljer vilken typ av objekt som ska läggas till i biblioteket.
 * Tillåter navigering till vyer för att lägga till böcker eller DVD:er, samt återgång till huvudmenyn.
 * 
 * Fungerar tillsammans med AddItemsChoice.fxml.
 * 
 * @author Linus, Emil, Oliver, Viggo
 */
public class AddItemsChoiceController {

    /**
     * Navigerar till fönstret för att lägga till en ny bok.
     * 
     * @param event ActionEvent som triggas vid knapptryckning
     */
    @FXML
    void AddBook(ActionEvent event) {
        String fxmlf = "AddBook.fxml";
        ChangeWindow changeWindow = new ChangeWindow();
        changeWindow.windowChange(event, fxmlf);
    }

    /**
     * Navigerar till fönstret för att lägga till en ny DVD.
     * 
     * @param event ActionEvent som triggas vid knapptryckning
     */
    @FXML
    void AddDvD(ActionEvent event) {
        String fxmlf = "AddDVD.fxml";
        ChangeWindow changeWindow = new ChangeWindow();
        changeWindow.windowChange(event, fxmlf);
    }

    /**
     * Går tillbaka till huvudmenyn.
     * 
     * @param event ActionEvent som triggas vid knapptryckning
     */
    @FXML
    void BackToMenu(ActionEvent event) {
        String fxmlf = "StaffStart.fxml";
        ChangeWindow changeWindow = new ChangeWindow();
        changeWindow.windowChange(event, fxmlf);
    }
}
