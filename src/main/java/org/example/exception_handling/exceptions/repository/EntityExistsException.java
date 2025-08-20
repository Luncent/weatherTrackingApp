package org.example.exception_handling.exceptions.repository;

public class EntityExistsException extends RuntimeException {
    public EntityExistsException(String entityData, Throwable cause) {
        super(entityData, cause);
    }
}
