package com.simple.core.common.entity;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;

/**
 * 报文密文
 *
 * @author Panda.HuangWei.
 * @version 1.0 .
 * @since 2017.03.31 8:39.
 */
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class MessageCipher extends Message implements Serializable {
    private static final long serialVersionUID = 11L;

    private String key;
    private Message cipher;

    public MessageCipher() {
    }

    public MessageCipher(String key, Message cipher) {
        this.key = key;
        this.cipher = cipher;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public Message getCipher() {
        return cipher;
    }

    public void setCipher(Message cipher) {
        this.cipher = cipher;
    }

    @Override
    public String toString() {
        return "MessageCipher{" +
                "key='" + key + '\'' +
                ", cipher=" + cipher +
                '}';
    }
}
