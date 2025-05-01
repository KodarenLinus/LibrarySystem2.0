/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.library_system.Models;

/**
 *
 * @author Linus, Emil, Oliver, Viggo
 */
public class LoanRow {
    private int itemID;
    private int loanID;

    public LoanRow(int itemID) {
        this.loanID = loanID;
        this.itemID = itemID;
    }
    
    public int getLoanID() {
        return loanID;
    }

    public int getItemID() {
        return itemID;
    }
}
