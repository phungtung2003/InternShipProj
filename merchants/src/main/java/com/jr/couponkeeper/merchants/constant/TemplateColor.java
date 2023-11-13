package com.jr.couponkeeper.merchants.constant;

/**
 优惠券的背景颜色的枚举
 */
public enum TemplateColor {
    RED(1,"red"),
    GREEN(2,"green"),
    BLUE(3,"blue")


    ;

    private Integer code;
    private String color;


    TemplateColor(Integer code, String color) {
        this.code = code;
        this.color = color;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }
}
