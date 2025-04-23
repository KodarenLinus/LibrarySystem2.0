/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.library_system.Logic;

import com.mycompany.library_system.Models.Book;
import com.mycompany.library_system.Database.DatabaseConnector;
import com.mycompany.library_system.Database.ConnDB;
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
 * En klass som hantera böcker som skall läggas in i databasen
 * 
 * @author Linus, Emil, Oliver, Viggo
 */
public class AddBook {

     /**
     * Lägger till en book i databasen
     *
     * @param Ett book objekt som vi skickar till databasen
     */
    public void insertBook (Book book) {
        
        DatabaseConnector connDB = new ConnDB();
        Connection conn = connDB.connect();
        String insertToItem = "INSERT INTO Item (GenreID, CategoryID, Title, Location, Available) VALUES (?, ?, ?, ?, ?)";
        String insertToBook = "INSERT INTO Book (ItemID, ISBN, PublisherID) VALUES ((SELECT max(ItemID) From item), ?, ?)";
        
        try (
            PreparedStatement stmt1 = conn.prepareStatement(insertToItem);
            PreparedStatement stmt2 = conn.prepareStatement(insertToBook);
        ){
            // Lägger in värden för item tabelen
            stmt1.setInt(1, 10);
            stmt1.setInt(2, 9);
            stmt1.setString(3, book.getTitle());
            stmt1.setString(4, book.getLocation());
            stmt1.setBoolean(5, true);
            stmt1.executeUpdate();
            
            // lägger in värden för book tabelen
            stmt2.setInt(1, book.getIsbn());
            stmt2.setInt(2, 7);
            stmt2.executeUpdate();

        } catch (SQLException ex){
            
        }
    }
}
