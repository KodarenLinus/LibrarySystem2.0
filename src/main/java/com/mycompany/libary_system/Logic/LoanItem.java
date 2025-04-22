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
    
    /**
    * Lägger till objekt i lån (LoanRow) för en viss kund.
    * Kontrollerar först om kunden får låna fler objekt utifrån sin lånegräns.
    * Om gränsen överskrids visas ett popup-meddelande.
    *
    * @param custID ID för kunden som lånar
    * @param itemsToLoan Lista med objekt som ska lånas
    * @param event Event som triggar popup-fönster vid fel
    */
    public void addToLoanRows(int custID, ArrayList<Items> itemsToLoan, Event event) {
        DatabaseConnector connDB = new ConnDB();
        try (Connection conn = connDB.connect()) {
            int currentLoans = getActiveLoanCount(conn, custID);
            int allowedLoan = getAllowedLoanLimit(conn, custID);
            
            //Kollar om användaren har för många aktiva lån, men också om loanlimit överskrids med aktiva lån + kundvagnen
            if (exceedsLoanLimit(currentLoans, itemsToLoan.size(), allowedLoan)) {
                boolean cartTooBig = currentLoans < allowedLoan;
                handleLoanLimitExceeded(event, cartTooBig);
                return;
            }

            int loanID = createLoanAndReturnID(conn, custID);
            if (loanID == -1) return;

            insertLoanRows(conn, loanID, itemsToLoan);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    /**
    * Hämtar antalet aktiva lån som en kund har.
    *
    * @param conn Databasanslutning
    * @param custID ID för kunden
    * @return Antalet aktiva lån
    * @throws SQLException vid fel i SQL-frågan
    */
    private int getActiveLoanCount(Connection conn, int custID) throws SQLException {
        String sql = "SELECT COUNT(loanrowID) AS total FROM LoanRow WHERE ActiveLoan = true AND LoanID IN (SELECT LoanID FROM Loan WHERE CustomerID = ?)";
        try (
            PreparedStatement stmt = conn.prepareStatement(sql)
        ) {
            stmt.setInt(1, custID);
            ResultSet rs = stmt.executeQuery();
            return rs.next() ? rs.getInt("total") : 0;
        }
    }
    
    /**
    * Hämtar den tillåtna lånegränsen för en kund baserat på deras kundkategori.
    *
    * @param conn Databasanslutning
    * @param custID ID för kunden
    * @return Max antal objekt kunden får låna
    * @throws SQLException vid fel i SQL-frågan
    */
    private int getAllowedLoanLimit(Connection conn, int custID) throws SQLException {
        String sql = "SELECT LoanLimit FROM CustomerCategory WHERE CustomerCategoryID IN (SELECT CustomerCategoryID FROM Customer WHERE CustomerID = ?)";
        try (
            PreparedStatement stmt = conn.prepareStatement(sql)
        ) {
            stmt.setInt(1, custID);
            ResultSet rs = stmt.executeQuery();
            return rs.next() ? rs.getInt("LoanLimit") : 0;
        }
    }
    
    /**
    * Kontrollerar om kundens nuvarande aktiva lån plus kundvagn överskrider lånegränsen.
    *
    * @param currentLoans Antal aktiva lån kunden har
    * @param cartSize Antal objekt i kundvagnen
    * @param allowedLoan Max tillåtna lån
    * @return true om gränsen överskrids, annars false
    */
    private boolean exceedsLoanLimit(int currentLoans, int cartSize, int allowedLoan) {
        return currentLoans >= allowedLoan || (currentLoans + cartSize) > allowedLoan;
    }
    
    /**
    * Visar ett popup-fönster om kunden försöker låna för många objekt.
    *
    * @param event Event som triggar popup
    * @param cartTooBig Om felet beror på att kundvagnen innehåller för många objekt
    */
    private void handleLoanLimitExceeded(Event event, boolean cartTooBig) {
        PopUpWindow popUpWindow = new PopUpWindow();
        String fxml;
        if (cartTooBig == true) {
            fxml = "PopUpToManyActiveLoansAndCartItems.fxml";
        } else {
            fxml = "PopUpToManyActiveLoans.fxml";
        }
        
        popUpWindow.popUp(event, fxml);
    }

    /**
    * Skapar ett nytt lån i databasen och returnerar ID:t för det nya lånet.
    *
    * @param conn Databasanslutning
    * @param custID ID för kunden
    * @return Det nya lånets ID eller -1 om något går fel
    * @throws SQLException vid fel i SQL-frågan
    */
    private int createLoanAndReturnID(Connection conn, int custID) throws SQLException {
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

        return -1;
    }
    
    /**
    * Lägger till flera rader i LoanRow-tabellen för ett lån.
    * Varje objekt får ett start- och slutdatum för utlåningen.
    *
    * @param conn Databasanslutning
    * @param loanID ID för det skapade lånet
    * @param itemsToLoan Objekt som ska lånas
    * @throws SQLException vid fel i SQL-frågan
    */
    private void insertLoanRows(Connection conn, int loanID, ArrayList<Items> itemsToLoan) throws SQLException {
        String getCategorySQL = "SELECT categoryID FROM item WHERE itemID = ?";
        String insertLoanRowSQL = "INSERT INTO loanrow (loanid, itemid, RowLoanStartDate, RowLoanEndDate, ActiveLoan) VALUES (?, ?, ?, ?, ?)";

        LocalDate today = LocalDate.now();

        try (
            PreparedStatement getCategoryStmt = conn.prepareStatement(getCategorySQL);
            PreparedStatement insertLoanRowStmt = conn.prepareStatement(insertLoanRowSQL)
        )   {
            for (Items item : itemsToLoan) {
                LocalDate endDate = calculatetLoanEndDate(getCategoryStmt, item.getItemID(), today);

                insertLoanRowStmt.setInt(1, loanID);
                insertLoanRowStmt.setInt(2, item.getItemID());
                insertLoanRowStmt.setString(3, today.toString());
                insertLoanRowStmt.setString(4, endDate.toString());
                insertLoanRowStmt.setBoolean(5, true);
                insertLoanRowStmt.addBatch();
            }

            insertLoanRowStmt.executeBatch();
        }
    }
    
    /**
    * Beräknar lånets slutdatum beroende på objektets kategori.
    * T.ex. böcker lånas i en månad, filmer i en vecka, etc.
    *
    * @param categoryStmt Förberedd SQL-fråga för att hämta kategoriID
    * @param itemID ID för objektet
    * @param date Startdatum för lånet
    * @return Uträknat slutdatum för lånet
    * @throws SQLException vid fel i SQL-frågan
    */
    private LocalDate calculatetLoanEndDate(PreparedStatement categoryStmt, int itemID, LocalDate date) throws SQLException {
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
