/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.library_system.Logic;

import com.mycompany.library_system.Database.ConnDB;
import com.mycompany.library_system.Database.DatabaseConnector;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;

/**
 *
 * @author Linus
 */
public class GetReservationDate {
     /**
     * Hämtar reservationsdatum för ett visst item.
     * Returnerar null om itemet inte är reserverat.
     */
    public LocalDate getReservationDateForItem(int itemId) throws SQLException {
        DatabaseConnector connDB = new ConnDB();
        Connection conn = connDB.connect();
        
        String sql = "SELECT reservationDate FROM reservation WHERE "
                + "ReservationID IN (SELECT ReservationID FROM ReservationRow WHERE itemID = ?)";
        
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, itemId);

            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getDate("ReservationDate").toLocalDate();
            }
        }

        return null; // Inget datum hittades
    }
}
