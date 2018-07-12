package com.simple.core.route.persist.dao;

import com.simple.core.route.persist.entity.PersistMsgInfo;
import com.simple.core.route.persist.entity.PersistMsgTrail;

/**
 * 报文持久化Dao
 *
 * @author Panda.HuangWei.
 * @version 1.0 .
 * @since 2017.01.18 17:43.
 */
public interface PersistDao {
    /**
     * 插入报文主信息
     *
     * @param entity 持久化报文实体
     * @return int
     */
    int insertMsg(PersistMsgInfo entity);

    /**
     * 根据Id获得报文实体
     *
     * @param msgId 报文Id
     * @return 持久化报文实体
     */
    PersistMsgInfo getMsgInfo(String msgId);

    /**
     * 更新报文的最后状态
     *
     * @param entity 持久化报文实体
     * @return int
     */
    int updateMsgState(PersistMsgInfo entity);

    /**
     * 插入报文轨迹
     *
     * @param entity 轨迹实体
     * @return int
     */
    int insertMsgTrail(PersistMsgTrail entity);

    /**
     * 更新航班信息中的状态与时间
     *
     * @param entity 持久化报文实体
     * @return int
     */
    int updateFlightMsgState(PersistMsgInfo entity);

    int insertMsgSize(PersistMsgInfo msgInfo);
}
