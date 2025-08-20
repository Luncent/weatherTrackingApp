package org.example.exception_handling.exceptions.repository;

public class DBException extends RuntimeException {
    public DBException(Throwable cause) {
        super(cause);
    }
}
