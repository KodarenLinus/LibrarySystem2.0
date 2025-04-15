/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.libary_system.Logic;

import com.mycompany.libary_system.Models.DVD;
import com.mycompany.libary_system.Database.DatabaseConnector;
import com.mycompany.libary_system.Database.ConnDB;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 *
 * @author Linus
 */
public class AddDVD {
    

    //En metod för att lägga till items av typen book!!!!!
    public void insertDVD (DVD dvd) {
        
        DatabaseConnector connDB = new ConnDB();
        Connection conn = connDB.connect();
        
        try {
            
            //Lägger till itemet i items!!
            PreparedStatement stmt1 = conn.prepareStatement("INSERT INTO Item (GenreID, CategoryID, Title, Location, Available) VALUES (?, ?, ?, ?, ?)");
                stmt1.setInt(1, 10);
                stmt1.setInt(2, 9);
                stmt1.setString(3, dvd.getTitle());
                stmt1.setString(4, dvd.getLocation());
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
