   
package com.mycompany.library_system.Logic.CustomerMangement;

import com.mycompany.library_system.Models.Customer;
import com.mycompany.library_system.Database.DatabaseConnector;
import com.mycompany.library_system.Database.ConnDB;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Klass som hanterar uppdatering av en kund i databasen.
 * Om kunden finns (baserat på e-post), uppdateras dess attribut.
 * 
 * @author Linus, Emil, Oliver, Viggo
 */
public class UpdateCustomer {

    private final DatabaseConnector dbConnector;

    public UpdateCustomer() {
        this.dbConnector = new ConnDB();
    }

    /**
     * Kollar om en kund finns i databasen (via e-post), och uppdaterar alla attribut om den finns.
     *
     * @param customer Customer-objektet med uppdaterad data
     * @return true om kunden uppdaterades, false om kunden inte fanns
     */
    public boolean updateCustomerIfExists(Customer customer) {
        Connection conn = dbConnector.connect();
        
        // SQL för att kolla om kunden finns
        String checkCustomer = "SELECT * FROM Customer WHERE Email = ?";
        
        // SQL för att uppdatera kunden
        String updateCustomer = "UPDATE Customer SET " +
                "CustomerCategoryID = ?, " +
                "CategoryName = ?, " +
                "FirstName = ?, " +
                "LastName = ?, " +
                "TelNumber = ?, " +
                "PasswordCustomer = ? " +
                "WHERE Email = ?";

        try (
            PreparedStatement checkStmt = conn.prepareStatement(checkCustomer);
            PreparedStatement updateStmt = conn.prepareStatement(updateCustomer)
        ) {
            // Kolla om kunden finns via e-post
            checkStmt.setString(1, customer.getEmail());
            ResultSet rs = checkStmt.executeQuery();

            if (rs.next()) {
                // Kunden finns – uppdatera
                updateStmt.setInt(1, customer.getCategoryID());
                updateStmt.setString(2, customer.getCategoryName());
                updateStmt.setString(3, customer.getFirstName());
                updateStmt.setString(4, customer.getLastName());
                updateStmt.setInt(5, customer.getTelNr());
                updateStmt.setString(6, customer.getPassword());
                updateStmt.setString(7, customer.getEmail());

                updateStmt.executeUpdate();
                return true;
            } else {
                // Kunden finns inte
                return false;
            }

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
