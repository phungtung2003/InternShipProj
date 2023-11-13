package com.jr.couponkeeper.merchants.service;

import com.jr.couponkeeper.merchants.vo.CouponTemplate;
import com.jr.couponkeeper.merchants.vo.CreateMerchantRequest;
import com.jr.couponkeeper.merchants.vo.Response;

/**
 * <h1>创建商户服务的接口</h1>
 */
public interface IMerchantService {

    Response createMerchant(CreateMerchantRequest request);

    Response buildMerchantInfoById(Integer id);

    Response promoteCouponTemplate(CouponTemplate couponTemplate);

}
