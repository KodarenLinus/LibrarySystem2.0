/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.library_system.Logic.AuthorMangement;

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
    private final DatabaseConnector dbConnector;

    public AddAuthorToBook () {
        this.dbConnector = new ConnDB();
    }
    
    /**
     * Lägger till en eller flera författare till en bok i BookAuthor-tabellen.
     *
     * @param book ett Book objekt som vi skickar till bookAuthor i databasen
     * @param authors en lista med författare som som vi kopplar till book objektet
     * @return true om man lyckas lägga till författare, false om ingen författare läggs till.
     */
    public boolean insertToBookAuthor(Book book, ArrayList<Author> authors) {
        // SQL fråga som gör en insert i BookAuthor
        String insertBookAuthor = "INSERT INTO BookAuthor (ItemID, AuthorID) VALUES (?, ?)";

        try (
            Connection conn = dbConnector.connect();
            PreparedStatement addAuthorToBookStmt = conn.prepareStatement(insertBookAuthor);
        ) {
            // Loppar igenom våran author lista och kopplar alla authors till samma bok
            for (Author author : authors) {
                addAuthorToBookStmt.setInt(1, book.getItemID());
                addAuthorToBookStmt.setInt(2, author.getAuthorID());
                addAuthorToBookStmt.addBatch();
            }
            
            // Skickar in det till databasen
            addAuthorToBookStmt.executeBatch();
            return true;

        } catch (SQLException ex) {
            System.err.println("Fel vid insättning i BookAuthor: " + ex.getMessage());
            return false;
        }
    }

}
