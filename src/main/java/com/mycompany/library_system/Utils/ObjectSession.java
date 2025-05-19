/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.library_system.Utils;

import com.mycompany.library_system.Models.Items;
/**
 * Singleton-klass som håller ett referensobjekt under en session.
 * Används exempelvis för att spara det objekt som en användare just nu arbetar med.
 * 
 * Exempel: ett valt Item (bok, film etc.) i gränssnittet.
 * 
 * @author Linus, Emil, Oliver, Viggo
 */
public class ObjectSession {
    
    // Den enda instansen av ObjectSession
    private static ObjectSession instance;
    
   // Det aktuella objektet som används i systemet (ex. bok, kund, etc.)
    private Object currentItem;

    // Privat konstruktor för att förhindra skapande av flera instanser
    private ObjectSession() {}

    /**
     * Hämtar instansen av ObjectSession.
     * Skapar en ny instans om den inte redan finns.
     *
     * @return den enda instansen av ObjectSession
     */
    public static ObjectSession getInstance() {
        if (instance == null) {
            instance = new ObjectSession();
        }
        return instance;
    }

    public void setCurrentItem(Items item) {
        this.currentItem = item;
    }

   
    public Object getCurrentItem() {
        return currentItem;
    }

    /**
     * Tömmer det aktuella objektet i sessionen.
     * Används exempelvis vid utloggning eller när ett objekt avmarkeras.
     */
    public void clear() {
        currentItem = null;
    }
}
