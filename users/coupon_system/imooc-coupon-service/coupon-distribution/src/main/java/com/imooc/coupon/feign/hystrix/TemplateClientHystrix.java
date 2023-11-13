package com.imooc.coupon.feign.hystrix;

import com.imooc.coupon.feign.TemplateClient;
import com.imooc.coupon.vo.CommonResponse;
import com.imooc.coupon.vo.CouponTemplateSDK;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@Slf4j
@Component
public class TemplateClientHystrix implements TemplateClient {

    @Override
    public CommonResponse<List<CouponTemplateSDK>> findAllUsableTemplate() {
        log.error("coupon-template microservice findAllUsableTemplate shutdown!!! hystrix triggered!!!");
        return new CommonResponse<>(-1, "findAllUsableTemplate shutdown", Collections.emptyList());
    }

    @Override
    public CommonResponse<Map<Integer, CouponTemplateSDK>> findIdsToTemplateSDK(Collection<Integer> ids) {
        log.error("coupon-template microservice findIdsToTemplateSDK shutdown!!! hystrix triggered!!!");
        return new CommonResponse<>(-1, "findIdsToTemplateSDK shutdown", Collections.emptyMap());
    }
}
