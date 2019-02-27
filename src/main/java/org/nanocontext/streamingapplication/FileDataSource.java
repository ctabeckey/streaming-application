package org.nanocontext.streamingapplication;

import org.nanocontext.streamingapplication.exceptions.ResourceNotFoundException;
import org.nanocontext.streamingapplication.exceptions.ResourcePersistenceException;
import org.nanocontext.streamingapplication.exceptions.ResourceRetrievalException;

import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * A DataSource implementation that writes everything to the file system.
 * The resources are written to a file with the unique identifier as the name and no
 * extension. The metadata is saved in an in-memory DB, i.e. a Map
 * For the first version, the files are all written to the root directory. This imposes
 * limitations to the number of files, particularly on older Windows machines.
 */
public class FileDataSource implements DataSource {
    // the root directory to save resources
    private final File root;

    // the resource metadata
    private final Map<String, ResourceMetadata> metadata = new HashMap<>();

    /**
     *
     * @param root
     */
    public FileDataSource(final File root){
        if (root == null)
            throw new IllegalArgumentException("The root directory must not be null.");

        if (!root.exists())
            root.mkdirs();
        if (root.canRead() && root.canWrite())
            this.root = root;
        else
            throw new IllegalArgumentException("The given root directory '" + root.getAbsolutePath() + "' is either not readable or not writable.");
    }


    /**
     *
     * @param resourceMetadata
     * @param inStream
     * @return
     * @throws ResourcePersistenceException
     */
    @Override
    public ResourceMetadata createResource(final ResourceMetadata resourceMetadata, final InputStream inStream)
            throws ResourcePersistenceException {
        ResourceMetadata result = null;
        String identifier = UUID.randomUUID().toString();
        File resourceFile = new File(root, identifier);
        OutputStream outStream = null;

        // todo: convert this to NIO
        int length = 0;
        byte[] buffy = new byte[2048];      // 2048 is just a reasonable number for buffer size
        try {
            resourceFile.createNewFile();
            outStream = new FileOutputStream(resourceFile);
            for (int bytesRead = inStream.read(buffy); bytesRead >= 0; bytesRead = inStream.read(buffy)) {
                outStream.write(buffy, 0, bytesRead);
                length += bytesRead;
            }

            // create a ResourceMetadata from the given instance plus the identifier
            // add the length as determined from writing the content
            result = ResourceMetadata.builder()
                    .with(resourceMetadata)
                    .withLength(Integer.valueOf(length))
                    .withIdentifier(identifier)
                    .build();
            metadata.put(identifier, result);
        } catch (IOException ioX) {
            throw new ResourcePersistenceException(identifier, ioX);
        } finally {
            if (outStream != null) {
                try {
                    outStream.close();
                } catch (IOException e) {
                    // unlikely to occur
                    e.printStackTrace();
                }
            }
        }

        return result;
    }

    @Override
    public ResourceMetadata readResourceMetadata(final String identifier) throws ResourceNotFoundException {
        ResourceMetadata result = metadata.get(identifier);
        if (result == null)
            throw new ResourceNotFoundException(identifier);
        return result;
    }

    /**
     * Note that the calling code MUST close the InputStream returned from this method,
     * this is potentially unsafe.
     *
     * @param identifier
     * @return
     * @throws ResourceNotFoundException
     */
    @Override
    public InputStream readResourceContent(final String identifier) throws ResourceNotFoundException, ResourceRetrievalException {
        File resourceFile = new File(root, identifier);
        if (resourceFile.exists()) {
            try {
                return new FileInputStream(resourceFile);
            } catch (FileNotFoundException e) {
                throw new ResourceRetrievalException(identifier);
            }
        } else
            throw new ResourceNotFoundException(identifier);
    }

    /**
     *
     * @param identifier
     * @return
     * @throws ResourceNotFoundException
     */
    @Override
    public ResourceMetadata deleteResource(final String identifier) throws ResourceNotFoundException {
        File resourceFile = new File(root, identifier);
        ResourceMetadata result = metadata.remove(identifier);
        if (result == null || !resourceFile.delete())
            throw new ResourceNotFoundException(identifier);

        return result;
    }
}
