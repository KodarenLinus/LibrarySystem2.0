/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.library_system.Models;

import com.mycompany.library_system.Logic.GetItemsByID;
import java.sql.SQLException;
import java.time.LocalDate;

/**
 *
 * @author Linus, Emil, Oliver, Viggo
 */
public class ReservationRow {
    private int reservationRowID;
    private int reservationID;
    private int itemID;
    private boolean isFufilled;
    private Items item;
    private LocalDate reservationDate;

    public ReservationRow(int reservationID, int itemID, boolean isFufilled, LocalDate reservationDate) {
        this.reservationID = reservationID;
        this.itemID = itemID;
        this.isFufilled = isFufilled;
        this.reservationDate = reservationDate;
    }

    public int getReservationRowID() {
        return reservationRowID;
    }

    public void setReservationRowID(int reservationRowID) {
        this.reservationRowID = reservationRowID;
    }

    public int getReservationID() {
        return reservationID;
    }

    public void setReservationID(int reservationID) {
        this.reservationID = reservationID;
    }
    
    public LocalDate getReservationDate() {
        return reservationDate;
    }
    
    public void setReservationDate(LocalDate reservationDate) {
        this.reservationDate = reservationDate;
    }

    public int getItemID() {
        return itemID;
    }

    public void setItemID(int itemID) {
        this.itemID = itemID;
    }

    public boolean getIsFufilled() {
        return isFufilled;
    }
    
    public void loadItem() throws SQLException {
        GetItemsByID itemFetcher = new GetItemsByID();
        this.item = itemFetcher.getItemById(this.itemID);
    }

    public void setActive(boolean active) {
        isFufilled = active;
    }

    @Override
    public String toString() {
        return "Date: " + reservationDate.toString() + " -> " + item.toString();
    }
}