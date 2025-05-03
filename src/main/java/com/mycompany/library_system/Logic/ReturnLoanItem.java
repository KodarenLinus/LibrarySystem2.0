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
 *En klass som hanterar åter lämning av lån.
 * 
 * @author Linus, Emil, Oliver, Viggo
 */
public class ReturnLoanItem {
    
    /**
     * En metod för att lämna tillbaka items
     * 
     * @param loanRows är de lån vi vi skall lämna tillbaka
     */
    public void returnItem (ArrayList<LoanRow> loanRows) {
        // Skapar en databaskoppling
        DatabaseConnector connDB = new ConnDB();
        Connection conn = connDB.connect();
        
        // SQL-fråga som updatera så att de aktiva lånet inte längre blir aktivt
        String updateLoanRow = "UPDATE LoanRow SET ActiveLoan = false " +
                "WHERE LoanRowID = ?";
        
        // Updaterar så att itemet blir tillgängligt för lån
        String updateItem = "UPDATE Item SET Available = true " +
                "WHERE ItemID = ?";

        try (
            PreparedStatement loanRowStmt = conn.prepareStatement(updateLoanRow);  
            PreparedStatement itemStmt = conn.prepareStatement(updateItem);   
        ){
            // Loppar igenom våran lista med loanRows och lämnar tillbaka varje item.
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
