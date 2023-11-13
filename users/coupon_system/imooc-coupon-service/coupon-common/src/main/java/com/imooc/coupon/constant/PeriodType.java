package com.imooc.coupon.constant;

// expiration time enum

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Objects;
import java.util.stream.Stream;

@Getter
@AllArgsConstructor
public enum PeriodType {
    REGULAR("fixed duration", 1),
    SHIFT("changing duration starts with the date when the user get it", 2);
    private String description;
    private Integer code;

    public static PeriodType of(Integer code) {
        Objects.requireNonNull(code);
        return Stream.of(values())
                .filter(bean -> bean.code.equals((code)))
                .findAny()
                .orElseThrow(()->new IllegalArgumentException(code + "does not exist!"));
    }
}
