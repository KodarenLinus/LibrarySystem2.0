/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.library_system.Utils;

/**
 *
 * @author Linus
 */
public class ObjectSession {
    
    private static ObjectSession instance;
    private ObjectSession() {}

    public static ObjectSession getInstance() {
        if (instance == null) {
            instance = new ObjectSession();
        }
        return instance;
    }
    private static Object currentItem;

    public static void setCurrentItem(Object item) {
        currentItem = item;
    }

    public static Object getCurrentItem() {
        return currentItem;
    }

    public static void clear() {
        currentItem = null;
    }
    
}
