package com.imooc.coupon.schedule;

import com.imooc.coupon.dao.CouponTemplateDao;
import com.imooc.coupon.entity.CouponTemplate;
import com.imooc.coupon.vo.TemplateRule;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

// clean expired template
@Slf4j
@Component
public class ScheduledTask {
    private final CouponTemplateDao templateDao;
    @Autowired
    public ScheduledTask(CouponTemplateDao templateDao) {
        this.templateDao = templateDao;
    }

    // clean template per hour
    @Scheduled(fixedDelay =  60 * 60 * 10000)
    public void offlineCouponTemplate() {
        log.info("Start to expire CouponTemplate!");
        List<CouponTemplate> templates = templateDao.findAllByExpired(false);
        if(CollectionUtils.isEmpty(templates)) {
            log.info("Expiration done!");
            return;
        }
        Date cur = new Date();
        List<CouponTemplate> expiredTemplates = new ArrayList<>(templates.size());
        templates.forEach(template -> {
            TemplateRule rule = template.getRule();
            if(rule.getExpiration().getDeadline() < cur.getTime()) {
                template.setExpired(true);
                expiredTemplates.add(template);
            }
        });
        if(!CollectionUtils.isEmpty(expiredTemplates)) {
            log.info("Expiration done! expired templates number: {}", templateDao.saveAll(expiredTemplates));
        }
    }
}
