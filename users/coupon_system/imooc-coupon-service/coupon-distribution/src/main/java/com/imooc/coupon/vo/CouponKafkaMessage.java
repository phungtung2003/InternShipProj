package com.imooc.coupon.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CouponKafkaMessage {
    // change the status of "ids" to "status"
    private Integer status;

    private List<Integer> ids;
}
