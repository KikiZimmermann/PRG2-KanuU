package at.ac.fhcampuswien.Repository;

import at.ac.fhcampuswien.DatabaseUtil;
import at.ac.fhcampuswien.exceptions.DatabaseException;
import at.ac.fhcampuswien.interfaces.IMovieReader;
import at.ac.fhcampuswien.models.Movie;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class MovieReadRepository implements IMovieReader {
    public List<Movie> findAll() throws DatabaseException {
        String sql = "SELECT * FROM movies;";
        List<Movie> movies = new ArrayList<>();

        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            try {
                while (rs.next()) {
                    UUID id = UUID.fromString(rs.getString("id"));
                    String title = rs.getString("title");
                    String genre = rs.getString("genre");
                    int releaseYear = rs.getInt("releaseYear");
                    movies.add(new Movie(id, title, genre, releaseYear));
                }
            }catch (SQLException e) {
                e.printStackTrace();
                throw new DatabaseException("Failed to find all movies");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DatabaseException("Failed connect to Data Base");
        }

        return movies;
    }

}
