package com.jr.couponkeeper.merchants.constant;

public enum ErrorCode {

    SUCCESS(0,""),
    DUPLICATE_NAME(1,"Name is duplicated"),
    EMPTY_LOGO(2, "Logo is empty"),
    EMPTY_BUSINESS_LICENSE(3,"Business license is empty"),
    ERROR_PHONE(4,"Phone number is wrong"),
    EMPTY_ADDRESS(5, "Address can not be empty"),
    MERCHANTS_NOT_EXIST(6, "This merchant doesn't exist"),
    EMPTY_NAME(7,"The name is empty");

    private Integer code;
    private String description;

    ErrorCode(Integer code, String description) {
        this.code = code;
        this.description = description;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
