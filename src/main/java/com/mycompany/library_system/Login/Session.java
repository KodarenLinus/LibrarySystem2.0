/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.library_system.Login;

/**
 *
 * @author Linus
 */
public class Session {
    
    private static Session instance;
    private int UserId;
    private String email;
    private String category;

    private Session() {}

    public static Session getInstance() {
        if (instance == null) {
            instance = new Session();
        }
        return instance;
    }

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

    public void clear() {
        instance = null;
    }
}
