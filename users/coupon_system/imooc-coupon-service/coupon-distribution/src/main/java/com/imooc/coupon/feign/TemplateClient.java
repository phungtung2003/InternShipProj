package com.imooc.coupon.feign;

import com.imooc.coupon.feign.hystrix.TemplateClientHystrix;
import com.imooc.coupon.vo.CommonResponse;
import com.imooc.coupon.vo.CouponTemplateSDK;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Collection;
import java.util.List;
import java.util.Map;

@FeignClient(value = "eureka-client-coupon-template", fallback = TemplateClientHystrix.class)
public interface TemplateClient {

    @RequestMapping(value = "coupon-template/template/sdk/all", method = RequestMethod.GET)
    CommonResponse<List<CouponTemplateSDK>> findAllUsableTemplate();

    @RequestMapping(value = "coupon-template/template/sdk/infos")
    CommonResponse<Map<Integer, CouponTemplateSDK>> findIdsToTemplateSDK(@RequestParam("ids") Collection<Integer> ids);
}
