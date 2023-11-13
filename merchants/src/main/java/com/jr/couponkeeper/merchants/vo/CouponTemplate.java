package com.jr.couponkeeper.merchants.vo;

import com.jr.couponkeeper.merchants.constant.ErrorCode;
import com.jr.couponkeeper.merchants.dao.MerchantDao;
import com.jr.couponkeeper.merchants.entity.Merchant;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * <h1>对投放的优惠券的对象定义</h1>
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CouponTemplate {

    /**所属商户的id,不是优惠券的id*/
    private Integer merchantId;
    /**优惠券的标题是唯一的*/
    private String title;
    private String summary;
    private String desc;
    /**优惠券发行的最大数量，如果没有限制的话，可以传null，-1都可以*/
    private Long limit;
    private Boolean hasToken;//token存储在redis中，每次领取从redis中获取
    private Integer background; //值是枚举类TempateColor中的值，数字
    private Date start;
    private Date end;


    /**
     * 校验优惠券的有效性，该商户是否存在
     * @param merchantDao {@link MerchantDao}
     * @return {@link ErrorCode}
     */
    public ErrorCode validate(MerchantDao merchantDao){
        //findById返回的是Optional，所以要加get()
         Merchant merchant = merchantDao.findById(merchantId).get();
         if(merchant == null){
             return ErrorCode.MERCHANTS_NOT_EXIST;
         }
         return ErrorCode.SUCCESS;
    }


}
