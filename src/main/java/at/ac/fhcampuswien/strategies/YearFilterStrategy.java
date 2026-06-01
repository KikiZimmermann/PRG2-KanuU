package at.ac.fhcampuswien.strategies;

import at.ac.fhcampuswien.models.Movie;

public class YearFilterStrategy implements IMovieFilterStrategy {
    private final String year;
    public YearFilterStrategy(String year) { this.year = year; }

    @Override
    public boolean matches(Movie movie) {
        return String.valueOf(movie.getReleaseYear()).contains(year);
    }
}
