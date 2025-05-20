package com.mycompany.library_system.Logic.LoanMangement;

import com.mycompany.library_system.Database.ConnDB;
import com.mycompany.library_system.Database.DatabaseConnector;
import com.mycompany.library_system.Login.Session;
import com.mycompany.library_system.Models.LoanRow;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;

/**
 * Klass som hämtar lånerader (LoanRow) för en inloggad användare.
 * Följer Single Responsibility Principle genom att endast ansvara för databaslogik kopplad till LoanRow.
 * 
 * @author Linus, Emil, Oliver, Viggo
 */
public class GetLoanRows {
    private final DatabaseConnector dbConnector;

    public GetLoanRows() {
        this.dbConnector = new ConnDB();
    }

    /**
     * Hämtar alla lånerader (LoanRow) som tillhör den inloggade användaren.
     * Du kan välja att endast hämta aktiva eller inaktiva lån.
     *
     * @param activeLoans true för att hämta aktiva lån, false för inaktiva
     * @return lista med LoanRow-objekt
     * @throws SQLException om något går fel med databasåtkomst
     */
    public ArrayList<LoanRow> getAllLoanRows(boolean activeLoans) throws SQLException {
        // En ArrayLista som vi spara våra LoanRow i
        ArrayList<LoanRow> loanRowList = new ArrayList<>();
        
        // En SQL-Fråga för att hämta LoanRows
        String getLoanRowQuery = "SELECT * FROM LoanRow WHERE (ActiveLoan = ?) AND ( LoanID IN "
                + "(SELECT LoanID FROM Loan WHERE CustomerID = ?))";

        try (
            Connection conn = dbConnector.connect();
            PreparedStatement loanRowStmt = conn.prepareStatement(getLoanRowQuery)
        ) {

            // Sätter parametrar i SQL-frågan
            loanRowStmt.setBoolean(1, activeLoans);
            loanRowStmt.setInt(2, Session.getInstance().getUserId());

            // Utför frågan
            try (ResultSet rs = loanRowStmt.executeQuery()) {
                while (rs.next()) {
                    // Extraherar data från resultatraden
                    int loanRowID = rs.getInt("LoanRowID");
                    int loanID = rs.getInt("LoanID");
                    int itemID = rs.getInt("ItemID");
                    Date sqlStartDate = rs.getDate("RowLoanStartDate");
                    Date sqlEndDate = rs.getDate("RowLoanEndDate");
                    boolean isActive = rs.getBoolean("ActiveLoan");
                    
                    LocalDate loanStartDate = sqlStartDate.toLocalDate();
                    LocalDate loanEndDate = sqlEndDate.toLocalDate();

                    // Skapar LoanRow-objekt och lägger till i listan
                    LoanRow loanRow = new LoanRow(loanID, itemID, loanStartDate, loanEndDate, isActive);
                    loanRow.setLoanRowID(loanRowID);
                    loanRowList.add(loanRow);
                }
            }
        }

        return loanRowList;
    }
}
