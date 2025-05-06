/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.library_system.Search;

import com.mycompany.library_system.Database.ConnDB;
import com.mycompany.library_system.Database.DatabaseConnector;
import com.mycompany.library_system.Models.Author;
import com.mycompany.library_system.Models.Book;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

/**
 *
 * @author Linus, Emil, Oliver, Viggo
 */
public class SearchAuthor {

    private final DatabaseConnector dbConnector;

    public SearchAuthor() {
        this.dbConnector = new ConnDB();
    }

    /**
     * Söker efter författare som matchar söktexten (förnamn eller efternamn)
     * och som inte redan är kopplade till den angivna boken.
     *
     * @param searchText Text att söka på (förnamn eller efternamn).
     * @param book Den bok som författarna inte ska vara kopplade till.
     * @return En lista med matchande Author-objekt.
     */
    public ArrayList<Author> search(String searchText, Book book) {
        ArrayList<Author> results = new ArrayList<>();

        // SQL-fråga för att hitta författare som inte redan är kopplade till boken
        String query = "SELECT * FROM Author " +
                       "WHERE (FirstName LIKE ? OR LastName LIKE ?) " +
                       "AND AuthorID NOT IN (SELECT AuthorID FROM BookAuthor WHERE itemID = ?)";

        try (
            Connection conn = dbConnector.connect(); // Öppna databasanslutning
            PreparedStatement stmt = conn.prepareStatement(query) // Förbered SQL-satsen
        ) {
            // Parametrar till sökningen
            stmt.setString(1, "%" + searchText + "%");
            stmt.setString(2, "%" + searchText + "%");
            stmt.setInt(3, book.getItemID());

            ResultSet rs = stmt.executeQuery();

            // Gå igenom resultatet och skapa Author-objekt
            while (rs.next()) {
                int id = rs.getInt("AuthorID");
                String firstname = rs.getString("FirstName");
                String lastname = rs.getString("LastName");

                Author author = new Author(firstname, lastname);
                author.setAuthorID(id);

                results.add(author);
            }

        } catch (Exception e) {
            e.printStackTrace(); // Vid fel, skriv ut stacktrace
        }

        return results;
    }
}

