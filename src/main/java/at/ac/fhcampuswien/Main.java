package at.ac.fhcampuswien;

import at.ac.fhcampuswien.controllers.HelloController;
import at.ac.fhcampuswien.controllers.MovieController;
import at.ac.fhcampuswien.exceptions.DatabaseException;
import at.ac.fhcampuswien.models.Movie;
import at.ac.fhcampuswien.services.MovieReadRepository;
import at.ac.fhcampuswien.services.MovieWriteRepository;
import com.sun.net.httpserver.HttpContext;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import java.io.IOException;
import java.net.InetSocketAddress;

public class Main {
    private final static int SERVER_PORT = 8080;

    public static void main(String[] args) throws IOException {
        // Create an HTTP server listening on defined port
        HttpServer server = HttpServer.create(new InetSocketAddress(SERVER_PORT), 0);

        MovieWriteRepository moviesWrite = new MovieWriteRepository();
        MovieReadRepository moviesRead = new MovieReadRepository();
        // Register controllers and their handlers - REST endpoints
        registerController(server, "/api/hello", new HelloController());
        registerController(server, "/api/movies", new MovieController(moviesWrite, moviesRead));

        // Start the server
        server.setExecutor(null);
        server.start();
        System.out.printf("Server is running on http://localhost:%d", SERVER_PORT);

        //test movies
        Movie movie1 = new Movie("The Dark Knight", "Action", 2008);
        Movie movie2 = new Movie("Howl's Moving Castle", "Drama", 2004);

        try {
            moviesWrite.add(movie1);
            moviesWrite.add(movie2);

            System.out.println(moviesRead.findAll());
            System.out.println();

            Movie updateMovie2 = new Movie(movie2.getId(), movie2.getTitle(), movie2.getGenre(), 2020);
            moviesWrite.update(updateMovie2);
            System.out.println(moviesRead.findAll());
            System.out.println();

            moviesWrite.delete(movie1);
            System.out.println(moviesRead.findAll());
        } catch (DatabaseException e) {
            System.out.println();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

    }

    private static void registerController(HttpServer server, String path, HttpHandler handler) {
        HttpContext context = server.createContext(path, handler);
        // Optionally add more configurations to context if needed
    }


}