package com.jr.couponkeeper.merchants.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * <h1>创建商户响应对象</h1>
 * 如果创建失败，这里并没有返回ErrorCode,然后在页面弹出错误信息。而是通过Log的方式去解决
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateMerchantResponse {

    /**创建成功返回id，创建失败则是-1*/
    private Integer id;


}
