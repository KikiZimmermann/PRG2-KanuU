package at.ac.fhcampuswien.services;

import at.ac.fhcampuswien.ApiUtils;
import at.ac.fhcampuswien.models.Movie;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class MovieService {
    public List<Movie> movies;

    public MovieService(List<Movie> movies) {
        this.movies = movies;
    }


    public void ADDMovie(String title, String genre, int releaseYear){
        Movie newMovie = new Movie(title, genre, releaseYear);
        if(MovieExists(newMovie)){
            throw new IllegalArgumentException("Movie already exists");
        }
        movies.add(newMovie);
    }
    public void DELETEMovie(String title, String genre, int releaseYear){
        Movie newMovie = new Movie(title, genre, releaseYear);
        if(!MovieExists(newMovie)){
            throw new IllegalArgumentException("Movie doesn't exist");
        }
        MovieExistsDELETE(newMovie);
    }
    private void MovieExistsDELETE(Movie Movie){
        for (Movie m : movies) {
            if (m.getTitle().equalsIgnoreCase(Movie.getTitle()) &&
                    m.getGenre().equalsIgnoreCase(Movie.getGenre()) &&
                    m.getReleaseYear() == Movie.getReleaseYear()) {

                movies.remove(m);
                return;
            }
        }

        throw new IllegalArgumentException("Movie not found");

    }
    public void UPDATEMovie(String title, String genre, int releaseYear, UUID id){
        for (Movie m : movies) {
            if (m.getId().equals(id)) {
                if (title != null) {
                    m.setTitle(title);
                }
                if (genre != null && ( genre.equals("Action") || genre.equals("Drama")|| genre.equals("Comedy")|| genre.equals("Sci-Fi")|| genre.equals("Horror")|| genre.equals("Thriller"))) {
                    m.setGenre(genre);
                }
                if (releaseYear > 1980) {
                    m.setReleaseYear(releaseYear);
                }
            }
        }
    }


    public Movie extractValues(String requestBody){
        try {
            String title = extractValue(requestBody, "title");
            String genre = extractValue(requestBody, "genre");
            int releaseYear = Integer.parseInt(extractValue(requestBody, "releaseYear"));
            return new Movie(title, genre, releaseYear);
        } catch (Exception e) {
            throw new IllegalArgumentException("Invalid movie data");
        }
    }

    public boolean MovieExists(Movie Movie){
        return movies.stream().anyMatch(m ->
                m.getTitle().equalsIgnoreCase(Movie.getTitle()) &&
                m.getGenre().equalsIgnoreCase(Movie.getGenre()) &&
                m.getReleaseYear() == Movie.getReleaseYear()
        );
    }

    public void validMovie(String requestBody){
        if (!requestBody.contains("title") ||
                !requestBody.contains("genre") ||
                !requestBody.contains("releaseYear")) {
            throw new IllegalArgumentException("Invalid movie data");
        }
    }
    public void validMovieID(String requestBody){
        if (!requestBody.contains("id")) {
            throw new IllegalArgumentException("Invalid movie data");
        }
    }

    private String extractValue(String json, String key) {
        String value = json.split("\"" + key + "\"\\s*:\\s*")[1];

        if (value.startsWith("\"")) {
            return value.split("\"")[1];
        } else {
            return value.split("[,}]")[0].trim();
        }
    }

    public String GETMOVIEPARAM(Map<String, String> params){
        String title = params.get("title");
        String genre = params.get("genre");
        String releaseYear = params.get("releaseYear");

        List<Movie> result = new ArrayList<>();

        for (Movie m : movies) {

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
