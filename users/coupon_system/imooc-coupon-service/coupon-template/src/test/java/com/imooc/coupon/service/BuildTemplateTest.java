package com.imooc.coupon.service;

import com.alibaba.fastjson.JSON;
import com.imooc.coupon.constant.CouponCategory;
import com.imooc.coupon.constant.DistributeTarget;
import com.imooc.coupon.constant.PeriodType;
import com.imooc.coupon.constant.ProductLine;
import com.imooc.coupon.vo.TemplateRequest;
import com.imooc.coupon.vo.TemplateRule;
import org.apache.commons.lang3.time.DateUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;
import java.util.Collections;
import java.util.Date;

// CouponTemplate Service Test
@SpringBootTest
@RunWith(SpringRunner.class)
public class BuildTemplateTest {
    @Autowired
    private IBuildTemplateService buildTemplateService;

    @Test
    public void testBuildTemplate() throws Exception {
        System.out.println(JSON.toJSONString(
                buildTemplateService.buildTemplate(fakeTemplateRequest())
        ));
        // buildTemplate() includes an async process, so set a delay to ensure the async process can complete.
        // otherwise the test method will be invoked and return instantly, the async process has no time to be executed
        Thread.sleep(10000);
    }

    private TemplateRequest fakeTemplateRequest() {
        TemplateRequest request = new TemplateRequest();
        request.setName("CouponTemplateForTest" + new Date().getTime());
        request.setLogo("a test logo");
        request.setDesc("a test template");
        request.setCategory(CouponCategory.FIXED_MOENYOFF.getCode());
        request.setProductLine(ProductLine.DAMAO.getCode());
        request.setCount(10000);
        request.setUserId(10001L);
        request.setTarget(DistributeTarget.SINGLE.getCode());

        TemplateRule rule = new TemplateRule();
        rule.setExpiration(new TemplateRule.Expiration(
                PeriodType.SHIFT.getCode(),
                1,
                DateUtils.addDays(new Date(), 60).getTime()
        ));
        rule.setDiscount(new TemplateRule.Discount(5, 1));
        rule.setLimitation(1);
        rule.setUsage(new TemplateRule.Usage("Ontario", "Toronto", JSON.toJSONString(Arrays.asList("food", "makeup"))));
        rule.setWeight(JSON.toJSONString(Collections.EMPTY_LIST));

        request.setRule(rule);

        return request;
    }
}
