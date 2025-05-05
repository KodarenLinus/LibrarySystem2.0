package com.mycompany.library_system.Search;

import com.mycompany.library_system.Database.ConnDB;
import com.mycompany.library_system.Logic.GetCategoryLoanTime;
import com.mycompany.library_system.Models.Book;
import com.mycompany.library_system.Models.DVD;
import com.mycompany.library_system.Models.Items;

import java.sql.*;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;

/**
 * SearchItems ansvarar för att hämta och filtrera mediaobjekt (Books, DVDs) baserat på söktext eller tillgänglighet.
 * 
 * @author Linus, Emil, Oliver, Viggo
 */
public class SearchItems {

    /**
     * Söker efter objekt vars titel matchar söktexten.
     * Om onlyAvailable är true returneras endast tillgängliga objekt.
     * 
     * @param searchText Användarens söktext
     * @param onlyAvailable avgör om man tar med alla tillgängliga objekt eller alla.
     * @return En lista med matchande tillgängliga Items
     */
    public ArrayList<Items> search(String searchText, boolean onlyAvailable) {
        ArrayList<Items> results = new ArrayList<>();
        
        // SQL-sträng för att hämta ett item baserat på searchText 
        // (Vi kan också plusa på andra delen av frågan som hämtar bara tillgängliga items)
        String query = "SELECT * FROM item WHERE title LIKE ?" + (onlyAvailable ? " AND available = true" : "");

        try (Connection conn = new ConnDB().connect();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, "%" + searchText + "%");
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Items item = createItemFromResultSet(rs, conn);
                if (item != null) {
                    results.add(item);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return results;
    }

    /**
     * Söker efter tillgängliga objekt vid ett givet datum. Tar hänsyn till reservationer och lån.
     * @param date Datumet för tillgänglighetskontroll
     * @return En lista med tillgängliga objekt
     */
    public ArrayList<Items> searchAvailableItems(LocalDate date, String searchText) {
        ArrayList<Items> availableItems = new ArrayList<>();
        GetCategoryLoanTime getLoanTime = new GetCategoryLoanTime();
        
        // SQL-sträng för att hämta ett item baserat på searchText
        String baseQuery = "SELECT * FROM Item WHERE title LIKE ?";

        try (Connection conn = new ConnDB().connect();
            PreparedStatement stmt = conn.prepareStatement(baseQuery)) {
            stmt.setString(1, "%" + searchText + "%");
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Items item = createItemFromResultSet(rs, conn);
                if (item == null) continue;

                int itemID = item.getItemID();
                LocalDate endDate = getLoanTime.calculatetLoanEndDate(conn, itemID, date);
                long loanDays = ChronoUnit.DAYS.between(date, endDate);

                if (isReserved(conn, itemID, date, endDate, loanDays)) continue;
                if (isOnLoan(conn, itemID, date, endDate)) continue;

                availableItems.add(item);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return availableItems;
    }

    /**
     * Skapar ett Items-objekt (Book eller DVD) baserat på resultatraden.
     * Identifierar objektets typ genom att kontrollera förekomsten i respektive tabell.
     * 
     * @param rs ResultSet från item-tabellen
     * @param conn En aktiv databasanslutning
     * @throws SQLException
     * @return Ett Items-objekt eller null om inget hittas
     */
    private Items createItemFromResultSet(ResultSet rs, Connection conn) throws SQLException {
        int id = rs.getInt("itemID");
        String title = rs.getString("title");
        String location = rs.getString("location");
        int categoryID = rs.getInt("CategoryID");
        String categoryName = rs.getString("CategoryName");
        int genreID = rs.getInt("genreID");
        String genreName = rs.getString("genreName");

        // Försök skapa Book
        if (isBook(conn, id)) {
            int isbn = getBookISBN(conn, id);
            Book book = new Book(title, location, isbn, categoryID, categoryName, genreID, genreName);
            book.setItemID(id);
            return book;
        }

        // Försök skapa DVD
        if (isDVD(conn, id)) {
            int directorID = getDVDDirectorID(conn, id);
            DVD dvd = new DVD(title, location, categoryID, categoryName, genreID, genreName, directorID);
            dvd.setItemID(id);
            return dvd;
        }

        return null;
    }
    
     /**
     * Kontrollerar om ett objekt är en bok.
     * 
     * @param conn En aktiv databasanslutning
     * @param itemID ett itemsID.
     * @throws SQLException
     * @return true eller false baserat på om itemID hittas i book tabellen
     */
    private boolean isBook(Connection conn, int itemID) throws SQLException {
        // SQL-sträng för att kontrollera att det är en bok.
        String query = "SELECT 1 FROM Book WHERE itemID = ?";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, itemID);
            return stmt.executeQuery().next();
        }
    }

