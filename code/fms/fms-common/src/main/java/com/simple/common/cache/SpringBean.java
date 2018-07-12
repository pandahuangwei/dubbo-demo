package com.simple.common.cache;

import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;

/**
 * @author Panda.HuangWei.
 * @version 1.0 .
 * @since 2017.02.15 18:42.
 */
public abstract class SpringBean implements InitializingBean, DisposableBean {
    protected BeanFactory beanFactory;
    private String beanName;

    public BeanFactory getBeanFactory() {
        return beanFactory;
    }

    public void setBeanFactory(BeanFactory beanFactory) {
        this.beanFactory = beanFactory;
    }

    public String getBeanName() {
        return beanName;
    }

    public void setBeanName(String beanName) {
        this.beanName = beanName;
    }

    @Override
    public void afterPropertiesSet() throws Exception {

    }

    @Override
    public void destroy() {

    }
}
