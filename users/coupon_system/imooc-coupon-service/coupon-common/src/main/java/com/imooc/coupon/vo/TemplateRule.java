package com.imooc.coupon.vo;

// the rule for creating the coupon

import com.imooc.coupon.constant.PeriodType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TemplateRule {
    private Expiration expiration;
    private Discount discount;
    // the maximum coupons can one user get
    private Integer limitation;
    private Usage usage;
    // what other coupons can be used at the same time with this coupon (same type coupon can not be used together)
    private String weight;



    // expiration rule
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Expiration {
        // PeriodType.code
        private Integer period;
        // valid time gap for fixed type
        private Integer gap;
        // deadline for fixed and changing type
        private Long deadline;
        boolean validate() {
            return null != PeriodType.of(period) && gap > 0 && deadline > 0;
        }
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Discount {
        // the amount of discount
        private Integer quota;
        // the threshold for full money off
        private Integer base;

        boolean validate() {
            return quota > 0 && base > 0;
        }
    }

    // specific situations where users can use the coupon
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Usage {
        private String province;
        private String city;
        // the type of the products that can be bought with coupon
        private String goodsType;

        boolean validate() {
            return StringUtils.isNotEmpty(province) && StringUtils.isNotEmpty(city) && StringUtils.isNotEmpty(goodsType);
        }
    }

    public boolean validate() {
        return expiration.validate() && discount.validate() && limitation > 0 && usage.validate() && StringUtils.isNotEmpty(weight);
    }
}
