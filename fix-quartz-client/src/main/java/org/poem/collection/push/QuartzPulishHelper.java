package org.poem.collection.push;

import org.poem.QuartzInstanceInfo;
import org.poem.config.FixedQuartzConfig;
import org.poem.ssl.url.OhttpUrl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.commons.util.InetUtilsProperties;
import org.springframework.core.SpringProperties;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;

/**
 * 发布
 *
 * @author poem
 */
@Service

public class QuartzPulishHelper {

    private static final Logger logger = LoggerFactory.getLogger(QuartzPulishHelper.class);

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private FixedQuartzConfig fixedQuartzConfig;

    @Autowired
    private InetUtilsProperties inetUtilsProperties;

    /**
     * 执行
     */
    private QuartzInstanceInfo quartzInstanceInfo;

    /**
     * get application name
     *
     * @return
     */
    private String getApplicationName() {
        String applicationName = SpringProperties.getProperty("spring.application.name");
        return StringUtils.hasText(applicationName) ? applicationName : "application";
    }


    /**
     * 获取端口
     *
     * @return
     */
    public String getPort() {
        return SpringProperties.getProperty("server.port");
    }


    /**
     * 创建
     */
    public void build() {
        String ip = inetUtilsProperties.getDefaultIpAddress();
        String hostName = inetUtilsProperties.getDefaultHostname();
    }

    /**
     * 推送
     */
    public void push() {
        String request = restTemplate.
                postForEntity(fixedQuartzConfig.getName() + OhttpUrl.serverPath, quartzInstanceInfo, String.class, new Object[0]).getBody();
        logger.info("this request is : " + request);
    }
}
