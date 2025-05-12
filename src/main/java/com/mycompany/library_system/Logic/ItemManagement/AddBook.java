/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.library_system.Logic.ItemManagement;

import com.mycompany.library_system.Models.Book;
import com.mycompany.library_system.Database.DatabaseConnector;
import com.mycompany.library_system.Database.ConnDB;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * Hanterar inmatning av bokobjekt till databasen.
 * Utför INSERT till både Item- och Book-tabellerna.
 * 
 * @author Linus, Emil, Oliver, Viggo
 */
public class AddBook {
    
    private final DatabaseConnector dbConnector;

    public AddBook () {
        this.dbConnector = new ConnDB();
    }

     /**
     * Lägger till en bok i databasen.
     * Först infogas bokens metadata i Item-tabellen,
     * därefter läggs tillhörande information in i Book-tabellen.
     *
     * @param book Ett bokobjekt som innehåller titel, genre, kategori, plats och ISBN.
     */
    public void insertBook (Book book) {
        
        // Skapar en databasanslutning
        Connection conn = dbConnector.connect();
        
        // SQL-fråga för att infoga i Item-tabellen
        String insertToItem = "INSERT INTO Item (GenreID, CategoryID, GenreName, CategoryName, Title, Location, Available) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?)";
        
        // SQL-fråga för att infoga i Book-tabellen
        String insertToBook = "INSERT INTO Book (ItemID, ISBN, PublisherID)"
                + "VALUES (?, ?, ?)";
        
        try (
            PreparedStatement insertToItemStmt = conn.prepareStatement(insertToItem, PreparedStatement.RETURN_GENERATED_KEYS);
            PreparedStatement insertToBookStmt = conn.prepareStatement(insertToBook);
        ){
            // Lägger in värden för item-tabelen
            insertToItemStmt.setInt(1, book.getGenreID());
            insertToItemStmt.setInt(2, book.getCategoryID());
            insertToItemStmt.setString(3, book.getGenreName());
            insertToItemStmt.setString(4, book.getCategoryName());
            insertToItemStmt.setString(5, book.getTitle());
            insertToItemStmt.setString(6, book.getLocation());
            insertToItemStmt.setBoolean(7, true);
            insertToItemStmt.executeUpdate();
            
            
        insertToItemStmt.executeUpdate();

        // Hämta det genererade ItemID:t
        int generatedItemID = -1;
        try (
            var generatedKeys = insertToItemStmt.getGeneratedKeys();
        ) {
            if (generatedKeys.next()) {
                generatedItemID = generatedKeys.getInt(1);
            } else {
                throw new SQLException("Misslyckades att hämta genererat ItemID.");
            }
        }

        // Sätt parametrar för andra INSERT (Book)
        insertToBookStmt.setInt(1, generatedItemID);
        insertToBookStmt.setInt(2, book.getIsbn());
        insertToBookStmt.setInt(3, book.getPublisherID());
        insertToBookStmt.executeUpdate();

        } catch (SQLException ex){
            ex.printStackTrace();
        }
    }
}
