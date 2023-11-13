package com.imooc.coupon.service;

import com.imooc.coupon.entity.CouponTemplate;
import com.imooc.coupon.exception.CouponException;
import com.imooc.coupon.vo.CouponTemplateSDK;

import java.util.Collection;
import java.util.List;
import java.util.Map;

public interface ITemplateBaseService {

    // get template info based on id
    CouponTemplate buildTemplateInfo(Integer id) throws CouponException;

    // query all available coupon templates
    List<CouponTemplateSDK> findAllUsableTemplates();

    Map<Integer, CouponTemplateSDK> findIdsToTemplateSDK(Collection<Integer> ids);
}
