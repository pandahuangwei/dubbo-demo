package com.simple.core.common.amq.common;

/**
 * @author Panda.HuangWei.
 * @version 1.0 .
 * @since 2017.04.17 14:58.
 */
public enum MQMsgType {
    Topic("Topic"), Queue("Queue");
    private String value;

    MQMsgType(String value) {
        this.value = value;
    }

    public boolean eq(String value) {
        return this.value.equals(value);
    }

    public String getValue() {
        return value;
    }

}
