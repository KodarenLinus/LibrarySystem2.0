/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.library_system.Logic;

import com.mycompany.library_system.Database.ConnDB;
import com.mycompany.library_system.Database.DatabaseConnector;
import com.mycompany.library_system.Models.Items;
import com.mycompany.library_system.Utils.AlertHandler;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import java.sql.Statement;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.stream.Collectors;

public class ReserveItem {
   
    private final AlertHandler alertHandler = new AlertHandler();
    private final GetCategoryLoanTime loanTimeHelper = new GetCategoryLoanTime();
    private final DatabaseConnector dbConnector = new ConnDB();

    /**
     * Försöker reservera en lista av objekt för en kund vid ett angivet datum.
     * Skapar en ny reservation, filtrerar bort otillgängliga objekt, och lägger till de tillgängliga i databasen.
     *
     * @param custID          ID för kunden som gör reservationen
     * @param itemsToReserve  Lista med objekt som kunden vill reservera
     * @param reserveDate     Datum då reservationen ska börja gälla
     * @return true om minst ett objekt reserverades, annars false
     */
    public boolean addToReservationRows(int custID, ArrayList<Items> itemsToReserve, LocalDate reserveDate) {
        try (Connection conn = dbConnector.connect()) {

            int reservationID = createReservation(conn, custID, reserveDate);
            if (reservationID == -1) return false;

            ArrayList<Items> validItems = filterAvailableItems(conn, itemsToReserve, reserveDate);
            if (validItems.isEmpty()) {
                alertHandler.createAlert("Reservation", "Ingen reservation kunde genomföras", "Inga objekt tillgängliga.");
                return false;
            }

            insertReservationRows(conn, reservationID, validItems);
            showSuccessAlert(validItems);
            return true;

        } catch (SQLException e) {
            e.printStackTrace();
            alertHandler.createAlert("Fel", "Databasfel", e.getMessage());
            return false;
        }
    }

     /**
     * Skapar en ny reservation i databasen.
     *
     * @param conn        Databasanslutning
     * @param custID      ID för kunden
     * @param reserveDate Datum för reservationen
     * @return ID för den nyskapade reservationen, eller kastar SQLException vid fel
     * @throws SQLException om det uppstår ett databasfel
     */
    private int createReservation(Connection conn, int custID, LocalDate reserveDate) throws SQLException {
        String sql = "INSERT INTO reservation (CustomerID, ReservationDate) VALUES (?, ?)";

        try (PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setInt(1, custID);
            stmt.setDate(2, Date.valueOf(reserveDate));
            stmt.executeUpdate();

            ResultSet keys = stmt.getGeneratedKeys();
            if (keys.next()) {
                return keys.getInt(1);
            }
            throw new SQLException("Kunde inte hämta reservationens ID.");
        }
    }

    /**
     * Filtrerar ut de objekt som är tillgängliga att reservera vid angivet datum.
     * Visar varningar för objekt som inte är tillgängliga.
     *
     * @param conn Databasanslutning
     * @param items Lista med objekt som önskas reserveras
     * @param reserveDate Datum då reservationen önskas
     * @return En lista med tillgängliga objekt
     * @throws SQLException om det uppstår ett databasfel
     */
    private ArrayList<Items> filterAvailableItems(Connection conn, ArrayList<Items> items, LocalDate reserveDate) throws SQLException {
        ArrayList<Items> validItems = new ArrayList<>();

        for (Items item : items) {
            LocalDate endDate = loanTimeHelper.calculatetLoanEndDate(conn, item.getItemID(), reserveDate);

            if (isAvailable(conn, item.getItemID(), reserveDate, endDate)) {
                validItems.add(item);
            } else {
                alertHandler.createAlert("Reservation", "Kan inte reservera", "Ej tillgänglig: " + item.getTitle());
            }
        }

        return validItems;
    }

