/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.library_system.Logic;

import com.mycompany.library_system.Database.ConnDB;
import com.mycompany.library_system.Database.DatabaseConnector;
import com.mycompany.library_system.Models.Book;
import com.mycompany.library_system.Models.DVD;
import com.mycompany.library_system.Models.Items;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 *
 * @author Linus
 */
public class GetItemsByID {
    /**
     * Fetches an item from the database using its ID.
     *
     * @param itemId The ID of the item to retrieve.
     * @return An Items object if found, otherwise null.
     * @throws SQLException If there's an issue with the database operation.
     */
   
    public Items getItemById(int itemId) throws SQLException {
        Items item = null;
        DatabaseConnector connDB = new ConnDB();
        Connection conn = connDB.connect();
        
        String itemQuery = "SELECT * FROM item WHERE ItemID = ?";
        String bookQuery = "SELECT * FROM BOOK WHERE itemID = ?";
        String dvdQuery = "SELECT * FROM DVD WHERE itemID = ?";
        
        try (
            PreparedStatement itemStmt = conn.prepareStatement(itemQuery)
            ) {

            itemStmt.setInt(1, itemId);

            ResultSet rsItem = itemStmt.executeQuery();
            PreparedStatement bookStmt = conn.prepareStatement(bookQuery);
            PreparedStatement dvdStmt = conn.prepareStatement(dvdQuery);
            while (rsItem.next()) {
                int id = rsItem.getInt("itemID");
                String title = rsItem.getString("title");
                String location = rsItem.getString("location");
                String categoryName = rsItem.getString("CategoryName");
                int categoryID = rsItem.getInt("CategoryID");
                String genreName = rsItem.getString("genreName");
                int genreID = rsItem.getInt("genreID");

                // Check categoryID to determine if it's a book or DVD
                bookStmt.setInt(1, id);
                ResultSet rsBook = bookStmt.executeQuery();
                if (rsBook.next()) { // Book category ID (example: 1)

                    int isbn = rsBook.getInt("ISBN");
                    Book book = new Book(title, location, isbn, categoryID, categoryName, genreID, genreName);
                    book.setItemID(id);
                    item = book;

                } else {
                    dvdStmt.setInt(1, id);
                    ResultSet rsDVD = dvdStmt.executeQuery(); 
                    if (rsDVD.next()) {
                        int directorID = rsDVD.getInt("DirectorID");
                        DVD dvd = new DVD(title, location, categoryID, categoryName, genreID, genreName, directorID);
                        dvd.setItemID(id);
                        item = dvd;
                    }
                }
            }
        }
        return item;
    }  
    
}

