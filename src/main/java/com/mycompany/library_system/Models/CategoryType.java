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
    * Det ända Kategorierna här är de som är lånabara!!!
    */
    public enum CategoryType {
        BOOK(1),
        COURSE_LITERATURE(2),
        DVD(3);

        private final int id;

        CategoryType(int id) { 
            this.id = id; 
        }
        
        public int getId() { 
            return id; 
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
