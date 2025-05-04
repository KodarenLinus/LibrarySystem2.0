/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.library_system.Search;

import com.mycompany.library_system.Models.Book;
import com.mycompany.library_system.Database.ConnDB;
import com.mycompany.library_system.Models.DVD;
import com.mycompany.library_system.Database.DatabaseConnector;
import com.mycompany.library_system.Logic.GetCategoryLoanTime;
import com.mycompany.library_system.Models.Items;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author Linus, Emil, Oliver, Viggo
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
                        int directorID = rsDVD.getInt("DirectorID");
                        DVD dvd = new DVD(title, location, categoryID, categoryName, genreID, genreName, directorID);
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
    
   /*SELECT *
"FROM Item i " +
"LEFT JOIN reservationRow rr ON i.itemID = rr.itemID " +
"LEFT JOIN reservation r ON rr.reservationID = r.reservationID " +
"WHERE i.itemID = 4 " +
"AND (r.reservationID IS NULL " +
"OR NOT ('2025-05-06' BETWEEN '2025-05-06' AND '2025-05-20'))"*/

    public ArrayList<Items> searchAvailableItems(LocalDate date) {
        ArrayList<Items> availableItems = new ArrayList<>();
        GetCategoryLoanTime getCategoryLoanTime = new GetCategoryLoanTime();

        try (Connection conn = new ConnDB().connect();
             PreparedStatement allItemsStmt = conn.prepareStatement("SELECT * FROM Item");
             PreparedStatement bookStmt = conn.prepareStatement("SELECT * FROM BOOK WHERE itemID = ?");
             PreparedStatement dvdStmt = conn.prepareStatement("SELECT * FROM DVD WHERE itemID = ?");
             PreparedStatement checkReservationStmt = conn.prepareStatement(
                 "SELECT 1 " +
                 "FROM reservationRow rr " +
                 "JOIN reservation r ON rr.reservationID = r.reservationID " +
                 "WHERE rr.itemID = ? " +
                 "AND (" +
                 "    r.reservationDate BETWEEN ? AND ? " +
                 "    OR ? BETWEEN r.reservationDate AND DATE_ADD(r.reservationDate, INTERVAL ? DAY)" +
                 ")"
        )) {

            ResultSet rs = allItemsStmt.executeQuery();

            ArrayList<Items> tempItems = new ArrayList<>();

            while (rs.next()) {
                int itemID = rs.getInt("itemID");
                String title = rs.getString("title");
                String location = rs.getString("location");
                String categoryName = rs.getString("CategoryName");
                int categoryID = rs.getInt("CategoryID");
                String genreName = rs.getString("genreName");
                int genreID = rs.getInt("genreID");

                // Try to create Book
                bookStmt.setInt(1, itemID);
                ResultSet rsBook = bookStmt.executeQuery();
                if (rsBook.next()) {
                    int isbn = rsBook.getInt("ISBN");
                    Book book = new Book(title, location, isbn, categoryID, categoryName, genreID, genreName);
                    book.setItemID(itemID);
                    tempItems.add(book);
                    continue;
                }

                // Try to create DVD
                dvdStmt.setInt(1, itemID);
                ResultSet rsDVD = dvdStmt.executeQuery();
                if (rsDVD.next()) {
                    int directorID = rsDVD.getInt("DirectorID");
                    DVD dvd = new DVD(title, location, categoryID, categoryName, genreID, genreName, directorID);
                    dvd.setItemID(itemID);
                    tempItems.add(dvd);
                }
            }

            // Now filter out reserved items
            for (Items wrapper : tempItems) {
                int itemID = wrapper.getItemID();

                LocalDate loanEndDate = getCategoryLoanTime.calculatetLoanEndDate(conn, itemID, date);
                long loanDays = ChronoUnit.DAYS.between(date, loanEndDate);

                checkReservationStmt.setInt(1, itemID);
                checkReservationStmt.setDate(2, java.sql.Date.valueOf(date));
                checkReservationStmt.setDate(3, java.sql.Date.valueOf(loanEndDate));
                checkReservationStmt.setDate(4, java.sql.Date.valueOf(date));
                checkReservationStmt.setLong(5, loanDays);

                ResultSet rsCheck = checkReservationStmt.executeQuery();
                if (!rsCheck.next()) {
                    availableItems.add(wrapper); // only add if no conflict found
                }
            }

            } catch (SQLException e) {
                e.printStackTrace();
            }

        return availableItems;
    }

}
