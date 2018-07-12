package com.simple.core.route.util;

import com.alibaba.fastjson.JSON;
import com.simple.core.common.amq.common.MQMsgType;
import com.simple.core.common.amq.common.MessageEntity;
import com.simple.core.common.amq.exception.AMQFactoryException;
import com.simple.core.common.amq.exception.AMQReceiverException;
import com.simple.core.common.amq.exception.AMQSendException;
import com.simple.core.common.amq.factory.AMQFactory;
import com.simple.core.common.amq.factory.AMQReceiver;
import com.simple.core.common.amq.factory.AMQSender;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

/**
 * route节点发送信息到active Mq的信息类
 *
 * @author Panda.HuangWei.
 * @version 1.0 .
 * @since 2017.04.17 15:33.
 */
public class RouteMsgAMQUtil {
    private static Logger logger = LoggerFactory.getLogger(RouteMsgAMQUtil.class);

    private AMQFactory factory = null;
    private AMQReceiver receiver = null;
    private AMQSender sender = null;
    private String routeQueue = null;
    private int sendNumber = 1;

    /**
     * 私有化构造方法，实例化active Mq工厂类
     */
    private RouteMsgAMQUtil() {
        try {
            String URL = RoutePropertyUtil.getInstance().getMsgStateMqURL();
            String userName = RoutePropertyUtil.getInstance().getMqUserName();
            String password = RoutePropertyUtil.getInstance().getMqPassword();
            routeQueue = RoutePropertyUtil.getInstance().getMqQueueName();

            factory = new AMQFactory(URL, userName, password);
            receiver = new AMQReceiver();
            sender = new AMQSender();

            logger.info("Init AMQFactory is succeed,URL:{},userName:{},password:{},and using Queue:{}", URL, userName, password, routeQueue);
        } catch (AMQFactoryException e) {
            e.printStackTrace();
        }
    }

    /**
     * 静态内部类实现单例，线程安全
     */
    private static class SingletonHolder {
        static RouteMsgAMQUtil instance = new RouteMsgAMQUtil();
    }

    /**
     * 获取实例
     *
     * @return 单例
     */
    public static RouteMsgAMQUtil getInstance() {
        return RouteMsgAMQUtil.SingletonHolder.instance;
    }

