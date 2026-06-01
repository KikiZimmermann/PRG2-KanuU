package at.ac.fhcampuswien.Repository;

import at.ac.fhcampuswien.exceptions.DatabaseException;
import at.ac.fhcampuswien.exceptions.MovieNotFoundException;
import at.ac.fhcampuswien.interfaces.IMovieWriter;
import at.ac.fhcampuswien.models.Movie;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import static at.ac.fhcampuswien.DatabaseUtil.getConnection;

public class MovieWriteRepository implements IMovieWriter {

    // Connection.prepareStatement():

    public void add(Movie movie) throws DatabaseException {

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

            }catch (SQLException e) {
                e.printStackTrace();
                throw new DatabaseException("Failed to add movie");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DatabaseException("Failed to connect to data base");
        }
    }

    public boolean delete(Movie movie) throws MovieNotFoundException, DatabaseException {

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
                throw new DatabaseException("Failed to delete movie");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DatabaseException("Failed to connect to data base");
        }
    if (success == 1) {
        return true;
    } else {
        throw new MovieNotFoundException();
    }
    }

    public boolean update(Movie movie) throws MovieNotFoundException,DatabaseException {

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
                throw new DatabaseException("Failed to update movie");
            }

        } catch (SQLException e) {
            e.printStackTrace();
            throw new DatabaseException("Failed to connect to Database");
        }
        if (success == 1) {
            return true;
        } else {
            throw new MovieNotFoundException();
        }

    }

}
