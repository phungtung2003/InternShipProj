package com.imooc.coupon.service.impl;

import com.alibaba.fastjson.JSON;
import com.imooc.coupon.CouponStatus;
import com.imooc.coupon.constant.Constant;
import com.imooc.coupon.dao.CouponDao;
import com.imooc.coupon.entity.Coupon;
import com.imooc.coupon.exception.CouponException;
import com.imooc.coupon.feign.SettlementClient;
import com.imooc.coupon.feign.TemplateClient;
import com.imooc.coupon.service.IRedisService;
import com.imooc.coupon.service.IUserService;
import com.imooc.coupon.vo.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Service
public class UserServiceImpl implements IUserService {
    private final CouponDao couponDao;
    private final IRedisService redisService;
    private final TemplateClient templateClient;
    private final SettlementClient settlementClient;
    // Kafka client
    private final KafkaTemplate<String, String> kafkaTemplate;
    @Autowired
    public UserServiceImpl (CouponDao couponDao, IRedisService redisService, TemplateClient templateClient, SettlementClient settlementClient, KafkaTemplate<String, String> kafkaTemplate) {
        this.couponDao = couponDao;
        this.redisService = redisService;
        this.templateClient = templateClient;
        this.settlementClient = settlementClient;
        this.kafkaTemplate = kafkaTemplate;
    }
    @Override
    public List<Coupon> findCouponByStatus(Long userId, Integer status) throws CouponException {
        List<Coupon> curCached = redisService.getCacheCoupons(userId, status);
        List<Coupon> preTarget;
        if(CollectionUtils.isNotEmpty(curCached)) {
            log.debug("current cached coupons, userId: {}, status: {}", userId, status);
            preTarget = curCached;
        } else {
            log.debug("coupon cached is empty! get coupon, userId: {}, status: {}", userId, status);
            List<Coupon> dbCoupons = couponDao.findAllByUserIdAndStatus(userId, CouponStatus.of(status));
            if(CollectionUtils.isEmpty(dbCoupons)) {
                log.debug("current db is empty!!!, userId: {}, status: {}", userId, status);
                return dbCoupons;
            }

            // CouponTemplateSDK is transient (not stored in DB), should be added manually
            Map<Integer, CouponTemplateSDK> idToTemplateSDK = templateClient.findIdsToTemplateSDK(
                    dbCoupons.stream().map(Coupon::getTemplateId).collect(Collectors.toList())
            ).getData();
            dbCoupons.forEach(dc -> dc.setTemplateSDK(idToTemplateSDK.get(dc.getTemplateId())));
            preTarget = dbCoupons;

            // add data queried from db to cache
            redisService.addCouponToCache(userId, preTarget, status);
        }

        // remove the -1 invalid coupon
        preTarget = preTarget.stream().filter((c -> c.getId() != -1)).collect(Collectors.toList());
        if(CouponStatus.of(status) == CouponStatus.USABLE) {
            CouponClassify classify = CouponClassify.classify(preTarget);
            if(CollectionUtils.isNotEmpty(classify.getExpired())) {
                redisService.addCouponToCache(userId, classify.getExpired(), CouponStatus.EXPIRED.getCode());
                // use kafka to send to db
                kafkaTemplate.send(
                        Constant.TOPIC,
                        JSON.toJSONString(new CouponKafkaMessage(
                                CouponStatus.EXPIRED.getCode(),
                                classify.getExpired().stream().map(Coupon::getId).collect(Collectors.toList())
                        ))
                );
            }
            return classify.getUsable();
        }

        return preTarget;
    }

    @Override
    public List<CouponTemplateSDK> findAvailableTemplate(Long userId) throws CouponException {
        return null;
    }

    @Override
    public Coupon acquireTemplate(AcquireTemplateRequest request) throws CouponException {
        return null;
    }

    @Override
    public SettlementInfo settlement(SettlementInfo info) throws CouponException {
        return null;
    }
}
