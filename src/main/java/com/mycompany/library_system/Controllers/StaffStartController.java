/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.library_system.Controllers;

import com.mycompany.library_system.Login.Session;
import com.mycompany.library_system.Utils.ChangeWindow;
import java.io.IOException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;

/**
 * Controller-klass för huvudmenyn i bibliotekssystemet för administratörer eller personal.
 * Hanterar navigation till olika delar av systemet såsom kundhantering, artikelhantering,
 * samt möjlighet att logga ut.
 *
 * Fungerar tillsammans med StaffStart.fxml
 * 
 * @author Linus, Emil, Oliver, Viggo
 */
public class StaffStartController {

    /**
     * Navigerar till vyn för hantering av artiklar (t.ex. redigering, borttagning).
     *
     * @param event Klick på "Hantera Artiklar"-knappen
     */
    @FXML
    void GoToItemHandling(ActionEvent event) {
        String fxmlf = "HandleItems.fxml";
        ChangeWindow changeWindow = new ChangeWindow();
        changeWindow.windowChange(event, fxmlf);
    }

    /**
     * Navigerar till vyn för att välja vilken typ av artikel som ska läggas till (bok, DVD etc.).
     *
     * @param event Klick på "Lägg till Artikel"-knappen
     * @throws IOException Om FXML-filen inte kan laddas
     */
    @FXML
    void addItem(ActionEvent event) throws IOException {
        String fxmlf = "AddItemsChoice.fxml";
        ChangeWindow changeWindow = new ChangeWindow();
        changeWindow.windowChange(event, fxmlf);
    }

    /**
     * Navigerar till vyn för att hantera kunder (t.ex. registrering av ny kund).
     *
     * @param event Klick på "Hantera Kund"-knappen
     * @throws IOException Om FXML-filen inte kan laddas
     */
    @FXML
    void manageCustomer(ActionEvent event) throws IOException {
        String fxmlf = "addCustomer.fxml";
        ChangeWindow changeWindow = new ChangeWindow();
        changeWindow.windowChange(event, fxmlf);
    }

    /**
     * Loggar ut användaren genom att rensa sessionen och återgå till inloggningsvyn.
     *
     * @param event Klick på "Logga ut"-knappen
     * @throws IOException Om FXML-filen inte kan laddas
     */
    @FXML
    void backToMenu(ActionEvent event) throws IOException {
        String fxmlf = "LoginView.fxml";
        ChangeWindow changeWindow = new ChangeWindow();
        changeWindow.windowChange(event, fxmlf);
        Session.getInstance().clear();
    }
}
