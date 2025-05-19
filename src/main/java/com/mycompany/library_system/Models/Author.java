/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.library_system.Models;


/**
 *
 * @author Linus, Emil, Oliver, Viggo
 */
public class Author {
    
    private int authorID;
    private String firstname;
    private String lastname;
    
    /**
     * Skapar en ny instans av Author med förnamn och efternamn.
     * 
     * @param firstname Författarens förnamn
     * @param lastname  Författarens efternamn
     */
    public Author (String firstname, String lastname) {
        this.firstname = firstname;
        this.lastname = lastname;
    }
    
    /**
     * Sätter författarens ID (vanligtvis hämtat från databasen).
     * 
     * @param authorID Det unika ID:t som tilldelats författaren
     */
    public void setAuthorID (int authorID) {
        this.authorID = authorID;
    }
    
    /**
     * Returnerar författarens unika ID.
     * 
     * @return Författarens ID
     */
    public int getAuthorID () {
        return authorID;
    }
    
    /**
     * Returnerar författarens förnamn.
     * 
     * @return Förnamn
     */
    public String getFirstname () {
        return firstname;
    }
     
    /**
     * Returnerar författarens efternamn.
     * 
     * @return Efternamn
     */
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
    
    /**
     * Returnerar författarens fullständiga namn som en sträng.
     * 
     * @return Förnamn och efternamn i formatet "Förnamn Efternamn"
     */
    @Override
    public String toString() {
        return firstname + " " + lastname; 
    }
    
}
