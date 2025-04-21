/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.libary_system.Logic;

import com.mycompany.libary_system.Database.DatabaseConnector;
import com.mycompany.libary_system.Database.ConnDB;
import com.mycompany.libary_system.Models.Items;
import com.mycompany.libary_system.Utils.PopUpWindow;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import javafx.event.Event;
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                    
/**
 *
 * @author emildahlback
 */
public class LoanItem {
    
    // Här hämtar vi kundvagn och lägger in den i loanrows!!!!
    public void addToLoanRows(int custID, ArrayList<Items> itemsToLoan, Event event) {
        DatabaseConnector connDB = new ConnDB();
        Connection conn = connDB.connect();
        String getNumberOfLoans = "SELECT COUNT(loanrowID) AS total FROM LoanRow WHERE (ActiveLoan = true) AND (LoanID IN (SELECT LoanID FROM Loan WHERE CustomerID = ?))";
        String getAllowedLoan = "SELECT LoanLimit FROM CustomerCategory WHERE CustomerCategoryID IN (SELECT CustomerCategoryID FROM Customer WHERE CustomerID = ?)";
        int allowedLoan = 0;
        
        try (
            PreparedStatement getNumberOfLoansStmt = conn.prepareStatement(getNumberOfLoans);
            PreparedStatement getAllowedLoanStmt = conn.prepareStatement(getAllowedLoan); 
        ) {
            getAllowedLoanStmt.setInt(1, custID);
            ResultSet rs1 = getAllowedLoanStmt.executeQuery();
            if (rs1.next()) {
                allowedLoan = rs1.getInt("LoanLimit");
            }
            
            getNumberOfLoansStmt.setInt(1, custID);
            ResultSet rs = getNumberOfLoansStmt.executeQuery();
            int count = 0;

            while (rs.next()) {
                count = rs.getInt("total");
            }
            if (count < allowedLoan) {    
                for (Items item : itemsToLoan) {
                    count++;
                }

                //Kör bara koden om man har lån
                if (count <= allowedLoan) {                
                    int loanID = insertLoanAndGetID(conn, custID); //Hämtar loanID för kund!
                    if (loanID == -1) return; //om vi inte har något lån avbryter vi metoden!

                    insertLoanRows(conn, loanID, itemsToLoan);
                } else {
                    PopUpWindow popUpWindow = new PopUpWindow();
                    String fxmlf = "PopUpToManyActiveLoansAndCartItems.fxml";
                    popUpWindow.popUp(event, fxmlf);
                }
            
            } else {
                PopUpWindow popUpWindow = new PopUpWindow();
                String fxmlf = "PopUpToManyActiveLoans.fxml";
                popUpWindow.popUp(event, fxmlf);
            
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    // Gör insert för ett lån i databasen samt hämtar kundens senast inloggning
    private int insertLoanAndGetID(Connection conn, int custID) throws SQLException {
        String insertLoanSQL = "INSERT INTO loan (customerID) VALUES (?)";
        String getLoanIDSQL = "SELECT MAX(loanid) FROM loan WHERE customerID = ?";

        try (
            PreparedStatement insertStmt = conn.prepareStatement(insertLoanSQL);
            PreparedStatement getLoanIDStmt = conn.prepareStatement(getLoanIDSQL);
        ) {
            insertStmt.setInt(1, custID);
            insertStmt.executeUpdate();

            getLoanIDStmt.setInt(1, custID);
            ResultSet rs = getLoanIDStmt.executeQuery();

            if (rs.next()) {
                return rs.getInt(1);
            }
        }

        return -1; //Hittar vi inget loan retunerar vi -1 vilket antyder att kunden inte har ett loan
    }
    
    // Här för vi in loanRows till databasen!!!
    private void insertLoanRows(Connection conn, int loanID, ArrayList<Items> itemsToLoan) throws SQLException {
        String getCategorySQL = "SELECT categoryID FROM item WHERE itemID = ?";
        String insertLoanRowSQL = "INSERT INTO loanrow (loanid, itemid, RowLoanStartDate, RowLoanEndDate, ActiveLoan) VALUES (?, ?, ?, ?, ?)";

        LocalDate today = LocalDate.now();

        try (
            PreparedStatement getCategoryStmt = conn.prepareStatement(getCategorySQL);
            PreparedStatement insertLoanRowStmt = conn.prepareStatement(insertLoanRowSQL)
        )   {
            for (Items item : itemsToLoan) {
                LocalDate endDate = getLoanEndDate(getCategoryStmt, item.getItemID(), today);

                insertLoanRowStmt.setInt(1, loanID);
                insertLoanRowStmt.setInt(2, item.getItemID());
                insertLoanRowStmt.setString(3, today.toString());
                insertLoanRowStmt.setString(4, endDate.toString());
                insertLoanRowStmt.setBoolean(5, true); // Låneraden ska alltid vara aktivt när vi skapar ett loanRow
                insertLoanRowStmt.addBatch();
            }

            insertLoanRowStmt.executeBatch();
        }
    }
    
    // En metod som håller koll på de olika kategoriernas lån lägnd och som håller koll på dagens datum!!!
    // metoden retunera ett datum baserat på kategori och dagens datum!!
    private LocalDate getLoanEndDate(PreparedStatement categoryStmt, int itemID, LocalDate date) throws SQLException {
        categoryStmt.setInt(1, itemID);
        ResultSet rs = categoryStmt.executeQuery();

        if (rs.next()) {
            int categoryID = rs.getInt("categoryID");

            switch (categoryID) {
                case 1: return date.plusMonths(1);
                case 2: return date.plusWeeks(1);
                case 3: return date.plusWeeks(2);
                default: return date;
            }
        }

        return date;
    }
}
