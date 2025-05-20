/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.library_system.Models;

import java.util.Objects;

/**
 * En DVD klass som vi använder när vi hämtar och skall lägga in DVD i databasen
 * 
 * @author Linus, Emil, Oliver, Viggo
 */
public class DVD implements Items {
    private int itemID;
    private String title;
    private int genreID;
    private int categoryID;
    private String genreName;
    private String categoryName;
    private String location;
    private boolean available;
    private int directorID;
    
    public DVD (String title, String location, int categoryID, String categoryName, int genreID, String genreName, int directorID) {
        this.title = title;
        this.location = location;
        this.categoryID = categoryID;
        this.categoryName = categoryName;
        this.genreID = genreID;
        this.genreName = genreName;
        this.directorID = directorID;
    }
    
    public void setItemID(int itemID) {
        this.itemID = itemID;
    }
    
    @Override
    public int getItemID() {
        return itemID;
    }
    
    @Override
    public int getGenreID() {
        return genreID;
    }

    @Override
    public int getCategoryID() {
        return categoryID;
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
    
    public int getDirectorID() {
        return directorID;
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        DVD other = (DVD) obj;
        return itemID == other.getItemID();
    }

    @Override
    public int hashCode() {
        return Objects.hash(itemID);
    }
    
    @Override
    public String toString() {
        return title; 
    }
}
