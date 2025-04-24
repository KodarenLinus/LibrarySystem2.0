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
 *
 * @author Linus
 */
public class GetGenres {
    
    public ArrayList<Genre> getAllGenres () throws SQLException {
        
        ArrayList<Genre> genreList = new ArrayList<>();
        
        ConnDB connDB = new ConnDB();
        Connection conn = connDB.connect();
        String selectAllGenres = "SELECT * FROM Genre";
        
        PreparedStatement genreStmt = conn.prepareStatement(selectAllGenres);
        ResultSet rsGenre = genreStmt.executeQuery();
            
            while (rsGenre.next()) {
                int genreID = rsGenre.getInt("GenreID");
                String genreName = rsGenre.getString("GenreName");
                
                Genre genre = new Genre(genreID, genreName);
                genreList.add(genre);
            }
            
        return genreList;
    }
}
