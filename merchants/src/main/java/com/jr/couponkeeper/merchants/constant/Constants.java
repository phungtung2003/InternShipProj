package com.jr.couponkeeper.merchants.constant;

/*
通用常量定义类
 */
public class Constants {

    /**商户优惠券投放的kafka topic*/
    public static final String TEMPLATE_TOPIC = "merchants-template";

    /**给通过验证的商户发放的token， 这是header的key*/
    public static final String TOKEN_SRTING = "token";

    /**具体的token信息，这里是hearder中发送的value，与上面的TOKEN_STRING共同组成键值对*/
    public static final String TOKEN = "jr-couponkeeper-merchants";
}
