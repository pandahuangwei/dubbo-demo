package com.simple.core.route.persist.dao.impl;

import com.simple.common.persistence.BaseDao;
import com.simple.core.route.persist.dao.PersistDao;
import com.simple.core.route.persist.entity.PersistMsgInfo;
import com.simple.core.route.persist.entity.PersistMsgTrail;
import org.springframework.stereotype.Repository;

/**
 * @author Panda.HuangWei.
 * @version 1.0 .
 * @since 2017.01.18 17:43.
 */
@Repository("persistDao")
public class PersistDaoImpl extends BaseDao implements PersistDao {

    @Override
    public int insertMsg(PersistMsgInfo entity) {
        return dao.insert(getStatement("insertPersistMsgInfo"), entity);
    }


    @Override
    public PersistMsgInfo getMsgInfo(String msgId) {
        return (PersistMsgInfo) dao.findOne(getStatement("selectPersistMsgInfo"), msgId);
    }

    @Override
    public int updateMsgState(PersistMsgInfo entity) {
        return dao.update(getStatement("updatePersistMsgInfoState"), entity);
    }

    @Override
    public int insertMsgTrail(PersistMsgTrail entity) {
        return dao.insert(getStatement("insertPersistMsgTrail"), entity);
    }

    @Override
    public int updateFlightMsgState(PersistMsgInfo entity) {
        return dao.update(getStatement("updateFlightMsgState"), entity);
    }

    @Override
    public int insertMsgSize(PersistMsgInfo entity) {
        return dao.insert(getStatement("insertMsgSize"), entity);
    }

    @Override
    protected String getStatement(String sqlId) {
        String NAMESPACE = "MsgPersistMapper";
        return super.getStatement(NAMESPACE, sqlId);
    }
}
