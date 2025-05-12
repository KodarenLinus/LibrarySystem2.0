package com.mycompany.library_system.Logic.ItemManagement;

import com.mycompany.library_system.Models.Book;
import com.mycompany.library_system.Models.DVD;
import com.mycompany.library_system.Models.Items;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Factory-klass för att skapa Items (Book eller DVD) baserat på data från databasen.
 */
public class ItemFactory {

    /**
     * Skapar ett Items-objekt från en resultatrad i databasen.
     * Beroende på om objektet är en bok eller DVD returneras ett motsvarande objekt.
     *
     * @param rs   ResultSet som innehåller basinformation om objektet (från en JOIN med kategori/genre).
     * @param conn Öppen databasanslutning för vidare kontroll om objektet är bok eller DVD.
     * @return Ett Book- eller DVD-objekt, eller null om typen inte kunde fastställas.
     * @throws SQLException vid problem med databasanrop.
     */
    public static Items createItemFromResultSet(ResultSet rs, Connection conn) throws SQLException {
        // Grundläggande information som gäller för både böcker och DVD:er
        int id = rs.getInt("itemID");
        String title = rs.getString("title");
        String location = rs.getString("location");
        int categoryID = rs.getInt("CategoryID");
        String categoryName = rs.getString("CategoryName");
        int genreID = rs.getInt("genreID");
        String genreName = rs.getString("genreName");
        

        // Om objektet är en bok, hämta ISBN och skapa Book-objekt
        if (isBook(conn, id)) {
            int isbn = getBookISBN(conn, id);
            int publisherID = getBookPublisherID(conn, id);
            Book book = new Book(title, location, isbn, categoryID, categoryName, genreID, genreName, publisherID);
            book.setItemID(id);
            return book;
        }

        // Om objektet är en DVD, hämta DirectorID och skapa DVD-objekt
        if (isDVD(conn, id)) {
            int directorID = getDVDDirectorID(conn, id);
            DVD dvd = new DVD(title, location, categoryID, categoryName, genreID, genreName, directorID);
            dvd.setItemID(id);
            return dvd;
        }

        // Om objektet inte är varken bok eller DVD, returnera null
        return null;
    }

    /**
     * Kontrollerar om ett objekt är en bok genom att söka i Book-tabellen.
     * 
     * @param conn en databas koppling
     * @param itemId för ett item
     * @return true om den hittar item i book tabelen annars false
     * @throws SQLException
     */
    private static boolean isBook(Connection conn, int itemId) throws SQLException {
        String query = "SELECT 1 FROM Book WHERE itemID = ?";
        try (
            PreparedStatement findBookStmt = conn.prepareStatement(query);
        ) {
            findBookStmt.setInt(1, itemId);
            try (ResultSet rs = findBookStmt.executeQuery()) {
                return rs.next(); // true om posten finns
            }
        }
    }

    /**
     * Hämtar ISBN för en bok baserat på dess itemID.
     * Kastar ett undantag om ingen matchande bok hittas.
     * 
     * @param conn en databas koppling
     * @param itemId bok itemID
     * @return ISBN för en bok
     * @throws SQLException
     */
    private static int getBookISBN(Connection conn, int itemId) throws SQLException {
        String query = "SELECT ISBN FROM Book WHERE itemID = ?";
        try (
            PreparedStatement findISBNforBookStmt = conn.prepareStatement(query);
        ) {
            findISBNforBookStmt.setInt(1, itemId);
            try (ResultSet rs = findISBNforBookStmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("ISBN");
                }
            }
        }
        throw new SQLException("ISBN kunde inte hittas för Book med itemID = " + itemId);
    }
    
     /**
     * Hämtar ISBN för en bok baserat på dess itemID.
     * Kastar ett undantag om ingen matchande bok hittas.
     * 
     * @param conn en databas koppling
     * @param itemId bok itemID
     * @return ISBN för en bok
     * @throws SQLException
     */
    private static int getBookPublisherID(Connection conn, int itemId) throws SQLException {
        String query = "SELECT PublisherID FROM Book WHERE itemID = ?";
        try (
            PreparedStatement findPublisherIDStmt = conn.prepareStatement(query);
        ) {
            findPublisherIDStmt.setInt(1, itemId);
            try (ResultSet rs = findPublisherIDStmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("PublisherID");
                }
            }
        }
        throw new SQLException("ISBN kunde inte hittas för Book med itemID = " + itemId);
    }


    /**
     * Kontrollerar om ett objekt är en DVD genom att söka i DVD-tabellen.
     * 
     * @param conn en databas koppling
     * @param itemId för ett item
     * @return true om den hittar item i dvd tabelen annars false
     * @throws SQLException
     */
    private static boolean isDVD(Connection conn, int itemId) throws SQLException {
        String query = "SELECT 1 FROM DVD WHERE itemID = ?";
        try (
            PreparedStatement findDVDStmt = conn.prepareStatement(query)
        ) {
            findDVDStmt.setInt(1, itemId);
            try (ResultSet rs = findDVDStmt.executeQuery()) {
                return rs.next(); // true om posten finns
            }
        }
    }

    /**
     * Hämtar DirectorID för en DVD baserat på dess itemID.
     * Kastar ett undantag om ingen matchande DVD hittas.
     * 
     * @param conn en databas koppling
     * @param itemId DVD itemID
     * @return DirectorsID för en DVD
     * @throws SQLException
     */
    private static int getDVDDirectorID(Connection conn, int itemId) throws SQLException {
        String query = "SELECT DirectorID FROM DVD WHERE itemID = ?";
        try (
            PreparedStatement findDirectorIDStmt = conn.prepareStatement(query)
        ) {
            findDirectorIDStmt.setInt(1, itemId);
            try (ResultSet rs = findDirectorIDStmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("DirectorID");
                }
            }
        }
        throw new SQLException("DirectorID kunde inte hittas för DVD med itemID = " + itemId);
    }
}
