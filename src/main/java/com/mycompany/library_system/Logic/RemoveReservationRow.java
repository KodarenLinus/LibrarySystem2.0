/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.library_system.Logic;

import com.mycompany.library_system.Database.ConnDB;
import com.mycompany.library_system.Database.DatabaseConnector;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 *
 * @author Linus, Emil, Oliver, Viggo
 */
public class RemoveReservationRow {
    /**
     * Tar bort en reservationsrad från databasen.
     *
     * @param reservationRowID ID för raden som ska tas bort
     * @return true om raden togs bort, annars false
     */
    public boolean deleteReservationRow(int reservationRowID) {
        String sql = "DELETE FROM ReservationRow WHERE reservationRowID = ?";

        DatabaseConnector connDB = new ConnDB();

        try (Connection conn = connDB.connect();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, reservationRowID);
            int affectedRows = stmt.executeUpdate();
            return affectedRows > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}

