/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.libary_system;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 *
 * @author Linus
 */
public class AddDVD {
    private ConnDB connDB = new ConnDB();

    //En metod för att lägga till items av typen DVD!!!!!
    void insertDVD (String title, String location) {
        
        try {
            Connection conn = DriverManager.getConnection(connDB.getDbUrl(), connDB.getDbUsername(), connDB.getDbPassword());
        
            //Lägger till itemet i items!!
            PreparedStatement stmt1 = conn.prepareStatement("INSERT INTO Item (GenreID, CategoryID, Title, Location, Available) VALUES (?, ?, ?, ?, ?)");
                stmt1.setInt(1, 1);
                stmt1.setInt(2, 1);
                stmt1.setString(3, title);
                stmt1.setString(4, location);
                stmt1.setBoolean(5, true);
                stmt1.executeUpdate();
            
            //Lägger till itemet i book tablen
            PreparedStatement stmt2 = conn.prepareStatement("INSERT INTO DvD (ItemID, DirectorID) VALUES ((SELECT max(ItemID) From item), ?)");
                stmt2.setInt(1, 1);
                stmt2.executeUpdate();

        } catch (SQLException ex){

        }
    }
}
