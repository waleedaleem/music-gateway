package com.walid.music.logging;

import java.util.UUID;

import org.apache.commons.lang.StringUtils;
import org.slf4j.MDC;

/**
 * generic loggic utility class
 * 
 * @author wmoustaf
 */
public class LogUtils {

    private static final String REQUEST_ID = "REQUEST_ID";
    private static final String REQUEST_URI = "REQUEST_URI";

    /**
     * sets logging MDC with request identifier for traceability
     * 
     * @param requestURI
     *            requestURI
     */
    public static void setMDC(String requestURI) {
        if (MDC.get(REQUEST_ID) == null) {
            MDC.put(REQUEST_ID, UUID.randomUUID().toString());
            MDC.put(REQUEST_URI, normaliseURI(requestURI));
        }
    }

    /**
     * cleans up the redundant slash at the end of a URI if exists
     *
     * @param uri
     *            end point uri
     * @return the uri without redundant slash
     */
    public static String normaliseURI(String uri) {
        if (StringUtils.isNotBlank(uri) && uri.endsWith("/")) {
            return uri.substring(0, uri.length() - 1);
        } else {
            return uri;
        }
    }
}
