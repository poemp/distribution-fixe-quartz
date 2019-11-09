package org.poem.exception;

public class RRException extends RuntimeException {


    /**
     * Constructs a {@code RRException} with no detail message.
     */
    public RRException() {
        super();
    }

    /**
     * Constructs a {@code RRException} with the specified
     * detail message.
     *
     * @param s the detail message.
     */
    public RRException(String s) {
        super(s);
    }


    public RRException(String s, Throwable r) {
        super(s, r);
    }
}
