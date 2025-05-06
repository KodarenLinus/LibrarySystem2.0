/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.library_system.Login;

import com.mycompany.library_system.Database.DatabaseConnector;
import com.mycompany.library_system.Database.ConnDB;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

/**
 *
 * @author Linus, Emil, Oliver, Viggo
 */
public class Login {
    private final DatabaseConnector dbConnector;

    public Login () {
        this.dbConnector = new ConnDB();
    }
    
    /**
     * Lägger till valt objekt i kundvagnen om det inte redan finns där.
     *
     * @param String username en sträng vi sparar användarnamnet som skrivs i användarnamn rutan
     * @param String password en sträng vi sparar lösenordet som skrivs i lösenord rutan
     * @return en boolean true eller false baserat på om man matchar användarnamn med rätt lösenord
     */
    public boolean doLogin (String username, String password) {
        
        Connection conn = dbConnector.connect();
        
        String getCustomerLogin = "SELECT customerID, passwordCustomer From Customer where Email = ?";
        
        try (
            PreparedStatement customerStmt = conn.prepareStatement(getCustomerLogin);      
        ) {
            customerStmt.setString(1, username);
            ResultSet rsCustomer = customerStmt.executeQuery();
            
            while (rsCustomer.next()) {
                String password_ = rsCustomer.getString("passwordCustomer");
                
                if (password_.equals(password)){
                    int customerId = rsCustomer.getInt("CustomerID");
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
