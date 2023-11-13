package com.imooc.coupon.converter;

// convert coupon category code (in entity) to code String (in database)

import com.imooc.coupon.constant.CouponCategory;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter
public class CouponCategoryConverter implements AttributeConverter<CouponCategory, String> {

    // convert CouponCategory to String storing in database (for persisting)
    @Override
    public String convertToDatabaseColumn(CouponCategory couponCategory) {
        return couponCategory.getCode();
    }

    // convert String code to CouponCategory code (for querying)
    @Override
    public CouponCategory convertToEntityAttribute(String code) {
        return CouponCategory.of(code);
    }
}
