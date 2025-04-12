/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.libary_system;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Linus
 */
public class Loan {
    
    private ArrayList<LoanRow> loanRows;
    
    public void createALoan() {
        // Create a loan for the customer
    }
    
    public void addALoanRow(LoanRow loanRow) {
        // Add temp loanRow to temporary loanrow list
        loanRows.add(loanRow);
    }
    
    public void removeALoanRow(LoanRow loanRow) {
        // Remove temp loanRow to temporary loanrow list
        loanRows.remove(loanRow);
        
    }
    
    public void addToLoanRows (int loanID) {
        
        // Skapar ett connection objekt ocj spara en SQL query i en String.
        DatabaseConnector connDB = new ConnDB();
        Connection conn = connDB.connect();
        String sql = "INSERT INTO loan_row (loan_id, item_id) VALUES (?, ?)";
        
        try {
            
            // Skapar ett preparedStatement objekt!!
            PreparedStatement stmt = conn.prepareStatement(sql);
            
            // En loop för att lägga in alla loanrows i loanrows tabellen
            for (LoanRow loanRow : loanRows) {
                
                stmt.setInt(1, loanID);
                stmt.setInt(2, loanRow.getItemID());
                stmt.addBatch();  
            }
            
        // Skickar in alla loanrows 
        stmt.executeBatch();
    
        } catch (SQLException e) {
                
        }
    }         
}
