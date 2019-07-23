package org.poem.collection.push;

import com.alibaba.fastjson.JSONObject;
import org.poem.QuartzInstanceInfo;
import org.poem.config.FixedQuartzConfig;
import org.poem.ssl.header.QuartzAccept;
import org.poem.ssl.url.OhttpUrl;
import org.poem.vo.QuartzServiceClass;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.commons.util.InetUtilsProperties;
import org.springframework.core.SpringProperties;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;

import java.util.List;

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
    private QuartzInstanceInfo build(List<QuartzServiceClass> quartzServiceClasses) {
        String ip = inetUtilsProperties.getDefaultIpAddress();
        String hostName = inetUtilsProperties.getDefaultHostname();
        return QuartzInstanceInfo.Builder.ip(ip).appName(getApplicationName()).hostName(hostName).quartzServiceClasses(quartzServiceClasses).build();
    }

    /**
     * 推送
     */
    public void push(List<QuartzServiceClass> quartzServiceClasses) {
        String uri = "http://" + fixedQuartzConfig.getName().toUpperCase() + ":" + fixedQuartzConfig.getPort() + OhttpUrl.serverPath;
        HttpHeaders headers = new HttpHeaders();
        headers.add(QuartzAccept.QUARTZ_ACCEPT_HEADER_KEY, QuartzAccept.QUARTZ_ACCEPT_HEADER_VALUE);
        headers.add("Content-Type", "application/json; charset=utf-8");
        HttpEntity<String> entity = new HttpEntity<>(JSONObject.toJSONString(build(quartzServiceClasses)), headers);

        logger.info(" future uri : " + uri);
        logger.info(" " +QuartzAccept.QUARTZ_ACCEPT_HEADER_KEY + ": " + QuartzAccept.QUARTZ_ACCEPT_HEADER_VALUE);
        logger.info(" Content-Type : application/json; charset=utf-8" );

        String request = restTemplate.
                postForEntity(uri, entity, String.class, new Object()).getBody();
        logger.info(" this request is : " + request);
    }
}
