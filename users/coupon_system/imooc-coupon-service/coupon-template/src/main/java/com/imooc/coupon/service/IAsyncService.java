package com.imooc.coupon.service;

import com.imooc.coupon.entity.CouponTemplate;
import org.springframework.scheduling.annotation.Async;

public interface IAsyncService {

    void asyncConstructCouponByTemplate(CouponTemplate template);
}
