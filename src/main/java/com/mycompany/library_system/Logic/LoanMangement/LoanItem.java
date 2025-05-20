/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.library_system.Logic.LoanMangement;

import com.mycompany.library_system.Logic.ItemManagement.GetCategoryLoanTime;
import com.mycompany.library_system.Database.DatabaseConnector;
import com.mycompany.library_system.Database.ConnDB;
import com.mycompany.library_system.Logic.ItemManagement.GetItemsByID;
import com.mycompany.library_system.Login.Session;
import com.mycompany.library_system.Models.Items;
import com.mycompany.library_system.Models.LoanRow;
import com.mycompany.library_system.Utils.AlertHandler;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                    
/**
 * Klass som hanterar utlåning av objekt och sparar lånerader i databasen.
 * Metoderna kontrollerar kundens lånegräns, skapar lån, lägger till LoanRow-poster
 * och visar popup-meddelanden med lånedatum och återlämningsdatum.
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
     * Annars skrivs ett kvito ut med hela ordern.
     *
     * @param custID ID för kunden som lånar
     * @param itemsToLoan Lista med objekt som ska lånas
     * @param event Event som triggar popup-fönster vid fel
     */
    public boolean addToLoanRows(int custID, ArrayList<Items> itemsToLoan) {
    AlertHandler alertHandler = new AlertHandler();
    String title;
    String header;
    StringBuilder content = new StringBuilder();

    try (Connection conn = dbConnector.connect()) {
        int currentLoans = getActiveLoanCount(conn, custID);
        int allowedLoan = getAllowedLoanLimit(conn, custID);

        if (exceedsLoanLimit(currentLoans, itemsToLoan.size(), allowedLoan)) {
            boolean cartTooBig = currentLoans < allowedLoan;
            handleLoanLimitExceeded(cartTooBig);
            return false;
        }

        int loanID = createLoanAndReturnID(conn, custID);
        if (loanID == -1) return false;

        // Få tillbaka loanrows med datum för popup
        ArrayList<LoanRow> loanRows = insertLoanRows(conn, loanID, itemsToLoan);

        // Pop-up med info
        if (loanRows.size() > 0){
            title = "Lån popUp";
            header = "Lyckat lån";
            content.append("Du har lånat följande:\n\n");
            GetItemsByID getItemsByID = new GetItemsByID();
            for (LoanRow row : loanRows) {
                content.append(String.format("%s - %s (Från: %s Till: %s)\n",
                    row.getItemID(),
                    getItemsByID.getItemById(row.getItemID()).getTitle(),
                    row.getLoanRowStartDate(),
                    row.getLoanRowEndDate()));
            }
        
        alertHandler.createAlert(title, header, content.toString());
        }
        
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
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
        String title;
        String header;
        String content;

        if (cartTooBig) {
            //fxml = "PopUpToManyActiveLoansAndCartItems.fxml";
            title = "För Många aktiva lån + föremål i kundvagnen";
            header = "Du måste lämna tillbaka föremål eller ta bort föremål ur din kundvagn";
            content = "Gå till 'Mina lån' för att lämna tillbaka föremål eller ta bort några föremål ur kundvagnen.";
        } else {
            title = "För Många aktiva lån";
            header = "Du måste lämna tillbaka föremål";
            content = "Gå till 'Mina lån' för att lämna tillbaka föremål.";
        }
        
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
     * @param itemsToLoan Lista med objekt som ska lånas
     * @return Lista med skapade LoanRow-objekt innehållande låneinformation
     */
    public ArrayList<LoanRow> insertLoanRows(Connection conn, int loanID, ArrayList<Items> itemsToLoan) {
        String title;
        String header;
        String content;
        
        int currentUserID = Session.getInstance().getUserId();
        LocalDate today = LocalDate.now();
        ArrayList<LoanRow> loanRows = new ArrayList<>();

        try (
            PreparedStatement insertLoanRowStmt = conn.prepareStatement(
                "INSERT INTO loanrow (loanid, itemid, RowLoanStartDate, RowLoanEndDate, ActiveLoan) VALUES (?, ?, ?, ?, true)",
                Statement.RETURN_GENERATED_KEYS
            );
            PreparedStatement checkReservationStmt = conn.prepareStatement(
                "SELECT r.CustomerID, res.reservationDate FROM reservationrow rr " +
                "JOIN reservation r ON rr.reservationID = r.reservationID " +
                "JOIN reservation res ON r.reservationID = res.reservationID " +
                "WHERE rr.itemID = ? AND res.reservationDate = ?"
            );
            PreparedStatement futureReservationStmt = conn.prepareStatement(
                "SELECT r.reservationDate FROM reservationrow rr " +
                "JOIN reservation r ON rr.reservationID = r.reservationID " +
                "WHERE rr.itemID = ? AND r.reservationDate > ?"
            );
            PreparedStatement updateReservationStmt = conn.prepareStatement(
                "UPDATE reservationrow SET IsFullfilled = true " +
                "WHERE itemID = ? AND reservationID IN (" +
                "SELECT reservationID FROM reservation WHERE CustomerID = ? AND reservationDate = ?)"
            );
        ) {
            for (Items item : itemsToLoan) {
                if (!canLoanItemToday(item.getItemID(), currentUserID, today, checkReservationStmt, updateReservationStmt)) {
                    System.out.println("Objektet med ID " + item.getItemID() + " är reserverat av annan användare idag och kan inte lånas.");
                    title = "Reserverat av annan användare";
                    header = "Du kan inte låna " + item.getTitle();
                    content = "Du kan inte låna " + item.getTitle() + " Eftersom det är reserverat idag";
                    AlertHandler alertHandler = new AlertHandler();
                    alertHandler.createAlert(title, header, content);
                    continue;
                }

                LocalDate endDate = calculateReturnDate(item.getItemID(), today, futureReservationStmt);

                insertLoanRowStmt.setInt(1, loanID);
                insertLoanRowStmt.setInt(2, item.getItemID());
                insertLoanRowStmt.setDate(3, java.sql.Date.valueOf(today));
                insertLoanRowStmt.setDate(4, java.sql.Date.valueOf(endDate));
                insertLoanRowStmt.executeUpdate();

                ResultSet generatedKeys = insertLoanRowStmt.getGeneratedKeys();
                if (generatedKeys.next()) {
                    LoanRow loanRow = new LoanRow(loanID, item.getItemID(), today, endDate, true);
                    loanRows.add(loanRow);
                }
            }
            System.out.println("Lånerader har lagts till.");
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return loanRows;
    }

    /**
     * Kontrollerar om objektet kan lånas idag, baserat på eventuella reservationer.
     * Uppdaterar reservationen som uppfylld om den tillhör aktuell användare.
     *
     * @param itemId Objektets ID
     * @param userId Aktuell användares ID
     * @param today Dagens datum
     * @param checkStmt Förberedd SQL-sats för att kontrollera reservation
     * @param updateStmt Förberedd SQL-sats för att uppdatera reservation
     * @return true om objektet får lånas, annars false
     * @throws SQLException vid SQL-fel
     */
    private boolean canLoanItemToday(int itemId, int userId, LocalDate today,
        PreparedStatement checkStmt, PreparedStatement updateStmt) throws SQLException {
        checkStmt.setInt(1, itemId);
        checkStmt.setDate(2, java.sql.Date.valueOf(today));
        ResultSet rs = checkStmt.executeQuery();

        if (rs.next()) {
            int reservingUserID = rs.getInt("CustomerID");
            if (reservingUserID != userId) {
                return false; // reserverat av någon annan
            } else {
                updateStmt.setInt(1, itemId);
                updateStmt.setInt(2, userId);
                updateStmt.setDate(3, java.sql.Date.valueOf(today));
                updateStmt.executeUpdate();
            }
        }
        return true;
    }

    /**
     * Beräknar ett slutdatum för utlåning, beroende på framtida reservationer.
     *
     * @param itemId Objektets ID
     * @param startDate Startdatum för utlåning
     * @param stmt Förberedd SQL-sats för framtida reservationer
     * @return Datum då objektet ska återlämnas
     * @throws SQLException vid SQL-fel
     */
    private LocalDate calculateReturnDate(int itemId, LocalDate startDate,
                                          PreparedStatement stmt) throws SQLException {
        stmt.setInt(1, itemId);
        stmt.setDate(2, java.sql.Date.valueOf(startDate));
        ResultSet rs = stmt.executeQuery();

        LocalDate defaultReturnDate = startDate.plusDays(14);
        if (rs.next()) {
            LocalDate reservedDate = rs.getDate("reservationDate").toLocalDate();
            return reservedDate.minusDays(1);
        }
        return defaultReturnDate;
    }
}



