package com.imooc.coupon.service.impl;

import com.imooc.coupon.dao.CouponTemplateDao;
import com.imooc.coupon.entity.CouponTemplate;
import com.imooc.coupon.exception.CouponException;
import com.imooc.coupon.service.ITemplateBaseService;
import com.imooc.coupon.vo.CouponTemplateSDK;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

// query coupon templates
@Slf4j
@Service
public class TemplateBaseServiceImpl implements ITemplateBaseService {
    private final CouponTemplateDao templateDao;

    public TemplateBaseServiceImpl(CouponTemplateDao templateDao) {
        this.templateDao = templateDao;
    }
    @Override
    public CouponTemplate buildTemplateInfo(Integer id) throws CouponException {
        Optional<CouponTemplate> template = templateDao.findById(id);
        if(!template.isPresent()){
            throw new CouponException("Template_" + id + " not exists!!");
        }

        return template.get();
    }

    @Override
    public List<CouponTemplateSDK> findAllUsableTemplates() {
        List<CouponTemplate> templates = templateDao.findAllByAvailableAndExpired(true, false);
        return templates.stream().map(this::templateToTemplateSDK).collect(Collectors.toList());
    }

    @Override
    public Map<Integer, CouponTemplateSDK> findIdsToTemplateSDK(Collection<Integer> ids) {
        List<CouponTemplate> templates = templateDao.findAllById(ids);
        return templates.stream().map(this::templateToTemplateSDK).collect(Collectors.toMap(CouponTemplateSDK::getId, Function.identity()));
    }

    private CouponTemplateSDK templateToTemplateSDK(CouponTemplate template) {
        return  new CouponTemplateSDK(
                template.getId(),
                template.getName(),
                template.getLogo(),
                template.getDesc(),
                template.getCategory().getCode(),
                template.getProductLine().getCode(),
                template.getKey(),
                template.getTarget().getCode(),
                template.getRule()
        );
    }
}
