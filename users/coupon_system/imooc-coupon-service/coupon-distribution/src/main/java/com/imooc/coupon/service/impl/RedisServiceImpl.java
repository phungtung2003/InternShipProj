package com.imooc.coupon.service.impl;

import com.alibaba.fastjson.JSON;
import com.imooc.coupon.CouponStatus;
import com.imooc.coupon.constant.Constant;
import com.imooc.coupon.entity.Coupon;
import com.imooc.coupon.exception.CouponException;
import com.imooc.coupon.service.IRedisService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.RandomUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.SessionCallback;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Slf4j
@Service
public class RedisServiceImpl implements IRedisService {
    private final StringRedisTemplate redisTemplate;
    @Autowired
    public RedisServiceImpl(StringRedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }
    @Override
    public List<Coupon> getCacheCoupons(Long userId, Integer status) {
        log.info("Get Coupons From Cache: {}, {}", userId, status);
        String redisKey = statusToRedisKey(status, userId);
        List<String> couponStrs = redisTemplate.opsForHash().values(redisKey).stream().map(o -> Objects.toString(o)).collect(Collectors.toList());
        if(CollectionUtils.isEmpty(couponStrs)) {
            saveEmptyCouponListToCache(userId, Collections.singletonList(status));
            return Collections.emptyList();
        }
        return couponStrs.stream().map(s -> JSON.parseObject(s, Coupon.class)).collect(Collectors.toList());
    }

    @Override
    @SuppressWarnings("all")
    public void saveEmptyCouponListToCache(Long userId, List<Integer> status) {
        log.info("Save Empty List to Cache. UserId: {}, Status: {}", userId, JSON.toJSONString(status));
        Map<String, String> invalidCouponMap = new HashMap<>();
        invalidCouponMap.put("-1", JSON.toJSONString(Coupon.invalidCoupon()));
        // Redis Session Callback, put command into Redis pipeline (async)
        // k: redisKey(status), v: map
        // mapK: couponId, mapV: coupon
        SessionCallback<Object> sessionCallback = new SessionCallback<Object>() {
            @Override
            public Object execute(RedisOperations redisOperations) throws DataAccessException {
                status.forEach(s -> {
                    String redisKey = statusToRedisKey(s, userId);
                    redisOperations.opsForHash().putAll(redisKey, invalidCouponMap);
                });
                return null;
            }
        };
        log.info("Redis Pipeline: {}", JSON.toJSONString(redisTemplate.executePipelined(sessionCallback)));
    }

    @Override
    public String tryToAcquireCouponCodeFromCache(Integer templateId) {
        String redisKey = String.format("%s%s", Constant.RedisPredix.COUPON_TEMPLATE, templateId.toString());
        // left or right pop, all ok
        String couponCode = redisTemplate.opsForList().leftPop(redisKey);
        log.info("Acquire coupon code:{}, {}, {}", templateId, redisKey, couponCode);
        return couponCode;
    }

    @Override
    public Integer addCouponToCache(Long userId, List<Coupon> coupons, Integer status) throws CouponException {
        log.info("Add coupon to cache: {}, {}, {}", userId, JSON.toJSONString(coupons), status);
        // number of coupons to be saved
        Integer result = -1;
        CouponStatus couponStatus = CouponStatus.of(status);
        switch (couponStatus) {
            case USABLE:
                result = addCouponToCacheForUsable(userId, coupons);
                break;
            case USED:
                result = addCouponToCacheForUsed(userId, coupons);
                break;
            case EXPIRED:
                result = addCouponToCacheForExpired(userId, coupons);
                break;
        }
        return result;
    }

