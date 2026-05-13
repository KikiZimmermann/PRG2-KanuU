package at.ac.fhcampuswien.services;

import at.ac.fhcampuswien.DatabaseUtil;
import at.ac.fhcampuswien.exceptions.MovieNotFoundException;
import at.ac.fhcampuswien.models.Movie;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static at.ac.fhcampuswien.DatabaseUtil.getConnection;

public class MovieRepository {

    // Connection.prepareStatement():

    public void add(Movie movie) {

        try (Connection conn = getConnection()) {
            String insertSQL = """
                    INSERT INTO movies (id, title, genre, releaseYear) VALUES (?, ?, ?, ?)      
                    """;

            try (PreparedStatement pstmt = conn.prepareStatement(insertSQL)) {
                pstmt.setObject(1, movie.getId());
                pstmt.setString(2, movie.getTitle());
                pstmt.setString(3, movie.getGenre());
                pstmt.setInt(4, movie.getReleaseYear());
                pstmt.executeUpdate();

            } catch (SQLException e) {
                e.printStackTrace();
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<Movie> findAll() {
        String sql = "SELECT * FROM movies;";
        List<Movie> movies = new ArrayList<>();

        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                UUID id = UUID.fromString(rs.getString("id"));
                String title = rs.getString("title");
                String genre = rs.getString("genre");
                int releaseYear = rs.getInt("releaseYear");
                movies.add(new Movie(id, title, genre, releaseYear));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return movies;
    }

    public boolean delete(Movie movie) throws MovieNotFoundException {

        int success = 0;

        try (Connection conn = getConnection()) {
            String deleteSQL = """
                     DELETE FROM movies WHERE title = ? AND genre = ? AND releaseYear = ?
                    """;

            try (PreparedStatement pstmt = conn.prepareStatement(deleteSQL)) {
                pstmt.setString(1, movie.getTitle());
                pstmt.setString(2, movie.getGenre());
                pstmt.setInt(3, movie.getReleaseYear());
                success = pstmt.executeUpdate();

            } catch (SQLException e) {
                e.printStackTrace();
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    if (success == 1) {
        return true;
    } else {
        throw new MovieNotFoundException();
    }
    }

    public boolean update(Movie movie) throws MovieNotFoundException {

        int success = 0;

        try (Connection conn = getConnection()) {
            String updateSQL = """
                      UPDATE movies SET title = ?,genre = ?, releaseYear = ? WHERE id = ?
                    """;

            try (PreparedStatement pstmt = conn.prepareStatement(updateSQL)) {
                pstmt.setString(1, movie.getTitle());
                pstmt.setString(2, movie.getGenre());
                pstmt.setInt(3, movie.getReleaseYear());
                pstmt.setObject(4, movie.getId());
                success = pstmt.executeUpdate();

            } catch (SQLException e) {
                e.printStackTrace();
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        if (success == 1) {
            return true;
        } else {
            throw new MovieNotFoundException();
        }

    }

}
