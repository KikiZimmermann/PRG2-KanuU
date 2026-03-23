package at.ac.fhcampuswien.controllers;

import at.ac.fhcampuswien.ApiUtils;
import at.ac.fhcampuswien.models.Movie;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.UUID;

public class MovieController implements HttpHandler {

    // handles HTTP requests to retrieve/add/delete/update movies (CRUD operations)
    // use manual JSON parsing (no external libraries)
    // follow structure of HelloController

    // Endpoints:
    // GET    /api/movies/getAll
    // POST   /api/movies/add
    // DELETE /api/movies/delete
    // PUT    /api/movies/update
    private final String BASE = "/api/movies/";
    public List<Movie> movies = Movie.generateDummyMovies();

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        // Get the HTTP method (GET, POST, etc.)
        String method = exchange.getRequestMethod();

        // Get the requested URI path (e.g. /api/hello/greet)
        String path = exchange.getRequestURI().getPath();

        // Route based on the path
        switch (path) {
            case BASE + "getAll" -> handleGetAllRequest(method, exchange);
            case BASE + "add" -> handleAddRequest(method, exchange);
            case BASE + "delete" -> handleDeleteRequest(method, exchange);
            case BASE + "update" -> handleUpdateRequest(method, exchange);
            default -> {
                // Path not found
                String response = "{ \"error\": \"Path not found\" }";
                ApiUtils.sendResponse(exchange, 404, response);
            }
        }
    }

    private void handleGetAllRequest(String method, HttpExchange exchange) throws IOException {
        // Handle GET and POST for /api/hello/greet
        switch (method) {
            case "GET" -> {
                String response = movies.toString();
                ApiUtils.sendResponse(exchange, 200, response);
            }
            default -> {
                String response = "{ \"error\": \"Method not allowed\" }";
                ApiUtils.sendResponse(exchange, 405, response);
            }
        }
    }
    private void handleAddRequest(String method, HttpExchange exchange) throws IOException {
        // Handle GET and POST for /api/hello/greet
        switch (method) {
            case "POST" -> {
                String requestBody = new String(exchange.getRequestBody().readAllBytes(), StandardCharsets.UTF_8);
                String response = "{ \"Movie added successfully\": " + requestBody + " }";

                if (!requestBody.contains("title") ||
                        !requestBody.contains("genre") ||
                        !requestBody.contains("releaseYear")) {

                    response = "{ \"error\": \"Invalid movie data\" }";
                    ApiUtils.sendResponse(exchange, 400, response);
                    return;
                }

                // Extract values (simple parsing)
                String title = null;
                String genre = null;
                int releaseYear = 0;
                try{
                    title = extractValue(requestBody, "title");
                    genre = extractValue(requestBody, "genre");
                }
                catch(Exception e) {
                    response = "{ \"error\": \"Method not allowed\" }";
                    ApiUtils.sendResponse(exchange, 405, response);
                    return;
                }

                try{
                    releaseYear = Integer.parseInt(extractValue(requestBody, "releaseYear"));
                }
                catch(Exception e) {
                    response = "{ \"error\": \"Invalid movie data\" }";
                    ApiUtils.sendResponse(exchange, 400, response);
                    return;
                }

                // Check if movie already exists
                for (Movie m : movies) {
                    if (m.getTitle().equalsIgnoreCase(title) &&
                            m.getGenre().equalsIgnoreCase(genre) &&
                            m.getReleaseYear() == releaseYear) {

                        response = "{ \"error\": \"Movie already exists\" }";
                        ApiUtils.sendResponse(exchange, 400, response);
                        return;
                    }
                }

                // Create new movie
                Movie newMovie = new Movie(title, genre, releaseYear);
                movies.add(newMovie);

                ApiUtils.sendResponse(exchange, 200, response);
            }
            default -> {
                String response = "{ \"error\": \"Method not allowed\" }";
                ApiUtils.sendResponse(exchange, 405, response);
            }
        }
    }
    private String extractValue(String json, String key) {
        String value = json.split("\"" + key + "\"\\s*:\\s*")[1];

        if (value.startsWith("\"")) {
            return value.split("\"")[1];
        } else {
            return value.split("[,}]")[0].trim();
        }
    }
    private void handleDeleteRequest(String method, HttpExchange exchange) throws IOException {
        // Handle GET and POST for /api/hello/greet
        switch (method) {
            case "DELETE" -> {
                String requestBody = new String(exchange.getRequestBody().readAllBytes(), StandardCharsets.UTF_8);
                String response = "{ \"error\": \"Movie not found\" }";

                if (!requestBody.contains("title") ||
                        !requestBody.contains("genre") ||
                        !requestBody.contains("releaseYear")) {

                    ApiUtils.sendResponse(exchange, 400, response);
                    return;
                }
                // Extract values (simple parsing)
                String title = extractValue(requestBody, "title");
                String genre = extractValue(requestBody, "genre");
                int releaseYear = 0;
                try{
                    releaseYear = Integer.parseInt(extractValue(requestBody, "releaseYear"));
                }
                catch(Exception e) {
                    response = "{ \"error\": \"Invalid movie data\" }";
                    ApiUtils.sendResponse(exchange, 400, response);
                    return;
                }

                // Check if movie already exists
                for (Movie m : movies) {
                    if (m.getTitle().equalsIgnoreCase(title) &&
                            m.getGenre().equalsIgnoreCase(genre) &&
                            m.getReleaseYear() == releaseYear) {

                        response = "{ \"Movie deleted successfully\": " + requestBody + " }";
                        movies.remove(m);
                        ApiUtils.sendResponse(exchange, 200, response);
                        return;
                    }
                }
                ApiUtils.sendResponse(exchange, 404, response);
            }
            default -> {
                String response = "{ \"error\": \"Method not allowed\" }";
                ApiUtils.sendResponse(exchange, 405, response);
            }
        }
    }
    private void handleUpdateRequest(String method, HttpExchange exchange) throws IOException {
        // Handle GET and POST for /api/hello/greet
        switch (method) {
            case "PUT" -> {
                String requestBody = new String(exchange.getRequestBody().readAllBytes(), StandardCharsets.UTF_8);
                String response = "{ \"The movie to be updated was not found\": }";

                if (!requestBody.contains("id")) {
                    response = "{ \"error\": \"Invalid movie data\" }";
                    ApiUtils.sendResponse(exchange, 400, response);
                    return;
                }

                // Extract values (simple parsing)
                UUID id = UUID.fromString(extractValue(requestBody, "id"));
                String title = extractValue(requestBody, "title");
                String genre = extractValue(requestBody, "genre");
                int releaseYear = 0;
                try{
                    releaseYear = Integer.parseInt(extractValue(requestBody, "releaseYear"));
                }
                catch(Exception e) {
                    response = "{ \"error\": \"Invalid movie data\" }";
                    ApiUtils.sendResponse(exchange, 400, response);
                    return;
                }


                // Check if movie was found
                for (Movie m : movies) {
                    if (m.getId().equals(id)) {

                        response = "{ \"message\": \"Movie updated successfully\" "+ requestBody +" }";
                        if(requestBody.contains("genre")){
                            if(genre.equals("Action") || genre.equals("Drama") ||
                            genre.equals("Comedy") || genre.equals("Sci-Fi") ||
                            genre.equals("Horror") || genre.equals("Thriller")){
                                m.setGenre(genre);
                            }
                            else{
                                response = "{ \"error\": \"Invalid movie data\" }";
                                ApiUtils.sendResponse(exchange, 400, response);
                                return;
                            }
                        }
                        if(requestBody.contains("title")) {
                            if(title instanceof String){
                                m.setTitle(title);
                            }
                            else{
                                response = "{ \"error\": \"Invalid movie data\" }";
                                ApiUtils.sendResponse(exchange, 400, response);
                                return;
                            }
                        }
                        if(requestBody.contains("releaseYear")){
                            if(releaseYear > 1980){
                                m.setReleaseYear(releaseYear);
                            }
                            else{
                                response = "{ \"error\": \"Invalid movie data\" }";
                                ApiUtils.sendResponse(exchange, 400, response);
                                return;
                            }

                        }

                        ApiUtils.sendResponse(exchange, 200, response);
                        return;
                    }
                }

                ApiUtils.sendResponse(exchange, 404, response);
            }
            default -> {
                String response = "{ \"error\": \"Method not allowed\" }";
                ApiUtils.sendResponse(exchange, 405, response);
            }
        }
    }
}
