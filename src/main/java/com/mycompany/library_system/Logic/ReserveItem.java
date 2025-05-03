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
import java.util.ArrayList;

public class ReserveItem {

    /**
     * Lägger till en reservation för en kund och returnerar true om det lyckas.
     * 
     * @param custID ID för kunden som reserverar
     * @param itemsToReserve Lista med objekt som ska reserveras
     * @return true om lyckad reservation
     */
    public boolean addToReservationRows(int custID, ArrayList<Items> itemsToReserve, LocalDate reserveDate) {
        AlertHandler alertHandler = new AlertHandler();

        DatabaseConnector connDB = new ConnDB();
        try (Connection conn = connDB.connect()) {
            int reservationID = createReservationAndReturnID(conn, custID, reserveDate);
            if (reservationID == -1) return false;

            insertReservationRows(conn, reservationID, itemsToReserve);

            String title = "Reservation";
            String header = "Lyckad reservation";
            StringBuilder content = new StringBuilder("Du har reserverat följande: ");
            for (Items item : itemsToReserve) {
                content.append(item.getTitle()).append(", ");
            }

            alertHandler.createAlert(title, header, content.toString());

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }

        return true;
    }

    /**
     * Skapar en ny reservation och returnerar ID:t.
     */
    private int createReservationAndReturnID(Connection conn, int custID, LocalDate reserveDate) throws SQLException {
        String insertReservationSQL = "INSERT INTO reservation (CustomerID, ReservationDate) VALUES (?, ?)";

        try (PreparedStatement insertStmt = conn.prepareStatement(insertReservationSQL, Statement.RETURN_GENERATED_KEYS)) {
            insertStmt.setInt(1, custID);
            insertStmt.setDate(2, Date.valueOf(reserveDate));
            insertStmt.executeUpdate();

            try (ResultSet generatedKeys = insertStmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    return generatedKeys.getInt(1);
                } else {
                    throw new SQLException("Misslyckades att hämta genererat reservation-ID.");
                }
            }
        }
    }

    /**
     * Lägger till rader i reservationrow-tabellen.
     */
    private void insertReservationRows(Connection conn, int reservationID, ArrayList<Items> itemsToReserve) throws SQLException {
        String insertRowSQL = "INSERT INTO reservationrow (ReservationID, ItemID, IsFullfilled) VALUES (?, ?, ?)";

        try (PreparedStatement stmt = conn.prepareStatement(insertRowSQL)) {
            for (Items item : itemsToReserve) {
                stmt.setInt(1, reservationID);
                stmt.setInt(2, item.getItemID());
                stmt.setBoolean(3, false); // Inte uppfylld än
                stmt.addBatch();
            }
            stmt.executeBatch();
        }
    }
}