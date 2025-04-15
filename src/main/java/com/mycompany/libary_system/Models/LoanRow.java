/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.libary_system.Models;

/**
 *
 * @author Linus
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
