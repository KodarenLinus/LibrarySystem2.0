/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.libary_system;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.scene.control.TextField;

/**
 *
 * @author emildahlback
 */
public class AddCustomer {
    private ConnDB connDB = new ConnDB();
    
    void insertCustomer(String firstName, String lastName, int telNr, String Email){
        
        DatabaseConnector connDB = new ConnDB();
        Connection conn = connDB.connect();
        
        try {
            
            PreparedStatement stmt1 = conn.prepareStatement
            ("INSERT INTO Customer (CustomerID, CustomerCategoryID, CategoryName, FirstName, LastName, Email, Adress, TelNumber, UserID) VALUES (?, ?, ?, ?, ?, ?, ?, ?)");
            
                stmt1.setInt(1, 10);
                stmt1.setInt(2, 1);
                stmt1.setString(3, "Staff");
                stmt1.setString(4, firstName);
                stmt1.setString(5, lastName);
                stmt1.setString(6, Email);
                stmt1.setString(7, "luleå");
                stmt1.setInt(8, telNr);
                stmt1.setInt(9, 2);
                
                stmt1.executeUpdate();
            
        } catch (SQLException ex){
            
        }
    }
}
