package com.simple.core.common.entity;

import com.simple.core.common.enums.MessageEnum;

import java.io.Serializable;

/**
 * @author Panda.HuangWei.
 * @version 1.0 .
 * @since 2017.02.06 17:56.
 */
public class MsgBody<T> implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 请求参数
     */
    private String input;
    /**
     * 输出参数
     */
    private String output;
    /**
     * 错误具体原因
     */
    private String errCause;

    /**
     * 错误代码(定义在枚举中MsgParseStateEnum)
     */
    private int errCode = MessageEnum.MsgReasonEnum.SERVICE_REQ_SUCC.getKey();
    private String errText = "";
    /**
     * 报文类型
     */
    private String msgType;
    /**
     * 报文体(对象Obj)
     */
    private T content;

    public String getInput() {
        return input;
    }

    public void setInput(String input) {
        this.input = input;
    }

    public String getOutput() {
        return output;
    }

    public void setOutput(String output) {
        this.output = output;
    }

    public String getErrCause() {
        return errCause;
    }

    public void setErrCause(String errCause) {
        this.errCause = errCause;
    }

    public int getErrCode() {
        return errCode;
    }

    public void setErrCode(int errCode) {
        this.errCode = errCode;
    }

    public String getErrText() {
        return errText;
    }

    public void setErrText(String errText) {
        this.errText = errText;
    }

    public void setErr(MessageEnum.MsgReasonEnum reson){
        setErrCode(reson.getKey());
        setErrText(reson.getValue2());
    }

    public String getMsgType() {
        return msgType;
    }

    public void setMsgType(String msgType) {
        this.msgType = msgType;
    }

    public T getContent() {
        return content;
    }

    public void setContent(T content) {
        this.content = content;
    }
}
