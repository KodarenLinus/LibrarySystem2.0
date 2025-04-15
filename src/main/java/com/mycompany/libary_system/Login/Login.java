/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.libary_system.Login;

import com.mycompany.libary_system.Utils.Session;
import com.mycompany.libary_system.Database.DatabaseConnector;
import com.mycompany.libary_system.Database.ConnDB;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

/**
 *
 * @author Linus
 */
public class Login {
    
    // En metod som kollar inlognings uppgifter
    public boolean doLogin (String username, String password) {
        
        // DB connection
        DatabaseConnector connDB = new ConnDB();
        Connection conn = connDB.connect();
        
        String sql = "SELECT customerID, passwordCustomer From Customer where Email = ?";
        
        try (
            PreparedStatement stmt = conn.prepareStatement(sql);      
        ) {
            
            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();
            
            //Loppar igenom alla resultat vi får av SQL-satsen!!!
            while (rs.next()) {
                String password_ = rs.getString("passwordCustomer");
                
                //Kollar lösenord och skapar en session
                if (password_.equals(password)){
                    int customerId = rs.getInt("CustomerID");
                    Session.getInstance().setUser(customerId, username); 
                    return true;
                }
            }
        
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        return false;
    }
}
