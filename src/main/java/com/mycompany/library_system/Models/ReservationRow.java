package com.mycompany.library_system.Models;

import com.mycompany.library_system.Logic.ItemManagement.GetItemsByID;
import java.sql.SQLException;
import java.time.LocalDate;

/**
 * Representerar en rad i en reservation, som kopplar ett objekt till en specifik reservation.
 * Innehåller information om objektets ID, reservationsdatum och om reservationen är uppfylld.
 * 
 * <p>Objektinformationen kan laddas med hjälp av {@code loadItem()}-metoden.</p>
 * 
 * @author Linus, Emil, Oliver, Viggo
 */
public class ReservationRow {

    /** Unikt ID för reservationsraden (vanligtvis sätts av databasen). */
    private int reservationRowID;

    /** ID för den reservation denna rad tillhör. */
    private int reservationID;

    /** ID för objektet som reserveras. */
    private int itemID;

    /** Anger om reservationen har uppfyllts (true = uppfylld, false = ej uppfylld). */
    private boolean isFulfilled;

    /** Objektet som är reserverat (kan laddas separat). */
    private Items item;

    /** Datum då reservationen gjordes. */
    private LocalDate reservationDate;

    /**
     * Skapar ett nytt {@code ReservationRow}-objekt.
     *
     * @param reservationID ID för den övergripande reservationen
     * @param itemID ID för objektet som reserveras
     * @param isFulfilled om reservationen har uppfyllts
     * @param reservationDate datumet då reservationen gjordes
     */
    public ReservationRow(int reservationID, int itemID, boolean isFulfilled, LocalDate reservationDate) {
        this.reservationID = reservationID;
        this.itemID = itemID;
        this.isFulfilled = isFulfilled;
        this.reservationDate = reservationDate;
    }

    /**
     * Hämtar ID för denna reservationsrad.
     *
     * @return reservationsradens ID
     */
    public int getReservationRowID() {
        return reservationRowID;
    }

    /**
     * Sätter ID för denna reservationsrad.
     *
     * @param reservationRowID nytt ID för reservationsraden
     */
    public void setReservationRowID(int reservationRowID) {
        this.reservationRowID = reservationRowID;
    }

    /**
     * Hämtar ID för den tillhörande reservationen.
     *
     * @return reservationens ID
     */
    public int getReservationID() {
        return reservationID;
    }

    /**
     * Sätter ID för den tillhörande reservationen.
     *
     * @param reservationID nytt reservations-ID
     */
    public void setReservationID(int reservationID) {
        this.reservationID = reservationID;
    }

    /**
     * Hämtar datumet då reservationen gjordes.
     *
     * @return reservationsdatumet
     */
    public LocalDate getReservationDate() {
        return reservationDate;
    }

    /**
     * Sätter datumet för reservationen.
     *
     * @param reservationDate nytt datum
     */
    public void setReservationDate(LocalDate reservationDate) {
        this.reservationDate = reservationDate;
    }

    /**
     * Hämtar ID för det reserverade objektet.
     *
     * @return objektets ID
     */
    public int getItemID() {
        return itemID;
    }

    /**
     * Sätter ID för det reserverade objektet.
     *
     * @param itemID nytt objekt-ID
     */
    public void setItemID(int itemID) {
        this.itemID = itemID;
    }

    /**
     * Returnerar om reservationen är uppfylld.
     *
     * @return true om reservationen är uppfylld, annars false
     */
    public boolean getIsFulfilled() {
        return isFulfilled;
    }

    /**
     * Sätter om reservationen är aktiv/uppfylld.
     *
     * @param active true för uppfylld, false för ej uppfylld
     */
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

    /**
     * Returnerar en strängrepresentation av reservationsraden.
     *
     * @return en sträng med datum och objektinformation
     */
    @Override
    public String toString() {
        return (item != null ? "Datum: " + reservationDate.toString() + " -> " + item.toString() : 
                "Det här Objekt är bortaget från vårt sortiment");
    }
}
