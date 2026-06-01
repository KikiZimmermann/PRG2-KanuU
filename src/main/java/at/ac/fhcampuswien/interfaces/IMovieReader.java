package at.ac.fhcampuswien.interfaces;

import at.ac.fhcampuswien.exceptions.DatabaseException;
import at.ac.fhcampuswien.models.Movie;

import java.util.List;

public interface IMovieReader {
    List<Movie> findAll() throws DatabaseException;
}
