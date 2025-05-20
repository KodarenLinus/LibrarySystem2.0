package com.mycompany.library_system.Models;

import com.mycompany.library_system.Logic.ItemManagement.GetItemsByID;
import java.sql.SQLException;
import java.time.LocalDate;

/**
 * En ReservationRow klass som vi använder när vi hämtar och skall lägga in ReservationRows i databasen
 * 
 * @author Linus, Emil, Oliver, Viggo
 */
public class ReservationRow {
    private int reservationRowID;
    private int reservationID;
    private int itemID;
    private boolean isFulfilled;
    private Items item;
    private LocalDate reservationDate;

    public ReservationRow(int reservationID, int itemID, boolean isFulfilled, LocalDate reservationDate) {
        this.reservationID = reservationID;
        this.itemID = itemID;
        this.isFulfilled = isFulfilled;
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

    public LocalDate getReservationDate() {
        return reservationDate;
    }

    public int getItemID() {
        return itemID;
    }

    public boolean getIsFulfilled() {
        return isFulfilled;
    }

    public void setActive(boolean active) {
        this.isFulfilled = active;
    }

    /**
     * Laddar det reserverade objektet baserat på itemID.
     * 
     * @throws SQLException om ett databasfel uppstår vid hämtning av objektet
     */
    public void loadItem() throws SQLException {
        if (this.getIsFulfilled() == false) {
            GetItemsByID itemFetcher = new GetItemsByID();
            this.item = itemFetcher.getItemById(this.itemID);
        }
    }


    @Override
    public String toString() {
        return (item != null ? "Datum: " + reservationDate.toString() + " -> " + item.toString() : 
                "Det här Objekt är bortaget från vårt sortiment");
    }
}
