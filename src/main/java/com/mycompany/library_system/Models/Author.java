/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.library_system.Models;

import java.util.Objects;

/**
 *
 * @author Linus
 */
public class Author {
    
    private int authorID;
    private String firstname;
    private String lastname;
    
    public Author (String firstname, String lastname) {
        this.firstname = firstname;
        this.lastname = lastname;
    }
    
    public void setAuthorID (int authorID) {
        this.authorID = authorID;
    }
    
    public int getAuthorID () {
        return authorID;
    }
    
    public String getFirstname () {
        return firstname;
    }
      
    public String getLastname () {
        return lastname;
    }
    
   @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Author other = (Author) obj;
        return this.authorID == other.authorID;  // jämför med ID
    }

    @Override
    public int hashCode() {
        return Integer.hashCode(authorID);
    }
    
    @Override
    public String toString() {
        return firstname + " " + lastname; 
    }
    
}
