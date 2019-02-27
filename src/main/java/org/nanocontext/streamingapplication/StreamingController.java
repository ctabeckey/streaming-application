package org.nanocontext.streamingapplication;

import org.nanocontext.streamingapplication.exceptions.ResourceNotFoundException;
import org.nanocontext.streamingapplication.exceptions.ResourcePersistenceException;
import org.nanocontext.streamingapplication.exceptions.ResourceRetrievalException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Validator;
import javax.validation.constraints.NotNull;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 */
@RestController
public class StreamingController {
    /** a logger instance */
    private final static Logger logger = LoggerFactory.getLogger(StreamingController.class);

    // this regex is used to match against HTTP header names, the headers that match this pattern
    // will be saved as metadata, the group within the regex named 'name' will be extracted to use as the
    // metadata name e.g. "xxx-name: CuteCat" will result in a metadata field with a key of "name" and a value of "CuteCat"
    // the regex pattern is treated as case insensitive, keys are converted to lowercase, values are unchanged
    private final static String METADATA_REGEX = "xxx-(?<name>[a-z_]+)";
    private final static String METADATA_NAME_GROUP = "name";
    private final static Pattern METADATA_KEY_PATTERN = Pattern.compile(METADATA_REGEX, Pattern.CASE_INSENSITIVE + Pattern.UNICODE_CASE);

    /** The DataSource implementation is the thing that persists the resources and the resource metadata. */
    private final DataSource dataSource;

    /** */
    private final Validator validator;

    /**
     *
     * @param dataSource
     */
    public StreamingController(@NotNull final DataSource dataSource, @NotNull final Validator validator) {
        this.dataSource = dataSource;
        this.validator = validator;
    }

    /**
     * handle a POST request,
     * the body of the POST will be treated as the file to save,
     * the HTTP headers that match the metadata pattern will be stored as metadata
     * @param request a "raw" request because we are not specific to the metadata values
     *                as contained in the HTTP headers
     *
     */
    @PostMapping(value = "/", consumes = {"*"})
    public ResourceMetadata post(final HttpServletRequest request) throws IOException, ResourcePersistenceException {
        String contentType = request.getHeader(HttpHeaders.CONTENT_TYPE);
        Integer length = request.getIntHeader(HttpHeaders.CONTENT_LENGTH);

        Map<String, Object> metadata = extractMetadata(request);
        // note that if a request has a "XXX-LENGTH" or "XXX-CONTENT-TYPE" then the value will be overwritten by the "real"
        // HTTP header values, we should throw an error in that case
        if (contentType != null)
            metadata.put(HttpHeaders.CONTENT_TYPE, contentType);
        if (length != null)
            metadata.put(HttpHeaders.CONTENT_LENGTH, length);

        ResourceMetadata resourceMetadata = ResourceMetadata.builder()
                .withAdditionalMetadata(metadata)
                .build();

        ResourceMetadata persistedMetadata = dataSource.createResource(resourceMetadata, request.getInputStream());

        return persistedMetadata;
    }

    /**
     *
     * @param identifier
     * @return
     * @throws ResourceNotFoundException
     */
    @RequestMapping(value="/{identifier}", method = {RequestMethod.HEAD})
    public ResponseEntity<String> head(@PathVariable("identifier") final String identifier)
            throws ResourceNotFoundException {
        ResourceMetadata resourceMetadata = dataSource.readResourceMetadata(identifier);
        HttpHeaders httpHeaders = buildHttpHeaders(resourceMetadata);

        return new ResponseEntity("", httpHeaders, HttpStatus.OK);
    }

    @RequestMapping(value="/{identifier}", method = {RequestMethod.GET})
    public ResponseEntity<InputStreamResource> get(@PathVariable("identifier") final String identifier, final HttpServletRequest request)
            throws ResourceNotFoundException, ResourceRetrievalException {
        // get the metadata first, we'll need that to populate header fields
        ResourceMetadata resourceMetadata = dataSource.readResourceMetadata(identifier);

        HttpHeaders httpHeaders = buildHttpHeaders(resourceMetadata);

        InputStream resourceContent = dataSource.readResourceContent(identifier);
        InputStreamResource inputStreamResource = new InputStreamResource(resourceContent);
        return new ResponseEntity(inputStreamResource, httpHeaders, HttpStatus.OK);
    }

    @RequestMapping(value="/{identifier}", method = {RequestMethod.DELETE})
    public void delete(@PathVariable("identifier") final String identifier) throws ResourceNotFoundException {
        dataSource.deleteResource(identifier);
    }

    private HttpHeaders buildHttpHeaders(ResourceMetadata resourceMetadata) {
        HttpHeaders httpHeaders = new HttpHeaders();
        for (String metadataKey : resourceMetadata.metadataKeys()) {
            if (StandardHeaders.isStandardHeader(metadataKey))
                httpHeaders.set(metadataKey, resourceMetadata.getMetadataAsString(metadataKey));
            else
                httpHeaders.set("xxx-" + metadataKey, resourceMetadata.getMetadataAsString(metadataKey));

        }
        return httpHeaders;
    }

    /**
    /**
     * Extract the header fields that match the 'miscellaneous metadata' pattern convention.
     *
     * @param request
     * @return
     */
    private Map<String, Object> extractMetadata(final HttpServletRequest request) {
        Map<String, Object> result = new HashMap<>();

        Enumeration<String> headerNames = request.getHeaderNames();
        if (headerNames == null) {
            throw new IllegalArgumentException("J2EE environment does not support getHeaderNames().");
        }
        while(headerNames.hasMoreElements()) {
            String headerName = headerNames.nextElement();
            Matcher matcher = METADATA_KEY_PATTERN.matcher(headerName);
            if (matcher.matches()) {
                String metadataName = matcher.group(METADATA_NAME_GROUP);
                result.put(metadataName.toLowerCase(), request.getHeader(headerName));
            }
        }

        return result;
    }

}
