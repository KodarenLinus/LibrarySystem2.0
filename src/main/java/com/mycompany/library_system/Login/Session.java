/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.library_system.Login;

/**
 * Singleton-klass som representerar en inloggad användares session.
 * Innehåller information som användarens ID, e-postadress och kategori.
 * Endast en instans av denna klass får existera under en session.
 * 
 * Används för att lagra inloggningsdata temporärt under programmets gång.
 * 
 * @author Linus, Emil, Oliver, Viggo
 */
public class Session {
    
    // Den enda instansen av sessionen (Singleton)
    private static Session instance;
    
    // Information om användaren
    private int UserId;
    private String email;
    private String category;
    
    // Privat konstruktor så att ingen annan kan skapa instanser
    private Session() {}
    
    /**
     * Hämtar den nuvarande instansen av sessionen.
     * Skapar en ny om ingen finns.
     * 
     * @return den enda instansen av Session
     */
    public static Session getInstance() {
        if (instance == null) {
            instance = new Session();
        }
        return instance;
    }
    
    /**
     * Sätter information om den inloggade användaren.
     * 
     * @param userId användarens ID
     * @param email användarens e-postadress
     */
    public void setUser(int userId, String email) {
        this.UserId = userId;
        this.email = email;
    }

    public int getUserId() {
        return UserId;
    }

    public String getEmail() {
        return email;
    }

    public String getCategory() {
        return category;
    }

    /**
     * Tömmer sessionen (används vid utloggning).
     */
    public void clear() {
        instance = null;
    }
}
