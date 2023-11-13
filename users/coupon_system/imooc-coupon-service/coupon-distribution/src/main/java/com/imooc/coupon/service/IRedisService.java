package com.imooc.coupon.service;

import com.imooc.coupon.entity.Coupon;
import com.imooc.coupon.exception.CouponException;

import java.util.List;

public interface IRedisService {

    // get coupon list record in cache based on userId and status
    List<Coupon> getCacheCoupons(Long userId, Integer status);

    // save empty list to cache for preventing cache penetration
    void saveEmptyCouponListToCache(Long userId, List<Integer> status);

    // user tries to get coupon code from cache based on templateId
    String tryToAcquireCouponCodeFromCache(Integer templateId);

    // return the number of coupons saved successfully
    Integer addCouponToCache(Long userId, List<Coupon> coupons, Integer status) throws CouponException;
}
