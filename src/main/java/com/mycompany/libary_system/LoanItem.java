/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.libary_system;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;

/**
 *
 * @author emildahlback
 */
public class LoanItem {
    
    public boolean addToLoanRows (int custID, ArrayList<Items> itemsToLoan) {
                
        // Skapar ett connection objekt ocj spara en SQL query i en String.
        DatabaseConnector connDB = new ConnDB();
        Connection conn = connDB.connect();
        String sql = "INSERT INTO loan (customerID) VALUES (?)";
        String selectMaxLoanId = "SELECT MAX(loanid) FROM loan WHERE customerID = ?";
        String sql_ = "INSERT INTO loanrow (loanid, itemid, RowLoanStartDate, RowLoanEndDate) VALUES (?, ?, ?, ?)";
        
        LocalDate today = LocalDate.now();
        
        // Add 1 week
        LocalDate inOneWeeks = today.plusWeeks(1);
        
        // Add 2 weeks
        LocalDate inTwoWeeks = today.plusWeeks(2);

        // Add 1 month
        LocalDate nextMonth = today.plusMonths(1);
        
        try ( 
            PreparedStatement stmt = conn.prepareStatement(sql);    
            PreparedStatement getLoanIDStmt = conn.prepareStatement(selectMaxLoanId);
            PreparedStatement stmt1 = conn.prepareStatement(sql_);
        ){
            
            stmt.setInt(1, custID);
            stmt.executeUpdate();

            getLoanIDStmt.setInt(1, custID);
            ResultSet rs = getLoanIDStmt.executeQuery();

            if (rs.next()) {
                int loanID = rs.getInt(1);

                for (Items item : itemsToLoan) {
                    stmt1.setInt(1, loanID);
                    stmt1.setInt(2, item.getItemID());
                    stmt1.setString(3, today.toString()); 
                    
                    //Lägg till if statment som väljer vilket datum saker skall göras.
                    stmt1.setString(4, nextMonth.toString());
                    stmt1.addBatch();
                }

                stmt1.executeBatch();
                return true;
            }

        } catch (SQLException e) {
            e.printStackTrace(); // or use a logger
        }

        return false;
    }
}
