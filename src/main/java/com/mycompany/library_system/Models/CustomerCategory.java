/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.library_system.Models;

/**
 *
 * @author Linus
 */
public class CustomerCategory {
    private int categoryID;
    private String categoryName;
    private int loanLimit;
    
    public CustomerCategory (int categoryID, String categoryName, int loanLimit) {
        this.categoryID = categoryID;
        this.categoryName = categoryName;
        this.loanLimit = loanLimit;
    }
    
    public int getCategoryID() {
        return categoryID;
    }
    
    public String getCategoryName() {
        return categoryName;
    }
    
    public int getLoanLimit() {
        return loanLimit;
    }
    
    @Override
    public String toString() {
        return categoryName;
    }
}