    /**
     * Hämtar ISBN-numret för en bok.
     * 
     * @param conn En aktiv databasanslutning
     * @param itemID ett itemsID.
     * @throws SQLException
     * @return Bookens ISBN nummer
     */
    private int getBookISBN(Connection conn, int itemID) throws SQLException {
        // SQL-sträng för att hämta ens Bok ISBN-nummer
        String query = "SELECT ISBN FROM Book WHERE itemID = ?";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, itemID);
            ResultSet rs = stmt.executeQuery();
            return rs.next() ? rs.getInt("ISBN") : 0;
        }
    }
    
    /**
     * Kontrollerar om ett objekt är en DVD.
     * 
     * @param conn En aktiv databasanslutning
     * @param itemID ett itemsID.
     * @throws SQLException
     * @return true eller false baserat på om itemID hittas i DVD tabellen
     */
    private boolean isDVD(Connection conn, int itemID) throws SQLException {
        // SQL-sträng för att kontrollera att itemet är en DVD
        String query = "SELECT 1 FROM DVD WHERE itemID = ?";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, itemID);
            return stmt.executeQuery().next();
        }
    }
    
    /**
     * Hämtar DirectorID för en DVD.
     * 
     * @param conn En aktiv databasanslutning
     * @param itemID ett itemsID.
     * @throws SQLException
     * @return DirectorID för DVD
     */
    private int getDVDDirectorID(Connection conn, int itemID) throws SQLException {
        // SQL-sträng för att hämta ens DVD directorID
        String query = "SELECT DirectorID FROM DVD WHERE itemID = ?";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, itemID);
            ResultSet rs = stmt.executeQuery();
            return rs.next() ? rs.getInt("DirectorID") : 0;
        }
    }
    
    /**
     * Kontrollerar om ett objekt är reserverat under en viss period.
     * Tar hänsyn till reservationens startdatum och låneperiodens längd.
     * 
     * @param conn En aktiv databasanslutning
     * @param itemID ett itemsID.
     * @param fromDate datum som man kollar ifrån
     * @param toDate datum man kollar till
     * @param perioden i dagar.
     * @throws SQLException
     * @return true eller false baserat på om itemet är reserverat eller inte
     */
    private boolean isReserved(Connection conn, int itemID, LocalDate fromDate, LocalDate toDate, long loanDays) throws SQLException {
        // SQL-sträng för att kolla om det finns en reservation under en agiven period
        String query = "SELECT 1 FROM reservationRow rr " +
            "JOIN reservation r ON rr.reservationID = r.reservationID " +
            "WHERE rr.itemID = ? " +
            "AND IsFullfilled = false " +
            "AND (r.reservationDate BETWEEN ? AND ? " + 
            "OR ? BETWEEN r.reservationDate AND DATE_ADD(r.reservationDate, INTERVAL ? DAY))";

        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, itemID);
            stmt.setDate(2, Date.valueOf(fromDate));
            stmt.setDate(3, Date.valueOf(toDate));
            stmt.setDate(4, Date.valueOf(fromDate));
            stmt.setLong(5, loanDays);
            return stmt.executeQuery().next();
        }
    }
    
    /**
     * Kontrollerar om ett objekt är utlånat under en angiven period.
     * 
     * @param conn En aktiv databasanslutning
     * @param itemID ett itemsID.
     * @param fromDate datum som man kollar ifrån
     * @param toDate datum man kollar till
     * @throws SQLException
     * @return true eller false baserat på om itemet är ut lånat eller inte
     */
    private boolean isOnLoan(Connection conn, int itemID, LocalDate fromDate, LocalDate toDate) throws SQLException {
        // SQL-sträng för att kolla om det finns ett aktivt lån under en agiven period
        String query = "SELECT 1 FROM loanRow " +
            "WHERE itemID = ? " +
            "AND ActiveLoan = true " +
            "AND (? BETWEEN RowLoanStartDate AND RowLoanEndDate " +
            "OR RowLoanStartDate BETWEEN ? AND ?) ";

        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, itemID);
            stmt.setDate(2, Date.valueOf(fromDate));
            stmt.setDate(3, Date.valueOf(fromDate));
            stmt.setDate(4, Date.valueOf(toDate));
            return stmt.executeQuery().next();
        }
    }
}