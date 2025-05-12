package com.mycompany.library_system.Search;

import com.mycompany.library_system.Database.ConnDB;
import com.mycompany.library_system.Database.DatabaseConnector;
import com.mycompany.library_system.Logic.ItemManagement.ItemFactory;
import com.mycompany.library_system.Logic.ItemManagement.GetCategoryLoanTime;
import com.mycompany.library_system.Models.Items;

import java.sql.*;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;

/**
 * Klassen SearchItems ansvarar för att söka och filtrera mediaobjekt (Books, DVDs)
 * baserat på söktext, tillgänglighet eller utlåningsstatus.
 * Den hämtar information från databasen och returnerar motsvarande Items-objekt.
 * 
 * Använder ItemFactory för att skapa objekt av rätt typ (Book eller DVD).
 * 
 * @author Linus, Emil, Oliver, Viggo
 */
public class SearchItems {

    private final DatabaseConnector dbConnector;

    public SearchItems() {
        this.dbConnector = new ConnDB();
    }

    /**
     * Söker efter objekt vars titel matchar angiven söktext.
     * Om onlyAvailable är true returneras endast objekt som är markerade som tillgängliga.
     *
     * @param searchText -> Söktext för titel (delmatchning används)
     * @param onlyAvailable -> True om endast tillgängliga objekt ska inkluderas
     * @return En lista med matchande Items-objekt
     */
    public ArrayList<Items> search(String searchText, boolean onlyAvailable) {
        ArrayList<Items> results = new ArrayList<>();
        String query = "SELECT * FROM item WHERE title LIKE ?" + (onlyAvailable ? " AND available = true" : "");

        try (Connection conn = dbConnector.connect();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, "%" + searchText + "%");
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Items item = ItemFactory.createItemFromResultSet(rs, conn);
                if (item != null) results.add(item);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return results;
    }

    /**
     * Söker efter objekt som är tillgängliga vid ett visst datum.
     * Kontrollerar reservationer och aktiva lån inom perioden.
     *
     * @param date -> Datum då objektet ska vara tillgängligt
     * @param searchText -> Söktext för titel (delmatchning används)
     * @return En lista med tillgängliga Items-objekt
     */
    public ArrayList<Items> searchAvailableItems(LocalDate date, String searchText) {
        ArrayList<Items> availableItems = new ArrayList<>();
        GetCategoryLoanTime getLoanTime = new GetCategoryLoanTime();
        String baseQuery = "SELECT * FROM Item WHERE title LIKE ?";

        try (Connection conn = dbConnector.connect();
             PreparedStatement stmt = conn.prepareStatement(baseQuery)) {

            stmt.setString(1, "%" + searchText + "%");
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Items item = ItemFactory.createItemFromResultSet(rs, conn);
                if (item == null) continue;

                int itemID = item.getItemID();
                LocalDate endDate = getLoanTime.calculateLoanEndDate(itemID, date);
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
     * Kontrollerar om ett objekt är reserverat under en viss period.
     * Tar hänsyn till reservationer som ännu inte fullföljts.
     *
     * @param conn -> Databasanslutning
     * @param itemID -> ID för objektet som kontrolleras
     * @param fromDate -> Startdatum för kontroll
     * @param toDate -> Slutdatum för kontroll
     * @param loanDays -> Antal dagar som objektet skulle vara utlånat
     * @return True om objektet är reserverat under perioden, annars false
     * @throws SQLException Om databasfel uppstår
     */
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

    /**
     * Kontrollerar om ett objekt är utlånat under en viss period.
     *
     * @param conn -> Databasanslutning
     * @param itemID -> ID för objektet som kontrolleras
     * @param fromDate -> Startdatum för kontroll
     * @param toDate -> Slutdatum för kontroll
     * @return True om objektet är utlånat under perioden, annars false
     * @throws SQLException Om databasfel uppstår
     */
    private boolean isOnLoan(Connection conn, int itemID, LocalDate fromDate, LocalDate toDate) throws SQLException {
        String query = "SELECT 1 FROM loanRow " +
                "WHERE itemID = ? " +
                "AND ActiveLoan = true " +
                "AND (? BETWEEN RowLoanStartDate AND RowLoanEndDate " +
                "OR RowLoanStartDate BETWEEN ? AND ?)";

        try (
            PreparedStatement stmt = conn.prepareStatement(query)
        ) {
            stmt.setInt(1, itemID);
            stmt.setDate(2, Date.valueOf(fromDate));
            stmt.setDate(3, Date.valueOf(fromDate));
            stmt.setDate(4, Date.valueOf(toDate));
            return stmt.executeQuery().next();
        }
    }
}
