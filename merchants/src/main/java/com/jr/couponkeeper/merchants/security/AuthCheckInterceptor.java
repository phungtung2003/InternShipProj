package com.jr.couponkeeper.merchants.security;

import com.jr.couponkeeper.merchants.constant.Constants;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 权限校验
 * 拦截器：拦截没有提供正确token的线程
 * ps:然后要去MerchantsApplication里面注册拦截器
 */
@Component
public class AuthCheckInterceptor implements HandlerInterceptor {
    /**
     *校验：检查线程里面是否携带token信息，如果携带，才能执行controller
     * token信息是放在http请求的header里面的
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        //getHeader(KEY的名字)，返回的是value (也就是key)
        String token = request.getHeader(Constants.TOKEN_SRTING);

        //如果header里面压根没有传相关的key
        if(StringUtils.isEmpty(token)){
            throw new Exception("We don't find " + Constants.TOKEN_SRTING + " related info in Header");
        }
        //如果key名字正确，但是value不正确
        if(!token.equals(Constants.TOKEN)){
            //这里千万别写成Constants.TOKEN了，否则就泄露了token的信息了
            throw new Exception("In Header, the value of " + Constants.TOKEN_SRTING + "is wrong");
        }

        AccessContext.setToken(token);
        return true;
    }

    /*
   postHandle vs afterCompletion
   postHandle是controller里的方法执行之后，如果有exception, 则不会再执行postHandler
   但是afterCompletion无论controller里发生什么都会执行，适合做一些清理工作
     */
    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {

    }

    /**
     *清理线程里面携带的token
     */
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        AccessContext.clearAccessKey();
    }
}
