/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.library_system.Controllers;

import com.mycompany.library_system.Logic.GetLoanRows;
import com.mycompany.library_system.Logic.ReturnLoanItem;
import com.mycompany.library_system.Models.Author;
import com.mycompany.library_system.Models.Book;
import com.mycompany.library_system.Models.LoanRow;
import com.mycompany.library_system.Search.SearchAuthor;
import com.mycompany.library_system.Utils.ChangeWindow;
import java.sql.SQLException;
import java.util.ArrayList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.input.MouseEvent;

/**
 *
 * @author Linus, Emil, Oliver, Viggo
 */
public class MyLoansController {
    
    @FXML
    private ListView<LoanRow> AktiveLoansList;

    @FXML
    private ListView<LoanRow> LoanHistory;

    @FXML
    private ListView<LoanRow> ReturnLoanList;
    
    @FXML
    void AddToReturnList(MouseEvent event) throws SQLException {
         LoanRow selectedItem = AktiveLoansList.getSelectionModel().getSelectedItem();

        if (selectedItem != null) {
            ReturnLoanList.getItems().add(selectedItem);
            applyFilter();
        }
    }

    @FXML
    void RemoveFromReturnList(MouseEvent event) throws SQLException {
          LoanRow selectedItem = ReturnLoanList.getSelectionModel().getSelectedItem();

        if (selectedItem != null) {
            ReturnLoanList.getItems().remove(selectedItem);
            applyFilter();
        }
    }

    @FXML
    void ReturnLoans(ActionEvent event) {
        ReturnLoanItem returnLoanItem = new ReturnLoanItem();
        ArrayList<LoanRow> loanRows = new ArrayList<>(ReturnLoanList.getItems());
        returnLoanItem.returnItem(loanRows);
        ReturnLoanList.getItems().clear();
    }
    
     @FXML
    void ReturnToCustomerView(ActionEvent event) {
        String fxmlf = "CustomerView.fxml";
        ChangeWindow changeWindow = new ChangeWindow();
        changeWindow.windowChange(event, fxmlf);
    }
    
    @FXML
    public void initialize() throws SQLException  {
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
    }
    
      private void applyFilter() throws SQLException {
        boolean activeLoan = true;
        GetLoanRows getLoanRows = new GetLoanRows();
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
    }
}
