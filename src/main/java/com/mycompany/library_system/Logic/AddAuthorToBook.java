/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.library_system.Logic;

import com.mycompany.library_system.Database.ConnDB;
import com.mycompany.library_system.Database.DatabaseConnector;
import com.mycompany.library_system.Models.Author;
import com.mycompany.library_system.Models.Book;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 * Klass som hanterar kopplingen mellan böcker och författare i databasen.
 * Infogar relationer i tabellen BookAuthor.
 * 
 * @author Linus, Emil, Oliver, Viggo
 */
public class AddAuthorToBook {
    
    /**
     * Lägger till en eller flera författare till en bok i BookAuthor-tabellen.
     *
     * @param book ett Book objekt som vi skickar till bookAuthor i databasen
     * @param authors en lista med författare som som vi kopplar till book objektet
     */
    public void insertToBookAuthor (Book book, ArrayList<Author> authors) {
        
        // Skapar en databasanslutning
        DatabaseConnector connDB = new ConnDB();
        Connection conn = connDB.connect();
        
        // SQL-fråga för att infoga i BookAuthor-tabellen
        String insertToBookAuthor = "INSERT INTO BookAuthor (ItemID, AuthorID) VALUES (?, ?)";
        
        try (
            PreparedStatement stmt1 = conn.prepareStatement(insertToBookAuthor);
        ){
            // Loopa igenom varje författare och lägg till kopplingen till boken
            for (Author author : authors) {
                stmt1.setInt(1, book.getItemID());
                stmt1.setInt(2, author.getAuthorID());
                stmt1.addBatch(); // Samlar alla INSERT-satser till ett batch-anrop
            }

            stmt1.executeBatch();

        } catch (SQLException ex){
            ex.printStackTrace();
        }
    }
}
