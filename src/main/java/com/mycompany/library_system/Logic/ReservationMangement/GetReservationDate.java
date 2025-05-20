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
 * Följer SRP (Single Responsibility Principle): denna klass ansvarar endast för att läsa ut ett datum.
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
        String sql = "SELECT reservationDate FROM reservation WHERE "
                + "ReservationID IN (SELECT ReservationID FROM ReservationRow WHERE itemID = ? and isfullfilled = false)";

        try (Connection conn = dbConnector.connect();
             PreparedStatement getReservationDateStmt = conn.prepareStatement(sql);
        ) {

            getReservationDateStmt.setInt(1, itemId);
            try (ResultSet rs = getReservationDateStmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getDate("reservationDate").toLocalDate();
                }
            }
        }

        // Ingen reservation hittad
        return null;
    }
}
