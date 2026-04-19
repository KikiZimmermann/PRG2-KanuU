package at.ac.fhcampuswien.services;

import at.ac.fhcampuswien.models.Movie;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

public class MovieServiceTest {

    private MovieService movieService;
    private List<Movie> movies;
    private Movie movie1;
    private Movie movie2;

    @BeforeEach
    void setUp() {
        movie1 = new Movie("Inception", "Sci-Fi", 2010);
        movie2 = new Movie("Titanic", "Drama", 1997);

        movies = new ArrayList<>();
        movies.add(movie1);
        movies.add(movie2);

        movieService = new MovieService(movies);
    }

    @Test
    void givenNewMovie_whenValidMovie_thenMovieAddedToList() {

        // Given
        String newTitle = "Lord of the Rings";
        String genre = "Fantasy";
        int releaseYear = 2001;

        // When
        movieService.ADDMovie(newTitle, genre, releaseYear);

        // Then
        assertEquals(3, movies.size());
    }

    @Test
    void givenNewMovie_whenAlreadyExists_thenThrowException() {

        // Given
        String newTitle = "Inception";
        String genre = "Sci-Fi";
        int releaseYear = 2010;

        // When + Then
        assertThrows(IllegalArgumentException.class, () ->
                movieService.ADDMovie(newTitle, genre, releaseYear));
    }

    @Test
    void givenWithInvalidReleaseYear_whenAddMovie_thenThrowIllegalArgumentException() {
        // Given
        String newTitle = "Lord of the Rings";
        String genre = "Fantasy";
        int releaseYear = 1978;

        // When + Then
        assertThrows(IllegalArgumentException.class, () ->
                movieService.ADDMovie(newTitle, genre, releaseYear)
        );
    }

    @Test
    void givenMovie_whenExistingMovie_thenDeleteMovie() {

        // Given
        String newTitle = "Inception";
        String genre = "Sci-Fi";
        int releaseYear = 2010;

        // When
        movieService.DELETEMovie(newTitle, genre, releaseYear);

        // Then
        assertEquals(1, movies.size());
    }

    @Test
    void givenMovie_whenNonExistingMovie_thenThrowException() {

        // Given
        String newTitle = "Lord of the Rings";
        String genre = "Fantasy";
        int releaseYear = 2001;

        // When + Then
        assertThrows(IllegalArgumentException.class, () ->
                movieService.DELETEMovie(newTitle, genre, releaseYear));
    }



    @Test
    void givenExistingMovieId_whenUpdateTitle_thenTitleIsUpdated() {
        // Given
        UUID id = movie1.getId();
        String newTitle = "Inception Updated";
        String genre = "Sci-Fi";
        int releaseYear = 2010;

        // When
        movieService.UPDATEMovie(newTitle, genre, releaseYear, id);

        // Then
        assertEquals("Inception Updated", movie1.getTitle());
    }

    @Test
    void givenExistingMovieId_whenUpdateGenre_thenGenreIsUpdated() {
        // Given
        UUID id = movie1.getId();
        String newGenre = "Action";
        int releaseYear = 2010;

        // When
        movieService.UPDATEMovie(null, newGenre, releaseYear, id);

        // Then
        assertEquals("Action", movie1.getGenre());
    }

    @Test
    void givenExistingMovieId_whenUpdateRelease_thenReleaseYearIsUpdated() {
        // Given
        UUID id = movie1.getId();
        int newReleaseYear = 2011;

        // When
        movieService.UPDATEMovie(null, null, newReleaseYear, id);

        // Then
        assertEquals(2011, movie1.getReleaseYear());
    }

    @Test
    void givenExistingMovieId_whenInvalidRelease_thenThrowException() {
        // Given
        UUID id = movie1.getId();
        String newTitle = "Lord of the Rings";
        String genre = "Fantasy";
        int newReleaseYear = 1979;

        // When
        movieService.UPDATEMovie(newTitle, genre, newReleaseYear, id);

        // Then
        assertNotEquals(newReleaseYear, movie1.getReleaseYear());
    }

    @Test
    void givenMovieWithSameValues_whenMovieExists_thenReturnTrue() {
        // Given
        Movie testMovie = new Movie("Inception", "Sci-Fi", 2010);

        // When
        boolean result = movieService.MovieExists(testMovie);

        // Then
        assertTrue(result);
    }

    @Test
    void givenMovieWithDifferentValues_whenMovieExists_thenReturnFalse() {
        // Given
        Movie testMovie = new Movie("Avatar", "Sci-Fi", 2009);

        // When
        boolean result = movieService.MovieExists(testMovie);

        // Then
        assertFalse(result);
    }

    @Test
    void givenValidMovieJson_whenValidMovie_thenNoExceptionIsThrown() {
        // Given
        String requestBody = """
                {
                  "title": "Avatar",
                  "genre": "Sci-Fi",
                  "releaseYear": 2009
                }
                """;

        // When + Then
        assertDoesNotThrow(() -> movieService.validMovie(requestBody));
    }

