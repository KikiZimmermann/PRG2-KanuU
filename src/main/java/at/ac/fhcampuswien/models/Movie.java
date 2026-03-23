package at.ac.fhcampuswien.models;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Movie {

    private UUID id; // datatype storing Universally Unique Identifiers
    private String title; // movie title
    private String genre;
    private int releaseYear;

    // Constructors

    // Default constructor -- automatically generate unique UUID
    public Movie() {
        this.id = UUID.randomUUID();
    }

    // Accept arguments title + genre + releaseYear; automatically generate ID
    public Movie(String title, String genre, int releaseYear) {
        this.id = UUID.randomUUID();
        this.title = title;
        this.genre = genre;
        this.releaseYear = releaseYear;
    }

    // Getter + Setter

    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getGenre() { return genre; }
    public void setGenre(String genre) { this.genre = genre; }

    public int getReleaseYear() { return releaseYear; }
    public void setReleaseYear(int releaseYear) { this.releaseYear = releaseYear; }

    // Override of toString() to return formatted string representation of movie

    @Override
    public String toString() {
        return "Movie={id=" + id + ", title='" + title + "', genre='" + genre + "', releaseYear=" + releaseYear + "}";
    }

    // Method generateDummyMovies() that returns LIST of 20 Movie obhects with random titles/genres/years

    public static List<Movie> generateDummyMovies() {
     List<Movie> movies = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            movies.add(new Movie(
                    // outstanding
            ));
        }
        return movies;
    }

}
