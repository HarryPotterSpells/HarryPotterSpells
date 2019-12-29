package com.hpspells.core.extension;

public class InvalidExtensionException extends Exception {

    /**
     * 
     */
    private static final long serialVersionUID = 824621409003471598L;

    /**
     * Constructs a new IllegalExtensionException based on the given Exception
     *
     * @param cause Exception that triggered this Exception
     */
    public InvalidExtensionException(final Throwable cause) {
        super(cause);
    }
    
    /**
     * Constructs a new IllegalExtensionException with the specified detail
     * message and cause.
     *
     * @param message the detail message (which is saved for later retrieval
     *     by the getMessage() method).
     * @param cause the cause (which is saved for later retrieval by the
     *     getCause() method). (A null value is permitted, and indicates that
     *     the cause is nonexistent or unknown.)
     */
    public InvalidExtensionException(final String message, final Throwable cause) {
        super(message, cause);
    }

    /**
     * Constructs a new IllegalExtensionException with the specified detail
     * message
     *
     * @param message TThe detail message is saved for later retrieval by the
     *     getMessage() method.
     */
    public InvalidExtensionException(final String message) {
        super(message);
    }
}
