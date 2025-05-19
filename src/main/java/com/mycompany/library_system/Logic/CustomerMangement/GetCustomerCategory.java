/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.library_system.Logic.CustomerMangement;

import com.mycompany.library_system.Database.ConnDB;
import com.mycompany.library_system.Database.DatabaseConnector;
import com.mycompany.library_system.Models.CustomerCategory;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 * Denna klass används för att hämta kundkategorier från databasen.
 * Den tillhandahåller funktionalitet för att hämta alla kategorier samt
 * en specifik kategori baserat på ID. Kategorier används till exempel 
 * vid registrering av nya kunder.
 * 
 * @author Linus, Emil, Oliver, Viggo
 */
public class GetCustomerCategory {
    private final DatabaseConnector dbConnector;

    public GetCustomerCategory() {
        this.dbConnector = new ConnDB();
    }

    /**
     * Hämtar alla kundkategorier från databasen.
     * Metoden ansluter till databasen och utför en SQL-fråga för att hämta alla rader
     * från tabellen "CustomerCategory".
     * 
     * @return en lista med alla kundkategorier i databasen
     * @throws SQLException om ett fel uppstår vid databasanslutning eller SQL-exekvering
     */
    public ArrayList<CustomerCategory> getAllCustomerCategory() throws SQLException {
        ArrayList<CustomerCategory> customerCategoryList = new ArrayList<>();

        // SQL-fråga för att hämta alla kundkategorier
        String selectAllCustomerCategories = "SELECT * FROM CustomerCategory";

        // Try-with-resources ser till att alla resurser stängs korrekt
        try (
            Connection conn = dbConnector.connect();
            PreparedStatement customerCategoryStmt = conn.prepareStatement(selectAllCustomerCategories);
            ResultSet rsCustomerCategory = customerCategoryStmt.executeQuery()
        ) {
            while (rsCustomerCategory.next()) {
                int customerCategoryID = rsCustomerCategory.getInt("CustomerCategoryID");
                String customerCategoryName = rsCustomerCategory.getString("CategoryName");
                int loanLimit = rsCustomerCategory.getInt("LoanLimit");

                // Skapar CustomerCategory-objekt och lägger till i listan
                CustomerCategory customerCategory = new CustomerCategory(customerCategoryID, customerCategoryName, loanLimit);
                customerCategoryList.add(customerCategory);
            }
        }

        return customerCategoryList;
    }

    /**
     * Hämtar en specifik kundkategori från databasen baserat på ID.
     * Används exempelvis för att identifiera en kunds kategori vid inloggning
     * eller detaljvisning.
     * 
     * @param customerCategoryID ID för den kundkategori som ska hämtas
     * @return ett CustomerCategory objekt om det hittas, annars null
     * @throws SQLException om ett fel uppstår vid databasanslutning eller SQL-exekvering
     */
    public CustomerCategory getCustomerCategoryByID(int customerCategoryID) throws SQLException {
        String findCustomerCategoryByID = "SELECT * FROM CustomerCategory WHERE CustomerCategoryID = ?";
        
        try (
            Connection conn = dbConnector.connect();
            PreparedStatement customerCategoryStmt = conn.prepareStatement(findCustomerCategoryByID)
        ) {
            customerCategoryStmt.setInt(1, customerCategoryID);
            ResultSet rsCustomerCategory = customerCategoryStmt.executeQuery();
            
            if (rsCustomerCategory.next()) {
                String customerCategoryName = rsCustomerCategory.getString("CategoryName");
                int loanLimit = rsCustomerCategory.getInt("LoanLimit");

                // Skapar CustomerCategory-objekt
                return new CustomerCategory(customerCategoryID, customerCategoryName, loanLimit);
            }
        }
        
        return null;
    }
}