    private Integer addCouponToCacheForUsable(Long userId, List<Coupon> coupons) {
        log.debug("Add usable coupon");
        Map<String, String> needCacheObject = new HashMap<>();
        coupons.forEach(c -> needCacheObject.put(c.getId().toString(), JSON.toJSONString(c)));
        String redisKey = statusToRedisKey(CouponStatus.USABLE.getCode(),userId);
        redisTemplate.opsForHash().putAll(redisKey, needCacheObject);
        log.info("Add {} Coupons to Cache: {}, {}", needCacheObject.size(), userId, redisKey);

        redisTemplate.expire(redisKey, getRandomExpirationTime(1, 2), TimeUnit.SECONDS);

        return needCacheObject.size();
    }
    @SuppressWarnings("all")
    private Integer addCouponToCacheForUsed(Long userId, List<Coupon> coupons) throws CouponException {
        log.debug("Add used coupon");
        Map<String, String> needCacheForUsed = new HashMap<>(coupons.size());
        String redisKeyForUsable = statusToRedisKey(CouponStatus.USABLE.getCode(), userId);
        String redisKeyForUsed = statusToRedisKey(CouponStatus.USED.getCode(), userId);
        List<Coupon> curUsableCoupons = getCacheCoupons(userId, CouponStatus.USABLE.getCode());
        assert curUsableCoupons.size() > coupons.size();
        coupons.forEach(c -> needCacheForUsed.put(c.getId().toString(), JSON.toJSONString(c)));

        List<Integer> curUsableIds = curUsableCoupons.stream().map(Coupon::getId).collect(Collectors.toList());
        List<Integer> paramIds = coupons.stream().map(Coupon::getId).collect(Collectors.toList());
        if(!CollectionUtils.isSubCollection(paramIds, curUsableIds)) {
            log.error("paramIds and curIds are not compatible!!!");
           throw new CouponException("paramIds and curIds are not compatible!!!");
        }
        List<String> needCleanKey = paramIds.stream().map(i -> i.toString()).collect(Collectors.toList());
        SessionCallback sessionCallback = new SessionCallback() {
            @Override
            public Object execute(RedisOperations redisOperations) throws DataAccessException {
                // add used coupons
                redisOperations.opsForHash().putAll(redisKeyForUsed, needCacheForUsed);
                // clean relative usable coupons
                redisOperations.opsForHash().delete(redisKeyForUsable, needCleanKey.toArray());
                // update expiration time
                redisOperations.expire(redisKeyForUsable, getRandomExpirationTime(1, 2), TimeUnit.SECONDS);
                redisOperations.expire(redisKeyForUsed, getRandomExpirationTime(1, 2), TimeUnit.SECONDS);
                return null;
            }
        };
        log.info("Pipeline result: {}", JSON.toJSONString(redisTemplate.executePipelined(sessionCallback)));
        return coupons.size();
    }

    @SuppressWarnings("all")
    private Integer addCouponToCacheForExpired(Long userId, List<Coupon> coupons) throws CouponException {
        log.debug("Add expired coupon");
        Map<String, String> needCacheForExpired = new HashMap<>(coupons.size());
        String redisKeyForUsable = statusToRedisKey(CouponStatus.USABLE.getCode(), userId);
        String redisKeyForExpired = statusToRedisKey(CouponStatus.EXPIRED.getCode(), userId);
        List<Coupon> curUsableCoupons = getCacheCoupons(userId, CouponStatus.USABLE.getCode());
        assert curUsableCoupons.size() > coupons.size();

        coupons.forEach(c -> needCacheForExpired.put(c.getId().toString(), JSON.toJSONString(c)));
        List<Integer> curUsableIds = curUsableCoupons.stream().map(Coupon::getId).collect(Collectors.toList());
        List<Integer> paramIds = coupons.stream().map(Coupon::getId).collect(Collectors.toList());

        if(!CollectionUtils.isSubCollection(paramIds, curUsableIds)) {
            log.error("paramIds and curIds are not compatible!!!");
            throw new CouponException("paramIds and curIds are not compatible!!!");
        }
        List<String> needCleanKey = paramIds.stream().map(i -> i.toString()).collect(Collectors.toList());
        SessionCallback sessionCallback = new SessionCallback() {
            @Override
            public Object execute(RedisOperations redisOperations) throws DataAccessException {
                redisOperations.opsForHash().putAll(redisKeyForExpired, needCacheForExpired);
                redisOperations.opsForHash().delete(redisKeyForExpired, needCleanKey.toArray());
                redisOperations.expire(redisKeyForUsable, getRandomExpirationTime(1, 3), TimeUnit.SECONDS);
                redisOperations.expire(redisKeyForExpired, getRandomExpirationTime(1, 3), TimeUnit.SECONDS);
                return null;
            }
        };
        log.info("Pipeline result: {}", JSON.toJSONString(redisTemplate.executePipelined(sessionCallback)));
        return coupons.size();
    }

    private String statusToRedisKey(Integer status, Long userId) {
        String redisKey = null;
        CouponStatus couponStatus = CouponStatus.of(status);
        switch (couponStatus) {
            case USABLE:
                redisKey = String.format("%s%s", Constant.RedisPredix.USER_COUPON_USABLE, userId);
                break;
            case USED:
                redisKey = String.format("%s%s", Constant.RedisPredix.USER_COUPON_USED, userId);
                break;
            case EXPIRED:
                redisKey = String.format("%s%s", Constant.RedisPredix.USER_COUPON_EXPIRED, userId);
                break;
        }
        return redisKey;
    }

    // avoid cache avalanche by setting random expiration time for the redis keys.
    private Long getRandomExpirationTime(Integer min, Integer max) {
        return RandomUtils.nextLong(min*60*60, max*60*60);
    }
}
