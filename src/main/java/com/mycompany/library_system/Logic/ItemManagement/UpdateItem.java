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
 *
 * @author Linus, Emil, Oliver, Viggo
 */
public class UpdateItem {
    private final DatabaseConnector dbConnector;

    public UpdateItem() {
        this.dbConnector = new ConnDB();
    }
    
    public Items updateItemTitle(Items item, String title) {
        int itemID = item.getItemID();
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
