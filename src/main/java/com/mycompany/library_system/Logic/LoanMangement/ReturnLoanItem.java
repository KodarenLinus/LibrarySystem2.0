package com.mycompany.library_system.Logic.LoanMangement;

import com.mycompany.library_system.Database.ConnDB;
import com.mycompany.library_system.Database.DatabaseConnector;
import com.mycompany.library_system.Models.LoanRow;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 * ReturnLoanItem hanterar återlämning av lånade objekt.
 * Denna klass uppdaterar både låneraden och objektets tillgänglighetsstatus.
 * 
 * 
 * @author Linus, Emil, Oliver, Viggo
 */
public class ReturnLoanItem {

    private final DatabaseConnector dbConnector;

    public ReturnLoanItem() {
        this.dbConnector = new ConnDB();
    }

    /**
     * Återlämnar en lista av lån genom att markera dem som inaktiva
     * och uppdatera tillhörande item som tillgängliga.
     *
     * @param loanRows Lista med LoanRow-objekt som ska återlämnas
     */
    public void returnItem(ArrayList<LoanRow> loanRows) {
        String updateLoanRow = "UPDATE LoanRow SET ActiveLoan = false WHERE LoanRowID = ?";
        String updateItem = "UPDATE Item SET Available = true WHERE ItemID = ?";

        try (
            Connection conn = dbConnector.connect();
            PreparedStatement loanRowStmt = conn.prepareStatement(updateLoanRow);
            PreparedStatement itemStmt = conn.prepareStatement(updateItem);
        ) {

            for (LoanRow loanRow : loanRows) {
                loanRowStmt.setInt(1, loanRow.getLoanRowID());
                itemStmt.setInt(1, loanRow.getItemID());

                loanRowStmt.executeUpdate();
                itemStmt.executeUpdate();
            }

        } catch (SQLException ex) {
            ex.printStackTrace(); // I produktion bör detta loggas på ett säkrare sätt
        }
    }
}
