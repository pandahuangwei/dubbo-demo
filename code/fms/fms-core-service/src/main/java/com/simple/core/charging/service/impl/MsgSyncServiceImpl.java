package com.simple.core.charging.service.impl;

import com.alibaba.fastjson.JSON;
import com.simple.common.utils.StringUtil;
import com.simple.core.charging.dao.MsgSyncDao;
import com.simple.core.charging.entity.MsgSize;
import com.simple.core.charging.entity.MsgSizeStatus;
import com.simple.core.charging.service.MsgSyncService;
import com.simple.core.charging.util.ChargingPropertyUtil;
import com.simple.core.common.entity.ReqParameter;
import com.simple.core.common.util.CryptUtil;
import com.simple.core.common.util.RestClientUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author Panda.HuangWei.
 * @version 1.0 .
 * @since 2017.04.14 18:41.
 */
@Service("msgSyncService")
public class MsgSyncServiceImpl implements MsgSyncService {
    private static Logger logger = LoggerFactory.getLogger(MsgSyncServiceImpl.class);
    private int pageSize = 10;

    @Resource(name = "msgSyncDao")
    private MsgSyncDao msgSyncDao;

    /**
     * 1、从远程获取最近一次的报文同步时间t;<br>
     * 2、如果时间为空，全量数据同步，如果不为空，以t为起点，同步t时间之后的所有数据;<br>
     */
    @Override
    public void syncMsgSize() {
        Date syncTm = getStartTm();
       // Date syncTm = null;
        long startTm = System.nanoTime();
        List<MsgSize> list = getMsgSizeSyncList(syncTm);
        logger.info("Start to post Date to MCP,and size :{}",list.size());
        String syncMethod = ChargingPropertyUtil.getInstance().getMsgSizeSyncMethod();
        List<MsgSize> lst = new ArrayList<>(64);
        for (int i=0,size=list.size(),lastIndex=size-1;i<size;i++) {
            lst.add(list.get(i));
            if (pageSize==i%pageSize || i==lastIndex) {
                postData2Mcp(lst,syncMethod);
                lst.clear();
            }
        }
        logger.info("End to post Date to MCP,{}", StringUtil.getSpentTm(startTm));
    }

    private void postData2Mcp(List<MsgSize> list,String msgSizeSyncMethod){
        String json = RestClientUtil.getInstance().postJson(list, msgSizeSyncMethod);
    }

    private List<MsgSize> getMsgSizeSyncList(Date startTm) {
        List<MsgSize> list = msgSyncDao.findMsgSizeList(startTm);
        return list != null ? list : new ArrayList<MsgSize>();
    }

    /**
     * 每隔一分钟去远程获取上一次同步的时间,如果经过n(=5)次都获取不到，则返回null
     *
     * @return 上一次同步的时间
     */
    private Date getStartTm() {
        ReqParameter req = new ReqParameter(ChargingPropertyUtil.getInstance().getAuthCarrierIata());
        String method = ChargingPropertyUtil.getInstance().getMsgSizeSyncStatusMethod();
        try {
            for (int i = 0; i < 5; i++) {
                Date date = getLastSyncTmFromMcp(req, method);
                logger.info("Now is the {} times to get lastSyncTm,and get Tm is :{}",i,date);
                if (date == null) {
                    Thread.sleep(60000);
                } else {
                    return date;
                }
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 从mcp获取上一次同步的时间
     *
     * @param req    请求参数
     * @param method 调用方法
     * @return 上一次同步的时间
     */
    private Date getLastSyncTmFromMcp(ReqParameter req, String method) {
        String json = RestClientUtil.getInstance().getJson(req, method);
        String decryptJson = decryptStr(json);
        MsgSizeStatus msgSizeStatus = JSON.parseObject(decryptJson, MsgSizeStatus.class);
        if (msgSizeStatus != null && msgSizeStatus.getLastSyncDt() != null) {
            return msgSizeStatus.getLastSyncDt();
        }
        return null;
    }

    /**
     * 解密
     *
     * @param param 原始参数
     * @return 解密后的参数
     */
    private String decryptStr(String param) {
        if (ChargingPropertyUtil.getInstance().isFacadeEncrypt()) {
            return CryptUtil.decryptMsg(param);
        }
        return param;
    }
}
