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

/**
 *
 * @author Linus
 */
public class AddBook {

    //En metod för att lägga till items av typen book!!!!!
    void insertBook (Book book) {
        
        DatabaseConnector connDB = new ConnDB();
        Connection conn = connDB.connect();
        
        try {
        
            //Lägger till itemet i items!!
            PreparedStatement stmt1 = conn.prepareStatement("INSERT INTO Item (GenreID, CategoryID, Title, Location, Available) VALUES (?, ?, ?, ?, ?)");
                stmt1.setInt(1, 1);
                stmt1.setInt(2, 1);
                stmt1.setString(3, book.getTitle());
                stmt1.setString(4, book.getLocation());
                stmt1.setBoolean(5, true);
                stmt1.executeUpdate();
            
            //Lägger till itemet i book tablen
            PreparedStatement stmt2 = conn.prepareStatement("INSERT INTO Book (ItemID, ISBN, PublisherID) VALUES ((SELECT max(ItemID) From item), ?, ?)");
                stmt2.setInt(1, book.getIsbn());
                stmt2.setInt(2, 1);
                stmt2.executeUpdate();

        } catch (SQLException ex){

        }
    }
}
