package com.jr.couponkeeper.merchants.dao;

import com.jr.couponkeeper.merchants.entity.Merchant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface MerchantDao extends JpaRepository<Merchant,Integer>, JpaSpecificationExecutor<Merchant> {



    /**
     * @param name 是唯一的
     * @return {@link Merchant}
     */

    public Merchant findByName(String name);

}
