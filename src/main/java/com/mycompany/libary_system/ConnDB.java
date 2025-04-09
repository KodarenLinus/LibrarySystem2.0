/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.libary_system;

/**
 *
 * @author Linus
 */
public class ConnDB {
    private static final String dbUrl = "jdbc:mysql://localhost:3306/book_libary";
    private static final String dbUsername = "root"; 
    private static final String dbPassword = "LinusS";
    
    public ConnDB() {
        
    }
    
    public String getDbUrl()
    {
        return dbUrl;
    }
    
    public String getDbUsername()
    {
        return dbUsername;
    }
     
    public String getDbPassword()
    {
        return dbPassword;
    }
}
