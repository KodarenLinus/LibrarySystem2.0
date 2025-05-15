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
            PreparedStatement insertToItemStmt = conn.prepareStatement(insertToItem, PreparedStatement.RETURN_GENERATED_KEYS);
            PreparedStatement insertToDVDStmt = conn.prepareStatement(insertToDVD);
        ){
            // lägger till värden i item-tabelen
            insertToItemStmt.setInt(1, dvd.getGenreID());
            insertToItemStmt.setInt(2, dvd.getCategoryID());
            insertToItemStmt.setString(3, dvd.getGenreName());
            insertToItemStmt.setString(4, dvd.getCategoryName());
            insertToItemStmt.setString(5, dvd.getTitle());
            insertToItemStmt.setString(6, dvd.getLocation());
            insertToItemStmt.setBoolean(7, true);
            insertToItemStmt.executeUpdate();
            
            // Hämta genererat ItemID
            int generatedItemID = -1;
            try (
                var generatedKeys = insertToItemStmt.getGeneratedKeys();
            ) {
                if (generatedKeys.next()) {
                    generatedItemID = generatedKeys.getInt(1);
                } else {
                    throw new SQLException("Kunde inte hämta genererat ItemID.");
                }
            }

            // Lägger till värden i DVD-tabellen
            insertToDVDStmt.setInt(1, generatedItemID);
            insertToDVDStmt.setInt(2, dvd.getDirectorID());
            insertToDVDStmt.executeUpdate();

        } catch (SQLException ex){
            ex.printStackTrace();
        }
    }
}
