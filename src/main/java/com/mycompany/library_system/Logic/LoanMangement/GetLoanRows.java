package com.mycompany.library_system.Logic.LoanMangement;

import com.mycompany.library_system.Database.ConnDB;
import com.mycompany.library_system.Database.DatabaseConnector;
import com.mycompany.library_system.Login.Session;
import com.mycompany.library_system.Models.LoanRow;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 * Klass som hämtar lånerader (LoanRow) för en inloggad användare.
 * Följer Single Responsibility Principle genom att endast ansvara för databaslogik kopplad till LoanRow.
 * 
 * Författare: Linus, Emil, Oliver, Viggo
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
        ArrayList<LoanRow> loanRowList = new ArrayList<>();

        String query = "SELECT * FROM LoanRow WHERE (ActiveLoan = ?) AND ( LoanID IN "
                + "(SELECT LoanID FROM Loan WHERE CustomerID = ?))";

        try (Connection conn = dbConnector.connect();
             PreparedStatement loanRowStmt = conn.prepareStatement(query)) {

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
                    String loanStartDate = rs.getString("RowLoanStartDate");
                    String loanEndDate = rs.getString("RowLoanEndDate");
                    boolean isActive = rs.getBoolean("ActiveLoan");

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
