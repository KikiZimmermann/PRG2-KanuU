package at.ac.fhcampuswien.strategies;

import at.ac.fhcampuswien.models.Movie;

public interface IMovieFilterStrategy {
    boolean matches(Movie movie);
}
