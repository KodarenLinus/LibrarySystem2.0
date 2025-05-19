package com.mycompany.library_system.Controllers;

import com.mycompany.library_system.Utils.ChangeWindow;
import com.mycompany.library_system.Login.Session;
import java.io.IOException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;

/**
 * Controllerklass för kundens huvudvy i bibliotekssystemet.
 * Hanterar navigering till funktioner som lån, reservationer, samt utloggning.
 * 
 * Fungerar tillsammans med CustomerView.fxml.
 * 
 * @author Linus, Emil, Oliver, Viggo
 */
public class CustomerStartController {

    /**
     * Navigerar till vyn där kunden kan se sina aktuella lån.
     * 
     * @param event ActionEvent som triggas vid knapptryckning
     */
    @FXML
    void GoToMyLoans(ActionEvent event) {
        String fxmlf = "MyLoans.fxml";
        ChangeWindow changeWindow = new ChangeWindow();
        changeWindow.windowChange(event, fxmlf);
    }

    /**
     * Navigerar till vyn för att reservera ett objekt i biblioteket.
     * 
     * @param event ActionEvent som triggas vid knapptryckning
     */
    @FXML
    void GoToReserveItem(ActionEvent event) {
        String fxmlf = "ReserveItem.fxml";
        ChangeWindow changeWindow = new ChangeWindow();
        changeWindow.windowChange(event, fxmlf);
    }

    /**
     * Navigerar till vyn för att låna ett objekt.
     * 
     * @param event ActionEvent som triggas vid knapptryckning
     * @throws IOException om fxml-filen inte kan laddas
     */
    @FXML
    void loanItem(ActionEvent event) throws IOException {
        String fxmlf = "LoanItem.fxml";
        ChangeWindow changeWindow = new ChangeWindow();
        changeWindow.windowChange(event, fxmlf);
    }

    /**
     * Loggar ut den aktuella användaren genom att rensa sessionen
     * och navigera tillbaka till inloggningsvyn.
     * 
     * @param event ActionEvent som triggas vid knapptryckning
     * @throws IOException om fxml-filen inte kan laddas
     */
    @FXML
    void logOut(ActionEvent event) throws IOException {
        Session.getInstance().clear(); // Rensar användarsessionen
        String fxmlf = "LoginView.fxml";
        ChangeWindow changeWindow = new ChangeWindow();
        changeWindow.windowChange(event, fxmlf);
    }
}
