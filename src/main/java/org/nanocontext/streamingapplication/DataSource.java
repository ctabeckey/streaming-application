package org.nanocontext.streamingapplication;

import org.nanocontext.streamingapplication.exceptions.ResourceNotFoundException;
import org.nanocontext.streamingapplication.exceptions.ResourcePersistenceException;
import org.nanocontext.streamingapplication.exceptions.ResourceRetrievalException;

import java.io.InputStream;

public interface DataSource {
    /**
     *
     * @param resourceMetadata
     * @param inStream
     * @return
     */
    ResourceMetadata createResource(ResourceMetadata resourceMetadata, InputStream inStream) throws ResourcePersistenceException;

    /**
     *
     * @param identifier
     * @return
     */
    ResourceMetadata readResourceMetadata(String identifier) throws ResourceNotFoundException;

    /**
     *
     * @param identifier
     * @return
     */
    InputStream readResourceContent(String identifier) throws ResourceNotFoundException, ResourceRetrievalException;

    /**
     *
     * @param identifier
     * @return
     */
    ResourceMetadata deleteResource(String identifier) throws ResourceNotFoundException;
}
