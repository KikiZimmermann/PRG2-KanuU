package at.ac.fhcampuswien;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.DriverManager;

public class DatabaseUtil {

    private static final String JDBC_URL = "jdbc:h2:~/movieDB";
    private static final String USER = "user";
    private static final String PASSWORD = "pw";

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(JDBC_URL, USER, PASSWORD);
    }

    public static void main(String[] args) {

        try (Connection conn = getConnection()) {
            String createTableSQL = """
                         CREATE TABLE IF NOT EXISTS movies (
                      id UUID PRIMARY KEY,
                      title VARCHAR(255) NOT NULL,
                      genre VARCHAR(100) NOT NULL,
                      release_year INT NOT NULL
                     )
                    
                    
                    """;

            try (PreparedStatement pstmt = conn.prepareStatement(createTableSQL)) {
                pstmt.executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

    }
}
