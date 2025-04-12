/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.libary_system;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 *
 * @author Linus
 */
public class SearchItems {
    
public ArrayList<Items> search(String searchText) {
        //Lista för att spara våra items
        ArrayList<Items> results = new ArrayList<>();
        
        // DB connection
        DatabaseConnector connDB = new ConnDB();
        Connection conn = connDB.connect();
        
        //Våra sql query
        String query = "SELECT itemID, title, location FROM item WHERE title LIKE ? and available = true";
        String query1 = "SELECT * FROM BOOK WHERE itemID = ?";
        String query2 = "SELECT * FROM DVD WHERE itemID = ?";
        
        try {
            //Förbereder en SQL-sats
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setString(1, "%" + searchText + "%");
            ResultSet rs = stmt.executeQuery();
            
            //Loppar igenom alla resultat vi får av SQL-satsen!!!
            while (rs.next()) {
                int id = rs.getInt("itemID");
                String title = rs.getString("title");
                String location = rs.getString("location");
                
                // Förbereder SQL-satser för att hämta data från DB!!!!
                PreparedStatement stmt1 = conn.prepareStatement(query1);
                PreparedStatement stmt2 = conn.prepareStatement(query2);
                stmt1.setInt(1, id);
                stmt2.setInt(1, id);
                
                //Kör en sql query mot book tabelen
                ResultSet rs1 = stmt1.executeQuery();
                
                //F år den ett result set (finns itemet i book) körs koden i if blocket
                if (rs1.next()) {
                    int isbn = rs1.getInt("ISBN");
                    Book book = new Book(title, location, isbn);
                    book.setItemID(id);
                    results.add(book);
                } else {
                    // Om itemet inte finns i book kollar vi ifall det finns i DVD tablen
                    ResultSet rs2 = stmt2.executeQuery();
                    if (rs2.next()) {
                        DVD dvd = new DVD(title, location);
                        dvd.setItemID(id);
                        results.add(dvd);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return results;
    }
    
}
