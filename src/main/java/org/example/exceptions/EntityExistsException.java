package org.example.exceptions;

public class EntityExistsException extends Exception{
    public EntityExistsException(String msg){
        super(msg);
    }
}
