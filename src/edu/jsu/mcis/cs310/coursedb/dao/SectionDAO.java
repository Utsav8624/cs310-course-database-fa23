package edu.jsu.mcis.cs310.coursedb.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class SectionDAO {

    private static final String QUERY_FIND = "SELECT * FROM section WHERE termid=? AND subjectid=? AND num=? ORDER By crn";

    private final DAOFactory daoFactory;

    SectionDAO(DAOFactory daoFactory) {
        this.daoFactory = daoFactory;
    }

    // Method to find sections based on termid, subjectid, and section number
    public String find(int termid, String subjectid, String num) {
        String result = "[]"; // Initialize the result as an empty JSON array

        PreparedStatement ps = null; // Declare PreparedStatement variable
        ResultSet rs = null; // Declare ResultSet variable

        try {
            Connection conn = daoFactory.getConnection(); // Get a database connection

            if (conn.isValid(0)) { // Check if the connection is valid

                ps = conn.prepareStatement(QUERY_FIND); // Prepare the SQL statement

                // Set the values for the parameters in the SQL statement
                ps.setInt(1, termid);
                ps.setString(2, subjectid);
                ps.setString(3, num);

                boolean hasResults = ps.execute(); // Execute the SQL statement and check if it has results

                if (hasResults) {
                    rs = ps.getResultSet(); // Get the ResultSet from the executed SQL statement
                    result = DAOUtility.getResultSetAsJson(rs); // Convert the ResultSet to a JSON string using utility method
                }
            }
        } catch (Exception e) {
            e.printStackTrace(); // Print any exceptions that occur during database operations
        } finally {
            // Close ResultSet and PreparedStatement to prevent memory leaks
            if (rs != null) {
                try {
                    rs.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            if (ps != null) {
                try {
                    ps.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        return result; // Return the JSON string containing the query results (or an empty array if no results)
    }
}