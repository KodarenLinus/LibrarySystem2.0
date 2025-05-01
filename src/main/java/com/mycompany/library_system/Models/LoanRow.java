/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.library_system.Models;

import com.mycompany.library_system.Logic.GetItemsByID;
import java.sql.SQLException;

/**
 *
 * @author Linus, Emil, Oliver, Viggo
 */
public class LoanRow {
    private int LoanRowID;
    private int itemID;
    private int loanID;
    private String loanRowStartDate;
    private String loanRowEndDate;
    private boolean activeLoan;
    private Items item;

    public LoanRow(int loanID, int itemID, String loanRowStartDate, String loanRowEndDate, boolean activeLoan) {
        this.loanID = loanID;
        this.itemID = itemID;
        this.loanRowStartDate = loanRowStartDate;
        this.loanRowEndDate = loanRowEndDate;
        this.activeLoan = activeLoan;
    }
    
    public void setLoanRowID(int LoanRowID) {
        this.LoanRowID = LoanRowID;
    }
    
    public int getLoanRowID() {
        return LoanRowID;
    }
    
    public int getLoanID() {
        return loanID;
    }

    public int getItemID() {
        return itemID;
    }
    
    public String getLoanRowStartDate () {
        return loanRowStartDate;
    }
    
    public String getLoanRowEndDate () {
        return loanRowEndDate;
    }
    
    public boolean getActiveLoan () {
        return activeLoan;
    }
    
     public void loadItem() throws SQLException {
        GetItemsByID itemFetcher = new GetItemsByID();
        this.item = itemFetcher.getItemById(this.itemID);
    }
     
     @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;

        LoanRow other = (LoanRow) obj;
        return this.getLoanRowID() == other.getLoanRowID(); // or use itemID + loanID combo if needed
    }

    @Override
    public int hashCode() {
        return Integer.hashCode(this.getLoanRowID()); // match with the equals() field
    }
    
    @Override
    public String toString() {
       return loanRowStartDate + " -> " + loanRowEndDate + ": " + item.toString();
    }
}

