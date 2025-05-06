/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.library_system.Logic;

import com.mycompany.library_system.Database.ConnDB;
import com.mycompany.library_system.Database.DatabaseConnector;
import com.mycompany.library_system.Models.Director;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 *
 * @author Linus
 */
public class GetDirectors {
    
    private final DatabaseConnector dbConnector;

    public GetDirectors () {
        this.dbConnector = new ConnDB();
    }
    /**
     * Hämtar alla director från databasen.
     *
     * @return En lista med director-objekt
     * @throws SQLException Om databasfrågan misslyckas
     */
    public ArrayList<Director> getAllDirectors () throws SQLException {
        ArrayList<Director> directorList = new ArrayList<>();
        
        Connection conn = dbConnector.connect();
        
        // En SQL-fråga för att hämta alla directors
        String selectAllDirectors = "SELECT * FROM Director";
        
        try (
            PreparedStatement directorStmt = conn.prepareStatement(selectAllDirectors);
            ResultSet rsDirector = directorStmt.executeQuery();
        ) {
            while (rsDirector.next()) {
                int directorID = rsDirector.getInt("DirectorID");
                String firstname = rsDirector.getString("Firstname");
                String lastname = rsDirector.getString("Lastname");
                
                Director director = new Director(firstname, lastname);
                director.setDirectorID(directorID);
                directorList.add(director);
            }
        }
            
        return directorList;
    }
}
