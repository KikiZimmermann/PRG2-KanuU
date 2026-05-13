package at.ac.fhcampuswien.controllers;

import at.ac.fhcampuswien.ApiUtils;
import at.ac.fhcampuswien.models.Movie;
import at.ac.fhcampuswien.services.MovieRepository;
import at.ac.fhcampuswien.services.MovieService;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
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
    public MovieRepository movies;
    private final MovieService movieService = new MovieService(movies);

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
            case BASE + "search" -> handleSearchRequest(method, exchange);
            default -> {
                // Path not found
                String response = "{ \"error\": \"Path not found\" }";
                ApiUtils.sendResponse(exchange, 404, response);
            }
        }
    }

    private void handleGetAllRequest(String method, HttpExchange exchange) throws IOException {
        // Handle GET and POST for /api/movies/getAll
        switch (method) {
            case "GET" -> {
                String response = movies.findAll().toString();
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

                try{
                    movieService.validMovie(requestBody);
                    Movie movie = movieService.extractValues(requestBody);
                    movieService.ADDMovie(movie.getTitle(), movie.getGenre(), movie.getReleaseYear());

                    ApiUtils.sendResponse(exchange, 201, response);
                }
                catch(IllegalArgumentException e){
                    ApiUtils.sendResponse(exchange, 400, "{ \"error\": \"" + e.getMessage() + "\" }");

                } catch (Exception e) {
                    ApiUtils.sendResponse(exchange, 500, "{ \"error\": \"Server error\" }");
                }
            }
            default -> {
                String response = "{ \"error\": \"Method not allowed\" }";
                ApiUtils.sendResponse(exchange, 405, response);
            }
        }
    }

    private void handleDeleteRequest(String method, HttpExchange exchange) throws IOException {
        // Handle GET and POST for /api/hello/greet
        switch (method) {
            case "DELETE" -> {
                String requestBody = new String(exchange.getRequestBody().readAllBytes(), StandardCharsets.UTF_8);
                String response = "{ \"Movie deleted successfully\": " + requestBody + " }";

                try{
                    movieService.validMovie(requestBody);
                    Movie movie = movieService.extractValues(requestBody);
                    movieService.DELETEMovie(movie.getTitle(), movie.getGenre(), movie.getReleaseYear());

                    ApiUtils.sendResponse(exchange, 200, response);
                }
                catch(IllegalArgumentException e){
                    ApiUtils.sendResponse(exchange, 400, "{ \"error\": \"" + e.getMessage() + "\" }");

                } catch (Exception e) {
                    ApiUtils.sendResponse(exchange, 500, "{ \"error\": \"Server error\" }");
                }
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
                String response = "{ \"message\": \"Movie updated successfully\" "+ requestBody +" }";

                try{
                    movieService.validMovieID(requestBody);
                    Movie movie = movieService.extractValues(requestBody);
                    movieService.UPDATEMovie(movie.getTitle(), movie.getGenre(), movie.getReleaseYear(), movie.getId());

                    ApiUtils.sendResponse(exchange, 200, response);
                }
                catch(IllegalArgumentException e){
                    ApiUtils.sendResponse(exchange, 400, "{ \"error\": \"" + e.getMessage() + "\" }");

                } catch (Exception e) {
                    ApiUtils.sendResponse(exchange, 500, "{ \"error\": \"Server error\" }");
                }
            }
            default -> {
                String response = "{ \"error\": \"Method not allowed\" }";
                ApiUtils.sendResponse(exchange, 405, response);
            }
        }
    }

    private void handleSearchRequest(String method, HttpExchange exchange) throws IOException {
        switch (method) {
            case "GET" -> {

                String query = exchange.getRequestURI().getQuery();
                Map<String, String> params = ApiUtils.parseQueryParams(query);

                try{
                    String response = movieService.GETMOVIEPARAM(params);
                    ApiUtils.sendResponse(exchange, 200, response);
                }
                catch(IllegalArgumentException e){
                    ApiUtils.sendResponse(exchange, 400, "{ \"error\": \"" + e.getMessage() + "\" }");

                } catch (Exception e) {
                    ApiUtils.sendResponse(exchange, 500, "{ \"error\": \"Server error\" }");
                }
            }

            default -> {
                String response = "{ \"error\": \"Method not allowed\" }";
                ApiUtils.sendResponse(exchange, 405, response);
            }
        }
    }
}
