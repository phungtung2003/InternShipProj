package com.jr.couponkeeper.merchants.service.impl;

import com.alibaba.fastjson.JSON;
import com.jr.couponkeeper.merchants.constant.Constants;
import com.jr.couponkeeper.merchants.constant.ErrorCode;
import com.jr.couponkeeper.merchants.dao.MerchantDao;
import com.jr.couponkeeper.merchants.entity.Merchant;
import com.jr.couponkeeper.merchants.service.IMerchantService;
import com.jr.couponkeeper.merchants.vo.CouponTemplate;
import com.jr.couponkeeper.merchants.vo.CreateMerchantRequest;
import com.jr.couponkeeper.merchants.vo.CreateMerchantResponse;
import com.jr.couponkeeper.merchants.vo.Response;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Optional;

@Service
@Slf4j
public class MerchantServiceImpl implements IMerchantService {

    private final MerchantDao merchantDao;
    /**kafka的客户端*/
    private final KafkaTemplate<String,String> kafkaTemplate;

    @Autowired
    public MerchantServiceImpl(MerchantDao merchantDao, KafkaTemplate<String, String> kafkaTemplate) {
        this.merchantDao = merchantDao;
        this.kafkaTemplate = kafkaTemplate;
    }

    @Override
    @Transactional //因为涉及到save所以一定要添加事务(成功就保存，失败就回滚）！！！
    public Response createMerchant(CreateMerchantRequest request) {
        Response response = new Response();
        CreateMerchantResponse merchantResponse = new CreateMerchantResponse();

        //校验商户注册信息是否符合要求，在createMerchantRequest里面有validate方法
        ErrorCode errorCode = request.validate(merchantDao);
        if(errorCode != ErrorCode.SUCCESS){
            merchantResponse.setId(-1);
            //设置给用户看的错误码和相关描述
            response.setErrorCode(errorCode.getCode());
            response.setErrorMsg(errorCode.getDescription());
        }else {
            //先保存该商户，然后取出其id值进行返回
            merchantResponse.setId(merchantDao.save(request.toMerchant()).getId());
        }
        //如果成功则返回生成的Id，失败则是-1
        response.setData(merchantResponse);
        return response;
    }

    /**如果*/
    @Override
    public Response buildMerchantInfoById(Integer id) {
        Response response = new Response();
        Optional<Merchant> merchant = merchantDao.findById(id);

        if(!merchant.isPresent()){
            response.setErrorCode(ErrorCode.MERCHANTS_NOT_EXIST.getCode());
            response.setErrorMsg(ErrorCode.MERCHANTS_NOT_EXIST.getDescription());
        }
        response.setData(merchant.get());
        return response;
    }

    /**商户在平台上投放优惠券*/
    @Override
    public Response promoteCouponTemplate(CouponTemplate couponTemplate) {
        Response response = new Response();
        ErrorCode errorCode = couponTemplate.validate(merchantDao);

        //查看该商户是否存在，只有存在的情况下才能用kafka
        if(errorCode != ErrorCode.SUCCESS){
            response.setErrorCode(errorCode.getCode());
            response.setErrorMsg(errorCode.getDescription());
        }else {
            //把couponTemplate转换成string方便投放，用Json序列化的方式
            String couponTemplateString  = JSON.toJSONString(couponTemplate);
            //通过kafka客户端进行投放
            kafkaTemplate.send(
                    Constants.TEMPLATE_TOPIC, //topic
                    Constants.TEMPLATE_TOPIC, //key
                    couponTemplateString //value
            );
            log.info("promoteCouponTemplate:{}", couponTemplateString);
        }
        return response;
    }
}
