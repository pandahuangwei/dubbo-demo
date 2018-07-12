package com.simple.core.route.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.core.io.support.PropertiesLoaderUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

/**
 * 提供服务与调用地址的关系.
 *
 * @author Panda.HuangWei.
 * @version 1.0 .
 * @since 2017.02.08 13:50.
 */
public class RouteAddressUtil implements InitializingBean {
    private static Logger logger = LoggerFactory.getLogger(RouteAddressUtil.class);
    private String prefix = "route.";

    public static RouteAddressUtil instance = new RouteAddressUtil();
    String location;

    private Map<String, String> msgTypeAddrMap = new HashMap<String, String>();

    public String getAddress(String msgType) {
        return msgTypeAddrMap.get(msgType);
    }

    public String getAddressIgnoreCase(String msgType) {
        return msgTypeAddrMap.get(msgType.toUpperCase());
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        Properties allProperties = PropertiesLoaderUtils.loadAllProperties(location);
        Set<Map.Entry<Object, Object>> entries = allProperties.entrySet();
        int index = prefix.length();
        String srcKey;
        for (Map.Entry<Object, Object> entry : entries) {
            srcKey = String.valueOf(entry.getKey());
            if (srcKey.startsWith(prefix)) {
                String key = srcKey.substring(index);

                msgTypeAddrMap.put(key.toUpperCase(), String.valueOf(entry.getValue()));
                logger.info("======msgType:{}, address:{}",key,String.valueOf(entry.getValue()));
            }
        }

        instance = this;
    }

    public void setLocation(String location) {
        this.location = location;
    }
}
