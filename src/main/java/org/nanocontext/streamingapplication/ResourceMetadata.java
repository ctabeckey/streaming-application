package org.nanocontext.streamingapplication;

import org.nanocontext.streamingapplication.validation.PersistedResource;
import org.springframework.http.HttpHeaders;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.*;

/**
 * A simple value object to contain the metadata values that will be
 * 1.) stored
 * 2.) written in response to a HEAD request for a file.
 */
public class ResourceMetadata implements Serializable {

    // the 'identifier' must be available after the resource has been persisted
    @NotNull(groups = {PersistedResource.class})
    private final String identifier;

    // the metadata is a map of all the metadata fields included with the resource
    @NotNull
    private final Map<@NotNull String, @NotNull Object> metadata;

    /**
     * The constructor requires all non-null arguments
     * @param identifier
     * @param metadata
     */
    private ResourceMetadata(final String identifier, final Map<String, Object> metadata) {
        this.identifier = identifier;
        this.metadata = metadata;
    }

    public String getIdentifier() {
        return identifier;
    }

    public Integer getLength() {
        return (Integer) metadata.get(HttpHeaders.CONTENT_LENGTH);
    }

    public String getContentType() {
        return (String) metadata.get(HttpHeaders.CONTENT_TYPE);
    }

    /**
     * returns a String representation of the metadata value
     * @param key
     * @return
     */
    public String getMetadataAsString(final String key) {
        Object value = metadata.get(key);
        return value.toString();
    }

    /**
     * returns an int representation of the given metadata value if the
     * value is a Number, else returns null
     * @param key
     * @return
     */
    public Integer getMetadataAsInt(final String key) {
        Object value = metadata.get(key);
        if (value instanceof Number)
            return ((Number)value).intValue();

        return null;
    }

    /**
     * Returns an iterator over the metadata keys
     * @return
     */
    public Set<@NotNull String> metadataKeys() {
        return metadata.keySet();
    }

    /**
     * the 'identifier' field is the sole determinant of equals and hashCode
     * @param o
     * @return
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ResourceMetadata that = (ResourceMetadata) o;
        return Objects.equals(identifier, that.identifier);
    }

    @Override
    public int hashCode() {
        return Objects.hash(identifier);
    }

    @Override
    public String toString() {
        return "ResourceMetadata{" +
                "identifier='" + identifier + '\'' +
                ", metadata=" + metadata +
                '}';
    }

    public static FileHeadResponseBuilder builder() {
        return new FileHeadResponseBuilder();
    }

    public static final class FileHeadResponseBuilder {
        private String identifier;
        private Map<String, Object> metadataMap = new HashMap<>();

        private FileHeadResponseBuilder() {
        }

        public FileHeadResponseBuilder withIdentifier(String identifier) {
            this.identifier = identifier;
            return this;
        }

        public FileHeadResponseBuilder withLength(Integer length) {
            this.metadataMap.put(HttpHeaders.CONTENT_LENGTH, length);
            return this;
        }

        public FileHeadResponseBuilder withContentType(String contentType) {
            this.metadataMap.put(HttpHeaders.CONTENT_TYPE, contentType);
            return this;
        }

        public FileHeadResponseBuilder withAdditionalMetadata(String key, String value) {
            this.metadataMap.put(key, value);
            return this;
        }

        public FileHeadResponseBuilder withAdditionalMetadata(Map<String, Object> values) {
            this.metadataMap.putAll(values);
            return this;
        }

        /**
         * Copies the contents of an existing ResourceMetadata to this builder.
         * This does not overwrite existing metadata unless the keys are the same.
         * This does not overwrite a non-null, existing, identifier.
         *
         * @param resourceMetadata
         * @return
         */
        public FileHeadResponseBuilder with(final ResourceMetadata resourceMetadata) {
            if(identifier == null)
                withIdentifier(identifier);
            withAdditionalMetadata(resourceMetadata.metadata);
            return this;
        }

        /**
         * Build a ResourceMetadata instance, assuring that required metadata is provided
         * @return a valid ResourceMetadata instance
         */
        public ResourceMetadata build() {
            if (!this.metadataMap.containsKey(HttpHeaders.CONTENT_LENGTH))
                throw new IllegalStateException("The LENGTH must be added before calling build()");
            if (!this.metadataMap.containsKey(HttpHeaders.CONTENT_TYPE))
                throw new IllegalStateException("The CONTENT_TYPE must be added before calling build()");

            ResourceMetadata resourceMetadata = new ResourceMetadata(this.identifier, this.metadataMap);
            return resourceMetadata;
        }

    }
}
