/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.library_system.Database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 *
 * Den här klassen hanterar våran databas koppling för mySQL
 * 
 * @author Linus, Emil, Oliver, Viggo
 */

public class ConnDB implements DatabaseConnector {
    // URL, Användarnamen och lösenord för mySQL databasen!!
    private static final String DB_URL = "jdbc:mysql://localhost:3306/Librarysystem";
    private static final String DB_USERNAME = "root"; 
    private static final String DB_PASSWORD = "Finnträsk69";
    
    // En metod som skapar en databaskoppling.
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
