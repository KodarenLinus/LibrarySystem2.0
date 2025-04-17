/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.libary_system.Logic;

import com.mycompany.libary_system.Models.Customer;
import com.mycompany.libary_system.Database.DatabaseConnector;
import com.mycompany.libary_system.Database.ConnDB;
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
    
    
    public void insertCustomer(Customer customer){
    
    DatabaseConnector connDB = new ConnDB();
    Connection conn = connDB.connect();

    try {
        PreparedStatement stmt1 = conn.prepareStatement(
            "INSERT INTO Customer (CustomerCategoryID, CategoryName, FirstName, LastName, Email, Adress, TelNumber, PasswordCustomer) " +
            "VALUES (?, ?, ?, ?, ?, ?, ?, ?)"
        );

        stmt1.setInt(1, 3);  // Exempel: Staff
        stmt1.setString(2, "Studenet");
        stmt1.setString(3, customer.getFirstName());
        stmt1.setString(4, customer.getLastName());
        stmt1.setString(5, customer.getEmail());
        stmt1.setString(6, "ltu"); // Du kan byta till riktig adress senare
        stmt1.setInt(7, customer.getTelNr());
        stmt1.setString(8, customer.getPassword());

        stmt1.executeUpdate();

    } catch (SQLException ex){
        ex.printStackTrace(); // Bra att skriva ut för felsökning
    }
}
}
