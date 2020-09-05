package com.walid.music.filter;

import static org.springframework.cloud.netflix.zuul.filters.support.FilterConstants.POST_TYPE;

import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;

/**
 * Logs the received response code corresponding to the request identifiers
 * 
 * @author wmoustaf
 */
@Component
public class TracingFilter extends ZuulFilter {

    private static final Logger logger = LoggerFactory.getLogger(TracingFilter.class);

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
        return true;
    }

    @Override
    public Object run() throws ZuulException {
        final RequestContext context = RequestContext.getCurrentContext();

        String requestID = UUID.randomUUID().toString();
        String requestURI = context.getRequest().getRequestURI();
        MDC.put("requestID", requestID);
        MDC.put("requestURI", requestURI);

        logger.info("Response received. status code: {}.", context.getResponseStatusCode());

        // Zuul ignore it anyway
        return null;
    }
}
