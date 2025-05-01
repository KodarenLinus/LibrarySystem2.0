/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.library_system.Models;

/**
 *
 * @author Linus
 */
public class Director {
    
    private int directorID;
    private String firstname;
    private String lastname;
    
    public Director (String firstname, String lastname) {
        this.firstname = firstname;
        this.lastname = lastname;
    }
    
    public void setDirectorID (int directorID) {
        this.directorID = directorID;
    }
    
    public int getDirectorID() {
        return directorID;
    }
    
     public String getFirstname() {
        return firstname;
    }
    
    public String getLastname() {
        return lastname;
    }
    
    @Override
    public String toString() {
        return firstname + " " + lastname;
    }
    
}
