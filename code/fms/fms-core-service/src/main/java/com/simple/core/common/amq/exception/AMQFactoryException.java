package com.simple.core.common.amq.exception;

/**
 * AMQ工厂自定义异常类
 *
 * @author Panda.HuangWei.
 * @version 1.0 .
 * @since 2017.04.17 15:01.
 */
public class AMQFactoryException extends Exception {
    private static final long serialVersionUID = 1L;

    public AMQFactoryException(String message) {
        super(message);
    }

    public AMQFactoryException(String message, Exception e) {
        super(message, e);
    }
}
