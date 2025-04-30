/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.library_system.Models;

/**
 *
 * @author Linus, Emil, Oliver, Viggo
 */
public class Genre {
     private int genreID;
    private String genreName;
    
    public Genre (int genreID, String genreName) {
        this.genreID = genreID;
        this.genreName = genreName;
    }
    
    public int getGenreID () {
        return genreID;
    }
    
    public String getGenreName () {
        return genreName;
    }
    
    @Override
    public String toString () {
        return genreName;
    }
}