    /**
     * 发送一个实体到mq <br>
     * 默认发送到routeQueue，消息类型为Queue
     *
     * @param obj 实体
     * @return boolean
     */
    public boolean sendObject(Object obj) {
        try {
            return sendTextMsg(JSON.toJSONString(obj), routeQueue, MQMsgType.Queue.getValue());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 非线程池方式发送字符串信息<br>
     * 默认发送到routeQueue，消息类型为Queue
     *
     * @param text 发送的信息
     * @return boolean
     */
    public boolean sendTextMsg(String text) {
        try {
            return sendTextMsg(text, routeQueue, MQMsgType.Queue.getValue());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }


    /**
     * 使用非连接池方式发送不带head的Map消息，使用默认发送次数
     *
     * @param dicMap 数据
     * @return boolean
     * @throws AMQSendException    发送异常
     * @throws AMQFactoryException mq工厂异常
     */
    public boolean sendMapMsg(Map<String, Object> dicMap) throws AMQSendException, AMQFactoryException {
        return sendMapMsg(dicMap, routeQueue, MQMsgType.Queue.getValue());
    }


    /**
     * 非连接池方式发送带head的Map消息，使用默认发送次数
     *
     * @param msgEntity
     * @param name        队列或topic名称
     * @param messageType 消息类型
     * @return boolean
     * @throws AMQSendException    发送异常
     * @throws AMQFactoryException mq工厂异常
     */
    public boolean sendMapMsg(MessageEntity msgEntity, String name, String messageType) throws AMQSendException, AMQFactoryException {
        return sendMapMsg(msgEntity, name, sendNumber, messageType);
    }

    /**
     * 使用非连接池方式发送不带head的Map消息，使用默认发送次数
     *
     * @param dicMap      map方式的信息
     * @param name        队列或topic名称
     * @param messageType 消息类型
     * @return boolean
     * @throws AMQSendException    发送异常
     * @throws AMQFactoryException mq工厂异常
     */
    public boolean sendMapMsg(Map<String, Object> dicMap, String name, String messageType) throws AMQSendException, AMQFactoryException {
        return sendMapMsg(dicMap, name, sendNumber, messageType);
    }

    /**
     * 使用非连接池方式发送带Head的Map消息，使用指定发送次数
     *
     * @param msgEntity   实体消息
     * @param name        队列或topic名称
     * @param sendTimes   发送次数
     * @param messageType 消息类型
     * @return boolean
     * @throws AMQSendException    发送异常
     * @throws AMQFactoryException mq工厂异常
     */
    public boolean sendMapMsg(MessageEntity msgEntity, String name, int sendTimes, String messageType) throws AMQSendException, AMQFactoryException {
        return sender.sendMapMsg(factory, msgEntity, name, sendTimes, messageType);
    }

    /**
     * 使用非连接池方式发送不带Head的Map消息，使用指定发送次数
     *
     * @param dicMap      map
     * @param name        队列或topic名称
     * @param sendTimes   发送次数
     * @param messageType 消息类型
     * @return boolean
     * @throws AMQSendException    发送异常
     * @throws AMQFactoryException mq工厂异常
     */
    public boolean sendMapMsg(Map<String, Object> dicMap, String name, int sendTimes, String messageType) throws AMQSendException, AMQFactoryException {
        return sender.sendMapMsg(factory, dicMap, name, sendTimes, messageType);
    }

    /**
     * 使用非连接池方式发送带head的Text消息
     *
     * @param msgEntity   消息实体
     * @param name        队列或topic名称
     * @param messageType 消息类型
     * @return boolean
     * @throws AMQSendException    发送异常
     * @throws AMQFactoryException mq工厂异常
     */
    public boolean sendTextMsg(MessageEntity msgEntity, String name, String messageType) throws AMQSendException, AMQFactoryException {
        return sender.sendTextMsg(factory, msgEntity, name, messageType);
    }

    /**
     * 使用非连接池方式发送不带head的Text消息
     *
     * @param text        txt
     * @param name        队列或topic名称
     * @param messageType 消息类型
     * @return boolean
     * @throws AMQSendException    发送异常
     * @throws AMQFactoryException mq工厂异常
     */
    public boolean sendTextMsg(String text, String name, String messageType) throws AMQSendException, AMQFactoryException {
        return sender.sendTextMsg(factory, text, name, messageType);
    }

    /**
     * 非连接池方式用户订阅消息与用户自定义消息处理类关联
     *
     * @param topicName   队列或topic名称
     * @param cls         消息
     * @param messageType 消息类型
     * @return boolean
     * @throws AMQSendException    发送异常
     * @throws AMQFactoryException mq工厂异常
     */
    public boolean setReceive(String topicName, Class<?> cls, String messageType) throws AMQReceiverException, AMQFactoryException {
        return receiver.setReceive(topicName, factory, cls, messageType);
    }

    /**
     * 非连接池方式用户订阅消息与用户自定义消息处理类关联
     *
     * @param topicName 队列或topic名称
     * @param cls       消息
     * @return boolean
     * @throws AMQSendException    发送异常
     * @throws AMQFactoryException mq工厂异常
     */
    public boolean setListener(String topicName, Class<?> cls, String messageType) throws AMQReceiverException, AMQFactoryException {
        return receiver.setListener(topicName, factory, cls, messageType);
    }

    /**
     * 释放所有连接
     *
     * @return boolean
     * @throws AMQFactoryException mq工厂异常
     */
    public boolean disposeAll() throws AMQFactoryException {
        return factory.DisposeAll();
    }

}
