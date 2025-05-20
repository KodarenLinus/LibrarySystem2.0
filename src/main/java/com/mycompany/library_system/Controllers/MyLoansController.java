/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.library_system.Controllers;

import com.mycompany.library_system.Logic.LoanMangement.GetLoanRows;
import com.mycompany.library_system.Logic.ReservationMangement.GetReservationRow;
import com.mycompany.library_system.Logic.ReservationMangement.RemoveReservationRow;
import com.mycompany.library_system.Logic.LoanMangement.ReturnLoanItem;
import com.mycompany.library_system.Models.LoanRow;
import com.mycompany.library_system.Models.ReservationRow;
import com.mycompany.library_system.Utils.AlertHandler;
import com.mycompany.library_system.Utils.ChangeWindow;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.RadioButton;
import javafx.scene.input.MouseEvent;

/**
 * En controller som hanterar lämna tillbaka lån, Ta bort reservationer och att lån historik
 * 
 * Fungerar tillsammans med MyLoans.fmxl
 * 
 * @author Linus, Emil, Oliver, Viggo
 */
public class MyLoansController {
    
    // Instans av AlertHandler för att visa meddelanden
    private AlertHandler alert = new AlertHandler();
    
    // Texter till popup-meddelanden
    private String title;
    private String header; 
    private String content;
    
    // FXML-kopplingar till gränssnittets komponenter
    @FXML
    private ListView<LoanRow> AktiveLoansList;
    
    @FXML
    private ListView<ReservationRow> ReservationList;
    
    @FXML
    private ListView<LoanRow> LoanHistory;

    @FXML
    private ListView<LoanRow> ReturnLoanList;
    
    @FXML
    private RadioButton LateLoansRadioButton;
    
