package org.nanocontext.streamingapplication.exceptions;

/**
 * An exception to indicate that a resource exists but is not retrievable for some reason.
 * The DataSource implementation should provide more details, perhaps as a root cause.
 */
public class ResourceRetrievalException extends Exception {
    /**
     *
     * @param identifier
     * @return
     */
    private static String createMessage(final String identifier) {
        return String.format("Unable to access existing resource [%s].", identifier);
    }

    public ResourceRetrievalException(String identifier) {
        super(createMessage(identifier));
    }

    public ResourceRetrievalException(String identifier, Throwable cause) {
        super(createMessage(identifier), cause);
    }
}
