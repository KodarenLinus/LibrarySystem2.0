package com.mycompany.library_system.Logic.ItemManagement;

import com.mycompany.library_system.Database.ConnDB;
import com.mycompany.library_system.Database.DatabaseConnector;
import com.mycompany.library_system.Models.Book;
import com.mycompany.library_system.Models.DVD;
import com.mycompany.library_system.Models.Items;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class RemoveItem {
    private final DatabaseConnector dbConnector;

    public RemoveItem() {
        this.dbConnector = new ConnDB();
    }

    /**
     * Tar bort ett item från Item-tabellen samt dess motsvarande Book- eller DVD-tabell.
     *
     * @param item Ett objekt av typen Book eller DVD.
     * @return true om allt togs bort korrekt, annars false.
     */
    public boolean deleteItem(Items item) {
        int itemID = item.getItemID();

        try (Connection conn = dbConnector.connect()) {
            conn.setAutoCommit(false); // Start transaktion

            // Ta bort från rätt tabell först
            boolean subtypeRemoved = false;

            if (item instanceof Book) {
                subtypeRemoved = deleteBook(conn, itemID);
            } else if (item instanceof DVD) {
                subtypeRemoved = deleteDVD(conn, itemID);
            } else {
                System.err.println("Objektet är varken Book eller DVD.");
                return false;
            }

            if (!subtypeRemoved) {
                conn.rollback();
                return false;
            }

            // Ta bort från Item-tabellen
            boolean itemRemoved = deleteFromItem(conn, itemID);
            if (!itemRemoved) {
                conn.rollback();
                return false;
            }

            conn.commit();
            return true;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    private boolean deleteBook(Connection conn, int itemID) throws SQLException {
        String sql = "DELETE FROM Book WHERE ItemID = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, itemID);
            return stmt.executeUpdate() > 0;
        }
    }

    private boolean deleteDVD(Connection conn, int itemID) throws SQLException {
        String sql = "DELETE FROM DVD WHERE ItemID = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, itemID);
            return stmt.executeUpdate() > 0;
        }
    }

    private boolean deleteFromItem(Connection conn, int itemID) throws SQLException {
        String sql = "DELETE FROM Item WHERE ItemID = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, itemID);
            return stmt.executeUpdate() > 0;
        }
    }
}
