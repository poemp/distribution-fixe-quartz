package org.poem;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

/**
 * @author poem
 */
@EnableEurekaClient
@SpringBootApplication
public class QuartzClientApplication {

    public static void main(String[] args) {
        SpringApplication.run(QuartzClientApplication.class, args);
    }
}