    /**
     * Kontrollerar om ett objekt är tillgängligt att reservera under en viss period.
     * Ett objekt anses tillgängligt om det inte redan är reserverat eller utlånat under perioden.
     *
     * @param conn Databasanslutning
     * @param itemID ID för objektet
     * @param startDate Startdatum för reservationen
     * @param endDate Slutdatum för reservationen (beräknad utifrån kategori)
     * @return true om objektet är tillgängligt, annars false
     * @throws SQLException om det uppstår ett databasfel
     */
    private boolean isAvailable(Connection conn, int itemID, LocalDate startDate, LocalDate endDate) throws SQLException {
        // Om vi inte har en loan konflikt och reservation konflikt så retunerar vi True annars false
        return !hasReservationConflict(conn, itemID, startDate, endDate) &&
               !hasLoanConflict(conn, itemID, startDate, endDate);
    }

    /**
     * Kontrollerar om det finns en krock med en befintlig reservation för objektet.
     *
     * @param conn Databasanslutning
     * @param itemID ID för objektet
     * @param start Startdatum för reservationen
     * @param end Slutdatum för reservationen
     * @return true om det finns en krock, annars false
     * @throws SQLException om det uppstår ett databasfel
     */
    private boolean hasReservationConflict(Connection conn, int itemID, LocalDate start, LocalDate end) throws SQLException {
        // SQL-sträng som hämtar reservationRow för ett item under en viss period
        String sql = "SELECT 1 FROM reservationRow rr " +
            "JOIN reservation r ON rr.reservationID = r.reservationID " +
            "WHERE rr.itemID = ? " +
            "AND (r.reservationDate BETWEEN ? AND ? " +
            "OR ? BETWEEN r.reservationDate AND DATE_ADD(r.reservationDate, INTERVAL ? DAY))";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            long loanDays = ChronoUnit.DAYS.between(start, end);
            stmt.setInt(1, itemID);
            stmt.setDate(2, Date.valueOf(start));
            stmt.setDate(3, Date.valueOf(end));
            stmt.setDate(4, Date.valueOf(start));
            stmt.setLong(5, loanDays);

            return stmt.executeQuery().next();
        }
    }

    /**
     * Kontrollerar om objektet är utlånat under den angivna perioden.
     *
     * @param conn Databasanslutning
     * @param itemID ID för objektet
     * @param start Startdatum för reservationen
     * @param end Slutdatum för reservationen
     * @return true om objektet är utlånat under perioden, annars false
     * @throws SQLException om det uppstår ett databasfel
     */
    private boolean hasLoanConflict(Connection conn, int itemID, LocalDate start, LocalDate end) throws SQLException {
        // SQL-sträng som hämtar loanRow som är aktiva för ett item under en viss period
        String sql = "SELECT 1 FROM loanRow " +
            "WHERE itemID = ? " +
            "AND ActiveLoan = true " +
            "AND (? BETWEEN RowLoanStartDate AND RowLoanEndDate " +
            "OR RowLoanStartDate BETWEEN ? AND ?)";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, itemID);
            stmt.setDate(2, Date.valueOf(start));
            stmt.setDate(3, Date.valueOf(start));
            stmt.setDate(4, Date.valueOf(end));

            return stmt.executeQuery().next();
        }
    }

    /**
     * Lägger till rader i tabellen för reserverade objekt (reservationRow).
     * Varje rad representerar ett objekt som kopplas till en reservation.
     *
     * @param conn Databasanslutning
     * @param reservationID ID för den nyss skapade reservationen
     * @param items Lista med objekt att lägga till i reservationen
     * @throws SQLException om det uppstår ett databasfel
     */
    private void insertReservationRows(Connection conn, int reservationID, ArrayList<Items> items) throws SQLException {
        String sql = "INSERT INTO reservationrow (ReservationID, ItemID, IsFullfilled) VALUES (?, ?, ?)";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            for (Items item : items) {
                stmt.setInt(1, reservationID);
                stmt.setInt(2, item.getItemID());
                stmt.setBoolean(3, false); // ej uppfylld än
                stmt.addBatch();
            }
            stmt.executeBatch();
        }
    }

    /**
     * Visar en bekräftelsealert med titlarna på alla lyckade reservationer.
     *
     * @param items Lista med reserverade objekt
     */
    private void showSuccessAlert(ArrayList<Items> items) {
        // Skapar en alert med alla items som vi reserverat.
        String titles = items.stream()
                             .map(Items::getTitle)
                             .collect(Collectors.joining(", "));
        alertHandler.createAlert("Reservation", "Lyckad reservation", titles);
    }
}