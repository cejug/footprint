package org.cejug.footprint.core.publisher;

/**
 * A publisher implementation can throw a set of diverse exceptions, but for the
 * external consumers this should be the only visible exception.
 * 
 * @author Felipe Gaucho
 */
public class FootprintPublisherException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    /**
     * A new exception.
     * 
     * @param message
     *            the error message.
     */
    public FootprintPublisherException(String message) {
        super(message);
    }

    /**
     * A new exception.
     * 
     * @param message
     *            the error message.
     * @param error
     *            the error stack trace.
     */
    public FootprintPublisherException(String message, Throwable error) {
        super(message, error);
    }
}
