/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.library_system.Models;

/**
 * Representerar en kundkategori i bibliotekssystemet.
 * 
 * En kundkategori definierar en grupp kunder med gemensamma egenskaper,
 * såsom ett maximalt antal lån (loan limit).
 * 
 * @author Linus, Emil, Oliver, Viggo
 */
public class CustomerCategory {
    private int categoryID;
    private String categoryName;
    private int loanLimit;
    
    /**
     * Skapar en ny kundkategori med angivet ID, namn och lånebegränsning.
     * 
     * @param categoryID   Det unika ID:t för kategorin
     * @param categoryName Namnet på kundkategorin
     * @param loanLimit    Max antal objekt kunden får låna samtidigt
     */
    
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
