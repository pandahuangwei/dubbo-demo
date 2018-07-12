package com.simple.common.utils;

import org.apache.commons.beanutils.BeanUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.core.io.support.PropertiesLoaderUtils;

import java.io.IOException;
import java.util.Map;
import java.util.Properties;

/**
 * 描述：根据指定文件来配置指定类的属性值<br>
 * 可以做到不配使用默认值，配了覆盖默认值得效果<br>
 * 避免增加配置时多个节点都需要修改，因为大多数情况都可以使用默认值
 *
 * @author Panda.HuangWei.
 * @version 1.0 .
 * @since 2017.02.08 10:55.
 */
public class PropertyConfigurer implements InitializingBean {

    Object configObject;
    String location;

    @Override
    public void afterPropertiesSet() throws Exception {
        Properties allProperties = PropertiesLoaderUtils.loadAllProperties(location);
        Map<String, String> configObjectProperties = BeanUtils.describe(configObject);

        for (Map.Entry<String, String> entry : configObjectProperties.entrySet()) {
            String propertyName = entry.getKey();
            Object value = allProperties.get(propertyName);
            if (value != null) {
                BeanUtils.setProperty(configObject, propertyName, value);
            }
        }
    }

    public void setConfigObject(Object configObject) {
        this.configObject = configObject;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public static void main(String[] args) {
        try {
            Properties allProperties = PropertiesLoaderUtils.loadAllProperties("classpath:service.properties");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
