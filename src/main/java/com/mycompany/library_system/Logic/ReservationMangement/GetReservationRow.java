package com.mycompany.library_system.Logic.ReservationMangement;

import com.mycompany.library_system.Database.ConnDB;
import com.mycompany.library_system.Database.DatabaseConnector;
import com.mycompany.library_system.Login.Session;
import com.mycompany.library_system.Models.ReservationRow;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;

/**
 * Klass som hanterar hämtning av reservationsrader från databasen.
 * 
 * @author Linus, Emil, Viggo, Oliver
 */
public class GetReservationRow {
    private final DatabaseConnector dbConnector;

    public GetReservationRow() {
        this.dbConnector = new ConnDB();
    }

    /**
     * Hämtar alla reservationsrader som tillhör den aktuella inloggade användaren (via session).
     * 
     * @return Lista av ReservationRow för aktuell användare
     * @throws SQLException om databasfel uppstår
     */
    public ArrayList<ReservationRow> getReservationRow() throws SQLException {
        // Skapar en Session varibel och hämtar nuvrande userID
        Session session = Session.getInstance();
        int userId = session.getUserId();
                
        // En ArrayList som vi lägger våra reservationRows i
        ArrayList<ReservationRow> reservationRows = new ArrayList<>();
        
        // En SQL-Fråga som hämtar reservationRow och datum som tillhör den
        String getReservationRowAndDateQuery = "SELECT rr.*, r.reservationDate " +
            "FROM reservationRow rr " +
            "JOIN reservation r ON rr.reservationID = r.reservationID " +
            "WHERE r.CustomerID = ? AND isFullfilled = false";

        try (
            Connection conn = dbConnector.connect();
            PreparedStatement getReservationRowAndDatestmt = conn.prepareStatement(getReservationRowAndDateQuery)
        ) {
            getReservationRowAndDatestmt.setInt(1, userId);
            ResultSet rs = getReservationRowAndDatestmt.executeQuery();
            
            // Loppar igenom resultsetet och stoppar reservationrows i vår ArrayLista
            while (rs.next()) {
                reservationRows.add(mapResultSetToReservationRow(rs));
            }
        }

        return reservationRows;
    }

    /**
     * Hämtar reservationsrader för en specifik reservation.
     * 
     * @param reservationId Reservationens ID
     * @return Lista av ReservationRow
     * @throws SQLException om databasfel uppstår
     */
    public ArrayList<ReservationRow> getReservationRowsByReservationId(int reservationId) throws SQLException {
         // En SQL-Fråga som hämtar reservationRow och datum som tillhör den
        String getReservationRowAndDateQuery = "SELECT rr.*, r.reservationDate " +
                       "FROM reservationRow rr " +
                       "JOIN reservation r ON rr.reservationID = r.reservationID " +
                       "WHERE rr.reservationID = ?";
        
        // En ArrayLista vi sparar våra ReservationsRows.
        ArrayList<ReservationRow> rows = new ArrayList<>();

        try (
            Connection conn = dbConnector.connect();
            PreparedStatement getReservationRowAndDatestmt = conn.prepareStatement(getReservationRowAndDateQuery)
        ) {

            getReservationRowAndDatestmt.setInt(1, reservationId);
            ResultSet rs = getReservationRowAndDatestmt.executeQuery();
            
            // Loppar igenom resultsetet och lägget till den i våran ArrayList
            while (rs.next()) {
                rows.add(mapResultSetToReservationRow(rs));
            }
        }

        return rows;
    }

    /**
     * Hjälpmetod för att konvertera en ResultSet-rad till ett ReservationRow-objekt.
     * 
     * @param rs ResultSet från databasfrågan
     * @return Ett färdigkonstruerat ReservationRow-objekt
     * @throws SQLException om något går fel vid läsning
     */
    private ReservationRow mapResultSetToReservationRow(ResultSet rs) throws SQLException {
        int resRowID = rs.getInt("reservationRowID");
        int resID = rs.getInt("reservationID");
        int itemID = rs.getInt("itemID");
        boolean isFulfilled = rs.getBoolean("isFullfilled");
        LocalDate reservationDate = rs.getDate("reservationDate").toLocalDate();

        ReservationRow reservationRow = new ReservationRow(resID, itemID, isFulfilled, reservationDate);
        reservationRow.setReservationRowID(resRowID);

        return reservationRow;
    }
}
