package at.ac.fhcampuswien.controllers;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;

public class MovieController implements HttpHandler {

    // handles HTTP requests to retrieve/add/delete/update movies (CRUD operations)
    // use manual JSON parsing (no external libraries)
    // follow structure of HelloController

    // Endpoints:
    // GET    /api/movies/getAll
    // POST   /api/movies/add
    // DELETE /api/movies/delete
    // PUT    /api/movies/update

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        //outstanding

    }


}
