package com.simple.core.charging.dao;

import com.simple.core.charging.entity.MsgSize;
import com.simple.core.common.entity.Auth;

import java.util.Date;
import java.util.List;

/**
 * @author Panda.HuangWei.
 * @version 1.0 .
 * @since 2017.04.14 17:27.
 */
public interface MsgSyncDao {
    List<MsgSize> findMsgSizeList(Date date);
}
