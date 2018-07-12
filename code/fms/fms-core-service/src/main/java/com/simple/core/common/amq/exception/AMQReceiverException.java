package com.simple.core.common.amq.exception;

/**
 * AMQ消息接受自定义异常类
 *
 * @author Panda.HuangWei.
 * @version 1.0 .
 * @since 2017.04.17 15:01.
 */
public class AMQReceiverException extends Exception {
    private static final long serialVersionUID = 1L;

    public AMQReceiverException(String message) {
        super(message);
    }

    public AMQReceiverException(String message, Exception e) {
        super(message, e);
    }
}
