package at.ac.fhcampuswien;

import at.ac.fhcampuswien.controllers.HelloController;
import com.sun.net.httpserver.HttpContext;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import java.io.IOException;
import java.net.InetSocketAddress;

//structure as Model-View-Controller pattern (Movie-API-MovieController)
//provide a simple REST API to create, read, update and delete Movies


// Create package  /models
// Create class    Movie


// Movie Class:

// private UUID id         // data type storing Universally Unique Identifiers
// private String title    // title of movie
// private String genre    // e.g. Action, Drama
// private int releaseYear

// Constructors:
// 1. default constructor automatically generate unique UUID --> UUID.randomUUID()
// 2. constructor: accept arguments: title, genre, releaseYear; automatically generate id

// Methods:
// Getters + Setters (all properties!)
// Override toString-Method --> return string representation of Movie object: Movie{id=<ID>, title='<title>', genre='<genre>', releaseYear=<year>}
// public static generateDummyMovies()  --> creates + returns a list of 20 Movie objects with random titles + genres + release years


// MovieController  // goes in cotrollers pckgs


public class Main {
    private final static int SERVER_PORT = 8080;

    public static void main(String[] args) throws IOException {
        // Create an HTTP server listening on defined port
        HttpServer server = HttpServer.create(new InetSocketAddress(SERVER_PORT), 0);

        // Register controllers and their handlers - REST endpoints
        registerController(server, "/api/hello", new HelloController());

        // Start the server
        server.setExecutor(null);
        server.start();
        System.out.printf("Server is running on http://localhost:%d", SERVER_PORT);
    }

    private static void registerController(HttpServer server, String path, HttpHandler handler) {
        HttpContext context = server.createContext(path, handler);
        // Optionally add more configurations to context if needed
    }
}