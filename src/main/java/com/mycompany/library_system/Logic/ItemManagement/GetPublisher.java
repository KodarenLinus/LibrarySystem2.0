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
        ArrayList<Publisher> publisherList = new ArrayList<>();

        String sql = "SELECT * FROM Publisher";

        try (
            Connection conn = dbConnector.connect();
            PreparedStatement findPublisherStmt = conn.prepareStatement(sql);
            ResultSet rs = findPublisherStmt.executeQuery()
        ) {
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
