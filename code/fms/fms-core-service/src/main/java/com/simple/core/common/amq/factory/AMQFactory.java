package com.simple.core.common.amq.factory;

import com.simple.core.common.amq.common.MQMsgType;
import com.simple.core.common.amq.exception.AMQFactoryException;
import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;
import java.util.HashMap;
import java.util.Map;

/**
 * AMQ实现工厂
 *
 * @author Panda.HuangWei.
 * @version 1.0 .
 * @since 2017.04.17 15:05.
 */
public class AMQFactory {
    private Connection connection;
    private Session producerSession;//生产者session
    private Session consumerSession;//消费者session
    private boolean isCreateP = false;//生产者session是否建立的标志
    private boolean isCreateC = false;//消费者session是否建立的标志
    private String URL;
    private String userName;
    private String password;
    //producer连接池,一个topicName生产一个producer
    private Map<String, MessageProducer> producerPool = new HashMap<>();
    //consumer连接池,一个topicName生产一个consumer
    private Map<String, MessageConsumer> consumerPool = new HashMap<>();

    public AMQFactory(String URL, String userName, String password) throws AMQFactoryException {
        this.URL = URL;
        this.userName = userName;
        this.password = password;
        init();
    }

    private void init() throws AMQFactoryException {
        try {
            ConnectionFactory factory = new ActiveMQConnectionFactory(URL);
            connection = factory.createConnection(userName, password);
            connection.start();
        } catch (Exception e) {
            throw new AMQFactoryException("ActiveMQ Connectioned is failed", e);
        }
    }

    /**
     * 获取mapMessage
     *
     * @return instance
     * @throws AMQFactoryException mq工厂异常
     */
    MapMessage getMapMessage() throws AMQFactoryException {
        if (null == producerSession) {
            throw new AMQFactoryException("producerSession is null");
        }
        try {
            return producerSession.createMapMessage();
        } catch (JMSException e) {
            throw new AMQFactoryException("createMapMessage error!", e);
        }
    }


    /**
     * 获取TextMessage
     *
     * @return instance
     * @throws AMQFactoryException mq工厂异常
     */
    TextMessage getTextMessage() throws AMQFactoryException {
        if (null == producerSession) {
            throw new AMQFactoryException("producerSession is null");
        }
        try {
            return producerSession.createTextMessage();
        } catch (JMSException e) {
            throw new AMQFactoryException("createTextMessage error!", e);
        }
    }

    /**
     * 在内存中获取topic的producer，如果不存在则创建一个
     *
     * @param topicName 管道名称
     * @return instance
     * @throws AMQFactoryException mq工厂异常
     */
    MessageProducer getProducer(String topicName, String messageType) throws AMQFactoryException {
        if (!isCreateP) {
            try {
                producerSession = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);//不支持事务，消息自动响应
                isCreateP = true;
            } catch (JMSException e) {
                throw new AMQFactoryException("create AMQ session error!", e);
            }
        }
        MessageProducer producer = null;
        if (producerPool.size() > 0) {
            for (Map.Entry<String, MessageProducer> entry : producerPool.entrySet()) {
                if (topicName.equals(entry.getKey())) {
                    producer = producerPool.get(entry.getKey());
                    break;
                }
            }
        }
        if (producer == null) {
            try {
                Destination destination = null;

                if (MQMsgType.Topic.eq(messageType)) {
                    destination = producerSession.createTopic(topicName);
                }
                if (MQMsgType.Queue.eq(messageType)) {
                    destination = producerSession.createQueue(topicName);
                }
                producer = producerSession.createProducer(destination);
                //设置消息持久化
                producer.setDeliveryMode(DeliveryMode.PERSISTENT);
                if (!producerPool.containsKey(topicName)) {
                    producerPool.put(topicName, producer);
                }
            } catch (JMSException e) {
                throw new AMQFactoryException("create AMQ producer error!", e);
            }
        }
        return producer;
    }

    /**
     * 在内存中获取topic的consumer，如果不存在则创建一个
     *
     * @param topicName 通道名称
     * @return instance
     * @throws AMQFactoryException mq工厂异常
     */
    MessageConsumer getConsumer(String topicName, String messageType) throws AMQFactoryException {
        if (!isCreateC) {
            try {
                consumerSession = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
            } catch (JMSException e) {
                throw new AMQFactoryException("create AMQ session error!", e);
            }
            isCreateC = true;
        }
        MessageConsumer consumer;
        if (consumerPool.size() > 0) {
            for (Map.Entry<String, MessageConsumer> entry : consumerPool.entrySet()) {
                if (topicName.equals(entry.getKey())) {
                    //consumer = consumerPool.get(key);//使用同一个consumer只能启用一个监听器
                    break;
                }
            }
        }

        try {
            Destination destination = null;
            if (MQMsgType.Topic.eq(messageType)) {
                destination = consumerSession.createTopic(topicName);
            }
            if (MQMsgType.Queue.eq(messageType)) {
                destination = consumerSession.createQueue(topicName);
            }
            consumer = consumerSession.createConsumer(destination);
            if (!consumerPool.containsKey(topicName)) {
                consumerPool.put(topicName, consumer);
            }
        } catch (JMSException e) {
            throw new AMQFactoryException("create AMQ consumer error!", e);
        }

        return consumer;
    }

    /**
     * 释放所有连接
     *
     * @return instance
     * @throws AMQFactoryException
     */
    public boolean DisposeAll() throws AMQFactoryException {
        try {
            if (producerPool.size() > 0) {
                for (Map.Entry<String, MessageProducer> entry : producerPool.entrySet()) {
                    producerPool.get(entry.getKey()).close();
                    producerPool.remove(entry.getKey());
                }

            }
            if (consumerPool.size() > 0) {
                for (Map.Entry<String, MessageConsumer> entry : consumerPool.entrySet()) {
                    consumerPool.get(entry.getKey()).close();
                    consumerPool.remove(entry.getKey());
                }
            }
            if (producerSession != null) {
                producerSession.close();
            }
            if (consumerSession != null) {
                consumerSession.close();
            }
            if (connection != null) {
                connection.close();
            }
            return true;
        } catch (Exception e) {
            throw new AMQFactoryException("Dispose AMQ All Connection Error", e);
        }
    }
}
