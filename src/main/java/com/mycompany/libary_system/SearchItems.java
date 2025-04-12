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
        ArrayList<Items> results = new ArrayList<>();
        DatabaseConnector connDB = new ConnDB();
        Connection conn = connDB.connect();

        String query = "SELECT itemID, title, location FROM item WHERE title LIKE ?";
        try {
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setString(1, "%" + searchText + "%");
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                int id = rs.getInt("itemID");
                String title = rs.getString("title");
                String location = rs.getString("location");
                
                //K
                PreparedStatement stmt1 = conn.prepareStatement("SELECT * FROM BOOK WHERE itemID = ?");
                PreparedStatement stmt2 = conn.prepareStatement("SELECT * FROM DVD WHERE itemID = ?");
                stmt1.setInt(1, id);
                stmt2.setInt(1, id);

                if (stmt1.executeQuery() != null) {
                    ResultSet rs1 = stmt1.executeQuery();

                    while (rs1.next()) {
                        int isbn = rs1.getInt("ISBN");
                        Book book = new Book(title, location, isbn);
                        book.setItemID(id);
                        results.add(book);
                    }
                } 
                else if (stmt2.executeQuery() != null) {
                    DVD dvd = new DVD(title, location);
                    dvd.setItemID(id);
                    results.add(dvd);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return results;
    }
    
}
