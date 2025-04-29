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
 *
 * @author Linus
 */
public class AddAuthorToBook {
    
    public void insertToBookAuthor (Book book, ArrayList<Author> authors) {
        
        // Skapar en databasanslutning
        DatabaseConnector connDB = new ConnDB();
        Connection conn = connDB.connect();
        
        // SQL-fråga för att infoga i BookAuthor-tabellen
        String insertToBookAuthor = "INSERT INTO BookAuthor (ItemID, AuthorID, AuthorFirstname, AuthorLastname) VALUES (?, ?, ?, ?)";
        
        try (
            PreparedStatement stmt1 = conn.prepareStatement(insertToBookAuthor);
        ){
            for (Author author : authors)
            {
                // Lägger in värden för item-tabelen
                stmt1.setInt(1, book.getItemID());
                stmt1.setInt(2, author.getAuthorID());
                stmt1.setString(3, author.getFirstname());
                stmt1.setString(4, author.getLastname());
                stmt1.addBatch();
            }

            stmt1.executeBatch();

        } catch (SQLException ex){
            ex.printStackTrace();
        }
    }
}
