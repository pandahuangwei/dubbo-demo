package com.simple.core.common.amq.factory;

import com.simple.common.utils.StringUtil;
import com.simple.core.common.amq.common.MessageEntity;
import com.simple.core.common.amq.exception.AMQFactoryException;
import com.simple.core.common.amq.exception.AMQSendException;

import javax.jms.MapMessage;
import javax.jms.MessageProducer;
import javax.jms.TextMessage;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.locks.ReentrantLock;

/**
 * AMQ发送消息管理类
 *
 * @author Panda.HuangWei.
 * @version 1.0 .
 * @since 2017.04.17 15:25.
 */
public class AMQSender {
    private ReentrantLock lock = new ReentrantLock();

    /**
     * 发送Map消息（带head和body）
     *
     * @param amqFactory  amq
     * @param entity      map
     * @param topicName   目的地
     * @param messageType 消息类型
     * @return boolean
     * @throws AMQSendException
     * @throws AMQFactoryException
     */
    public boolean sendMapMsg(AMQFactory amqFactory, MessageEntity entity, String topicName, int NUM, String messageType) throws AMQSendException, AMQFactoryException {
        if (null == amqFactory) {
            throw new AMQSendException("AMQFactory is null");
        }
        if (entity == null) {
            throw new AMQSendException("Message Content must be not null");
        }
        if (StringUtil.isEmpty(topicName)) {
            throw new AMQSendException("topicName must be not null");
        }
        if (NUM == 0 || NUM < 0) {
            throw new AMQSendException("NUM must greater than 0");
        }
        //进入锁定状态
        lock.lock();
        MessageProducer producer = amqFactory.getProducer(topicName, messageType);
        MapMessage mapMsg = amqFactory.getMapMessage();
        Map<String, Object> headMap = entity.getHeadMap();
        Map<String, Object> bodyMap = entity.getBodyMap();
        List<String> keyList = new ArrayList<>(bodyMap.keySet());
        boolean ifHasMsg = false;//是否还有剩余没法送消息标识
        try {
            //存放head信息
            if (headMap.size() > 0 && !headMap.isEmpty()) {
                for (Map.Entry<String, Object> entry : headMap.entrySet()) {
                    mapMsg.setObjectProperty(entry.getKey(), entry.getValue());
                }
            }

            //存放body信息
            for (int i = 0, size = keyList.size(); i < size; i++) {
                ifHasMsg = true;
                mapMsg.setObject(keyList.get(i), bodyMap.get(keyList.get(i)));
                if ((i + 1) % NUM == 0) {
                    producer.send(mapMsg);
                    mapMsg.clearBody();
                    ifHasMsg = false;
                }
            }
            //如果还存在剩余没有发送的消息则把剩余消息发送
            if (ifHasMsg) {
                producer.send(mapMsg);
                mapMsg.clearBody();
            }
            return true;
        } catch (Exception e) {
            throw new AMQSendException("send Map Message Error", e);
        } finally {
            lock.unlock();
        }
    }

    /**
     * 发送Map消息
     *
     * @param amqFactory  amq
     * @param dictMap     map
     * @param topicName   目的地
     * @param messageType 消息类型
     * @return boolean
     * @throws AMQSendException
     * @throws AMQFactoryException
     */
    public boolean sendMapMsg(AMQFactory amqFactory, Map<String, Object> dictMap, String topicName, int NUM, String messageType) throws AMQSendException, AMQFactoryException {
        if (null == amqFactory) {
            throw new AMQSendException("AMQFactory is null");
        }
        if (dictMap.size() == 0) {
            throw new AMQSendException("Message Content must be not null");
        }
        if (null == topicName || "".equals(topicName)) {
            throw new AMQSendException("topicName must be not null");
        }
        if (NUM == 0 || NUM < 0) {
            throw new AMQSendException("NUM must greater than 0");
        }
        //进入锁定状态
        lock.lock();
        MessageProducer producer = amqFactory.getProducer(topicName, messageType);
        MapMessage mapMsg = amqFactory.getMapMessage();
        boolean ifHasMsg = false;//是否还有剩余没法送消息标识
        try {
            //存放body信息
            int i = 0;
            for (Map.Entry<String, Object> entry : dictMap.entrySet()) {
                ifHasMsg = true;
                mapMsg.setObject(entry.getKey(), entry.getValue());
                if ((i + 1) % NUM == 0) {
                    producer.send(mapMsg);
                    mapMsg.clearBody();
                    ifHasMsg = false;
                }
                i++;
            }

            //如果还存在剩余没有发送的消息则把剩余消息发送
            if (ifHasMsg) {
                producer.send(mapMsg);
                mapMsg.clearBody();
            }
            return true;
        } catch (Exception e) {
            throw new AMQSendException("send Map Message Error", e);
        } finally {
            lock.unlock();
        }
    }

    /**
     * 发送文本消息（带head和body的）
     *
     * @param amqFactory  amq
     * @param entity      实体消息
     * @param topicName   目的地
     * @param messageType 消息类型
     * @return boolean
     * @throws AMQSendException
     * @throws AMQFactoryException
     */
    public boolean sendTextMsg(AMQFactory amqFactory, MessageEntity entity, String topicName, String messageType) throws AMQSendException, AMQFactoryException {
        if (null == amqFactory) {
            throw new AMQSendException("AMQFactory is null");
        }
        if (null == entity) {
            throw new AMQSendException("Message Content must be not null");
        }
        if (null == topicName || "".equals(topicName)) {
            throw new AMQSendException("parameter topicName must be not null");
        }
        //进入锁定状态，防止狸猫换太子
        lock.lock();
        MessageProducer producer = amqFactory.getProducer(topicName, messageType);
        Map<String, Object> headMap = entity.getHeadMap();
        Map<String, Object> bodyMap = entity.getBodyMap();
        try {
            TextMessage textMsg = amqFactory.getTextMessage();
            if (!headMap.isEmpty() && headMap.size() > 0) {
                for (Map.Entry<String, Object> entry : headMap.entrySet()) {
                    textMsg.setObjectProperty(entry.getKey(), entry.getValue());
                }
            }
            for (Map.Entry<String, Object> entry : bodyMap.entrySet()) {
                textMsg.setText(String.valueOf(entry.getValue()));
            }
            producer.send(textMsg);
            return true;
        } catch (Exception e) {
            throw new AMQSendException("send text Message error", e);
        } finally {
            lock.unlock();
        }
    }

    /**
     * 发送文本消息
     *
     * @param amqFactory  amq
     * @param text        文本消息
     * @param topicName   目的地
     * @param messageType 消息类型
     * @return boolean
     * @throws AMQSendException    发送异常
     * @throws AMQFactoryException 工厂异常
     */
    public boolean sendTextMsg(AMQFactory amqFactory, String text, String topicName, String messageType) throws AMQSendException, AMQFactoryException {
        if (null == amqFactory) {
            throw new AMQSendException("AMQFactory is null");
        }
        if (null == text || "".equals(text)) {
            throw new AMQSendException("Message Content must be not null");
        }
        if (null == topicName || "".equals(topicName)) {
            throw new AMQSendException("parameter topicName must be not null");
        }
        //进入锁定状态
        lock.lock();
        MessageProducer producer = amqFactory.getProducer(topicName, messageType);
        try {
            TextMessage textMsg = amqFactory.getTextMessage();
            textMsg.setText(text);
            producer.send(textMsg);
            return true;
        } catch (Exception e) {
            throw new AMQSendException("send text Message error", e);
        } finally {
            lock.unlock();
        }
    }

}
