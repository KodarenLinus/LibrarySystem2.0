/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.library_system.Logic;

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
 *
 * @author Linus
 */
public class GetCategoryLoanTime {
      /**
    * Beräknar lånets slutdatum beroende på objektets kategori.
    * T.ex. böcker lånas i en månad, filmer i en vecka, etc.
    *
    * @param categoryStmt Förberedd SQL-fråga för att hämta kategoriID
    * @param itemID ID för objektet
    * @param date Startdatum för lånet
    * @return Uträknat slutdatum för lånet
    * @throws SQLException vid fel i SQL-frågan
    */
    public LocalDate calculatetLoanEndDate(Connection conn, int itemID, LocalDate date) throws SQLException {
        
        String getCategorySQL = "SELECT categoryID FROM item WHERE itemID = ?";
        PreparedStatement categoryStmt = conn.prepareStatement(getCategorySQL);
        categoryStmt.setInt(1, itemID);
        ResultSet rs = categoryStmt.executeQuery();

        if (rs.next()) {
            int categoryID = rs.getInt("categoryID");
            CategoryType categoryType = CategoryType.fromId(categoryID);

            switch (categoryType) {
                case BOOK: 
                    return date.plusMonths(1);                    
                case COURSE_LITERATURE: 
                    return date.plusWeeks(1);
                case DVD: 
                    return date.plusWeeks(2);
                default: 
                    return date;
            }
        }
        return date;
    }
}
