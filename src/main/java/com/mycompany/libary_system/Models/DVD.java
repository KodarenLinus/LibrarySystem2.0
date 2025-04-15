/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.libary_system.Models;

import com.mycompany.libary_system.Models.Items;
import java.util.ArrayList;

/**
 *
 * @author Linus
 */
public class DVD implements Items {
    
    private int itemID;
    private String title;
    private ArrayList<Integer> directorsID;
    private String genreName;
    private String categoryName;
    private String location;
    private boolean available;
    
    // Konstruktor
    public DVD (String title, String location) {
        this.title = title;
        this.location = location;
    }
    
    public void setItemID(int itemID) {
        this.itemID = itemID;
    }
    
    @Override
    public int getItemID() {
        return itemID;
    }

    @Override
    public String getGenreName() {
        return genreName;
    }

    @Override
    public String getCategoryName() {
        return categoryName;
    }

    @Override
    public String getTitle() {
        return title;
    }

    @Override
    public String getLocation() {
        return location;
    }

    @Override
    public boolean getAvailable() {
        return available;
    }
    
    public Integer getDirector () {
        
        // loppar igenom list med directors
        for (int directorID : directorsID)
        {
            return directorID; // Retunera varje author;
        }
        
        return null; //
    }
    
    
    @Override
    public String toString() {
        return title; // Display only the title in ListView
    }
}
