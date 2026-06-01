package at.ac.fhcampuswien.interfaces;

import at.ac.fhcampuswien.exceptions.DatabaseException;
import at.ac.fhcampuswien.exceptions.MovieNotFoundException;
import at.ac.fhcampuswien.models.Movie;


public interface IMovieWriter {
    void add(Movie movie) throws DatabaseException;
    boolean delete(Movie movie) throws MovieNotFoundException, DatabaseException;
    boolean update(Movie movie) throws MovieNotFoundException, DatabaseException;
}
