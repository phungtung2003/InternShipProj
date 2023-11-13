package com.jr.couponkeeper.merchants.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * <h1>通用的响应对象</h1>
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Response {

    /**如果正确，返回0*/
    private Integer errorCode = 0;
    /**如果正确，返回空*/
    private String errorMsg = "";
    /**返回的对象*/
    private Object data;

    /**正确响应的构造函数*/
    public Response(Object data){
        this.data = data;
    }

}
