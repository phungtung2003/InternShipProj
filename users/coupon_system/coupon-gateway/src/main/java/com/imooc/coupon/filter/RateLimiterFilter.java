package com.imooc.coupon.filter;

import com.google.common.util.concurrent.RateLimiter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

// the filter to limit the rate
@Slf4j
@Component
@SuppressWarnings("all")
public class RateLimiterFilter extends AbstractPreZuulFilter {

    // get two token per 2s
    RateLimiter rateLimiter = RateLimiter.create(2.0);
    @Override
    protected Object cRun() {
        HttpServletRequest request = context.getRequest();
        if(rateLimiter.tryAcquire()) {
            log.info("get rate token successfully!");
            return success();
        }else{
            log.error("rate limit: {}", request.getRequestURI());
            return fail(402, "error: rate is limited!");
        }
    }

    @Override
    public int filterOrder() {
        return 1;
    }
}
