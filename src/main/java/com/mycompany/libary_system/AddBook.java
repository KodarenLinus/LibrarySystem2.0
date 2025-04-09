/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.libary_system;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.scene.control.TextField;
/**
 *
 * @author Linus
 */
public class AddBook {
    ConnDB connDB = new ConnDB();

    //En metod för att lägga till items av typen book!!!!!
    void insertBook (String title, String location, int isbn) {
        
        try {
            Connection conn = DriverManager.getConnection(connDB.getDbUrl(), connDB.getDbUsername(), connDB.getDbPassword());
        
            //Lägger till itemet i items!!
            PreparedStatement stmt1 = conn.prepareStatement("INSERT INTO Item (GenreID, CategoryID, Title, Location, Available) VALUES (?, ?, ?, ?, ?)");
                stmt1.setInt(1, 1);
                stmt1.setInt(2, 1);
                stmt1.setString(3, title);
                stmt1.setString(4, location);
                stmt1.setBoolean(5, true);
                stmt1.executeUpdate();
            
            //Lägger till itemet i book tablen
            PreparedStatement stmt2 = conn.prepareStatement("INSERT INTO Book (ItemID, ISBN, PublisherID) VALUES ((SELECT max(ItemID) From item), ?, ?)");
                stmt2.setInt(1, isbn);
                stmt2.setInt(2, 1);
                stmt2.executeUpdate();

        } catch (SQLException ex){

        }
    }
}
