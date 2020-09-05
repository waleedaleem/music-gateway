package com.walid.music.filter;

import static org.springframework.cloud.netflix.zuul.filters.support.FilterConstants.POST_TYPE;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.walid.music.logging.LogUtils;

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
        return 100;
    }

    @Override
    public boolean shouldFilter() {
        return true;
    }

    @Override
    public Object run() {
        final RequestContext context = RequestContext.getCurrentContext();
        LogUtils.setMDC(context.getRequest().getRequestURI());

        logger.info("Response received. status code: {}.", context.getResponseStatusCode());

        // Zuul ignore it anyway
        return null;
    }
}
