/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.library_system.Models;

/**
* En enum som representerar olika kategorier av objekt i biblioteket.
* Används för att identifiera hur länge olika typer av objekt får lånas.
*
* Varje kategori har ett kopplat ID som matchar värdet i databasen.
* Metoden {@code fromId(int id)} används för att konvertera ett heltal från databasen
* till motsvarande enum-typ.
*
* @author Linus, Emil, Oliver, Viggo
*/
public enum CategoryType {
    BOOK(1, "Bok"),
    COURSE_LITERATURE(2, "Kurslitteratur"),
    DVD(3, "DVD"),
    REFRENCE_COPY(4, "Referensexemplar"),
    MAGAZINE(5, "Tidskrift");

    private final int id;
    private final String displayName;

    CategoryType(int id, String displayName) {
        this.id = id;
        this.displayName = displayName;
    }

    public int getId() {
        return id;
    }

    public String getDisplayName() {
        return displayName;
    }
    
    /**
    * Returnerar enum-typen som matchar det angivna ID:t.
    *
    * @param id Kategorins ID från databasen
    * @return Matchande CategoryType eller null om inget matchar
    */
    public static CategoryType fromId(int id) {
        for (CategoryType type : values()) {
            if (type.id == id) return type;
        }
        return null;
    }
        
        
}
