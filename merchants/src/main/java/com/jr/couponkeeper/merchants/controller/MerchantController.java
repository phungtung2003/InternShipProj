package com.jr.couponkeeper.merchants.controller;

import com.alibaba.fastjson.JSON;
import com.jr.couponkeeper.merchants.service.IMerchantService;
import com.jr.couponkeeper.merchants.vo.CouponTemplate;
import com.jr.couponkeeper.merchants.vo.CreateMerchantRequest;
import com.jr.couponkeeper.merchants.vo.Response;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * <h1>商户服务MerchantService对应的controller</h1>
 * MerchantService里面有多少个方法/服务，这里一般就要出现与之对应的controller方法
 */
@Slf4j
@RestController
@RequestMapping("/merchant")
public class MerchantController {

    private final IMerchantService merchantService;

    public MerchantController(IMerchantService merchantService) {
        this.merchantService = merchantService;
    }

    @PostMapping("/create")
    public Response createMerchant(@RequestBody CreateMerchantRequest request){
        //把请求内容以json的方式打印在日志
        log.info("CreateMerchant:{}", JSON.toJSONString(request));
        return merchantService.createMerchant(request);
    }

    @GetMapping("/{merchantId}")
    public Response buildMerchantInfoById(@PathVariable("merchantId") Integer id){
        log.info("buildMerchantInfoById：{}", id);
        return merchantService.buildMerchantInfoById(id);
    }

    @PostMapping("/promote")
    public Response promoteCouponTemplate(@RequestBody CouponTemplate couponTemplate){
        //log.info("promoteCouponTemplate:{}", JSON.toJSONString(couponTemplate));
        log.info("promoteCouponTemplate:{}", couponTemplate);
        return merchantService.promoteCouponTemplate(couponTemplate);
    }
}
