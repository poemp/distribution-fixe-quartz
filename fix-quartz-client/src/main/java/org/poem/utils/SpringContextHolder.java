package org.poem.utils;

import org.poem.collection.QuartzCollections;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Service;

/**
 * @author poem
 */
@Service
@Order(-1)
public class SpringContextHolder implements ApplicationContextAware {

    private static ApplicationContext applicationContext;


    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        SpringContextHolder.applicationContext = applicationContext;
        QuartzCollections.init();
    }

    public static ApplicationContext getApplicationContext() {
        return applicationContext;
    }
}
