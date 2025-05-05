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
     * Söker efter tillgängliga objekt vars titel matchar den angivna söktexten.
     * @param searchText Användarens söktext
     * @return En lista med matchande tillgängliga Items
     */
    public ArrayList<Items> search(String searchText, boolean onlyAvailable) {
        ArrayList<Items> results = new ArrayList<>();
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
     * Skapar ett Items-objekt (Book eller DVD) från databasen beroende på typ.
     * @param rs ResultSet från item-tabellen
     * @param conn En aktiv databasanslutning
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

    private boolean isBook(Connection conn, int itemID) throws SQLException {
        String query = "SELECT 1 FROM Book WHERE itemID = ?";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, itemID);
            return stmt.executeQuery().next();
        }
    }

    private int getBookISBN(Connection conn, int itemID) throws SQLException {
        String query = "SELECT ISBN FROM Book WHERE itemID = ?";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, itemID);
            ResultSet rs = stmt.executeQuery();
            return rs.next() ? rs.getInt("ISBN") : 0;
        }
    }

    private boolean isDVD(Connection conn, int itemID) throws SQLException {
        String query = "SELECT 1 FROM DVD WHERE itemID = ?";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, itemID);
            return stmt.executeQuery().next();
        }
    }

    private int getDVDDirectorID(Connection conn, int itemID) throws SQLException {
        String query = "SELECT DirectorID FROM DVD WHERE itemID = ?";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, itemID);
            ResultSet rs = stmt.executeQuery();
            return rs.next() ? rs.getInt("DirectorID") : 0;
        }
    }

    private boolean isReserved(Connection conn, int itemID, LocalDate fromDate, LocalDate toDate, long loanDays) throws SQLException {
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

    private boolean isOnLoan(Connection conn, int itemID, LocalDate fromDate, LocalDate toDate) throws SQLException {
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