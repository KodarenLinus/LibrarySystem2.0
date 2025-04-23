/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.library_system.Logic;

import com.mycompany.library_system.Models.DVD;
import com.mycompany.library_system.Database.DatabaseConnector;
import com.mycompany.library_system.Database.ConnDB;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 *
 * @author Linus
 */
public class AddDVD {

    /**
     * Lägger till en book i databasen
     *
     * @param Ett dvd objekt som vi skickar till databasen
     */
    public void insertDVD (DVD dvd) {
        
        DatabaseConnector connDB = new ConnDB();
        Connection conn = connDB.connect();
        String insertToItem = "INSERT INTO Item (GenreID, CategoryID, Title, Location, Available) VALUES (?, ?, ?, ?, ?)";
        String insertToDVD = "INSERT INTO DvD (ItemID, DirectorID) VALUES ((SELECT max(ItemID) From item), ?)";
                
        try (
            PreparedStatement stmt1 = conn.prepareStatement(insertToItem);
            PreparedStatement stmt2 = conn.prepareStatement(insertToDVD);
        ){
            // lägger till värden i item tabelen
            stmt1.setInt(1, 10);
            stmt1.setInt(2, 9);
            stmt1.setString(3, dvd.getTitle());
            stmt1.setString(4, dvd.getLocation());
            stmt1.setBoolean(5, true);
            stmt1.executeUpdate();
            
            // lägger till värden i dvd tabelen
            stmt2.setInt(1, 1);
            stmt2.executeUpdate();

        } catch (SQLException ex){

        }
    }
}
