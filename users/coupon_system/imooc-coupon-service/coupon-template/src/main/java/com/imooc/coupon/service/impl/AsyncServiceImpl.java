package com.imooc.coupon.service.impl;

import com.google.common.base.Stopwatch;
import com.imooc.coupon.constant.Constant;
import com.imooc.coupon.dao.CouponTemplateDao;
import com.imooc.coupon.entity.CouponTemplate;
import com.imooc.coupon.service.IAsyncService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.util.StopWatch;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
public class AsyncServiceImpl implements IAsyncService {

    private final CouponTemplateDao templateDao;

    private final StringRedisTemplate redisTemplate;

    @Autowired
    public AsyncServiceImpl(CouponTemplateDao templateDao, StringRedisTemplate redisTemplate) {
        this.templateDao = templateDao;
        this.redisTemplate = redisTemplate;
    }
    @Override
    @Async("getAsyncExecutor")
    public void asyncConstructCouponByTemplate(CouponTemplate template) {
        StopWatch watch = new StopWatch("Total build time StopWatch");
        watch.start();
        Set<String> couponCodes = buildCouponCode(template);
        String redisKey = String.format("%s%s", Constant.RedisPredix.COUPON_TEMPLATE, template.getId().toString());
        log.info("Push CouponCode to Redis {}", redisTemplate.opsForList().rightPushAll(redisKey, couponCodes));
        template.setAvailable(true);
        templateDao.save(template);
        watch.stop();
        log.info(watch.prettyPrint());

        //TODO inform the user that the coupon template is now usable!!!
        log.info("CouponTemplate({}) is available now!", template.getId());
    }
    // generate 18 digits coupon code
    private Set<String> buildCouponCode(CouponTemplate template) {
        StopWatch watch = new StopWatch("coupon code building StopWatch");
        watch.start();

        Set<String> result = new HashSet<>(template.getCount());

        // first 4 digits
        String prefix4 = template.getProductLine().getCode().toString() + template.getCategory().getCode();

        String date = new SimpleDateFormat("yyMMdd").format(template.getCreateTime());

        for(int i = 0; i != template.getCount(); i++){
            result.add(prefix4 + buildCouponCodeSuffix14(date));
        }

        while(result.size() < template.getCount()) {
            result.add(prefix4 + buildCouponCodeSuffix14(date));
        }

        assert result.size() == template.getCount();

        watch.stop();
        log.info(watch.prettyPrint());

        return result;
    }

    // generate last 14 digits code
    private String buildCouponCodeSuffix14(String date) {
        char[] bases = new char[]{'1', '2', '3', '4', '5', '6', '7', '8', '9'};
        List<Character> chars = date.chars().mapToObj(e -> (char) e).collect(Collectors.toList());
        Collections.shuffle(chars);
        String mid6 = chars.stream().map(Object::toString).collect(Collectors.joining());
        String suffix8 = RandomStringUtils.random(1, bases) + RandomStringUtils.randomNumeric(7);

        return mid6 + suffix8;
    }
}
