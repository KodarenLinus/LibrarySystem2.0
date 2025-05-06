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
             PreparedStatement stmt1 = conn.prepareStatement(insertAuthor);   
        ){
            stmt1.setString(1, author.getFirstname());
            stmt1.setString(2, author.getLastname());
          
            stmt1.executeUpdate();

        } catch (SQLException ex){
            ex.printStackTrace(); 
        }
    }
    
}
