package com.simple.core.route.util;

import com.simple.core.route.service.impl.RouteMsgServiceImpl;
import org.apache.axis.client.Call;
import org.apache.axis.client.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.xml.namespace.QName;

/**
 * 主动往全网通发送报文
 *
 * @author Panda.HuangWei.
 * @version 1.0 .
 * @since 2017.03.17 14:04.
 */
public class SendMsgWsUtil {
    private static Logger logger = LoggerFactory.getLogger(SendMsgWsUtil.class);

    private static SendMsgWsUtil instance;
    private static Service service;
    private static String wsUrl;

    private SendMsgWsUtil() {
        this.service = new Service();
        this.wsUrl = RouteAddressUtil.instance.getAddressIgnoreCase(ParamEnum.URLKEY.getValue());
    }

    public static SendMsgWsUtil getInstance() {
        if (instance == null) {
            synchronized (SendMsgWsUtil.class) {
                if (instance == null) {
                    instance = new SendMsgWsUtil();
                }
            }
        }
        return instance;
    }

    public String sendAxisProxy(String paramValue) throws Exception {
        Call call = (Call) service.createCall();

        call.setTargetEndpointAddress(wsUrl);
        call.setOperationName(new QName(ParamEnum.NAMESPACE.getValue(), ParamEnum.METHOD.getValue()));
        //call.addParameter("msg",org.apache.axis.encoding.XMLType.XSD_STRING, javax.xml.rpc.ParameterMode.IN);
        call.addParameter(ParamEnum.PARAM.getValue(), null, javax.xml.rpc.ParameterMode.IN);
        call.setReturnClass(String.class);

        return (String) call.invoke(new Object[]{paramValue});
    }

    enum ParamEnum {
        NAMESPACE("urn:fms"), METHOD("notify"), PARAM("msg"), URLKEY("wsNotify");
        private String value;

        private ParamEnum(String itemValue) {
            this.value = itemValue;
        }

        public String getValue() {
            return value;
        }
    }
}
