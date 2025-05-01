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
    
    public ArrayList<Author> search (String searchText, Book book) {
        
        ArrayList<Author> results = new ArrayList<Author>();
        DatabaseConnector connDB = new ConnDB();
        Connection conn = connDB.connect();
               
        String authorSearch = "SELECT * FROM Author " +
                      "WHERE (FirstName LIKE ? OR LastName LIKE ?) " +
                      "AND AuthorID NOT IN (SELECT AuthorID FROM BookAuthor WHERE itemID = ?)";

        try (
            PreparedStatement authorSearchStmt = conn.prepareStatement(authorSearch)
        ) {

            authorSearchStmt.setString(1, "%" + searchText + "%");
            authorSearchStmt.setString(2, "%" + searchText + "%");
            authorSearchStmt.setInt(3, book.getItemID());

            ResultSet rs = authorSearchStmt.executeQuery();

            while (rs.next()) {
                int id = rs.getInt("AuthorID");
                String firstname = rs.getString("FirstName");
                String lastname = rs.getString("LastName");

                Author author = new Author(firstname, lastname);
                author.setAuthorID(id);

                results.add(author);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        
        return results;
    }
    
}
