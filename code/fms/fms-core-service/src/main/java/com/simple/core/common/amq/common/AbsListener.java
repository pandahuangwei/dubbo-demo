package com.simple.core.common.amq.common;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.jms.*;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

/**
 * 消息转换处理监听类
 *
 * @author Panda.HuangWei.
 * @version 1.0 .
 * @since 2017.04.17 14:51.
 */
public abstract class AbsListener implements MessageListener {
    private static Logger logger = LoggerFactory.getLogger(AbsListener.class);

    abstract void DealWithMsgMapValue(Map<String, Object> dicMap);

    abstract void DealWithMsgTextValue(String text);

    @Override
    @SuppressWarnings("unchecked")
    public void onMessage(Message message) {
        try {
            if (message instanceof MapMessage) {
                MapMessage mapMsg = (MapMessage) message;
                Map<String, Object> dicMap = new HashMap<>();

                for (Enumeration<String> em = mapMsg.getMapNames(); em.hasMoreElements(); ) {
                    String key = em.nextElement();
                    dicMap.put(key, mapMsg.getObject(key));
                }
                //Map消息监听接口
                this.DealWithMsgMapValue(dicMap);
            } else if (message instanceof TextMessage) {
                TextMessage textMsg = (TextMessage) message;
                String text;
                text = textMsg.getText();
                //Text消息监听接口
                this.DealWithMsgTextValue(text);
            }
        } catch (JMSException e) {
            logger.error("Paser is Err:{}", e.getCause());
        }
    }

}
