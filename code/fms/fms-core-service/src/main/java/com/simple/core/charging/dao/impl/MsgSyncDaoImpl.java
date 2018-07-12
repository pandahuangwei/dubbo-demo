package com.simple.core.charging.dao.impl;

import com.simple.common.persistence.BaseDao;
import com.simple.core.charging.dao.MsgSyncDao;
import com.simple.core.charging.entity.MsgSize;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

/**
 * @author Panda.HuangWei.
 * @version 1.0 .
 * @since 2017.04.14 17:27.
 */
@Repository("msgSyncDao")
public class MsgSyncDaoImpl extends BaseDao implements MsgSyncDao {

    private String NAMESPACE = "MsgSyncMapper";

    @Override
    public List<MsgSize> findMsgSizeList(Date date) {
        return dao.findList(getStatement(NAMESPACE, "selectMsgSize"), date);
    }

}
