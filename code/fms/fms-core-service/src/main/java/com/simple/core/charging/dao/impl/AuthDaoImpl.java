package com.simple.core.charging.dao.impl;

import com.simple.common.persistence.BaseDao;
import com.simple.core.charging.dao.AuthDao;
import com.simple.core.charging.entity.AcrAuth;
import com.simple.core.charging.entity.CpyAcr;
import com.simple.core.common.entity.Auth;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author Panda.HuangWei.
 * @version 1.0 .
 * @since 2017.02.21 15:35.
 */
@Repository("authDao")
public class AuthDaoImpl extends BaseDao implements AuthDao {

    private String NAMESPACE = "AuthMapper";

    @Override
    public List<Auth> findAuthList() {
        return dao.findList(getStatement("findAuthAll"));
    }

    @Override
    public int deleteAcrAuth(String id) {
        return dao.delete(getStatement("deleteAcrAuth"), id);
    }

    @Override
    public int deleteCpyAcr(String id) {
        return dao.delete(getStatement("deleteCpyAcr"), id);
    }

    @Override
    public int insertAcrAuth(List<AcrAuth> list) {
        if (list == null || list.isEmpty()) {
            return 0;
        }
        return dao.insert(getStatement("insertAcrAuth"), list);
    }

    @Override
    public int insertCpyAcr(List<CpyAcr> list) {
        if (list == null || list.isEmpty()) {
            return 0;
        }
        return dao.insert(getStatement("insertCpyAcr"), list);
    }

    @Override
    protected String getStatement(String sqlId) {
        return super.getStatement(NAMESPACE, sqlId);
    }
}
