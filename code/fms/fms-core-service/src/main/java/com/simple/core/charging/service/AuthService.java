package com.simple.core.charging.service;

import com.simple.core.common.entity.Auth;

import java.util.Date;
import java.util.List;

/**
 * 授权服务接口
 *
 * @author Panda.HuangWei.
 * @version 1.0 .
 * @since 2017.02.07 14:02.
 */
public interface AuthService {

    /**
     * 验证授权
     *
     * @param acrNo   飞机号
     * @param msgType 报文类型
     * @param msgTm   报文时间
     * @return true/false
     */
    boolean isAuth(String acrNo, String msgType, Date msgTm);

    /**
     * 获取授权实体
     * @param acReg 飞机号
     * @param msgType 报文类型
     * @return 授权实体
     */
    Auth getAuth(String acReg, String msgType);

    /**
     * 获取所有的授权信息
     * @return list
     */
    List<Auth> findAuthList();

    void saveAuthFromMcp();
}
