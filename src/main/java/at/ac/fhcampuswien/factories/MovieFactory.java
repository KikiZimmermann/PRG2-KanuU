package at.ac.fhcampuswien.factories;

import at.ac.fhcampuswien.models.Movie;
import java.util.UUID;

public class MovieFactory {
    public static Movie create(String title, String genre, int releaseYear) {
        return new Movie(title, genre, releaseYear);
    }
    public static Movie createWithId(UUID id, String title, String genre, int releaseYear) {
        return new Movie(id, title, genre, releaseYear);
    }
}
