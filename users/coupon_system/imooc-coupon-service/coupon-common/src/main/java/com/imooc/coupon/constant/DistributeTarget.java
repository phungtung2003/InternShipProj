package com.imooc.coupon.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Objects;
import java.util.stream.Stream;

@Getter
@AllArgsConstructor
public enum DistributeTarget {

    SINGLE("user need to acquire the coupon", 1),
    MULTI("sysem distribute to the users", 2);
    private String description;
    private Integer code;

    public static DistributeTarget of(Integer code) {
        Objects.requireNonNull(code);
        return Stream.of(values())
                .filter(bean -> bean.code.equals((code)))
                .findAny()
                .orElseThrow(()->new IllegalArgumentException(code + "does not exist!"));
    }
}
