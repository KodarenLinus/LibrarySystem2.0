/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.library_system.Search;

import com.mycompany.library_system.Models.Book;
import com.mycompany.library_system.Database.ConnDB;
import com.mycompany.library_system.Models.DVD;
import com.mycompany.library_system.Database.DatabaseConnector;
import com.mycompany.library_system.Models.Items;
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
    
     /**
     * söker upp items baserat på vad användaren skriver i sök rutan
     *
     * @param en sträng som innehåller det användaren skrivit i sök rutan
     * @return En arrayLista som innehåller items eller om sökning inte får någon träff så retuneras inget.
     */
    public ArrayList<Items> search(String searchText) {

        ArrayList<Items> results = new ArrayList<Items>();
        DatabaseConnector connDB = new ConnDB();
        Connection conn = connDB.connect();
        
        String itemSearch = "SELECT * FROM item WHERE title LIKE ? and available = true";
        String itemSearchInBook = "SELECT * FROM BOOK WHERE itemID = ?";
        String itemSearchInDVD = "SELECT * FROM DVD WHERE itemID = ?";
        
        try (
            PreparedStatement itemStmt = conn.prepareStatement(itemSearch);   
            PreparedStatement bookStmt = conn.prepareStatement(itemSearchInBook);
            PreparedStatement dvdStmt = conn.prepareStatement(itemSearchInDVD);
        ){
            itemStmt.setString(1, "%" + searchText + "%");
            ResultSet rsItem = itemStmt.executeQuery();
            
            while (rsItem.next()) {
                int id = rsItem.getInt("itemID");
                String title = rsItem.getString("title");
                String location = rsItem.getString("location");
                String categoryName = rsItem.getString("CategoryName");
                int categoryID = rsItem.getInt("CategoryID");
                String genreName = rsItem.getString("genreName");
                int genreID = rsItem.getInt("genreID");
               
                bookStmt.setInt(1, id);
                dvdStmt.setInt(1, id);
               
                ResultSet rsBook = bookStmt.executeQuery();
                if (rsBook.next()) {
                    int isbn = rsBook.getInt("ISBN");
                    Book book = new Book(title, location, isbn, categoryID, categoryName, genreID, genreName);
                    book.setItemID(id);
                    results.add(book);
                    
                } else {
                    ResultSet rsDVD = dvdStmt.executeQuery();
                    if (rsDVD.next()) {
                        DVD dvd = new DVD(title, location, categoryID, categoryName, genreID, genreName);
                        dvd.setItemID(id);
                        results.add(dvd);
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return results;
    }
    
}
