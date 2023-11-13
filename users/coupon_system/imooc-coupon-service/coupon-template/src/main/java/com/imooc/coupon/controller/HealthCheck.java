package com.imooc.coupon.controller;

import com.imooc.coupon.exception.CouponException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.cloud.client.serviceregistry.Registration;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
public class HealthCheck {

    // get info of other microservices from eureka server
    private final DiscoveryClient client;
    // get the current server id from eureka
    private final Registration registration;

    @Autowired
    public HealthCheck(DiscoveryClient client, Registration registration) {
        this.client = client;
        this.registration = registration;
    }
    // localhost:7001/coupon-template/health
    @GetMapping("/health")
    public String health() {
        log.debug("view health api");
        return "CouponTemplate is OK!";
    }
    // localhost:7001/coupon-template/exception
    @GetMapping("/exception")
    public String exception() throws CouponException {
        log.debug("view exception api");
        throw new CouponException("CouponTemplate has some problem");
    }

    // localhost:7001/coupon-template/info
    @GetMapping("/info")
    public List<Map<String, Object>> info() {
        // wait for 2min to get the registration info on eureka
        // there are multiple instances in one cluster (server)
        List<ServiceInstance> instances = client.getInstances(registration.getServiceId());
        List<Map<String, Object>> result = new ArrayList<>(instances.size());
        instances.forEach(i -> {
            Map<String, Object> info = new HashMap<>();
            info.put("serviceId", i.getServiceId()); // only one service id for the instances
            info.put("instanceId", i.getInstanceId()); // different id for different instance
            info.put("port", i.getPort());
            result.add(info);
        });

        return  result;
    }

}
