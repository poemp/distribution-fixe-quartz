package org.poem.utils.exception;

public class ParameterException extends Exception {

    public ParameterException() {
        super( "" );
    }

    public ParameterException(String message) {
        super( message );
    }
}
