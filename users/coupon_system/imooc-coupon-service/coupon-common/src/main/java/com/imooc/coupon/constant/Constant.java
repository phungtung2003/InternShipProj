package com.imooc.coupon.constant;


public class Constant {

    // kafka topic
    public static final String TOPIC = "imooc_user_coupon_op";

    // redis key prefix
    public static class RedisPredix {

        // coupon code
        public static final String COUPON_TEMPLATE = "imooc_coupon_template_code_";

        // current all usable coupons for the user
        public static final String USER_COUPON_USABLE = "imooc_user_coupon_usable_";

        public static final String USER_COUPON_USED = "imooc_user_coupon_used_";

        public static final String USER_COUPON_EXPIRED = "imooc_user_coupon_expired_";
    }
}
