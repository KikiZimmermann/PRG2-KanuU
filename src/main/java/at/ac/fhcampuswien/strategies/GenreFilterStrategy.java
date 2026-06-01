package at.ac.fhcampuswien.strategies;

import at.ac.fhcampuswien.models.Movie;

public class GenreFilterStrategy implements IMovieFilterStrategy {
    private final String genre;
    public GenreFilterStrategy(String genre) { this.genre = genre; }

    @Override
    public boolean matches(Movie movie) {
        return movie.getGenre().toLowerCase().contains(genre.toLowerCase());
    }
}

