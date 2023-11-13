package com.jr.couponkeeper.merchants.security;

/**
 * <h1>用ThreadLocal去单独存储每个线程携带的token信息</h1>
 */
public class AccessContext {

    public static final ThreadLocal<String> token = new ThreadLocal<String>();

    public static String getToken(){
        return token.get();
    }

    public static void setToken(String tokenStr){
        token.set(tokenStr);
    }

    public static void clearAccessKey(){
        token.remove();
    }

}
