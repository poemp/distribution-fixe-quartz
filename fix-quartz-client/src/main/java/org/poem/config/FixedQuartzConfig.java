package org.poem.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @author poem
 */
@Configuration
@ConfigurationProperties(prefix = "fixed.serve", ignoreUnknownFields = true)
public class FixedQuartzConfig {

    /**
     * ser
     */
    private String name;


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
