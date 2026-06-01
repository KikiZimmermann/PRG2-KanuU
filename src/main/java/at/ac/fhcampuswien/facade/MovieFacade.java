package at.ac.fhcampuswien.facade;

import at.ac.fhcampuswien.Repository.MovieReadRepository;
import at.ac.fhcampuswien.exceptions.DatabaseException;
import at.ac.fhcampuswien.exceptions.MovieNotFoundException;
import at.ac.fhcampuswien.interfaces.IMovieReader;
import at.ac.fhcampuswien.models.Movie;
import at.ac.fhcampuswien.services.*;

import java.util.List;
import java.util.Map;
import java.util.UUID;

public class MovieFacade {
    private final MovieService movieService;
    private final MovieValidator movieValidator;
    private final IMovieReader moviesRead;

    public MovieFacade(MovieService movieService, MovieValidator movieValidator, IMovieReader moviesRead) {
        this.movieService = movieService;
        this.movieValidator = movieValidator;
        this.moviesRead = moviesRead;
    }

    public String getAll() throws DatabaseException {
        return moviesRead.findAll().toString();
    }

    public void add(String requestBody) throws Exception {
        movieValidator.validMovie(requestBody);
        Movie movie = movieService.extractValues(requestBody);
        movieService.ADDMovie(movie.getTitle(), movie.getGenre(), movie.getReleaseYear());
    }

    public void delete(String requestBody) throws Exception {
        movieValidator.validMovie(requestBody);
        Movie movie = movieService.extractValues(requestBody);
        movieService.DELETEMovie(movie.getTitle(), movie.getGenre(), movie.getReleaseYear());
    }

    public void update(String requestBody) throws Exception {
        movieValidator.validMovieID(requestBody);
        Movie movie = movieService.extractValues(requestBody);
        movieService.UPDATEMovie(movie.getTitle(), movie.getGenre(), movie.getReleaseYear(), movie.getId());
    }

    public String search(Map<String, String> params) throws Exception {
        return movieService.GETMOVIEPARAM(params);
    }


}
