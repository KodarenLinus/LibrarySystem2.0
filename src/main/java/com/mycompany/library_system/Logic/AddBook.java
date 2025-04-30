/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.library_system.Logic;

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

     /**
     * Lägger till en bok i databasen.
     * Först infogas bokens metadata i Item-tabellen,
     * därefter läggs tillhörande information in i Book-tabellen.
     *
     * @param book Ett bokobjekt som innehåller titel, genre, kategori, plats och ISBN.
     */
    public void insertBook (Book book) {
        
        // Skapar en databasanslutning
        DatabaseConnector connDB = new ConnDB();
        Connection conn = connDB.connect();
        
        // SQL-fråga för att infoga i Item-tabellen
        String insertToItem = "INSERT INTO Item (GenreID, CategoryID, GenreName, CategoryName, Title, Location, Available) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?)";
        
        // SQL-fråga för att infoga i Book-tabellen
        String insertToBook = "INSERT INTO Book (ItemID, ISBN, PublisherID)"
                + "VALUES (?, ?, ?)";
        
        try (
            PreparedStatement stmt1 = conn.prepareStatement(insertToItem);
            PreparedStatement stmt2 = conn.prepareStatement(insertToBook);
        ){
            // Lägger in värden för item-tabelen
            stmt1.setInt(1, book.getGenreID());
            stmt1.setInt(2, book.getCategoryID());
            stmt1.setString(3, book.getGenreName());
            stmt1.setString(4, book.getCategoryName());
            stmt1.setString(5, book.getTitle());
            stmt1.setString(6, book.getLocation());
            stmt1.setBoolean(7, true);
            stmt1.executeUpdate();
            
            
        stmt1.executeUpdate();

        // Hämta det genererade ItemID:t
        int generatedItemID = -1;
        try (var generatedKeys = stmt1.getGeneratedKeys()) {
            if (generatedKeys.next()) {
                generatedItemID = generatedKeys.getInt(1);
            } else {
                throw new SQLException("Misslyckades att hämta genererat ItemID.");
            }
        }

        // Sätt parametrar för andra INSERT (Book)
        stmt2.setInt(1, generatedItemID);
        stmt2.setInt(2, book.getIsbn());
        stmt2.setInt(3, 7); // TODO: Byt till dynamiskt PublisherID senare
        stmt2.executeUpdate();
        
        for (Integer authorId : book.getAuthorIDs()) {
            PreparedStatement stmt3 = conn.prepareStatement("INSERT INTO BookAuthor (ItemID, AuthorID) VALUES (?, ?)");
            stmt3.setInt(1, generatedItemID);
            stmt3.setInt(2, authorId);
            stmt3.executeUpdate();
            stmt3.close();
        }

        } catch (SQLException ex){
            ex.printStackTrace();
        }
    }
}
