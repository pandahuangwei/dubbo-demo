package com.simple.core.common.amq.common;

import java.util.Map;

/**
 * 消息类
 *
 * @author Panda.HuangWei.
 * @version 1.0 .
 * @since 2017.04.17 14:57.
 */
public class MessageEntity {
    private Map<String, Object> headMap;
    private Map<String, Object> bodyMap;

    public Map<String, Object> getHeadMap() {
        return headMap;
    }

    public void setHeadMap(Map<String, Object> headMap) {
        this.headMap = headMap;
    }

    public Map<String, Object> getBodyMap() {
        return bodyMap;
    }

    public void setBodyMap(Map<String, Object> bodyMap) {
        this.bodyMap = bodyMap;
    }
}
