package at.ac.fhcampuswien.services;

import at.ac.fhcampuswien.exceptions.DatabaseException;
import at.ac.fhcampuswien.exceptions.MovieNotFoundException;
import at.ac.fhcampuswien.interfaces.IMovieReader;
import at.ac.fhcampuswien.interfaces.IMovieWriter;
import at.ac.fhcampuswien.factories.MovieFactory;
import at.ac.fhcampuswien.models.Movie;
import at.ac.fhcampuswien.strategies.GenreFilterStrategy;
import at.ac.fhcampuswien.strategies.IMovieFilterStrategy;
import at.ac.fhcampuswien.strategies.TitleFilterStrategy;
import at.ac.fhcampuswien.strategies.YearFilterStrategy;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class MovieService {
    // public List<Movie> movies;

    private final IMovieWriter moviesWrite;
    private final IMovieReader moviesRead;

    public MovieService(IMovieWriter moviesWrite, IMovieReader moviesRead) {
        this.moviesWrite = moviesWrite;
        this.moviesRead = moviesRead;
    }


    public void ADDMovie(String title, String genre, int releaseYear) throws MovieNotFoundException, DatabaseException {
        Movie newMovie = MovieFactory.create(title, genre, releaseYear);
        if(MovieExists(newMovie)){
            throw new IllegalArgumentException("Movie already exists");
        }
        if(releaseYear < 1980) {
            throw new IllegalArgumentException("Release year can't be earlier than 1980");
        }
        moviesWrite.add(newMovie);
    }
    public void DELETEMovie(String title, String genre, int releaseYear) throws MovieNotFoundException,DatabaseException {
        Movie newMovie = MovieFactory.create(title, genre, releaseYear);
        if(!MovieExists(newMovie)){
            throw new MovieNotFoundException();
        }
        MovieExistsDELETE(newMovie);
    }
    private void MovieExistsDELETE(Movie movie) throws MovieNotFoundException, DatabaseException {
        for (Movie m : moviesRead.findAll()) {
            if (m.getTitle().equalsIgnoreCase(movie.getTitle()) &&
                    m.getGenre().equalsIgnoreCase(movie.getGenre()) &&
                    m.getReleaseYear() == movie.getReleaseYear()) {

                moviesWrite.delete(m);
                return;
            }
        }
        throw new MovieNotFoundException();
    }
    public void UPDATEMovie(String title, String genre, int releaseYear, UUID id) throws MovieNotFoundException, DatabaseException {
        Movie updatedMovies = MovieFactory.createWithId(id, title, genre, releaseYear);
        for (Movie m : moviesRead.findAll()) {
            if (m.getId().equals(id)) {
                if (releaseYear > 1980 && title != null && genre != null && GenreValidator.isValid(genre)) {
                    moviesWrite.update(updatedMovies);
                }
                else{
                    throw new IllegalArgumentException("Update data Not allowed");
                }
            }
        }
    }

    public Movie extractValues(String requestBody) throws JsonSyntaxException{
        try {
            Movie movie = extractValue(requestBody);
            return movie;
        } catch (Exception e) {
            throw new IllegalArgumentException("Invalid movie data");
        }
    }

    public boolean MovieExists(Movie movie) throws DatabaseException{
        if (moviesRead.findAll().stream().anyMatch(m ->
                m.getTitle().equalsIgnoreCase(movie.getTitle()) &&
                m.getGenre().equalsIgnoreCase(movie.getGenre()) &&
                m.getReleaseYear() == movie.getReleaseYear()))
        {
            return true;
        } else {
            return false;
        }
    }

    private Movie extractValue(String json) throws JsonSyntaxException {

        Gson gson = new Gson();

        Movie movie = gson.fromJson(json, Movie.class);

        return movie;

    }

    public String GETMOVIEPARAM(Map<String, String> params) throws DatabaseException {
        List<IMovieFilterStrategy> filters = new ArrayList<>();
        if (params.get("title") != null) {
            filters.add(new TitleFilterStrategy(params.get("title")));
        }
        if (params.get("genre") != null) {
            filters.add(new GenreFilterStrategy(params.get("genre")));
        }
        if (params.get("releaseYear") != null) {
            filters.add(new YearFilterStrategy(params.get("releaseYear")));
        }

        List<Movie> result = new ArrayList<>();

        for (Movie m : moviesRead.findAll()) {
            // Movie must match ALL active filters
            if (filters.stream().allMatch(f -> f.matches(m))) {
                result.add(m);
            }
        }
        return result.toString();
    }



}
