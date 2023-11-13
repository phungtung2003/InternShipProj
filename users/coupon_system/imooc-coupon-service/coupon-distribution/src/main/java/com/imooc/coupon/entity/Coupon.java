package com.imooc.coupon.entity;

// the record of the coupons gotten by users

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.imooc.coupon.CouponStatus;
import com.imooc.coupon.converter.CouponStatusConverter;
import com.imooc.coupon.serialization.CouponSerialize;
import com.imooc.coupon.vo.CouponTemplateSDK;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@EntityListeners(AuditingEntityListener.class)
@JsonSerialize(using = CouponSerialize.class)
@Table(name = "coupon")
public class Coupon {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    // a logical foreign key connected to coupon template
    @Column(name = "template_id", nullable = false)
    private Integer templateId;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(name = "coupon_code", nullable = false)
    private String couponCode;

    @CreatedDate
    @Column(name = "assign_time", nullable = false)
    private Date assignTime;

    @Column(name = "status", nullable = false)
    @Convert(converter = CouponStatusConverter.class)
    private CouponStatus status;

    @Transient
    private CouponTemplateSDK templateSDK;

    // return an invalid coupon
    public static Coupon invalidCoupon() {
        Coupon coupon = new Coupon();
        coupon.setId(-1);
        return coupon;
    }

    public Coupon(Integer templateId, Long userId, String couponCode, CouponStatus status) {
        this.templateId = templateId;
        this.userId = userId;
        this.couponCode = couponCode;
        this.status = status;
    }
}
