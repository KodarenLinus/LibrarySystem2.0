/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.library_system.Logic;

import com.mycompany.library_system.Database.ConnDB;
import com.mycompany.library_system.Database.DatabaseConnector;
import com.mycompany.library_system.Models.Author;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 *
 * @author Linus
 */
public class AddAuthor {
    
    public void insertAuthor (Author author) {
        DatabaseConnector connDB = new ConnDB();
        Connection conn = connDB.connect();
        String insertAuthor = "INSERT INTO Author (firstname, lastname) " +
                "VALUES (?, ?)";

        try (
             PreparedStatement stmt1 = conn.prepareStatement(insertAuthor);   
        ){
            stmt1.setString(1, author.getFirstname());
            stmt1.setString(2, author.getLastname());
          
            stmt1.executeUpdate();

        } catch (SQLException ex){
            ex.printStackTrace(); // Bra att skriva ut för felsökning
        }
    }
    
}
