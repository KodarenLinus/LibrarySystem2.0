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
 * En klass som hantera Dvd:er som skall läggas in i databasen
 * 
 * @author Linus, Emil, Oliver, Viggo
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
        String insertToItem = "INSERT INTO Item (GenreID, CategoryID, GenreName, CategoryName, Title, Location, Available) VALUES (?, ?, ?, ?, ?, ?, ?)";
        String insertToDVD = "INSERT INTO DvD (ItemID, DirectorID) VALUES ((SELECT max(ItemID) From item), ?)";
                
        try (
            PreparedStatement stmt1 = conn.prepareStatement(insertToItem);
            PreparedStatement stmt2 = conn.prepareStatement(insertToDVD);
        ){
            // lägger till värden i item tabelen
            stmt1.setInt(1, dvd.getGenreID());
            stmt1.setInt(2, dvd.getCategoryID());
            stmt1.setString(3, dvd.getGenreName());
            stmt1.setString(4, dvd.getCategoryName());
            stmt1.setString(5, dvd.getTitle());
            stmt1.setString(6, dvd.getLocation());
            stmt1.setBoolean(7, true);
            stmt1.executeUpdate();
            
            // lägger till värden i dvd tabelen
            stmt2.setInt(1, 1);
            stmt2.executeUpdate();

        } catch (SQLException ex){

        }
    }
}
