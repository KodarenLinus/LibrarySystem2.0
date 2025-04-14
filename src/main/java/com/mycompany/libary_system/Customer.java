/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.libary_system;

import java.util.ArrayList;

/**
 *
 * @author emildahlback
 */
public class Customer {
    private int customerID;
    private String firstName;
    private String lastName;
    private int telNr;
    private String email;
    private String password;
    
    
    public Customer(String firstName, String lastName, int telNr, String email, String password) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.telNr = telNr;
        this.email = email;
        this.password = password;
    }
    
    public void setCustomerID(int customerID) {
        this.customerID = customerID;
    }
    
    //@Override
    public int getCustomerID() {
        return customerID;
    }
    
    //@Override
    public String getFirstName(){
        return firstName;
    }
    
    //@Override
    public String getLastName(){
        return lastName;
    }
    
    //@Override
    public int getTelNr(){
        return telNr;
    }
    
    //@Override
    public String getEmail(){
        return email;
    }
    
    public String getPassword(){
        return password;
    }
    
    
    @Override
    public String toString() {
        return lastName; // Display only the title in ListView
    }
}

