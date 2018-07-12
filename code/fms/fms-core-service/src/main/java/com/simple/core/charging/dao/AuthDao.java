package com.simple.core.charging.dao;


import com.simple.core.charging.entity.AcrAuth;
import com.simple.core.charging.entity.CpyAcr;
import com.simple.core.common.entity.Auth;

import java.util.List;

/**
 * @author Panda.HuangWei.
 * @version 1.0 .
 * @since 2017.02.21 15:35.
 */
public interface AuthDao {

    /**
     * 获取所有的授权信息
     *
     * @return list
     */
    List<Auth> findAuthList();

    int deleteAcrAuth(String id);

    int deleteCpyAcr(String id);

    int insertAcrAuth(List<AcrAuth> list);

    int insertCpyAcr(List<CpyAcr> list);
}
