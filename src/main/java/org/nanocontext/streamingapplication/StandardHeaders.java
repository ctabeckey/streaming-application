package org.nanocontext.streamingapplication;

import org.springframework.http.HttpHeaders;

import java.util.ArrayList;
import java.util.List;

public class StandardHeaders {
    private final static List<String> STANDARD_HEADERS;

    static {
        STANDARD_HEADERS = new ArrayList<>();

        STANDARD_HEADERS.add(HttpHeaders.ACCEPT);
        STANDARD_HEADERS.add(HttpHeaders.ACCEPT_CHARSET);
        STANDARD_HEADERS.add(HttpHeaders.ACCEPT_ENCODING);
        STANDARD_HEADERS.add(HttpHeaders.ACCEPT_LANGUAGE);
        STANDARD_HEADERS.add(HttpHeaders.ACCEPT_RANGES);
        STANDARD_HEADERS.add(HttpHeaders.ACCESS_CONTROL_ALLOW_CREDENTIALS);
        STANDARD_HEADERS.add(HttpHeaders.ACCESS_CONTROL_ALLOW_HEADERS);
        STANDARD_HEADERS.add(HttpHeaders.ACCESS_CONTROL_ALLOW_METHODS);
        STANDARD_HEADERS.add(HttpHeaders.ACCESS_CONTROL_ALLOW_ORIGIN);
        STANDARD_HEADERS.add(HttpHeaders.ACCESS_CONTROL_EXPOSE_HEADERS);
        STANDARD_HEADERS.add(HttpHeaders.ACCESS_CONTROL_MAX_AGE);
        STANDARD_HEADERS.add(HttpHeaders.ACCESS_CONTROL_REQUEST_HEADERS);
        STANDARD_HEADERS.add(HttpHeaders.ACCESS_CONTROL_REQUEST_METHOD);
        STANDARD_HEADERS.add(HttpHeaders.AGE);
        STANDARD_HEADERS.add(HttpHeaders.ALLOW);
        STANDARD_HEADERS.add(HttpHeaders.AUTHORIZATION);
        STANDARD_HEADERS.add(HttpHeaders.CACHE_CONTROL);
        STANDARD_HEADERS.add(HttpHeaders.CONNECTION);
        STANDARD_HEADERS.add(HttpHeaders.CONTENT_ENCODING);
        STANDARD_HEADERS.add(HttpHeaders.CONTENT_DISPOSITION);
        STANDARD_HEADERS.add(HttpHeaders.CONTENT_LANGUAGE);
        STANDARD_HEADERS.add(HttpHeaders.CONTENT_LENGTH);
        STANDARD_HEADERS.add(HttpHeaders.CONTENT_LOCATION);
        STANDARD_HEADERS.add(HttpHeaders.CONTENT_RANGE);
        STANDARD_HEADERS.add(HttpHeaders.CONTENT_TYPE);
        STANDARD_HEADERS.add(HttpHeaders.COOKIE);
        STANDARD_HEADERS.add(HttpHeaders.DATE);
        STANDARD_HEADERS.add(HttpHeaders.ETAG);
        STANDARD_HEADERS.add(HttpHeaders.EXPECT);
        STANDARD_HEADERS.add(HttpHeaders.EXPIRES);
        STANDARD_HEADERS.add(HttpHeaders.FROM);
        STANDARD_HEADERS.add(HttpHeaders.HOST);
        STANDARD_HEADERS.add(HttpHeaders.IF_MATCH);
        STANDARD_HEADERS.add(HttpHeaders.IF_MODIFIED_SINCE);
        STANDARD_HEADERS.add(HttpHeaders.IF_NONE_MATCH);
        STANDARD_HEADERS.add(HttpHeaders.IF_RANGE);
        STANDARD_HEADERS.add(HttpHeaders.IF_UNMODIFIED_SINCE);
        STANDARD_HEADERS.add(HttpHeaders.LAST_MODIFIED);
        STANDARD_HEADERS.add(HttpHeaders.LINK);
        STANDARD_HEADERS.add(HttpHeaders.LOCATION);
        STANDARD_HEADERS.add(HttpHeaders.MAX_FORWARDS);
        STANDARD_HEADERS.add(HttpHeaders.ORIGIN);
        STANDARD_HEADERS.add(HttpHeaders.PRAGMA);
        STANDARD_HEADERS.add(HttpHeaders.PROXY_AUTHENTICATE);
        STANDARD_HEADERS.add(HttpHeaders.PROXY_AUTHORIZATION);
        STANDARD_HEADERS.add(HttpHeaders.RANGE);
        STANDARD_HEADERS.add(HttpHeaders.REFERER);
        STANDARD_HEADERS.add(HttpHeaders.RETRY_AFTER);
        STANDARD_HEADERS.add(HttpHeaders.SERVER);
        STANDARD_HEADERS.add(HttpHeaders.SET_COOKIE);
        STANDARD_HEADERS.add(HttpHeaders.SET_COOKIE2);
        STANDARD_HEADERS.add(HttpHeaders.TE);
        STANDARD_HEADERS.add(HttpHeaders.TRAILER);
        STANDARD_HEADERS.add(HttpHeaders.TRANSFER_ENCODING);
        STANDARD_HEADERS.add(HttpHeaders.UPGRADE);
        STANDARD_HEADERS.add(HttpHeaders.USER_AGENT);
        STANDARD_HEADERS.add(HttpHeaders.VARY);
        STANDARD_HEADERS.add(HttpHeaders.VIA);
        STANDARD_HEADERS.add(HttpHeaders.WARNING);
        STANDARD_HEADERS.add(HttpHeaders.WWW_AUTHENTICATE);
    }

    private StandardHeaders() {}

    public static boolean isStandardHeader(final String value) {
        return STANDARD_HEADERS.contains(value);
    }

}
