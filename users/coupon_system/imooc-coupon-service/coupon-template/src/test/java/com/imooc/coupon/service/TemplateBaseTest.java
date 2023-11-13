package com.imooc.coupon.service;

import com.alibaba.fastjson.JSON;
import com.imooc.coupon.exception.CouponException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.Arrays;

@SpringBootTest
@RunWith(SpringRunner.class)
public class TemplateBaseTest {
    @Autowired
    private ITemplateBaseService baseService;

    @Test
    public void testBuildTemplateInfo() throws CouponException {
        System.out.println(JSON.toJSONString(baseService.buildTemplateInfo(10)));
    }

    @Test
    public void testFindAllUsableTemplate() {
        System.out.println(JSON.toJSONString(baseService.findAllUsableTemplates()));
    }

    @Test
    public void testFindIdsToTemplateSDK() {
        System.out.println(JSON.toJSONString(baseService.findIdsToTemplateSDK(Arrays.asList(1,2,3,10))));
    }
}
