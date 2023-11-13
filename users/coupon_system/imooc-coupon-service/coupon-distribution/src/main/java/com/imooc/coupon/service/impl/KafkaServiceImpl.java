package com.imooc.coupon.service.impl;

//  synchronize the changes in cache to DB

import com.alibaba.fastjson.JSON;
import com.imooc.coupon.CouponStatus;
import com.imooc.coupon.constant.Constant;
import com.imooc.coupon.dao.CouponDao;
import com.imooc.coupon.entity.Coupon;
import com.imooc.coupon.service.IKafkaService;
import com.imooc.coupon.vo.CouponKafkaMessage;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Slf4j
@Component
public class KafkaServiceImpl implements IKafkaService {

    private final CouponDao couponDao;

    @Autowired
    public KafkaServiceImpl(CouponDao couponDao) {
        this.couponDao = couponDao;
    }
    @Override
    @KafkaListener(topics = {Constant.TOPIC}, groupId = "imooc-coupon-1")
    public void consumeCouponKafkaMessage(ConsumerRecord<?, ?> record) {
        Optional<?> kafkaMessage = Optional.ofNullable(record.value());
        if(kafkaMessage.isPresent()) {
            Object message = kafkaMessage.get();
            CouponKafkaMessage couponInfo = JSON.parseObject(message.toString(), CouponKafkaMessage.class);
            log.info("Receive CouponKafkaMessage: {}", message.toString());
            CouponStatus status = CouponStatus.of(couponInfo.getStatus());
            switch (status) {
                case USABLE:
                    break;
                case USED:
                    processCouponsByStatus(couponInfo, status);
                    break;
                case EXPIRED:
                    processCouponsByStatus(couponInfo, status);
                    break;
            }
        }

    }

    private void processCouponsByStatus(CouponKafkaMessage kafkaMessage, CouponStatus status) {
        List<Coupon> coupons = couponDao.findAllById(kafkaMessage.getIds());
        if(CollectionUtils.isEmpty(coupons) || coupons.size() != kafkaMessage.getIds().size()) {
            log.error("Can not find correct coupon info: {}", JSON.toJSONString(kafkaMessage));
            return;
        }

        coupons.forEach(c -> c.setStatus(status));
        log.info("CouponKafkaMessage coupon count: {}", couponDao.saveAll(coupons).size());
    }
}
