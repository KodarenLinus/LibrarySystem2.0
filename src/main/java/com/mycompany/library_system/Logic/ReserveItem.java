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

public class ReserveItem {
    AlertHandler alertHandler = new AlertHandler();
    /**
     * Lägger till en reservation för en kund och returnerar true om det lyckas.
     * 
     * @param custID ID för kunden som reserverar
     * @param itemsToReserve Lista med objekt som ska reserveras
     * @return true om lyckad reservation
     */
    public boolean addToReservationRows(int custID, ArrayList<Items> itemsToReserve, LocalDate reserveDate) {
        

        DatabaseConnector connDB = new ConnDB();
        try (Connection conn = connDB.connect()) {
            int reservationID = createReservationAndReturnID(conn, custID, reserveDate);
            if (reservationID == -1) return false;

            insertReservationRows(conn, reservationID, itemsToReserve, reserveDate);

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
   private void insertReservationRows(Connection conn, int reservationID, ArrayList<Items> itemsToReserve, LocalDate reserveDate) throws SQLException {
        String insertRowSQL = "INSERT INTO reservationrow (ReservationID, ItemID, IsFullfilled) VALUES (?, ?, ?)";
        GetCategoryLoanTime getCategoryLoanTime = new GetCategoryLoanTime();
        ArrayList<Items> toRemove = new ArrayList<>();

        try (
            PreparedStatement stmt = conn.prepareStatement(insertRowSQL);
            PreparedStatement checkReservationStmt = conn.prepareStatement(
                 "SELECT 1 " +
                 "FROM reservationRow rr " +
                 "JOIN reservation r ON rr.reservationID = r.reservationID " +
                 "WHERE rr.itemID = ? " +
                 "AND (r.reservationDate BETWEEN ? AND ?) " +
                 "OR (? BETWEEN r.reservationDate AND DATE_ADD(r.reservationDate, INTERVAL ? DAY))");
            PreparedStatement checkLoanStmt = conn.prepareStatement(
                    "SELECT 1 "
                    + "FROM loanRow "
                    + "WHERE itemID = ? "
                    + "AND (ActiveLoan = true) "
                    + "AND (? BETWEEN RowLoanStartDate AND RowLoanEndDate) "
                    + "OR (RowLoanStartDate BETWEEN ? AND ?)"
            );
        ) {
            for (Items item : itemsToReserve) {
                int itemID = item.getItemID();

                LocalDate loanEndDate = getCategoryLoanTime.calculatetLoanEndDate(conn, itemID, reserveDate);
                long loanDays = ChronoUnit.DAYS.between(reserveDate, loanEndDate);

                // Kontrollera reservation
                checkReservationStmt.setInt(1, itemID);
                checkReservationStmt.setDate(2, java.sql.Date.valueOf(reserveDate));
                checkReservationStmt.setDate(3, java.sql.Date.valueOf(loanEndDate));
                checkReservationStmt.setDate(4, java.sql.Date.valueOf(reserveDate));
                checkReservationStmt.setLong(5, loanDays);

                ResultSet rsCheckReservation = checkReservationStmt.executeQuery();
                if (rsCheckReservation.next()) {
                    alertHandler.createAlert("Reservation", "Kan inte genomföra reservation", "Är inte tillgänglig: " + item.getTitle());
                    toRemove.add(item);
                    continue;
                }

                // Kontrollera aktivt lån
                checkLoanStmt.setInt(1, itemID);
                checkLoanStmt.setDate(2, java.sql.Date.valueOf(reserveDate));
                checkLoanStmt.setDate(3, java.sql.Date.valueOf(reserveDate));
                checkLoanStmt.setDate(4, java.sql.Date.valueOf(loanEndDate));

                ResultSet rsCheckLoan = checkLoanStmt.executeQuery();
                if (rsCheckLoan.next()) {
                    alertHandler.createAlert("Reservation", "Kan inte genomföra reservation", "Är inte tillgänglig: " + item.getTitle());
                    toRemove.add(item);
                    continue;
                }

                stmt.setInt(1, reservationID);
                stmt.setInt(2, item.getItemID());
                stmt.setBoolean(3, false); // Inte uppfylld än
                stmt.addBatch();
            }

            // Ta bort ogiltiga objekt efter loopen
            itemsToReserve.removeAll(toRemove);

            String title = "Reservation";
            String header = itemsToReserve.size() > 0 ? "Lyckad reservation" : "Ingen reservation genomfördes";
            StringBuilder content = new StringBuilder();

            for (Items item : itemsToReserve) {
                content.append(item.getTitle()).append(", ");
            }

            if (itemsToReserve.size() > 0) {
                stmt.executeBatch();
            }
            alertHandler.createAlert(title, header, content.toString());
        }
    }
}