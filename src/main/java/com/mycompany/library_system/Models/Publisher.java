package com.mycompany.library_system.Models;

/**
 * En Publisher klass som vi använder när vi hämtar publishers i databasen
 * 
 * @author Linus, Emil, Oliver, Viggo
 */
public class Publisher {
    private int publisherID;
    private String publisherName;
    private String email;
    private String adress;

    public Publisher(String publisherName, String email, String adress) {
        this.publisherName = publisherName;
        this.email = email;
        this.adress = adress;
    }
    
    public void setPublisherID(int publisherID) {
         this.publisherID = publisherID;
    }

    public int getPublisherID() {
        return publisherID;
    }

    public String getPublisherName() {
        return publisherName;
    }
    
    public String getEmail() {
        return email;
    }
    
    public String getAdress() {
        return adress;
    }

    @Override
    public String toString() {
        return publisherName;
    }
}