package com.jr.couponkeeper.merchants.vo;

import com.jr.couponkeeper.merchants.constant.ErrorCode;
import com.jr.couponkeeper.merchants.dao.MerchantDao;
import com.jr.couponkeeper.merchants.entity.Merchant;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;

/**
 * <h1>商户注册时，发送请求时用到的对象</h1>
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateMerchantRequest {

    private String name;
    private String logoUrl;
    private String businessLicenseUrl;
    private String phone;
    private String address;

    public ErrorCode validate(MerchantDao merchantDao){
        if(this.name == null){
            return ErrorCode.EMPTY_NAME;
        }
        if(merchantDao.findByName(this.name) != null){
            return ErrorCode.DUPLICATE_NAME;
        }
        if(this.logoUrl == null){
            return ErrorCode.EMPTY_LOGO;
        }
        if(this.businessLicenseUrl == null){
            return ErrorCode.EMPTY_BUSINESS_LICENSE;
        }
        if(this.phone == null){
            return ErrorCode.ERROR_PHONE;
        }
        if(this.address == null){
            return ErrorCode.EMPTY_ADDRESS;
        }
        return ErrorCode.SUCCESS;
    }

    /**
     * 把请求者转换成注册商户（但是还没有验证其营业执照）
     * 也就是转换成可以保存在数据库里面的对象
     * @return
     */
    public Merchant toMerchant(){
        Merchant merchant = new Merchant();
        //this.name = name
        merchant.setName(this.name);
        merchant.setAddress(address);
        merchant.setBusinessLicenseUrl(businessLicenseUrl);
        merchant.setLogoUrl(logoUrl);
        merchant.setPhone(phone);

        return  merchant;
    }
}
