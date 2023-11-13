package com.imooc.coupon.constant;

// categories of coupon

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Objects;
import java.util.stream.Stream;

@Getter
@AllArgsConstructor
public enum CouponCategory {
    FULL_MONEYOFF("coupon for Money off when exceeds a certain threshold", "001"),
    DISCOUNT("coupon for discount", "002"),
    FIXED_MOENYOFF("coupon for fixed Money off", "003");
    private String description;
    private String code;

    public static CouponCategory of(String code) {
        Objects.requireNonNull(code);
        return Stream.of(values())
                .filter(bean -> bean.code.equals((code)))
                .findAny()
                .orElseThrow(()->new IllegalArgumentException(code + "does not exist!"));
    }

}
