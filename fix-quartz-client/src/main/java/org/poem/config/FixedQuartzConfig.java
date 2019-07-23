package org.poem.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @author poem
 */
@Configuration
@ConfigurationProperties(prefix = "fixed.server", ignoreUnknownFields = false)
public class FixedQuartzConfig {

    /**
     * ser
     */
    private String name;


    private String port;

    public String getPort() {
        return port;
    }

    public void setPort(String port) {
        this.port = port;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
