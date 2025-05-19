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
 * Om kunden finns (baserat på e-postadress), uppdateras dess attribut.
 * Används vid ändring av exempelvis namn, lösenord eller telefonnummer.
 * 
 * @author Linus, Emil, Oliver, Viggo
 */
public class UpdateCustomer {

    /** Objekt för att hantera databaskopplingen */
    private final DatabaseConnector dbConnector;

    /**
     * Skapar en ny instans av {@code UpdateCustomer} med standarddatabaskoppling.
     */
    public UpdateCustomer() {
        this.dbConnector = new ConnDB();
    }

    /**
     * Kontrollerar om en kund finns i databasen via e-postadress.
     * Om kunden finns, uppdateras alla kundens attribut i databasen.
     *
     * @param customer Customer objektet med uppdaterad information
     * @return true om kunden hittades och uppdaterades,
     *         false om kunden inte fanns eller om ett fel uppstod
     */
    public boolean updateCustomerIfExists(Customer customer) {
        Connection conn = dbConnector.connect();
        
        // SQL-fråga för att kontrollera om kunden finns
        String checkCustomer = "SELECT * FROM Customer WHERE Email = ?";
        
        // SQL-fråga för att uppdatera kundens information
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
            // Kontrollera om kunden finns via e-post
            checkStmt.setString(1, customer.getEmail());
            ResultSet rs = checkStmt.executeQuery();

            if (rs.next()) {
                // Kunden finns – uppdatera kundens uppgifter
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
