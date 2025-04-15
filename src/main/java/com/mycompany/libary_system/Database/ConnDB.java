/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.libary_system.Database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 *
 * @author Linus
 */

public class ConnDB implements DatabaseConnector {
    private static final String DB_URL = "jdbc:mysql://localhost:3306/LibrarySystem";
    private static final String DB_USERNAME = "root"; 
    private static final String DB_PASSWORD = "Finnträsk69";
    
    @Override
    public Connection connect() {
        try {
            return DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD);
        } catch (SQLException e) {
            System.out.println("Failed to connect to DB: " + e.getMessage());
            return null;
        }
    }
}
