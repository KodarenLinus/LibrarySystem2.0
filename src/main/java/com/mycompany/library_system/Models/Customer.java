/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.library_system.Models;

/**
 * Representerar en kund i bibliotekssystemet med personliga uppgifter och kategori.
 * 
 * Klassen innehåller information som namn, telefonnummer, e-post, lösenord samt
 * koppling till en kategori.
 *
 * @author Linus, Emil, Oliver, Viggo
 */
public class Customer {
    private int customerID;
    private String firstName;
    private String lastName;
    private int telNr;
    private String email;
    private String password;
    private int categoryID;
    private String categoryName;
    
    public Customer(String firstName, String lastName, int telNr, String email, String password, int categoryID, String categoryName) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.telNr = telNr;
        this.email = email;
        this.password = password;
        this.categoryID = categoryID;
        this.categoryName = categoryName;
    }
    
    public void setCustomerID(int customerID) {
        this.customerID = customerID;
    }
    
    public int getCustomerID() {
        return customerID;
    }
    
    public String getFirstName(){
        return firstName;
    }
    
    public String getLastName(){
        return lastName;
    }
    
    public int getTelNr(){
        return telNr;
    }
    
    public String getEmail(){
        return email;
    }
    
    public String getPassword(){
        return password;
    }
    
    public int getCategoryID() {
        return categoryID;
    }
    
    public String getCategoryName() {
        return categoryName;
    }
    
    @Override
    public String toString() {
        return firstName + " " + lastName; 
    }
}

