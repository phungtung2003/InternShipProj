package com.jr.couponkeeper.merchants.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data //getter setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "t_merchant")
public class Merchant {
// 'id' int(10) unsigned NOT NULL AUTO_INCREMENT,
//    'name' varchar(64) COLLATE utf8_bin NOT NULL,
//    'logo_url' varchar(256) COLLATE utf8_bin NOT NULL,
//    'business_license_url' varchar(256) COLLATE utf8_bin NOT NULL,
//    'phone' varchar(64) COLLATE utf8_bin NOT NULL,
//    'address' varchar(64) COLLATE utf8_bin NOT NULL,
//    'is_audit' BOOLEAN NOT NULL COMMENT 'verify if business license ok',


    @Id
    @GeneratedValue
    @Column(name = "id", nullable = false)
    private Integer id;

    @Basic
    @Column(name = "name", unique = true, nullable = false)
    private String name;

    @Column(name = "logo_url", nullable = false)
    private String logoUrl;

    @Column(name = "business_license_url", nullable = false)
    private String businessLicenseUrl;

    @Column(name = "phone", nullable = false)
    private String phone;

    @Column(name = "address", nullable = false)
    private String address;

    @Column(name = "isAudit", nullable = false)
    private Boolean isAudit = false;
}
