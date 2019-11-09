package org.poem.servers;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

/**
 * @author poem
 */
@RestController
@RequestMapping("/eurekaCentre")
public class EurekaController {

    @Autowired
    private DiscoveryClient discoveryClient;

    /**
     * 获取注册在Eureka中的服务名称
     *
     * @return
     */
    @GetMapping("/getEurekaServices")
    public List<String> getEurekaServices() {
        List<String> services = new ArrayList<>();
        List<String> serviceNames = discoveryClient.getServices();
        for (String serviceName : serviceNames) {
            List<ServiceInstance> serviceInstances = discoveryClient.getInstances(serviceName);
            for (ServiceInstance serviceInstance : serviceInstances) {
                services.add(String.format("%s:%s", serviceName, serviceInstance.getUri()));
            }
        }
        return services;
    }

}
