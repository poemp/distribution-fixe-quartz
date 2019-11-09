package org.poem.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @author poem
 */
@Data
@Configuration
@ConfigurationProperties(prefix = "fixed.server", ignoreUnknownFields = false)
public class FixedQuartzConfig {

    /**
     * ser
     */
    private String name;
}
