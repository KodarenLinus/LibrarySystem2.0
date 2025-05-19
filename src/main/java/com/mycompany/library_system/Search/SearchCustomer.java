package com.mycompany.library_system.Search;

import com.mycompany.library_system.Database.ConnDB;
import com.mycompany.library_system.Database.DatabaseConnector;
import com.mycompany.library_system.Models.Customer;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

/**
 * SearchCustomer ansvarar för att hämta kunder baserat på en söktext som matchar för- eller efternamn.
 * Klassen följer Single Responsibility-principen och använder ett DatabaseConnector-interface för lös koppling.
 * 
 * @author Linus, Emil, Oliver, Viggo
 */
public class SearchCustomer {

    private final DatabaseConnector dbConnector;

    public SearchCustomer() {
        this.dbConnector = new ConnDB();
    }

    /**
     * Söker efter kunder vars för- eller efternamn matchar den angivna söktexten.
     *
     * @param searchText -> Söksträng som användaren skriver in
     * @return En lista med kunder som matchar sökningen
     */
    public ArrayList<Customer> searchCustomer(String searchText) {
        ArrayList<Customer> results = new ArrayList<>();

        String customerSearch = "SELECT CustomerID, FirstName, LastName, TelNumber, Email, PasswordCustomer, CustomerCategoryID, CategoryName " +
                                "FROM Customer WHERE FirstName LIKE ? OR LastName LIKE ?";

        try (
            Connection conn = dbConnector.connect();
            PreparedStatement findCustomerStmt = conn.prepareStatement(customerSearch);
        ) {

            findCustomerStmt.setString(1, "%" + searchText + "%");
            findCustomerStmt.setString(2, "%" + searchText + "%");

            ResultSet rs = findCustomerStmt.executeQuery();

            while (rs.next()) {
                int id = rs.getInt("CustomerID");
                String firstName = rs.getString("FirstName");
                String lastName = rs.getString("LastName");
                int telNr = rs.getInt("TelNumber");
                String email = rs.getString("Email");
                String password = rs.getString("PasswordCustomer");
                int categoryID  = rs.getInt("CustomerCategoryID");
                String categoryName  = rs.getString("CategoryName");

                Customer customer = new Customer(firstName, lastName, telNr, email, password, categoryID, categoryName);
                customer.setCustomerID(id);

                results.add(customer);
            }

        } catch (Exception e) {
            e.printStackTrace(); // I produktion bör detta hanteras bättre (loggning, felmeddelande etc.)
        }

        return results;
    }
}
