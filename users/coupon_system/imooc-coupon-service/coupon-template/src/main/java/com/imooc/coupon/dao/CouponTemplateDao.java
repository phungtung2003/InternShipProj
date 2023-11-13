package com.imooc.coupon.dao;

import com.imooc.coupon.entity.CouponTemplate;
import com.sun.org.apache.xpath.internal.operations.Bool;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

// define the dao interface of CouponTemplate
// entity type: CouponTemplate, primary key type: Integer
public interface CouponTemplateDao extends JpaRepository<CouponTemplate, Integer> {
    CouponTemplate findByName(String name);

    List<CouponTemplate> findAllByAvailableAndExpired(Boolean available, Boolean expired);

    List<CouponTemplate> findAllByExpired(Boolean expired);
}
