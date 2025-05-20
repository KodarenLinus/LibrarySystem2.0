package com.mycompany.library_system.Logic.ReservationMangement;

import com.mycompany.library_system.Database.ConnDB;
import com.mycompany.library_system.Database.DatabaseConnector;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;

/**
 * Klass som ansvarar för att hämta reservationsdatum kopplat till ett visst item.
 * 
 * @author Linus, Emil, Oliver, Viggo
 */
public class GetReservationDate {
    private final DatabaseConnector dbConnector;

    public GetReservationDate() {
        this.dbConnector = new ConnDB();
    }

    /**
     * Hämtar reservationsdatum för det item med angivet ID.
     * Returnerar det första matchande datumet eller null om ingen reservation finns.
     *
     * @param itemId ID på itemet
     * @return LocalDate för reservationen eller null om ingen hittas
     * @throws SQLException vid databasfel
     */
    public LocalDate getReservationDateForItem(int itemId) throws SQLException {
        // En SQL-Fråga som hämtar reservations datum från reservation tabellen för items
        String getReservationDate = "SELECT reservationDate FROM reservation WHERE "
                + "ReservationID IN (SELECT ReservationID FROM ReservationRow WHERE itemID = ? and isfullfilled = false)";

        try (
            Connection conn = dbConnector.connect();
            PreparedStatement getReservationDateStmt = conn.prepareStatement(getReservationDate);
        ) {

            getReservationDateStmt.setInt(1, itemId);
            try (
                ResultSet rs = getReservationDateStmt.executeQuery()
            ) {
                // Retunerar reservations datum om det finns ett resultset.
                if (rs.next()) {
                    return rs.getDate("reservationDate").toLocalDate();
                }
            }
        }

        // Ingen reservation hittad
        return null;
    }
}
