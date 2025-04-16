/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.libary_system.Login;

/**
 *
 * @author Linus
 */
public class Session {
    
    private static Session instance;
    private int userId;
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
        this.userId = userId;
        this.email = email;
    }

    public int getUserId() {
        return userId;
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
