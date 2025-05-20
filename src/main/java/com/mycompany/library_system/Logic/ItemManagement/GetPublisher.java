package com.mycompany.library_system.Logic.ItemManagement;

import com.mycompany.library_system.Database.ConnDB;
import com.mycompany.library_system.Database.DatabaseConnector;
import com.mycompany.library_system.Models.Publisher;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 * Klass för att hämta publishers från databasen.
 * Hämtar alla publishers från Publisher-tabellen.
 * 
 * @author Emil, Oliver, Viggo, Linus
 */
public class GetPublisher {
    private final DatabaseConnector dbConnector;

    public GetPublisher() {
        this.dbConnector = new ConnDB();
    }

    /**
     * Hämtar alla publishers från databasen.
     *
     * @return En lista med Publisher-objekt
     * @throws SQLException Om databasförfrågan misslyckas
     */
    public ArrayList<Publisher> getAllPublishers() throws SQLException {
        // En ArrayList för våra publishers
        ArrayList<Publisher> publisherList = new ArrayList<>();
        
        //En SQL-Fråga för att hämta alla publishers
        String getAllPublishersQuery = "SELECT * FROM Publisher";

        try (
            Connection conn = dbConnector.connect();
            PreparedStatement findPublisherStmt = conn.prepareStatement(getAllPublishersQuery);
            ResultSet rs = findPublisherStmt.executeQuery()
        ) {
            // Loppar igenom vårt resultset och lägger in publishers i vår ArrayList
            while (rs.next()) {
                int publisherID = rs.getInt("PublisherID");
                String compName = rs.getString("compName");
                String email = rs.getString("email");
                String adress = rs.getString("adress");

                Publisher publisher = new Publisher(compName, email, adress);
                publisher.setPublisherID(publisherID);
                publisherList.add(publisher);
            }
        }

        return publisherList;
    }
}