    @Test
    void givenJsonWithoutTitle_whenValidMovie_thenThrowIllegalArgumentException() {
        // Given
        String requestBody = """
                {
                  "genre": "Sci-Fi",
                  "releaseYear": 2009
                }
                """;

        // When + Then
        assertThrows(IllegalArgumentException.class, () ->
                movieService.validMovie(requestBody)
        );
    }

    @Test
    void givenJsonWithoutGenre_whenValidMovie_thenThrowIllegalArgumentException() {
        // Given
        String requestBody = """
                {
                  "title": "Avatar",
                  "releaseYear": 2009
                }
                """;

        // When + Then
        assertThrows(IllegalArgumentException.class, () ->
                movieService.validMovie(requestBody)
        );
    }

    @Test
    void givenJsonWithoutReleaseYear_whenValidMovie_thenThrowIllegalArgumentException() {
        // Given
        String requestBody = """
                {
                  "title": "Avatar",
                  "genre": "Sci-Fi"
                }
                """;

        // When + Then
        assertThrows(IllegalArgumentException.class, () ->
                movieService.validMovie(requestBody)
        );
    }



    @Test
    void givenJsonWithId_whenValidMovieId_thenNoExceptionIsThrown() {
        // Given
        String requestBody = """
                {
                  "id": "123e4567-e89b-12d3-a456-426614174000"
                }
                """;

        // When + Then
        assertDoesNotThrow(() -> movieService.validMovieID(requestBody));
    }

    @Test
    void givenJsonWithoutId_whenValidMovieId_thenThrowIllegalArgumentException() {
        // Given
        String requestBody = """
                {
                  "title": "Avatar",
                  "genre": "Sci-Fi",
                  "releaseYear": 2009
                }
                """;

        // When + Then
        assertThrows(IllegalArgumentException.class, () ->
                movieService.validMovieID(requestBody)
        );
    }

    @Test
    void givenValidMovieJson_whenExtractValues_thenReturnMovieWithCorrectTitle() {
        // Given
        String requestBody = """
                {
                  "title": "Avatar",
                  "genre": "Sci-Fi",
                  "releaseYear": 2009
                }
                """;

        // When
        Movie movie = movieService.extractValues(requestBody);

        // Then
        assertEquals("Avatar", movie.getTitle());
    }

    @Test
    void givenValidMovieJson_whenExtractValues_thenReturnMovieWithCorrectGenre() {
        // Given
        String requestBody = """
                {
                  "title": "Avatar",
                  "genre": "Sci-Fi",
                  "releaseYear": 2009
                }
                """;

        // When
        Movie movie = movieService.extractValues(requestBody);

        // Then
        assertEquals("Sci-Fi", movie.getGenre());
    }

    @Test
    void givenValidMovieJson_whenExtractValues_thenReturnMovieWithCorrectReleaseYear() {
        // Given
        String requestBody = """
                {
                  "title": "Avatar",
                  "genre": "Sci-Fi",
                  "releaseYear": 2009
                }
                """;

        // When
        Movie movie = movieService.extractValues(requestBody);

        // Then
        assertEquals(2009, movie.getReleaseYear());
    }

    @Test
    void givenTitleParameter_whenGetMovieParam_thenReturnMatchingMovie() {
        // Given
        Map<String, String> params = new HashMap<>();
        params.put("title", "Inception");

        // When
        String result = movieService.GETMOVIEPARAM(params);

        // Then
        assertTrue(result.contains("Inception"));
    }

    @Test
    void givenPartialTitleParameter_whenGetMovieParam_thenReturnMatchingMovie() {
        // Given
        Map<String, String> params = new HashMap<>();
        params.put("title", "cep");

        // When
        String result = movieService.GETMOVIEPARAM(params);

        // Then
        assertTrue(result.contains("Inception"));
    }

    @Test
    void givenGenreParameterIgnoringCase_whenGetMovieParam_thenReturnMatchingMovie() {
        // Given
        Map<String, String> params = new HashMap<>();
        params.put("genre", "drama");

        // When
        String result = movieService.GETMOVIEPARAM(params);

        // Then
        assertTrue(result.contains("Titanic"));
    }

    @Test
    void givenReleaseYearParameter_whenGetMovieParam_thenReturnMatchingMovie() {
        // Given
        Map<String, String> params = new HashMap<>();
        params.put("releaseYear", "2010");

        // When
        String result = movieService.GETMOVIEPARAM(params);

        // Then
        assertTrue(result.contains("Inception"));
    }

    @Test
    void givenTitleParameter_whenGetMovieParam_thenDoNotReturnNonMatchingMovie() {
        // Given
        Map<String, String> params = new HashMap<>();
        params.put("title", "Inception");

        // When
        String result = movieService.GETMOVIEPARAM(params);

        // Then
        assertFalse(result.contains("Titanic"));
    }
}