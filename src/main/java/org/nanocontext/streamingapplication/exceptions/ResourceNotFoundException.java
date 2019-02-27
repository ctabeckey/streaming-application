package org.nanocontext.streamingapplication.exceptions;

/**
 * An exception that is thrown when an identifier cannot be used to locate a resource.
 */
public class ResourceNotFoundException extends Exception {
    private static String createMessage(final String identifier) {
        return String.format("Resource [%s] could not be found", identifier);
    }

    public ResourceNotFoundException(String identifier) {
        super(createMessage(identifier));
    }
}
