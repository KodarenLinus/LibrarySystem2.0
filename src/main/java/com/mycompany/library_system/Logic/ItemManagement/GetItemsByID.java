package com.mycompany.library_system.Logic.ItemManagement;

import com.mycompany.library_system.Logic.ItemManagement.ItemFactory;
import com.mycompany.library_system.Database.ConnDB;
import com.mycompany.library_system.Database.DatabaseConnector;
import com.mycompany.library_system.Models.Items;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Klass som hämtar ett enskilt objekt (Item) baserat på dess ID.
 * Använder ItemFactory för att skapa rätt typ av objekt (Book eller DVD).
 * 
 * @author Linus
 */
public class GetItemsByID {

    private final DatabaseConnector dbConnector;

    public GetItemsByID() {
        this.dbConnector = new ConnDB();
    }

    /**
     * Hämtar ett objekt (Item) från databasen utifrån dess ID.
     *
     * @param itemId ID för det önskade objektet
     * @return Ett Items-objekt (Book eller DVD), eller null om inget hittades
     * @throws SQLException vid databasfel
     */
    public Items getItemById(int itemId) throws SQLException {
        Items item = null;

        String query = "SELECT * FROM item WHERE ItemID = ?";

        try (
            Connection conn = dbConnector.connect();
            PreparedStatement findItemStmt = conn.prepareStatement(query)
        ) {
            findItemStmt.setInt(1, itemId);
            ResultSet rs = findItemStmt.executeQuery();

            if (rs.next()) {
                // Använd vår factory för att skapa rätt objekt
                item = ItemFactory.createItemFromResultSet(rs, conn);
            }
        }

        return item;
    }
}
