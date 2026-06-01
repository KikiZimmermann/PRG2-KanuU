package at.ac.fhcampuswien.strategies;

import at.ac.fhcampuswien.models.Movie;

public class TitleFilterStrategy implements IMovieFilterStrategy {
    private final String title;
    public TitleFilterStrategy(String title) { this.title = title; }

    @Override
    public boolean matches(Movie movie) {
        return movie.getTitle().toLowerCase().contains(title.toLowerCase());
    }
}
