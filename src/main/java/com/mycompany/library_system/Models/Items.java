/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.library_system.Models;

/**
 *
 * @author Linus, Emil, Oliver, Viggo
 */

// Ett interface för items
public interface Items {
    int getItemID();
    int getGenreID();
    int getCategoryID();
    String getGenreName();
    String getCategoryName();
    String getTitle();
    String getLocation();
    boolean getAvailable();
}
