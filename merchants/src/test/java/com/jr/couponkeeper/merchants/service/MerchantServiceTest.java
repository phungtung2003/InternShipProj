package com.jr.couponkeeper.merchants.service;

import com.alibaba.fastjson.JSON;
import com.jr.couponkeeper.merchants.constant.TemplateColor;
import com.jr.couponkeeper.merchants.dao.MerchantDao;
import com.jr.couponkeeper.merchants.vo.CouponTemplate;
import com.jr.couponkeeper.merchants.vo.CreateMerchantRequest;
import com.jr.couponkeeper.merchants.vo.Response;
import org.apache.commons.lang.time.DateUtils;
import org.assertj.core.util.DateUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.junit4.SpringRunner;

import javax.transaction.Transactional;
import java.util.Date;

/**
 * <h1>测试MerchantServiceImpl里的所有方法</h1>
 * 做测试之前别忘了把resources文件夹也拷贝到test里面
 */
@RunWith(SpringJUnit4ClassRunner.class)
//@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
public class MerchantServiceTest {

    //不要写成了MerchantServiceImpl哦
    @Autowired
    private IMerchantService merchantService;

    @Test
    //当@Transactional检测到是@Test状态时，默认回滚（因为是测试，不应该改动数据库）
    //如果是test又想要修改数据库的话，可以再加一个@Rollback(false)
    @Transactional
    public void testCreateMerchant(){
        CreateMerchantRequest merchantRequest = new CreateMerchantRequest();
        merchantRequest.setName("KFC_Paris");
        merchantRequest.setAddress("5 street");
        merchantRequest.setBusinessLicenseUrl("KFC_Paris_Business_License");
        merchantRequest.setPhone("+33 7 78 98 78 66");
        merchantRequest.setLogoUrl("www.kfc.fr");

        Response response = merchantService.createMerchant(merchantRequest);
        System.out.println(JSON.toJSONString(response));
    }


    @Test
    public void testBuildMerchantInfoById(){
        Response response = merchantService.buildMerchantInfoById(5);
        System.out.println(JSON.toJSONString(response));
    }

    @Test
    public void testPromoteCouponTemplate(){
        CouponTemplate couponTemplate = new CouponTemplate();
        couponTemplate.setMerchantId(5);
        couponTemplate.setTitle("KFC_breakfast_promo");
        couponTemplate.setDesc("20% reduction");
        couponTemplate.setHasToken(false);
        couponTemplate.setSummary("20% reduction for all coupon holders");
        couponTemplate.setBackground(TemplateColor.GREEN.getCode());
        couponTemplate.setStart(new Date());
        couponTemplate.setEnd(DateUtils.addDays(new Date(), 20)); //从今天开始，直到20天后

        Response response = merchantService.promoteCouponTemplate(couponTemplate);
        System.out.println(JSON.toJSONString(response));


    }
}
