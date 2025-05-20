/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.library_system.Logic.ItemManagement;

import com.mycompany.library_system.Database.ConnDB;
import com.mycompany.library_system.Database.DatabaseConnector;
import com.mycompany.library_system.Models.Category;
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
public class GetCategories {
    private final DatabaseConnector dbConnector;

    public GetCategories () {
        this.dbConnector = new ConnDB();
    }
   
    /**
     * Hämtar alla kategorier från databasen.
     *
     * @return En lista med Category-objekt
     * @throws SQLException Om databasfrågan misslyckas
     */
    public ArrayList<Category> getAllCategories () throws SQLException {
        // En ArrayList vi sparar ner all Kategorier
        ArrayList<Category> categoryList = new ArrayList<>();
      
        // En SQL-fråga för att hämta alla categorier
        String selectAllCategories = "SELECT * FROM Category";
        
        try (
            Connection conn = dbConnector.connect();
            PreparedStatement categoryStmt = conn.prepareStatement(selectAllCategories);
            ResultSet rsCategory = categoryStmt.executeQuery();
        ) {
            // Loppar igenom vårat resultset och lägger till kategorier till vår ArrayList
            while (rsCategory.next()) {
                int categoryID = rsCategory.getInt("CategoryID");
                String categoryName = rsCategory.getString("CategoryName");

                Category category = new Category(categoryID, categoryName);
                categoryList.add(category);
            }
        }
            
        return categoryList;
    }
}
