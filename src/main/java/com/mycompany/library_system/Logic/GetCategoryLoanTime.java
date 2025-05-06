package com.mycompany.library_system.Logic;

import com.mycompany.library_system.Database.ConnDB;
import com.mycompany.library_system.Database.DatabaseConnector;
import com.mycompany.library_system.Models.CategoryType;
import static com.mycompany.library_system.Models.CategoryType.BOOK;
import static com.mycompany.library_system.Models.CategoryType.COURSE_LITERATURE;
import static com.mycompany.library_system.Models.CategoryType.DVD;
import java.sql.Connection;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;

/**
 * Klass för att beräkna lånetid baserat på kategori av ett objekt.
 * Exempelvis: böcker = 1 månad, kurslitteratur = 1 vecka, DVD = 2 veckor.
 * 
 * Förbättringar:
 * - Använder try-with-resources för att stänga databasresurser automatiskt
 * - Felhantering för okända kategori-ID:n
 * - Fixat metodnamn från 'calculatetLoanEndDate' till 'calculateLoanEndDate'
 */
public class GetCategoryLoanTime {

    private final DatabaseConnector dbConnector;

    public GetCategoryLoanTime() {
        this.dbConnector = new ConnDB();
    }

    /**
     * Beräknar lånets slutdatum beroende på objektets kategori.
     *
     * @param itemID ID för objektet
     * @param date Startdatum för lånet
     * @return Uträknat slutdatum för lånet
     * @throws SQLException vid fel i SQL-frågan
     */
    public LocalDate calculateLoanEndDate(int itemID, LocalDate date) throws SQLException {
        String getCategorySQL = "SELECT categoryID FROM item WHERE itemID = ?";
        Connection conn = dbConnector.connect();
        // Använder try-with-resources för att automatiskt stänga resurser
        try (PreparedStatement categoryStmt = conn.prepareStatement(getCategorySQL)) {
            categoryStmt.setInt(1, itemID);

            try (ResultSet rs = categoryStmt.executeQuery()) {
                if (rs.next()) {
                    int categoryID = rs.getInt("categoryID");

                    // Använder enum för att mappa från ID till kategori
                    CategoryType categoryType = CategoryType.fromId(categoryID);

                    if (categoryType == null) {
                        // Om okänd kategori, logga och returnera originaldatum
                        System.err.println("Okänt kategori-ID: " + categoryID);
                        return date;
                    }

                    // Beräkna lånetid baserat på kategori
                    switch (categoryType) {
                        case BOOK:
                            return date.plusMonths(1);
                        case COURSE_LITERATURE:
                            return date.plusWeeks(1);
                        case DVD:
                            return date.plusWeeks(2);
                        default:
                            return date; // Fallback
                    }
                }
            }
        }

        // Om inget resultat hittades i databasen, returnera originaldatum
        return date;
    }
}
