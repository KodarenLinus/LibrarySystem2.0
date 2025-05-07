/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.library_system.Logic.ItemManagement;

import com.mycompany.library_system.Database.ConnDB;
import com.mycompany.library_system.Database.DatabaseConnector;

/**
 *
 * @author Linus
 */
public class UpdateItem {
    private final DatabaseConnector dbConnector;

    public UpdateItem() {
        this.dbConnector = new ConnDB();
    }
}
