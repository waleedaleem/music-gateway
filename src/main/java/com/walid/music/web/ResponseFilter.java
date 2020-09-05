package com.walid.music.web;

import static org.springframework.cloud.netflix.zuul.filters.support.FilterConstants.POST_TYPE;

import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;

/**
 * Intercepts the search response and retrieves artist's releases for searches yielding one artist
 * 
 * @author wmoustaf
 */
@Component
public class ResponseFilter extends ZuulFilter {

    private static final Logger logger = LoggerFactory.getLogger(ResponseFilter.class);

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

        logger.info("Served request ID=" + requestID + " - URI=" + requestURI);

        return null;
    }
}
