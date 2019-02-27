package org.nanocontext.streamingapplication.exceptions;

/**
 * An exception that is thrown when the DataSource is unable to save a resource.
 * The DataSource implementation should include a more specific exception as the root
 * cause.
 */
public class ResourcePersistenceException extends Exception {
    private static String createMessage(final String identifier) {
        return String.format("Unable to persist resource [%s].", identifier);
    }

    public ResourcePersistenceException(final String identifier) {
        super(createMessage(identifier));
    }

    public ResourcePersistenceException(final String identifier, final Throwable cause) {
        super(createMessage(identifier), cause);
    }
}
