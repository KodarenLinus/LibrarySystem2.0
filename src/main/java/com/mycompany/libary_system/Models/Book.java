/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.libary_system.Models;

import com.mycompany.libary_system.Models.Items;
import java.util.ArrayList;
import java.util.Objects;

/**
 *
 * @author Linus
 */
public class Book implements Items{
    private int itemID;
    private String title;
    private ArrayList<Integer> authorsID;
    private String genreName;
    private String categoryName;
    private String location;
    private boolean available;
    private int isbn;
    
    
    public Book(String title, String location, int isbn) {
        this.title = title;
        this.location = location;
        this.isbn = isbn;
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
    
    public int getIsbn () {
        return isbn;
    }
    
    public Integer getAuthors () {
        
        // Loopar igenom lista med authors
        for (int authorID : authorsID) {
            return authorID; // Retunera varje author;
        }
        
        return null; //
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Book other = (Book) obj;
        return itemID == other.getItemID();
    }

    @Override
    public int hashCode() {
        return Objects.hash(itemID);
    }
    
    @Override
    public String toString() {
        return title; // Display only the title in ListView
    }
}
