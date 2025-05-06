/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.library_system.Logic.ItemManagement;

import com.mycompany.library_system.Database.ConnDB;
import com.mycompany.library_system.Database.DatabaseConnector;
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
 * @author Linus, Emil, Oliver, Viggo
 */
public class GetGenres {

    private final DatabaseConnector dbConnector;

    public GetGenres() {
        this.dbConnector = new ConnDB();
    }

    /**
     * Hämtar alla genrer från databasen.
     *
     * @return En lista med Genre-objekt
     * @throws SQLException Om databasförfrågan misslyckas
     */
    public ArrayList<Genre> getAllGenres() throws SQLException {
        ArrayList<Genre> genreList = new ArrayList<>();

        // SQL-fråga för att hämta alla genrer
        String selectAllGenres = "SELECT * FROM Genre";

        // Try-with-resources ser till att alla resurser stängs korrekt
        try (
            Connection conn = dbConnector.connect();
            PreparedStatement genreStmt = conn.prepareStatement(selectAllGenres);
            ResultSet rsGenre = genreStmt.executeQuery()
        ) {
            while (rsGenre.next()) {
                int genreID = rsGenre.getInt("GenreID");
                String genreName = rsGenre.getString("GenreName");

                // Skapar Genre-objekt och lägger till i listan
                Genre genre = new Genre(genreID, genreName);
                genreList.add(genre);
            }
        }

        return genreList;
    }
}