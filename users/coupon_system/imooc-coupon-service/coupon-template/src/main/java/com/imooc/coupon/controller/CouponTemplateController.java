package com.imooc.coupon.controller;

import com.alibaba.fastjson.JSON;
import com.imooc.coupon.entity.CouponTemplate;
import com.imooc.coupon.exception.CouponException;
import com.imooc.coupon.service.IBuildTemplateService;
import com.imooc.coupon.service.ITemplateBaseService;
import com.imooc.coupon.vo.CouponTemplateSDK;
import com.imooc.coupon.vo.TemplateRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
public class CouponTemplateController {
    private final IBuildTemplateService buildTemplateService;
    private final ITemplateBaseService templateBaseService;

    @Autowired
    public CouponTemplateController(IBuildTemplateService buildTemplateService, ITemplateBaseService templateBaseService) {
        this.buildTemplateService = buildTemplateService;
        this.templateBaseService = templateBaseService;
    }

    // localhost:7001/coupon-template/template/build
    // visit through gateway: localhost:9000/imooc/coupon-template/template/build
    @PostMapping("/template/build")
    public CouponTemplate buildTemplate(@RequestBody TemplateRequest request) throws CouponException {
        log.info("Build Template: {}", JSON.toJSONString(request));
        return buildTemplateService.buildTemplate(request);
    }

    // localhost:7001/coupon-template/template/info?id=1
    @GetMapping("/template/info")
    public CouponTemplate buildTemplateInfo(@RequestParam("id") Integer id) throws CouponException {
        log.info("Build Template Info For: {}", id);
        return templateBaseService.buildTemplateInfo(id);
    }

    // localhost:7001/coupon-template/template/sdk/all
    @GetMapping("/template/sdk/all")
    public List<CouponTemplateSDK> findAllUsableTemplate() {
        log.info("Find All Usable Templates");
        return templateBaseService.findAllUsableTemplates();
    }

    // localhost:7001/coupon-template/template/sdk/infos
    @GetMapping("/template/sdk/infos")
    public Map<Integer, CouponTemplateSDK> findIdsToTemplateSDK(@RequestParam("ids") Collection<Integer> ids) {
        log.info("FindIdsToTemplateSDK: {}", JSON.toJSONString(ids));
        return templateBaseService.findIdsToTemplateSDK(ids);
    }
}
