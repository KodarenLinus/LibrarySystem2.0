/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.library_system.Models;


/**
 * Modellen för en författare med förnamn, efternamn och ett unikt ID.
 * 
 * Klassen innehåller metoder för att hämta och sätta författarens egenskaper,
 * samt metoder för att jämföra författarobjekt baserat på deras ID.
 *
 * @author Linus, Emil, Oliver, Viggo
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
    
    /**
     * Kontrollerar om två Author-objekt är lika baserat på deras ID.
     * 
     * @param obj Objektet som ska jämföras
     * @return true om objekten har samma ID, annars false
     */
   @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Author other = (Author) obj;
        return this.authorID == other.authorID;  
    }
    
    /**
     * Returnerar hashkod baserat på författarens ID.
     * 
     * @return hashkod för Author-objektet
     */
    @Override
    public int hashCode() {
        return Integer.hashCode(authorID);
    }
    
    @Override
    public String toString() {
        return firstname + " " + lastname; 
    }
    
}
