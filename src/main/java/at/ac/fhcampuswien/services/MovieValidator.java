package at.ac.fhcampuswien.services;

public class MovieValidator {

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
}
