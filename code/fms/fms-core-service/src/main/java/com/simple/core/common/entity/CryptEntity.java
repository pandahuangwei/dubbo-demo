package com.simple.core.common.entity;

/**
 * 解密后的信息实体
 *
 * @author Panda.HuangWei.
 * @version 1.0 .
 * @since 2017.04.06 10:31.
 */
public class CryptEntity {

    private String key;
    private String cipher;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getCipher() {
        return cipher;
    }

    public void setCipher(String cipher) {
        this.cipher = cipher;
    }
}
