package at.ac.fhcampuswien.exceptions;

public class DatabaseException extends Exception {

    public DatabaseException(String msg) {
        super(msg);
    }

    public DatabaseException() {
        super("Database error!");
    }
}
