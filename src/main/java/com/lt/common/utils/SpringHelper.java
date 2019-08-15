package com.lt.common.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.stereotype.Component;

@Component
public class SpringHelper implements ApplicationContextAware {
    protected static final Logger LOGGER = LoggerFactory.getLogger(SpringHelper.class);
    private static ApplicationContext context;

    public SpringHelper() {
    }

    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        context = applicationContext;
    }

    public static void setContext(ApplicationContext applicationContext) {
        if (context == null) {
            context = applicationContext;
        }

    }

    public static Object getBean(String beanId) {
        try {
            return context.getBean(beanId);
        } catch (Exception var2) {
            var2.printStackTrace();
            LOGGER.error("getBean:" + beanId + "," + var2.getMessage());
            return null;
        }
    }

    public static <T> T getBean(Class<T> beanClass) {
        try {
            return context.getBean(beanClass);
        } catch (Exception var2) {
            var2.printStackTrace();
            LOGGER.error("getBean:" + beanClass + "," + var2.getMessage());
            return null;
        }
    }

    public static void cleanup() {
        context = null;
    }

    public static String getProperty(Class clazz, String attrName) {
        ConfigurableApplicationContext configurableApplicationContext = (ConfigurableApplicationContext)context;
        DefaultListableBeanFactory defaultListableBeanFactory = (DefaultListableBeanFactory)configurableApplicationContext.getBeanFactory();
        String[] strs = defaultListableBeanFactory.getBeanNamesForType(clazz);
        String[] var5 = strs;
        int var6 = strs.length;

        for(int var7 = 0; var7 < var6; ++var7) {
            String str = var5[var7];
            BeanDefinition bd = defaultListableBeanFactory.getBeanDefinition(str);
            if (bd.getBeanClassName() != null && bd.getBeanClassName().equals(clazz.getName())) {
                return bd.getAttribute(attrName) == null ? null : bd.getAttribute(attrName).toString();
            }
        }

        return null;
    }

    public static String getProperty(String beanName, String attrName) {
        ConfigurableApplicationContext configurableApplicationContext = (ConfigurableApplicationContext)context;
        DefaultListableBeanFactory defaultListableBeanFactory = (DefaultListableBeanFactory)configurableApplicationContext.getBeanFactory();
        BeanDefinition bd = defaultListableBeanFactory.getBeanDefinition(beanName);
        if (bd != null) {
            return bd.getAttribute(attrName) == null ? null : bd.getAttribute(attrName).toString();
        } else {
            return null;
        }
    }
}
