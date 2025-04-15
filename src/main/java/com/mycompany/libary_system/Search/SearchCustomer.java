/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.libary_system.Search;

import com.mycompany.libary_system.Database.ConnDB;
import com.mycompany.libary_system.Models.Customer;
import com.mycompany.libary_system.Database.DatabaseConnector;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 *
 * @author emildahlback
 */
public class SearchCustomer {

    public ArrayList<Customer> searchCustomer(String searchText) {
        
        ArrayList<Customer> results = new ArrayList<>();

        DatabaseConnector connDB = new ConnDB();
        Connection conn = connDB.connect();

        // Sök i förnamn eller efternamn
        String query = "SELECT CustomerID, FirstName, LastName, TelNumber, Email, PasswordCustomer FROM Customer " +
                       "WHERE FirstName LIKE ? OR LastName LIKE ?";

        try (PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, "%" + searchText + "%");
            stmt.setString(2, "%" + searchText + "%");

            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                int id = rs.getInt("CustomerID");
                String firstName = rs.getString("FirstName");
                String lastName = rs.getString("LastName");
                int telNr = rs.getInt("TelNumber");
                String email = rs.getString("Email");
                String password = rs.getString("PasswordCustomer");

                Customer customer = new Customer(firstName, lastName, telNr, email, password);
                customer.setCustomerID(id);

                results.add(customer);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return results;
    }
}