package com.walid.music.filter;

import static com.netflix.zuul.constants.ZuulConstants.ZUUL_INITIAL_STREAM_BUFFER_SIZE;
import static com.walid.music.logging.LogUtils.normaliseURI;
import static javax.servlet.http.HttpServletResponse.SC_INTERNAL_SERVER_ERROR;
import static org.springframework.cloud.netflix.zuul.filters.support.FilterConstants.POST_TYPE;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Collections;
import java.util.List;
import java.util.zip.GZIPInputStream;

import org.apache.commons.lang.StringUtils;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.stereotype.Component;

import com.google.common.io.CharStreams;
import com.netflix.config.DynamicIntProperty;
import com.netflix.config.DynamicPropertyFactory;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;
import com.walid.music.logging.LogUtils;

/**
 * Intercepts the search response and conditionally enriches it with more details. For instance,
 * retrieves artist's releases for searches yielding one artist
 *
 * @author wmoustaf
 */
@Component
public class EnrichFilter extends ZuulFilter {

    private static final Logger logger = LoggerFactory.getLogger(EnrichFilter.class);
    private static final DynamicIntProperty STREAM_BUFFER_SIZE = DynamicPropertyFactory.getInstance().getIntProperty(
            ZUUL_INITIAL_STREAM_BUFFER_SIZE, 8192);
    private static final List<String> enrichableRequestURIs = Collections.singletonList(
            "/music/artist");

    @Override
    public String filterType() {
        return POST_TYPE;
    }

    @Override
    public int filterOrder() {
        return 0;
    }

    @Override
    public boolean shouldFilter() {
        RequestContext context = RequestContext.getCurrentContext();
        String requestURI = normaliseURI(context.getRequest().getRequestURI());
        LogUtils.setMDC(requestURI);

        logger.debug("Checking response eligibility for enrichment");

        if (!enrichableRequestURIs.contains(requestURI)) {
            return false;
        }

        String responseBody = extractResponseBody(context);
        if (getEntityCount(responseBody) == 1) {
            logger.debug("Response eligible for enrichment with more details");
            return true;
        }
        return false;
    }

    /**
     * extracts response body from {@link RequestContext} as a string
     * 
     * @param context
     *            Zuul request context
     * @return response body string
     */
    private String extractResponseBody(RequestContext context) {
        if (context.getResponseBody() != null) {
            return context.getResponseBody();
        } else if (context.getResponseDataStream() != null) {
            try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream(
                    STREAM_BUFFER_SIZE.get())) {
                IOUtils.copy(context.getResponseDataStream(), outputStream);
                final byte[] bytes = outputStream.toByteArray();
                context.setResponseDataStream(new ByteArrayInputStream(bytes));
                return CharStreams.toString(
                        new InputStreamReader(
                                new GZIPInputStream(new ByteArrayInputStream(bytes))));
            } catch (IOException e) {
                final String error = "Failed to extract response body";
                logger.error(error, e);
                context.setResponseStatusCode(SC_INTERNAL_SERVER_ERROR);
                return StringUtils.EMPTY;
            }
        }
        return StringUtils.EMPTY;
    }

    /**
     * extracts the count of entities from the response body
     * 
     * @param response
     *            response body in JSON format
     * @return entity count
     */
    private int getEntityCount(String response) {
        if (StringUtils.isNotBlank(response)) {
            try {
                JSONObject result = new JSONObject(response);
                return result.getInt("count");
            } catch (JSONException e) {
                return Integer.MIN_VALUE;
            }
        }
        return Integer.MIN_VALUE;
    }

    @Override
    public Object run() throws ZuulException {
        logger.info("search returned a single entity. Fetching entity specific details...");

        // TODO enrich response here

        // Zuul ignore it anyway
        return null;
    }
}
