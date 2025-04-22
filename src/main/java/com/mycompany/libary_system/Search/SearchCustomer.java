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
    
     /**
     * söker upp customer baserat på vad man skriver i sök rutan
     *
     * @param en sträng som innehåller det användaren skrivit i sök rutan
     * @return En arrayLista som innehåller customer eller om sökning inte får någon träff så retuneras inget.
     */
    public ArrayList<Customer> searchCustomer(String searchText) {
        
        ArrayList<Customer> results = new ArrayList<>();

        DatabaseConnector connDB = new ConnDB();
        Connection conn = connDB.connect();

        String customerSearch = "SELECT CustomerID, FirstName, LastName, TelNumber, Email, PasswordCustomer FROM Customer " +
                       "WHERE FirstName LIKE ? OR LastName LIKE ?";

        try (
            PreparedStatement customerSearchStmt = conn.prepareStatement(customerSearch)
        ) {

            customerSearchStmt.setString(1, "%" + searchText + "%");
            customerSearchStmt.setString(2, "%" + searchText + "%");

            ResultSet rs = customerSearchStmt.executeQuery();

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