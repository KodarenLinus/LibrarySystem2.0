/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.library_system.Logic.AuthorMangement;

import com.mycompany.library_system.Database.ConnDB;
import com.mycompany.library_system.Database.DatabaseConnector;
import com.mycompany.library_system.Models.Author;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * En klass för att skapa författare och lägga till dem i vår databas
 * 
 * @author Linus, Emil, Oliver, Viggo
 */
public class AddAuthor {
    private final DatabaseConnector dbConnector;

    public AddAuthor () {
        this.dbConnector = new ConnDB();
    }
     /**
     * Lägger till en författare i våran databasen
     *
     * @param Ett Author objekt som vi lägger till i databasen
     */
    public void insertAuthor (Author author) {
        
        Connection conn = dbConnector.connect();
        
        // SQL-fråga som lägger till first och lastname i Author tabelen
        String insertAuthor = "INSERT INTO Author (firstname, lastname) " +
                "VALUES (?, ?)";

        try (
             PreparedStatement insertAuthorStmt = conn.prepareStatement(insertAuthor);   
        ){
            insertAuthorStmt.setString(1, author.getFirstname());
            insertAuthorStmt.setString(2, author.getLastname());
          
            insertAuthorStmt.executeUpdate();

        } catch (SQLException ex){
            ex.printStackTrace(); 
        }
    }
    
}
