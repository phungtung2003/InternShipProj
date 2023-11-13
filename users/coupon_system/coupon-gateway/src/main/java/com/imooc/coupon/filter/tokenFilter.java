package com.imooc.coupon.filter;

// a filter that verifies the token in request

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

@Slf4j
@Component
public class tokenFilter extends AbstractPreZuulFilter {

    @Override
    protected Object cRun() {
        // context is already initialized in run()
        HttpServletRequest request = context.getRequest();
        log.info(String.format("%s request to %s", request.getMethod(), request.getRequestURI().toString()));
        Object token = request.getParameter("token");
        if(token == null) {
            String errorMsg = "error: token is missing!";
            log.error(errorMsg);
            return fail(401, errorMsg);
        }
        return success();
    }

    // the smaller, the more prior
    @Override
    public int filterOrder() {
        return 2;
    }
}
