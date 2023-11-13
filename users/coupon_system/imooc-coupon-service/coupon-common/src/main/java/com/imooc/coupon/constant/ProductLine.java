package com.imooc.coupon.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Objects;
import java.util.stream.Stream;

// enum for product line
@Getter
@AllArgsConstructor
public enum ProductLine {
    DAMAO("the first product line: damao", 1),
    DABAO("the second product line: dabao", 2);
    private String description;
    private Integer code;

    public static ProductLine of(Integer code) {
        Objects.requireNonNull(code);
        return Stream.of(values())
                .filter(bean -> bean.code.equals((code)))
                .findAny()
                .orElseThrow(()->new IllegalArgumentException(code + "does not exist!"));
    }
}
