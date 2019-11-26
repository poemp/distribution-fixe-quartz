package org.poem;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

/**
 * @author poem
 */
@EnableDiscoveryClient
@SpringBootApplication
public class QuartzServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(QuartzServerApplication.class, args);
        QuartzServerApplication quartzServerApplication = new QuartzServerApplication();
        quartzServerApplication.done();
    }

    @Bean
    @LoadBalanced
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    @Autowired
    private RestTemplate restTemplate;

    @Bean
    @LoadBalanced
    public  RestTemplate getRestTemplate(){
        return new RestTemplate();
    }

    private void done(){
        String s = restTemplate.getForEntity("http://www.jiaobu365.com/payn/order/pid/34635.html",String.class).getBody();
        System.out.println(s);

    }

}
