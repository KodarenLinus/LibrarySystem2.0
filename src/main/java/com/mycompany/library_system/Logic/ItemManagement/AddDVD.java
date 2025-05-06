/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.library_system.Logic.ItemManagement;

import com.mycompany.library_system.Models.DVD;
import com.mycompany.library_system.Database.DatabaseConnector;
import com.mycompany.library_system.Database.ConnDB;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * Hanterar inmatning av DVD-objekt i databasen.
 * Lägger först till information i Item-tabellen,
 * följt av ett infogande i DVD-tabellen.
 *  
 * @author Linus, Emil, Oliver, Viggo
 */
public class AddDVD {
    
    private final DatabaseConnector dbConnector;

    public AddDVD () {
        this.dbConnector = new ConnDB();
    }

    /**
     * Lägger till en DVD i databasen.
     * Använder RETURN_GENERATED_KEYS för att hämta ItemID efter första insert.
     *
     * @param dvd Ett DVD-objekt som innehåller titel, genre, kategori, plats och regissör.
     */
    public void insertDVD (DVD dvd) {
        
         // Skapar en databasanslutning
        Connection conn = dbConnector.connect();
        
        // SQL-fråga för att infoga i Item-tabellen
        String insertToItem = "INSERT INTO Item (GenreID, CategoryID, GenreName, CategoryName, Title, Location, Available) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?)";
        
        // SQL-fråga för att infoga i DVD-tabellen
        String insertToDVD = "INSERT INTO DVD (ItemID, DirectorID) "
                + "VALUES (?, ?)";
                
        try (
            PreparedStatement stmt1 = conn.prepareStatement(insertToItem);
            PreparedStatement stmt2 = conn.prepareStatement(insertToDVD);
        ){
            // lägger till värden i item-tabelen
            stmt1.setInt(1, dvd.getGenreID());
            stmt1.setInt(2, dvd.getCategoryID());
            stmt1.setString(3, dvd.getGenreName());
            stmt1.setString(4, dvd.getCategoryName());
            stmt1.setString(5, dvd.getTitle());
            stmt1.setString(6, dvd.getLocation());
            stmt1.setBoolean(7, true);
            stmt1.executeUpdate();
            
            // Hämta genererat ItemID
            int generatedItemID = -1;
            try (var generatedKeys = stmt1.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    generatedItemID = generatedKeys.getInt(1);
                } else {
                    throw new SQLException("Kunde inte hämta genererat ItemID.");
                }
            }

            // Lägger till värden i DVD-tabellen
            stmt2.setInt(1, generatedItemID);
            stmt2.setInt(2, dvd.getDirectorID());
            stmt2.executeUpdate();

        } catch (SQLException ex){
            ex.printStackTrace();
        }
    }
}
