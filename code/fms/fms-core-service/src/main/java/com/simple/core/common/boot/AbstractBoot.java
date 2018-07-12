package com.simple.core.common.boot;

import com.simple.common.config.AppServerUtils;
import com.simple.common.config.BufferConfigMgr;
import com.simple.common.config.ConfigBufferReader;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.InitializingBean;

import java.util.Iterator;


public abstract class AbstractBoot implements InitializingBean {
    protected static final Logger logger = Logger.getLogger(AbstractBoot.class);

    @Override
    public void afterPropertiesSet() throws Exception {
        try {
            BufferConfigMgr.getInstance().initial(AppServerUtils.getAppServerDir(),".properties",true);
            PropertiesConfiguration cfg=BufferConfigMgr.getInstance().getConfig("service");
            if(null!=cfg){
                logger.info("\n************************************************\n" +
                        ConfigBufferReader.getString(cfg,"dubbo.application.name","NO")
                        +":"+ConfigBufferReader.getString(cfg,"dubbo.application.description","NO")+"\n"+
                        "************************************************\n");
                logger.info("service config list:");
                Iterator<String> iterator = cfg.getKeys();
                while (iterator.hasNext())
                {
                    String key = iterator.next();
                    logger.info(key+"="+cfg.getString(key));
                }
                logger.info("\n");
            }else{
                logger.error("Can not find file:service.properties!");
                logger.info("Service is starting...");
            }
            onBoot();
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
    }

    /**
     * 启动入口：需要重载实现
     */
    protected abstract void onBoot();
}
