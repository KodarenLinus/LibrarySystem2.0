/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.library_system.Models;

/**
 * Representerar en kategori för ett objekt i bibliotekssystemet.
 * En kategori har ett unikt ID och ett namn.
 * 
 * @author Linus, Emil, Oliver, Viggo
 */
public class Category {
    private int categoryID;
    private String categoryName;
    
    /**
     * Skapar en ny kategori med angivet ID och namn.
     * 
     * @param categoryID   Unikt ID för kategorin
     * @param categoryName Namnet på kategorin
     */
    
    public Category (int categoryID, String categoryName) {
        this.categoryID = categoryID;
        this.categoryName = categoryName;
    }
    
    public int getCategoryID () {
        return categoryID;
    }
    
    public String getCategoryName () {
        return categoryName;
    }
    
    @Override
    public String toString () {
        return categoryName;
    }
}
