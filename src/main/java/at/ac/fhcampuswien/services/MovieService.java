package at.ac.fhcampuswien.services;

import at.ac.fhcampuswien.ApiUtils;
import at.ac.fhcampuswien.exceptions.DatabaseException;
import at.ac.fhcampuswien.exceptions.MovieNotFoundException;
import at.ac.fhcampuswien.models.Movie;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class MovieService {
    // public List<Movie> movies;

    MovieRepository movies;

    public MovieService(MovieRepository movies) {
        this.movies = movies;
    }


    public void ADDMovie(String title, String genre, int releaseYear) throws MovieNotFoundException, DatabaseException {
        Movie newMovie = new Movie(title, genre, releaseYear);
        if(MovieExists(newMovie)){
            throw new IllegalArgumentException("Movie already exists");
        }
        if(releaseYear < 1980) {
            throw new IllegalArgumentException("Release year can't be earlier than 1980");
        }
        movies.add(newMovie);
    }
    public void DELETEMovie(String title, String genre, int releaseYear) throws MovieNotFoundException,DatabaseException {
        Movie newMovie = new Movie(title, genre, releaseYear);
        if(!MovieExists(newMovie)){
            throw new MovieNotFoundException();
        }
        MovieExistsDELETE(newMovie);
    }
    private void MovieExistsDELETE(Movie movie) throws MovieNotFoundException, DatabaseException {
        for (Movie m : movies.findAll()) {
            if (m.getTitle().equalsIgnoreCase(movie.getTitle()) &&
                    m.getGenre().equalsIgnoreCase(movie.getGenre()) &&
                    m.getReleaseYear() == movie.getReleaseYear()) {

                movies.delete(m);
                return;
            }
        }
        throw new MovieNotFoundException();
    }
    public void UPDATEMovie(String title, String genre, int releaseYear, UUID id) throws MovieNotFoundException, DatabaseException {
        Movie updatedMovies = new Movie(id, title, genre, releaseYear);
        for (Movie m : movies.findAll()) {
            if (m.getId().equals(id)) {
                if (releaseYear > 1980 && title != null && genre != null && ( genre.equals("Action") || genre.equals("Drama")|| genre.equals("Comedy")|| genre.equals("Sci-Fi")|| genre.equals("Horror")|| genre.equals("Thriller"))) {
                    movies.update(updatedMovies);
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

    public boolean MovieExists(Movie movie) throws MovieNotFoundException, DatabaseException{
        if (movies.findAll().stream().anyMatch(m ->
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

    public String GETMOVIEPARAM(Map<String, String> params) throws DatabaseException{
        String title = params.get("title");
        String genre = params.get("genre");
        String releaseYear = params.get("releaseYear");

        List<Movie> result = new ArrayList<>();

        for (Movie m : movies.findAll()) {

            boolean matches = true;

            if (title != null && !m.getTitle().toLowerCase().contains(title.toLowerCase())) {
                matches = false;
            }

            if (genre != null && !m.getGenre().toLowerCase().contains(genre.toLowerCase())) {
                matches = false;
            }

            if (releaseYear != null &&
                    !String.valueOf(m.getReleaseYear()).contains(releaseYear)) {
                matches = false;
            }

            if (matches) {
                result.add(m);
            }
        }

        return result.toString();
    }



}
