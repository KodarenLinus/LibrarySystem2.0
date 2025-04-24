/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.library_system.Logic;

import com.mycompany.library_system.Database.ConnDB;
import com.mycompany.library_system.Models.Category;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 *
 * @author Linus
 */
public class GetCategories {
   
    public ArrayList<Category> getAllCategories () throws SQLException {
        
        ArrayList<Category> categoryList = new ArrayList<>();
        
        ConnDB connDB = new ConnDB();
        Connection conn = connDB.connect();
        String selectAllCategories = "SELECT * FROM Category";
        
        PreparedStatement categoryStmt = conn.prepareStatement(selectAllCategories);
        ResultSet rsCategory = categoryStmt.executeQuery();
            
            while (rsCategory.next()) {
                int categoryID = rsCategory.getInt("CategoryID");
                String categoryName = rsCategory.getString("CategoryName");
                
                Category category = new Category(categoryID, categoryName);
                categoryList.add(category);
            }
            
        return categoryList;
    }
}
