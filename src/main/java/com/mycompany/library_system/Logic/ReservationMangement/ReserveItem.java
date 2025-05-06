package com.mycompany.library_system.Logic.ReservationMangement;

import com.mycompany.library_system.Logic.ItemManagement.GetCategoryLoanTime;
import com.mycompany.library_system.Database.ConnDB;
import com.mycompany.library_system.Database.DatabaseConnector;
import com.mycompany.library_system.Models.Items;
import com.mycompany.library_system.Utils.AlertHandler;

import java.sql.*;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

/**
 * En klass som hanterar reservationer av objekt i bibliotekssystemet.
 * Klassen ansvarar för att kontrollera tillgänglighet, skapa reservationer
 * och reservera tillgängliga objekt.
 *
 * @author Linus, Emil, Oliver, Viggo
 */
public class ReserveItem {

    private static final Logger logger = Logger.getLogger(ReserveItem.class.getName());

    private final AlertHandler alertHandler;
    private final GetCategoryLoanTime loanTimeHelper;
    private final DatabaseConnector dbConnector;

    public ReserveItem() {
        this.dbConnector = new ConnDB();
        this.alertHandler = new AlertHandler();
        this.loanTimeHelper = new GetCategoryLoanTime();
    }

    /**
     * Försöker reservera en lista av objekt för en kund vid ett angivet datum.
     *
     * @param custID         ID för kunden som gör reservationen
     * @param itemsToReserve Lista med objekt att reservera
     * @param reserveDate    Datum då reservationen ska börja gälla
     * @return true om minst ett objekt reserverades, annars false
     */
    public boolean addToReservationRows(int custID, ArrayList<Items> itemsToReserve, LocalDate reserveDate) {
        try (
            Connection conn = dbConnector.connect();
        ) {
            conn.setAutoCommit(false); // Startar transaktion

            int reservationID = createReservation(conn, custID, reserveDate);
            if (reservationID == -1) return false;

            ArrayList<Items> validItems = filterAvailableItems(conn, itemsToReserve, reserveDate);
            if (validItems.isEmpty()) {
                alertHandler.createAlert("Reservation", "Ingen reservation kunde genomföras", "Inga objekt tillgängliga.");
                conn.rollback(); // Rullar tillbaka om inget kunde reserveras
                return false;
            }

            insertReservationRows(conn, reservationID, validItems);
            conn.commit(); // Bekräftar alla ändringar

            showSuccessAlert(validItems);
            return true;

        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Fel vid reservation", e);
            alertHandler.createAlert("Fel", "Databasfel", e.getMessage());
            return false;
        }
    }

    /**
     * Skapar en ny reservation i databasen.
     *
     * @param conn en databas koppling
     * @param custID ett customerID
     * @param reserveDate ett reserverings datum
     * @return ID för den skapade reservationen
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
            throw new SQLException("Kunde inte hämta reservations-ID.");
        }
    }

    /**
     * Filtrerar ut tillgängliga objekt genom att kontrollera reservationer och lån.
     * 
     * @param conn en databas koppling
     * @param items en lista med items
     * @reserveDate ett reserverings datum
     * @return en lista med tillgängliga items
     */
    private ArrayList<Items> filterAvailableItems(Connection conn, ArrayList<Items> items, LocalDate reserveDate) throws SQLException {
        ArrayList<Items> validItems = new ArrayList<>();

        for (Items item : items) {
            LocalDate endDate = loanTimeHelper.calculateLoanEndDate(item.getItemID(), reserveDate);

            if (isAvailable(conn, item.getItemID(), reserveDate, endDate)) {
                validItems.add(item);
            } else {
                alertHandler.createAlert("Reservation", "Kan inte reservera", "Ej tillgänglig: " + item.getTitle());
            }
        }

        return validItems;
    }

    /**
     * Kollar om ett objekt är tillgängligt under en viss period.
     * 
     * @param conn en databas koppling
     * @param itemID items id som ska reserveras
     * @param start startdatum för reservation
     * @param end slutdatum för reservation
     * @throws SQLException
     * @return true om de inte finns någon konflikt i lån eller reservation. Om de finns en kolfikt retunerar den false
     */
    private boolean isAvailable(Connection conn, int itemID, LocalDate startDate, LocalDate endDate) throws SQLException {
        return !hasReservationConflict(conn, itemID, startDate, endDate)
            && !hasLoanConflict(conn, itemID, startDate, endDate);
    }

    /**
     * Kollar om det redan finns en reservation som krockar.
     * 
     * @param conn en databas koppling
     * @param itemID items id som ska reserveras
     * @param start startdatum för reservation
     * @param end slutdatum för reservation
     * @throws SQLException
     * @return true om de finns en reservations konflikt, annars false
     */
    private boolean hasReservationConflict(Connection conn, int itemID, LocalDate start, LocalDate end) throws SQLException {
        String sql = "SELECT 1 FROM reservationRow rr " +
            "JOIN reservation r ON rr.reservationID = r.reservationID " +
            "WHERE rr.itemID = ? " +
            "AND (r.reservationDate BETWEEN ? AND ? " +
            "OR ? BETWEEN r.reservationDate AND DATE_ADD(r.reservationDate, INTERVAL ? DAY))";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            long days = ChronoUnit.DAYS.between(start, end);
            stmt.setInt(1, itemID);
            stmt.setDate(2, Date.valueOf(start));
            stmt.setDate(3, Date.valueOf(end));
            stmt.setDate(4, Date.valueOf(start));
            stmt.setLong(5, days);

            return stmt.executeQuery().next();
        }
    }

    /**
     * Kollar om objektet är utlånat under perioden.
     * 
     * @param conn en databas koppling
     * @param itemID items id som ska reserveras
     * @param start startdatum för reservation
     * @param end slutdatum för reservation
     * @throws SQLException
     * @return true om de finns en lån konflikt, annars false
     */
    private boolean hasLoanConflict(Connection conn, int itemID, LocalDate start, LocalDate end) throws SQLException {
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
     * Lägger till de tillgängliga objekten i reservationRow-tabellen.
     * 
     * @param conn en databas koppling
     * @param reservationID det reservationID som våran reservationRow ska ha.
     * @param items en lista med items som skall reserveras
     * @throws SQLException
     */
    private void insertReservationRows(Connection conn, int reservationID, ArrayList<Items> items) throws SQLException {
        String sql = "INSERT INTO reservationrow (ReservationID, ItemID, IsFullfilled) VALUES (?, ?, ?)";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            for (Items item : items) {
                stmt.setInt(1, reservationID);
                stmt.setInt(2, item.getItemID());
                stmt.setBoolean(3, false); // Ej uppfylld ännu
                stmt.addBatch();
            }
            stmt.executeBatch();
        }
    }

    /**
     * Visar bekräftelsemeddelande för alla reserverade objekt.
     * 
     * @param items lista med items som ska reserveras
     */
    private void showSuccessAlert(ArrayList<Items> items) {
        String titles = items.stream()
            .map(Items::getTitle)
            .collect(Collectors.joining(", "));
        alertHandler.createAlert("Reservation", "Lyckad reservation", titles);
    }
}
