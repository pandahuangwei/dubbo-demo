package com.simple.core.common.amq.exception;

/**
 * @author Panda.HuangWei.
 * @version 1.0 .
 * @since 2017.04.17 15:01.
 */
public class AMQSendException extends Exception {
    private static final long serialVersionUID = 1L;

    public AMQSendException(String message) {
        super(message);
    }

    public AMQSendException(String message, Exception e) {
        super(message, e);
    }
}

