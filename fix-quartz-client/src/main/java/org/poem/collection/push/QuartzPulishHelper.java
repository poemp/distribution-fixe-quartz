package org.poem.collection.push;

import org.poem.vo.QuartzServiceClass;
import org.springframework.core.SpringProperties;
import org.springframework.util.StringUtils;

import java.util.List;

/**
 * 发布
 *
 * @author poem
 */

public class QuartzPulishHelper {

    /**
     * 执行
     */
    private List<QuartzServiceClass> quartzServiceClasses;

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
     * 推送
     */
    public void push() {

    }
}
