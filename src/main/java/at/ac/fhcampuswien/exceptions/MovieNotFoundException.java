package at.ac.fhcampuswien.exceptions;

public class MovieNotFoundException extends Exception {

    public MovieNotFoundException(String msg) {
        super(msg);
    }


    public MovieNotFoundException() {
        super("Movie does not exist in database");
    }
}
