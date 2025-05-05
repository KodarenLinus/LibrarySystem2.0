/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.library_system.Logic;
import com.mycompany.library_system.Database.ConnDB;
import com.mycompany.library_system.Database.DatabaseConnector;
import com.mycompany.library_system.Login.Session;
import com.mycompany.library_system.Models.ReservationRow;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class GetReservationRow {

    public ArrayList<ReservationRow> getReservationRowById() throws SQLException {
        Session session = Session.getInstance();
        int id = session.getUserId();
        String query =  "SELECT rr.*, r.reservationDate " +
                   "FROM reservationRow rr " +
                   "JOIN Reservation r ON rr.reservationID = r.ReservationID " +
                   "WHERE r.CustomerID = ?";

        DatabaseConnector connDB = new ConnDB();
        ArrayList<ReservationRow> reservationRows = new ArrayList<>();

        try (Connection conn = connDB.connect();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                reservationRows.add(mapResultSetToReservationRow(rs));
            }
        }
        return reservationRows;
    }


    public List<ReservationRow> getReservationRowsByReservationId(int reservationId) throws SQLException {
        List<ReservationRow> rows = new ArrayList<>();
        String query = "SELECT * FROM reservationRow WHERE reservationID = ?";

        DatabaseConnector connDB = new ConnDB();

        try (Connection conn = connDB.connect();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, reservationId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                rows.add(mapResultSetToReservationRow(rs));
            }
        }
        return rows;
    }

    private ReservationRow mapResultSetToReservationRow(ResultSet rs) throws SQLException {
        int resRowID = rs.getInt("reservationRowID");
        int resID = rs.getInt("reservationID");
        int itemID = rs.getInt("itemID");
        boolean isFufilled = rs.getBoolean("isFullfilled");
        LocalDate reservationDate = rs.getDate("reservationDate").toLocalDate();
        ReservationRow reservationRow = new ReservationRow(resID, itemID, isFufilled, reservationDate);
        reservationRow.setReservationRowID(resRowID);
        return reservationRow;
    }
}
