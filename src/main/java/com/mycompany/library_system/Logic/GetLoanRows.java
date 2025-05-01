/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.library_system.Logic;

import com.mycompany.library_system.Database.ConnDB;
import com.mycompany.library_system.Login.Session;
import com.mycompany.library_system.Models.LoanRow;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 *
 * @author Linus, Emil, Oliver, Viggo
 */
public class GetLoanRows {
    public ArrayList<LoanRow> getAllLoanRows (boolean activeLoans) throws SQLException {
        ArrayList<LoanRow> loanRowList = new ArrayList<>();
        
        // Skapar en databasanslutning
        ConnDB connDB = new ConnDB();
        Connection conn = connDB.connect();
        
        // En SQL-fråga för att hämta alla loanRows för en kund
        String selectAllLoanRows = "SELECT * FROM LoanRow WHERE (ActiveLoan = ?) AND ( LoanID IN "
                + "(SELECT LoanID FROM Loan WHERE CustomerID = ?))";
        
        try (
            PreparedStatement loanRowStmt = conn.prepareStatement(selectAllLoanRows);
        ) {
            loanRowStmt.setBoolean(1, activeLoans);
            loanRowStmt.setInt(2, Session.getInstance().getUserId());
            ResultSet rsLoanRow = loanRowStmt.executeQuery();
            
            while (rsLoanRow.next()) {
                int loanRowID = rsLoanRow.getInt("LoanRowID");
                int loanID = rsLoanRow.getInt("LoanID");
                int itemID = rsLoanRow.getInt("ItemID");
                String rowLoanStartDate = rsLoanRow.getString("RowLoanStartDate");
                String rowLoanEndDate = rsLoanRow.getString("RowLoanEndDate");
                boolean activeLoan = rsLoanRow.getBoolean("ActiveLoan");
                
                LoanRow loanRow = new LoanRow(loanID, itemID, rowLoanStartDate, rowLoanEndDate, activeLoan);
                loanRow.setLoanRowID(loanRowID);
                loanRowList.add(loanRow);
            }
        } 
        return loanRowList;
    }
}
