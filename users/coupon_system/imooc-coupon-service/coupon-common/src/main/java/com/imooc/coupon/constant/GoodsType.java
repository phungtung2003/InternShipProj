package com.imooc.coupon.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Objects;
import java.util.stream.Stream;

@Getter
@AllArgsConstructor
public enum GoodsType {
    ENTERTAINMENT("entertainment", 1),
    SEAFOOD("seafood", 2),
    FURNITURE("furniture", 3),
    OTHERS("others", 4),
    ALL("all", 5);

    private String description;
    private Integer code;
    private static GoodsType of(Integer code) {
        Objects.requireNonNull(code);
        return Stream.of(values())
                .filter(bean -> bean.code.equals(code))
                .findAny()
                .orElseThrow(()->new IllegalArgumentException(code + "does not exist!"));
    }
}
