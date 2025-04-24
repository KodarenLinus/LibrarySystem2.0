/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.library_system.Logic;

import com.mycompany.library_system.Database.ConnDB;
import com.mycompany.library_system.Models.Genre;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 * Klass för att hämta kategorier från databasen.
 * Hämtar alla kategorier från Category-tabellen.
 * 
 * @author Linus
 */
public class GetGenres {
    
    /**
     * Hämtar alla fenres från databasen.
     *
     * @return En lista med genre-objekt
     * @throws SQLException Om databasfrågan misslyckas
     */
    public ArrayList<Genre> getAllGenres () throws SQLException {
        ArrayList<Genre> genreList = new ArrayList<>();
        
        // Skapar en databasanslutning
        ConnDB connDB = new ConnDB();
        Connection conn = connDB.connect();
        
        // En SQL-fråga för att hämta alla categorier
        String selectAllGenres = "SELECT * FROM Genre";
        
        try (
            PreparedStatement genreStmt = conn.prepareStatement(selectAllGenres);
            ResultSet rsGenre = genreStmt.executeQuery();
        ) {
            while (rsGenre.next()) {
                int genreID = rsGenre.getInt("GenreID");
                String genreName = rsGenre.getString("GenreName");
                
                Genre genre = new Genre(genreID, genreName);
                genreList.add(genre);
            }
        } 
        return genreList;
    }
}
