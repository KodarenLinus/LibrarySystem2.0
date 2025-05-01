/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.library_system.Logic;

import com.mycompany.library_system.Database.ConnDB;
import com.mycompany.library_system.Database.DatabaseConnector;
import com.mycompany.library_system.Models.LoanRow;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 *
 * @author Linus, Emil, Oliver, Viggo
 */
public class ReturnLoanItem {
    
    public void returnItem (ArrayList<LoanRow> loanRows) {
        // Skapar en databaskoppling
        DatabaseConnector connDB = new ConnDB();
        Connection conn = connDB.connect();
        
        // SQL-fråga som lägger till first och lastname i Author tabelen
        String updateLoanRow = "UPDATE LoanRow SET ActiveLoan = false " +
                "WHERE LoanRowID = ?";
        
        String updateItem = "UPDATE Item SET Available = true " +
                "WHERE ItemID = ?";

        try (
            PreparedStatement loanRowStmt = conn.prepareStatement(updateLoanRow);  
            PreparedStatement itemStmt = conn.prepareStatement(updateItem);   
        ){
            for (LoanRow loanRow: loanRows)
            {
                loanRowStmt.setInt(1, loanRow.getLoanRowID());
                itemStmt.setInt(1, loanRow.getItemID());

                loanRowStmt.executeUpdate();
                itemStmt.executeUpdate();
            }
            
        } catch (SQLException ex){
            ex.printStackTrace(); 
        }
    }
}
