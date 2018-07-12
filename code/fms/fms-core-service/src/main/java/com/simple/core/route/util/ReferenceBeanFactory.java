package com.simple.core.route.util;

import com.simple.core.common.facade.ParseMsgFacade;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

/**
 * 报文类型对应的接口，工厂实现.<br>
 * 单例工厂实现，保证单例
 * @author Panda.HuangWei.
 * @version 1.0 .
 * @since 2017.03.29 13:55.
 */
public class ReferenceBeanFactory {
    private static Logger logger = LoggerFactory.getLogger(ReferenceBeanFactory.class);
    private Map<String,ParseMsgFacade> interfaceMap = new HashMap<>();

    private ReferenceBeanFactory(){}

    private static class SingletonHolder{
        static ReferenceBeanFactory instance = new ReferenceBeanFactory();
    }

    public static ReferenceBeanFactory getInstance(){
        return SingletonHolder.instance;
    }

    public ParseMsgFacade getFacade(String msgType) {
        logger.info("msgType:{}",msgType);
        ParseMsgFacade facade = interfaceMap.get(msgType);
        if (facade == null) {
            logger.info("msgType:{},get facade is null",msgType);
        }
        return facade;
    }

    public void setInterfaceMap(Map<String, ParseMsgFacade> interfaceMap) {
        this.interfaceMap = interfaceMap;
    }
}
