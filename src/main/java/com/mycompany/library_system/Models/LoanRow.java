package com.mycompany.library_system.Models;


import com.mycompany.library_system.Logic.ItemManagement.GetItemsByID;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Objects;

/**
 * En LoanRow klass som vi använder när vi hämtar och skall lägga in LoanRows i databasen
 * 
 * @author Linus, Emil, Oliver, Viggo
 */
public class LoanRow {
    private int loanRowID;
    private final int itemID;
    private final int loanID;
    private final LocalDate loanRowStartDate;
    private final LocalDate loanRowEndDate;
    private final boolean activeLoan;
    private Items item;

    public LoanRow(int loanID, int itemID, LocalDate loanRowStartDate, LocalDate loanRowEndDate, boolean activeLoan) {
        this.loanID = loanID;
        this.itemID = itemID;
        this.loanRowStartDate = loanRowStartDate;
        this.loanRowEndDate = loanRowEndDate;
        this.activeLoan = activeLoan;
    }

    public void setLoanRowID(int loanRowID) {
        this.loanRowID = loanRowID;
    }

    public int getLoanRowID() {
        return loanRowID;
    }

    public int getLoanID() {
        return loanID;
    }

    public int getItemID() {
        return itemID;
    }

    public LocalDate getLoanRowStartDate() {
        return loanRowStartDate;
    }

    public LocalDate getLoanRowEndDate() {
        return loanRowEndDate;
    }

    public boolean isActiveLoan() {
        return activeLoan;
    }
    
    /**
     * Hämtar objektet från databasen baserat på itemID och sparar det i fältet {@code item}.
     *
     * @throws SQLException om databaskopplingen eller frågan misslyckas
     */
    public void loadItem() throws SQLException {
        this.item = new GetItemsByID().getItemById(this.itemID);
    }

    public Items getItem() {
        return item;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        LoanRow other = (LoanRow) obj;
        return loanRowID != 0 ? loanRowID == other.loanRowID : loanID == other.loanID && itemID == other.itemID;
    }

    @Override
    public int hashCode() {
        return loanRowID != 0 ? Integer.hashCode(loanRowID) : Objects.hash(loanID, itemID);
    }

    @Override
    public String toString() {
        return loanRowStartDate + " -> " + loanRowEndDate + ": " + (item != null ? item.toString() : "Item not loaded");
    }
}
