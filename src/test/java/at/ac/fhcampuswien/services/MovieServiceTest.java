package at.ac.fhcampuswien.services;

import at.ac.fhcampuswien.exceptions.DatabaseException;
import at.ac.fhcampuswien.exceptions.MovieNotFoundException;
import at.ac.fhcampuswien.models.Movie;
import com.google.errorprone.annotations.DoNotMock;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class MovieServiceTest {

    private MovieService movieService;
    @Mock
    private MovieRepository movieRepository;

    @BeforeEach
    void setUp() throws DatabaseException {
        //Mock the MovieRepository
        movieRepository = mock(MovieRepository.class);

        UUID id = UUID.fromString("b2bcf03a-36e6-41ce-8da7-7a313a0441cb");
        //Sample movies
        List<Movie> movies = new ArrayList<>(Arrays.asList(
            new Movie(id,"Inception", "Sci-Fi", 2010),
                new Movie("HalloWelt", "Sci-Fi", 2000),
            new Movie("Titanic", "Drama", 1997)
        ));

        //Mock the repository methods
        when(movieRepository.findAll()).thenReturn(movies);

        movieService = new MovieService(movieRepository);
    }

    @Test
    void givenNewMovie_whenValidMovie_thenMovieAddedToList() throws MovieNotFoundException, DatabaseException{

        // Given
        String newTitle = "Lord of the Rings";
        String genre = "Fantasy";
        int releaseYear = 2001;

        // When
        movieService.ADDMovie(newTitle, genre, releaseYear);

        // Then
        verify(movieRepository, times(1)).add(any(Movie.class));
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
    void givenMovie_whenExistingMovie_thenDeleteMovie() throws DatabaseException, MovieNotFoundException {

        // Given
        String newTitle = "Inception";
        String genre = "Sci-Fi";
        int releaseYear = 2010;

        // When
        movieService.DELETEMovie(newTitle, genre, releaseYear);

        // Then
        verify(movieRepository).delete(any(Movie.class));
    }

    @Test
    void givenMovie_whenNonExistingMovie_thenThrowException() {

        // Given
        String newTitle = "Lord of the Rings";
        String genre = "Fantasy";
        int releaseYear = 2001;

        // When + Then
        assertThrows(MovieNotFoundException.class, () ->
                movieService.DELETEMovie(newTitle, genre, releaseYear));
    }



    @Test
    void givenExistingMovieId_whenUpdateTitle_thenTitleIsUpdated() throws DatabaseException, MovieNotFoundException {
        // Given
        UUID id = UUID.fromString("b2bcf03a-36e6-41ce-8da7-7a313a0441cb");
        String newTitle = "Inception Updated";
        String genre = "Sci-Fi";
        int releaseYear = 2010;

        // When
        movieService.UPDATEMovie(newTitle, genre, releaseYear, id);

        // Then
        verify(movieRepository, times(1)).update(any(Movie.class));
    }

    @Test
    void givenExistingMovieId_whenUpdateGenre_thenGenreIsUpdated() throws DatabaseException, MovieNotFoundException {
        // Given
        UUID id = UUID.fromString("b2bcf03a-36e6-41ce-8da7-7a313a0441cb");
        String newGenre = "Action";
        int releaseYear = 2010;
        String newTitle = "Inception Updated";

        // When
        movieService.UPDATEMovie(newTitle, newGenre, releaseYear, id);

        // Then
        verify(movieRepository, times(1)).update(any(Movie.class));
    }

    @Test
    void givenExistingMovieId_whenUpdateRelease_thenReleaseYearIsUpdated() throws DatabaseException, MovieNotFoundException {
        // Given
        UUID id = UUID.fromString("b2bcf03a-36e6-41ce-8da7-7a313a0441cb");
        String newTitle = "Inception Updated";
        String genre = "Sci-Fi";
        int newReleaseYear = 2011;

        // When
        movieService.UPDATEMovie(newTitle, genre, newReleaseYear, id);

        // Then
        verify(movieRepository, times(1)).update(any(Movie.class));
    }


    @Test
    void givenExistingMovieId_whenInvalidRelease_thenThrowException() {
        // Given
        UUID id = UUID.fromString("b2bcf03a-36e6-41ce-8da7-7a313a0441cb");
        String newTitle = "Inception";
        String genre = "Fantasy";
        int newReleaseYear = 1979;

        // When + Then
        assertThrows(IllegalArgumentException.class, () ->
                movieService.UPDATEMovie(newTitle, genre, newReleaseYear, id)
        );
    }


    @Test
    void givenMovieWithSameValues_whenMovieExists_thenReturnTrue() throws DatabaseException, MovieNotFoundException {
        // Given
        Movie testMovie = new Movie("Inception", "Sci-Fi", 2010);

        // When
        boolean result = movieService.MovieExists(testMovie);

        // Then
        assertTrue(result);
    }

    @Test
    void givenMovieWithDifferentValues_whenMovieExists_thenReturnFalse() throws DatabaseException, MovieNotFoundException {
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
    void givenTitleParameter_whenGetMovieParam_thenReturnMatchingMovie() throws DatabaseException {
        // Given
        Map<String, String> params = new HashMap<>();
        params.put("title", "Inception");

        // When
        String result = movieService.GETMOVIEPARAM(params);

        // Then
        assertTrue(result.contains("Inception"));
    }


    @Test
    void givenPartialTitleParameter_whenGetMovieParam_thenReturnMatchingMovie() throws DatabaseException {
        // Given
        Map<String, String> params = new HashMap<>();
        params.put("title", "cep");

        // When
        String result = movieService.GETMOVIEPARAM(params);

        // Then
        assertTrue(result.contains("Inception"));
    }


    @Test
    void givenGenreParameterIgnoringCase_whenGetMovieParam_thenReturnMatchingMovie() throws DatabaseException {
        // Given
        Map<String, String> params = new HashMap<>();
        params.put("genre", "drama");

        // When
        String result = movieService.GETMOVIEPARAM(params);

        // Then
        assertTrue(result.contains("Titanic"));
    }


    @Test
    void givenReleaseYearParameter_whenGetMovieParam_thenReturnMatchingMovie() throws DatabaseException {
        // Given
        Map<String, String> params = new HashMap<>();
        params.put("releaseYear", "2010");

        // When
        String result = movieService.GETMOVIEPARAM(params);

        // Then
        assertTrue(result.contains("Inception"));
    }


    @Test
    void givenTitleParameter_whenGetMovieParam_thenDoNotReturnNonMatchingMovie() throws DatabaseException {
        // Given
        Map<String, String> params = new HashMap<>();
        params.put("title", "Inception");

        // When
        String result = movieService.GETMOVIEPARAM(params);

        // Then
        assertFalse(result.contains("Titanic"));
    }
    @Test
    void should_throw_database_exception_when_deleting_movie_with_db_error() throws DatabaseException, MovieNotFoundException {
        // Given
        when(movieRepository.delete(any(Movie.class)))
                .thenThrow(new DatabaseException("Database connection error"));

        // When + Then
        assertThrows(DatabaseException.class, () ->
                movieService.DELETEMovie("Inception", "Sci-Fi", 2010)
        );
    }
    @Test
    void should_throw_MovieNotFound_exception_when_deleting_movie_that_does_not_change_the_Database() throws DatabaseException, MovieNotFoundException {
        // Given
        when(movieRepository.delete(any(Movie.class)))
                .thenThrow(new MovieNotFoundException());

        // When + Then
        assertThrows(MovieNotFoundException.class, () ->
                movieService.DELETEMovie("Inception", "Sci-Fi", 2010)
        );
    }
}