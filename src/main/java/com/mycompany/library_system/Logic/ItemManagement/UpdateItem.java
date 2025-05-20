/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.library_system.Logic.ItemManagement;

import com.mycompany.library_system.Database.ConnDB;
import com.mycompany.library_system.Database.DatabaseConnector;
import com.mycompany.library_system.Models.Items;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * Klassen ansvarar för att uppdatera information om ett objekt (Item) i databasen.
 * 
 * Här finns en metod för att uppdatera titeln på ett specifikt objekt baserat på dess ID.
 * Klassen använder sig av en databasanslutning för att utföra SQL-uppdateringar.
 * 
 * @author Linus, Emil, Oliver, Viggo
 */
public class UpdateItem {
    private final DatabaseConnector dbConnector;
    
    public UpdateItem() {
        this.dbConnector = new ConnDB();
    }
    
    /**
     * Uppdaterar titeln på ett item i databasen baserat på dess ID.
     *
     * @param item  Objektet vars titel ska uppdateras
     * @param title Den nya titeln som ska sparas i databasen
     * @return null (ingen återkoppling på objektet hanteras här)
     */
    public Items updateItemTitle(Items item, String title) {
        // Hämtar ItemID från vårt item objekt och spara ned det i itemID
        int itemID = item.getItemID();
        
        // En SQL-Fråga för att uppdatera items title
        String updateTitle = "UPDATE Item "
                + "SET Title = ? "
                + "WHERE ItemID = ?";
        
        try (
            Connection conn = dbConnector.connect();
            PreparedStatement updateTitleStmt = conn.prepareStatement(updateTitle);
        ) {
            updateTitleStmt.setString(1, title);
            updateTitleStmt.setInt(2, itemID);
            updateTitleStmt.executeUpdate();
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return null;
    }
}
