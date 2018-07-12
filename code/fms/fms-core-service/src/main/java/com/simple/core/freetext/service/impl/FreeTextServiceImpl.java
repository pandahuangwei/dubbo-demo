package com.simple.core.freetext.service.impl;

import com.simple.common.utils.StringUtil;
import com.simple.core.common.service.AbstractCoreMsgService;
import com.simple.core.freetext.entity.UpLinkFreeText;
import com.simple.core.common.entity.Flight;
import com.simple.core.common.entity.MsgBody;
import com.simple.core.common.enums.MessageEnum;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**********************************************************************************
 * Copyright(c)2017 Dcits-air.com All rights reserved.
 * @Title: FreeTextServiceImpl.java.
 * @Package com.simple.core.freetext.service.impl.
 * @Description: FreeTextServiceImpl.
 *
 * @author Administrator.
 * @version 1.0.
 * @created 2017/4/10 10:14.
 **********************************************************************************/
@Service("freetextService")
public class FreeTextServiceImpl extends AbstractCoreMsgService {

    @Override
    public Pair<MessageEnum.MsgReasonEnum, Flight> filterFlight(List<Flight> list) {
        //返回最后一个航班
        return Pair.of(MessageEnum.MsgReasonEnum.SERVICE_REQ_SUCC,list.get(list.size()-1));
    }

    @Override
    public List<MsgBody> parseMessage(Flight flight, String msgContext) {
        List<MsgBody> retList = new ArrayList<>();

        MsgBody msg=new MsgBody();
        msg.setMsgType(MessageEnum.MessageType.FREETEXT.getKey());
        msg.setErr(MessageEnum.MsgReasonEnum.SERVICE_REQ_SUCC);
        //根据msgContext做不同处理
        UpLinkFreeText freeText = convertToFreeText(flight,msgContext+" received.");
        msg.setContent(freeText);
        retList.add(msg);
        return retList;
    }

    @Override
    public List<MsgBody> parseMessageManual(Flight flight, String msgContext) {
        List<MsgBody> retList = new ArrayList<>();

        MsgBody msg=new MsgBody();
        msg.setMsgType(MessageEnum.MessageType.FREETEXT.getKey());
        msg.setErr(MessageEnum.MsgReasonEnum.SERVICE_REQ_SUCC);
        UpLinkFreeText freeText = convertToFreeText(flight,msgContext);
        msg.setContent(freeText);
        retList.add(msg);
        return retList;
    }

    /**
     * 组装自由报
     * @param flight，可为空
     * @param text
     * @return
     */
    public static UpLinkFreeText convertToFreeText(Flight flight, String text) {
        if (StringUtil.isBlank(text)) return null;

        UpLinkFreeText resq = new UpLinkFreeText();
        resq.setText(text);
        return resq;
    }
}
