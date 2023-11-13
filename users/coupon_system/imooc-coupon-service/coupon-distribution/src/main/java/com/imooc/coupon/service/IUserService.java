package com.imooc.coupon.service;

import com.imooc.coupon.entity.Coupon;
import com.imooc.coupon.exception.CouponException;
import com.imooc.coupon.vo.AcquireTemplateRequest;
import com.imooc.coupon.vo.CouponTemplateSDK;
import com.imooc.coupon.vo.SettlementInfo;

import java.util.List;

public interface IUserService {
    List<Coupon> findCouponByStatus(Long userId, Integer status) throws CouponException;

    List<CouponTemplateSDK> findAvailableTemplate(Long userId) throws CouponException;

    // user get coupon
    Coupon acquireTemplate(AcquireTemplateRequest request) throws CouponException;

    // arg info has no Double cost, return info has Double cost
    // get the cost after coupon
    SettlementInfo settlement(SettlementInfo info) throws CouponException;
}
