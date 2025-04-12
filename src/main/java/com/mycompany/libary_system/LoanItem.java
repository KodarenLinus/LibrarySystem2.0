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
        
        //SQL-statments
        String sql = "INSERT INTO loan (customerID) VALUES (?)";
        String selectMaxLoanId = "SELECT MAX(loanid) FROM loan WHERE customerID = ?";
        String selectCategoryId = "SELECT categoryID FROM item WHERE itemID = ?";
        String sql_ = "INSERT INTO loanrow (loanid, itemid, RowLoanStartDate, RowLoanEndDate) VALUES (?, ?, ?, ?)";
        
        LocalDate today = LocalDate.now();
        
        // Add 1 week
        LocalDate inOneWeek = today.plusWeeks(1);
        
        // Add 2 weeks
        LocalDate inTwoWeeks = today.plusWeeks(2);

        // Add 1 month
        LocalDate nextMonth = today.plusMonths(1);
        
        try ( 
            PreparedStatement stmt = conn.prepareStatement(sql);    
            PreparedStatement getLoanIDStmt = conn.prepareStatement(selectMaxLoanId);
            PreparedStatement stmt1 = conn.prepareStatement(sql_);
            PreparedStatement getCategoryIDStmt = conn.prepareStatement(selectCategoryId);
        ){
            // Sätter kund id på loanet
            stmt.setInt(1, custID);
            stmt.executeUpdate();
            
            // hämtar lånet baserats på kundens senast lån
            getLoanIDStmt.setInt(1, custID);
            ResultSet rs = getLoanIDStmt.executeQuery();
            
            // Kollar igenom resultsetet
            if (rs.next()) {
                //Sparar loanID i en lokal variabel
                int loanID = rs.getInt(1);
                
                //Lopar igenom alla våra items i våran kundvagn
                for (Items item : itemsToLoan) {
                    stmt1.setInt(1, loanID);
                    stmt1.setInt(2, item.getItemID());
                    stmt1.setString(3, today.toString()); 
                    
                    //Lägg till if statment som väljer vilket datum saker skall göras.
                    getCategoryIDStmt.setInt(1, item.getItemID());
                    ResultSet rs1 = getCategoryIDStmt.executeQuery();
                    if (rs1.next()) {
                        int categoryID = rs1.getInt("categoryID");

                        // kollar items category och väljer hur länge som man får låna itemet!!
                        switch (categoryID) {
                            case 1:
                                stmt1.setString(4, nextMonth.toString());
                                break;
                            case 2:
                                stmt1.setString(4, inTwoWeeks.toString());
                                break;
                            case 3:
                                stmt1.setString(4, inOneWeek.toString());
                                break;
                            default:
                                stmt1.setString(4, today.toString()); 
                                break;
                        }
                    }
                            
                    //spara in våra loanrows
                    stmt1.addBatch();
                }
                
                //Skickar in dem i databasen
                stmt1.executeBatch();
                return true;
            }

        } catch (SQLException e) {
            e.printStackTrace(); 
        }

        return false;
    }
}
