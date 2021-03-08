package org.tomw.daoutils;


/**
 * Exception thrown if operation on dao cannot be executed
 */
public class DataIntegrityException extends Exception{
    public DataIntegrityException(String message) {
        super(message);
    }
}
