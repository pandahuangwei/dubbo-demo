package com.simple.core.common.facade;

import com.simple.core.common.entity.Flight;
import com.simple.core.common.entity.MsgBody;
import com.simple.core.common.enums.MessageEnum;
import org.apache.commons.lang3.tuple.Pair;

import java.util.List;

/**
 * 报文解释接口，针对内部服务提供
 *
 * @author Panda.HuangWei.
 * @version 1.0 .
 * @since 2017.02.06 18:53.
 */
public interface ParseMsgFacade {

    /**
     * 根据业务过滤航班
     * @param flight
     * @return <错误代码,航班>
     */
    Pair<MessageEnum.MsgReasonEnum,Flight> filterFlight(List<Flight> flight);

    /**
     * 内部报文解释
     *
     * @param flight       航班实体
     * @param msgContext 报文内容
     * @return 报文体
     */
    List<MsgBody> parseMessage(Flight flight, String msgContext);

    /**
     * 手动发起报文请求服务
     * @param flight
     * @param msgContext
     * @return
     */
    List<MsgBody> parseMessageManual(Flight flight, String msgContext);
}
