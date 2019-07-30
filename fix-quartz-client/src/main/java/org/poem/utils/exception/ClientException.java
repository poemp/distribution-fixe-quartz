package org.poem.utils.exception;

/**
 * @author Administrator
 */
public class ClientException extends Exception {

    public ClientException() {
        super( "" );
    }

    public ClientException(String message) {
        super( message );
    }
}
