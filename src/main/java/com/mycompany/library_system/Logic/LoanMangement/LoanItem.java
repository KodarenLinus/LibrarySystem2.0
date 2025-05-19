/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.library_system.Logic.LoanMangement;

import com.mycompany.library_system.Logic.ItemManagement.GetCategoryLoanTime;
import com.mycompany.library_system.Database.DatabaseConnector;
import com.mycompany.library_system.Database.ConnDB;
import com.mycompany.library_system.Models.Items;
import com.mycompany.library_system.Utils.AlertHandler;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                    
/**
 *
 * En klass som hantera lån och lägger in dem i databasen
 * 
 * @author Linus, Emil, Oliver, Viggo
 */
public class LoanItem {    
    
    private final DatabaseConnector dbConnector;

    public LoanItem () {
        this.dbConnector = new ConnDB();
    }
    
    /**
    * Lägger till objekt i lån (LoanRow) för en viss kund.
    * Kontrollerar först om kunden får låna fler objekt utifrån sin lånegräns.
    * Om gränsen överskrids visas ett popup-meddelande.
    *
    * @param custID ID för kunden som lånar
    * @param itemsToLoan Lista med objekt som ska lånas
    * @param event Event som triggar popup-fönster vid fel
    */
    public boolean addToLoanRows(int custID, ArrayList<Items> itemsToLoan) {
        AlertHandler alertHandler = new AlertHandler();
        String title;
        String header; 
        String content;
        
        try (
            Connection conn = dbConnector.connect()
        ) {
            int currentLoans = getActiveLoanCount(conn, custID);
            int allowedLoan = getAllowedLoanLimit(conn, custID);
            
            //Kollar om användaren har för många aktiva lån, men också om loanlimit överskrids med aktiva lån + kundvagnen
            if (exceedsLoanLimit(currentLoans, itemsToLoan.size(), allowedLoan)) {
                boolean cartTooBig = currentLoans < allowedLoan;
                handleLoanLimitExceeded(cartTooBig);
                return false;
            }

            int loanID = createLoanAndReturnID(conn, custID);
            if (loanID == -1) return false;

            insertLoanRows(conn, loanID, itemsToLoan);
            title = "Lån popUp";
            header ="Lyckat lån"; 
            content = "Du har lånat följande: ";
            for(Items item: itemsToLoan){ content += (item.getTitle() + ", "); }
            alertHandler.createAlert(title, header, content);

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return true;
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
            PreparedStatement countLoanRowsStmt = conn.prepareStatement(sql)
        ) {
            countLoanRowsStmt.setInt(1, custID);
            ResultSet rs = countLoanRowsStmt.executeQuery();
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
            PreparedStatement findLoanLimitStmt = conn.prepareStatement(sql)
        ) {
            findLoanLimitStmt.setInt(1, custID);
            ResultSet rs = findLoanLimitStmt.executeQuery();
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
    * @param cartTooBig Om felet beror på att kundvagnen innehåller för många objekt
    */
    private void handleLoanLimitExceeded(boolean cartTooBig) {
        //PopUpWindow popUpWindow = new PopUpWindow();
        //String fxml;
        String title;
        String header;
        String content;

        if (cartTooBig) {
            //fxml = "PopUpToManyActiveLoansAndCartItems.fxml";
            title = "För Många aktiva lån + föremål i kundvagnen";
            header = "Du måste lämna tillbaka föremål eller ta bort föremål ur din kundvagn";
            content = "Gå till 'Mina lån' för att lämna tillbaka föremål eller ta bort några föremål ur kundvagnen.";
        } else {
            //fxml = "PopUpToManyActiveLoans.fxml";
            title = "För Många aktiva lån";
            header = "Du måste lämna tillbaka föremål";
            content = "Gå till 'Mina lån' för att lämna tillbaka föremål.";
        }
        
        //popUpWindow.popUp(event, fxml);
        AlertHandler alertHandler = new AlertHandler();
        alertHandler.createAlert(title, header, content);
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

        try (
            PreparedStatement insertStmt = conn.prepareStatement(insertLoanSQL, Statement.RETURN_GENERATED_KEYS)
        ) {
            insertStmt.setInt(1, custID);
            insertStmt.executeUpdate();

            try (ResultSet generatedKeys = insertStmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    return generatedKeys.getInt(1);
                } else {
                    throw new SQLException("Misslyckades att hämta genererat loan-ID.");
                }
            }
        }
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
        String insertLoanRowSQL = "INSERT INTO loanrow (loanid, itemid, RowLoanStartDate, RowLoanEndDate, ActiveLoan) VALUES (?, ?, ?, ?, ?)";
        String reservationSQL = "SELECT r.reservationDate " +
                            "FROM reservationrow rr " +
                            "JOIN reservation r ON rr.reservationID = r.reservationID " +
                            "WHERE rr.itemID = ? AND r.reservationDate >= ?";
        
        LocalDate today = LocalDate.now();

        try (
            PreparedStatement insertLoanRowStmt = conn.prepareStatement(insertLoanRowSQL);
            PreparedStatement checkReservationStmt = conn.prepareStatement(reservationSQL);
        )   {
            for (Items item : itemsToLoan) {
                GetCategoryLoanTime getCategoryLoanTime = new GetCategoryLoanTime();
                LocalDate endDate = getCategoryLoanTime.calculateLoanEndDate(item.getItemID(), today);
                LocalDate finalEndDate = endDate;
                
                checkReservationStmt.setInt(1, item.getItemID());
                checkReservationStmt.setDate(2, java.sql.Date.valueOf(today));
                ResultSet rs = checkReservationStmt.executeQuery();

                if (rs.next() && rs.getDate("reservationDate") != null) {
                    LocalDate reservedDate = rs.getDate("reservationDate").toLocalDate();

                    // Om reservationens datum är före det vanliga återlämningsdatumet
                    if (reservedDate.isBefore(endDate)) {
                        finalEndDate = reservedDate.minusDays(1); // sätt till dagen innan
                    }
                }
                // Lägger in värden i loanRow tabelen
                insertLoanRowStmt.setInt(1, loanID);
                insertLoanRowStmt.setInt(2, item.getItemID());
                insertLoanRowStmt.setString(3, today.toString());
                insertLoanRowStmt.setString(4, finalEndDate.toString());
                insertLoanRowStmt.setBoolean(5, true);
                insertLoanRowStmt.addBatch();
            }

            insertLoanRowStmt.executeBatch();
        }
    } 
}
