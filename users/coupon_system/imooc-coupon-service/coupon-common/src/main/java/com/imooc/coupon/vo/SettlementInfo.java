package com.imooc.coupon.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SettlementInfo {
    private Long userId;
    private List<GoodsInfo> goodsInfos;
    private List<CouponAndTemplateInfo> couponAndTemplateInfos;

    // employ or actually use the coupon? true: employ, false: use
    private Boolean employ;
    // final cost fee after coupon
    private Double cost;
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    private static class CouponAndTemplateInfo {
        private Integer id;
        private CouponTemplateSDK templateSDK;
    }
}