    /**
     * Visar aktiva lån, med möjlighet att filtrera fram endast försenade lån.
     *
     * @param event Händelse som triggas vid klick på en relevant knapp (t.ex. "Visa lån")
     * @throws SQLException om ett fel uppstår vid databasåtkomst
     */
    @FXML
    void ShowLateLoans(ActionEvent event) throws SQLException {
        // Vi vill bara ha aktiva lån
        boolean activeLoan = true;
        
        // Skapar GetLoanRows objekt för att hämta LoanRows
        GetLoanRows getLoanRows = new GetLoanRows();
        
        //Hämtar alla användarens LoanRows
        ArrayList<LoanRow> allLoanRows = getLoanRows.getAllLoanRows(activeLoan);

        // Ladda in tillhörande objekt
        for (LoanRow row : allLoanRows) {
            try {
                row.loadItem();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        // Om radioknappen är markerad, filtrera sena lån
        if (LateLoansRadioButton.isSelected()) {
            LocalDate today = LocalDate.now();
            allLoanRows.removeIf(row -> !row.getLoanRowEndDate().isBefore(today));
        }

        // Filtrera bort de som redan är i retur-listan
        allLoanRows.removeAll(ReturnLoanList.getItems());

        // Uppdatera listan
        AktiveLoansList.getItems().setAll(allLoanRows);
    }
    
    /**
     * Lägger ett valt aktivt lån till i listan för retur.
     * 
     * @param event Klick på ett objekt i listan över aktiva lån
     * @throws SQLException om något går fel vid uppdatering av vyn
     */
    @FXML
    void AddToReturnList(MouseEvent event) throws SQLException {
        // Sparar ner valt LoanRow från AktiveLoansList
        LoanRow selectedItem = AktiveLoansList.getSelectionModel().getSelectedItem();
        
        // Valda LoanRows flytar vi till ReturnLoanList
        if (selectedItem != null) {
            ReturnLoanList.getItems().add(selectedItem);
            applyFilter();
        }
    }
    
    /**
     * Tar bort ett lån från retur-listan.
     * 
     * @param event Klick på ett objekt i retur-listan
     * @throws SQLException om något går fel vid uppdatering av vyn
     */
    @FXML
    void RemoveFromReturnList(MouseEvent event) throws SQLException {
        // Sparar ner valt LoanRow från ReturnLoanList
        LoanRow selectedItem = ReturnLoanList.getSelectionModel().getSelectedItem();
        
        // Valda LoanRows flytar vi till AktiveLoansList
        if (selectedItem != null) {
            AktiveLoansList.getItems().remove(selectedItem);
            applyFilter();
        }
    }
    
    /**
     * Returnerar alla lån som lagts till i retur-listan. Listan töms efter att operationen är klar.
     * 
     * @param event Klick på "Returnera"-knappen
     */
    @FXML
    void ReturnLoans(ActionEvent event) {
        // Skapar ett ReturnLoanItem objekt
        ReturnLoanItem returnLoanItem = new ReturnLoanItem();
        
        // Skapar en ArrayLista med LoanRows som vi har i return list
        ArrayList<LoanRow> loanRows = new ArrayList<>(ReturnLoanList.getItems());
        
        // Skickar in ArrayListan till metoden returnitem och sedan tömmer jag listan
        returnLoanItem.returnItem(loanRows);
        ReturnLoanList.getItems().clear();
    }
    
    /**
     * Tar bort vald reservation från listan och databasen.
     * Visar en varning om ingen reservation är vald.
     * 
     * @param event Klick på "Ta bort reservation"-knappen
     */
    @FXML
    void RemoveReservation(ActionEvent event) {
        // Sparar ned valt ReservationRow från ReservationList i selectedRow
        ReservationRow selectedRow = ReservationList.getSelectionModel().getSelectedItem();
        
        /*
         * Kollar om vi valt något ReservationRow. HAr vi valt ett kommer det att
         * tas bort. Har vi inte valt något kommer användaren att få en alert som informerar dem
         */
        if (selectedRow != null) {
            // Ta bort från UI-listan
            ReservationList.getItems().remove(selectedRow);

            // Ta bort från databasen
            RemoveReservationRow remover = new RemoveReservationRow(); 
            remover.deleteReservationRow(selectedRow.getReservationRowID());
        } else {
            title = "Ingen reservation är vald";
            header = "Du har inte valt en reservation"; 
            content = "Du har missat att välja en reservation att ta bort";
            alert.createAlert(title, header, content);
        }
    }
    
    /**
     * Navigerar tillbaka till kundvyn.
     * 
     * @param event Klick på "Tillbaka"-knappen
     */
    @FXML
    void ReturnToCustomerView(ActionEvent event) {
        String fxmlf = "CustomerStart.fxml";
        ChangeWindow changeWindow = new ChangeWindow();
        changeWindow.windowChange(event, fxmlf);
    }
    
     /**
     * Initierar vyerna när sidan laddas. Hämtar:
     * - Aktiva lån
     * - Tidigare lån (historik)
     * - Aktuella reservationer
     * Sätter även upp cellrenderers för att visa rätt information i listorna.
     * 
     * @throws SQLException om databasfel uppstår
     */
    @FXML
    public void initialize() throws SQLException  {
        // Hämtar aktiva Lån rader
        GetLoanRows getLoanRows = new GetLoanRows();
        boolean activeLoan;
        
        AktiveLoansList.setCellFactory(list -> new ListCell<LoanRow>() {
            @Override
            protected void updateItem(LoanRow item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(item.toString()); 
                }
            }
        });
    
        // Laddar in alla objekt vid start
        activeLoan = true;
        ArrayList<LoanRow> allLoanRows = getLoanRows.getAllLoanRows(activeLoan);
        
        // Load the item for each LoanRow before display
        for (LoanRow row : allLoanRows) {
            try {
                row.loadItem();
            } catch (SQLException e) {
                e.printStackTrace(); // or handle more gracefully
            }
        }
        
        allLoanRows.removeAll(ReturnLoanList.getItems());
        AktiveLoansList.getItems().setAll(allLoanRows);
        
        LoanHistory.setCellFactory(list -> new ListCell<LoanRow>() {
            @Override
            protected void updateItem(LoanRow loanRow, boolean empty) {
                super.updateItem(loanRow, empty);
                if (empty || loanRow == null) {
                    setText(null);
                } else {
                    setText(loanRow.toString()); 
                }
            }
        });
    
        // Laddar in alla objekt vid start
        activeLoan = false;
        ArrayList<LoanRow> allLoanRowsHistory = getLoanRows.getAllLoanRows(activeLoan);
        
        // Load the item for each LoanRow before display
        for (LoanRow row : allLoanRowsHistory) {
            try {
                row.loadItem();
            } catch (SQLException e) {
                e.printStackTrace(); // or handle more gracefully
            }
        }
        
        LoanHistory.getItems().setAll(allLoanRowsHistory);
        
         LoanHistory.setCellFactory(list -> new ListCell<LoanRow>() {
            @Override
            protected void updateItem(LoanRow loanRow, boolean empty) {
                super.updateItem(loanRow, empty);
                if (empty || loanRow == null) {
                    setText(null);
                } else {
                    setText(loanRow.toString()); 
                }
            }
        });
         
        // Hämtar aktiva reservations rader
        GetReservationRow getReservationRow = new GetReservationRow();
        
        // Laddar in alla objekt vid start
        ArrayList<ReservationRow> reservationRows = getReservationRow.getReservationRow();
        
        // Load the item for each LoanRow before display
        for (ReservationRow row : reservationRows) {
            try {
                row.loadItem();
            } catch (SQLException e) {
                e.printStackTrace(); // or handle more gracefully
            }
        }
        
        ReservationList.getItems().setAll(reservationRows);
    }
    
    
    /**
     * Hjälpmetod som uppdaterar listan över aktiva lån, filtrerar bort lån som redan finns i retur-listan.
     * 
     * @throws SQLException om databasfel uppstår
     */
    private void applyFilter() throws SQLException {
        boolean activeLoan = true;
        GetLoanRows getLoanRows = new GetLoanRows();
        ArrayList<LoanRow> allLoanRows = getLoanRows.getAllLoanRows(activeLoan);
        
        // Ladda item för varje LoanRow
        for (LoanRow row : allLoanRows) {
            try {
                row.loadItem();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        
        // Rensa aktiva lån listan från LoanRows som ligger i ReturnLoanList
        allLoanRows.removeAll(ReturnLoanList.getItems());
        AktiveLoansList.getItems().setAll(allLoanRows);
    }
}
