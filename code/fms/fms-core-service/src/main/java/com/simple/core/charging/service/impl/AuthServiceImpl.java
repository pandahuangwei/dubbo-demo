package com.simple.core.charging.service.impl;

import com.alibaba.fastjson.JSON;
import com.simple.common.utils.DateTimeUtil;
import com.simple.common.utils.StringUtil;
import com.simple.core.charging.cache.AuthCache;
import com.simple.core.charging.dao.AuthDao;
import com.simple.core.charging.entity.AcrAuth;
import com.simple.core.charging.entity.CpyAcr;
import com.simple.core.charging.service.AuthService;
import com.simple.core.charging.util.ChargingPropertyUtil;
import com.simple.core.common.entity.Auth;
import com.simple.core.common.entity.CryptEntity;
import com.simple.core.common.entity.ReqParameter;
import com.simple.core.common.util.CryptUtil;
import com.simple.core.common.util.RestClientUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;

/**
 * 授权提供实现
 *
 * @author Panda.HuangWei.
 * @version 1.0 .
 * @since 2017.02.07 14:37.
 */
@Service("authService")
public class AuthServiceImpl implements AuthService {
    private static Logger logger = LoggerFactory.getLogger(AuthServiceImpl.class);
    private int pageSize = 50;
    private int batchSaveSize = 50;

    @Resource(name = "authDao")
    private AuthDao authDao;

    /**
     * 飞机号、报文类型存在，且报文时间在授权的时间范围内，则为有效授权
     *
     * @param acReg   飞机号
     * @param msgType 报文类型
     * @param msgTm   报文时间
     * @return 授权状态
     */
    @Override
    public boolean isAuth(String acReg, String msgType, Date msgTm) {
        logger.info("param,acReg:{},msgType:{},msgTm:{}", acReg, msgType, msgTm);

        Auth auth = AuthCache.getInstance().getAuth(acReg, msgType);
        if (auth == null) {
            return false;
        }
        logger.info("Get Auth info:{}", auth.toString());

        if (!DateTimeUtil.isBetweenTwoDate(msgTm, auth.getEffectiveDt(), auth.getExpireDt())) {
            return false;
        }
        return true;
    }

    @Override
    public Auth getAuth(String acReg, String msgType) {
        logger.info("param,acReg:{},msgType:{}", acReg, msgType);

        Auth auth = AuthCache.getInstance().getAuth(acReg, msgType);
        if (auth == null) {
            return null;
        }
        logger.info("Get Auth info:{}", auth.toString());
        return auth;
    }

    @Override
    public List<Auth> findAuthList() {
        return authDao.findAuthList();
    }

    @Override
    public void saveAuthFromMcp() {
        logger.info("getAuthFromMcp,start...");
        long startTm = System.nanoTime();
        String method = ChargingPropertyUtil.getInstance().getAuthSyncMethod();
        String carrierIata = ChargingPropertyUtil.getInstance().getAuthCarrierIata();
        ReqParameter req;
        int startIndex = 0;
        List<Auth> list = new ArrayList<>(1024);
        for (; ; ) {
            req = new ReqParameter(carrierIata, false, startIndex, pageSize);
            List<Auth> remoteAuth = getRemoteAuth(req, method);
            logger.info("getAuthFromMcp,startIndex:{},and Get result list size:{}", startIndex, remoteAuth.size());
            if (remoteAuth.size() == 0) {
                break;
            }
            list.addAll(remoteAuth);
            startIndex += pageSize;
        }
        logger.info("company:{} ,getAuthFromMcp end,result list size:{},{}, begin to save..", carrierIata, list.size(), StringUtil.getSpentTm(startTm));
        saveAuth(list);
        logger.info("End to save...,{}", StringUtil.getSpentTm(startTm));
        //同步完远程数据后，通知缓存加载
        AuthCache.getInstance().reload();
        logger.info("getAuthFromMcp,End...");
    }

    /**
     * 保存权限数据
     *
     * @param list 权限列表
     */
    private void saveAuth(List<Auth> list) {
        if (list == null || list.isEmpty()) {
            return;
        }
        Map<CpyAcr, List<AcrAuth>> cpyAcrListMap = groupByCpyAcr(list);
        authDao.deleteAcrAuth(null);
        authDao.deleteCpyAcr(null);

        List<AcrAuth> list1 = new ArrayList<>(256);
        List<CpyAcr> list2 = new ArrayList<>(256);
        int cnt = 0,lastIndex =cpyAcrListMap.size() -1;
        for (Map.Entry<CpyAcr, List<AcrAuth>> entry : cpyAcrListMap.entrySet()) {
            list2.add(entry.getKey());
            list1.addAll(entry.getValue());

            if (cnt == batchSaveSize || cnt == lastIndex) {
                authDao.insertAcrAuth(list1);
                authDao.insertCpyAcr(list2);
                list2.clear();
                list1.clear();
            }
            cnt++;
        }
    }

    private Map<CpyAcr,List<AcrAuth>> groupByCpyAcr(List<Auth> list) {
        Map<CpyAcr,List<AcrAuth>> map = new HashMap<>(list.size());
        for (Auth e : list ) {
            CpyAcr cpyAcr = new CpyAcr(e);
            AcrAuth acrAuth = new AcrAuth(e);
            List<AcrAuth> lst = map.get(cpyAcr);
            lst = lst != null?lst:new ArrayList<AcrAuth>();
            lst.add(acrAuth);
            map.put(cpyAcr,lst);
        }
        return map;
    }




    /**
     * 获取远程mcp的权限数据
     *
     * @param req    参数实体
     * @param method 调用的方法名称
     * @return 授权列表
     */
    private List<Auth> getRemoteAuth(ReqParameter req, String method) {
        String json = RestClientUtil.getInstance().getJson(req, method);
        String decryptJson = decryptStr(json);
        return convertList(decryptJson);
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

    /**
     * TODO <br>
     * 该处从json转list的时候需要注意，如果是加密串的话，还需要做处理
     *
     * @param param json
     * @return
     */
    private List<Auth> convertList(String param) {
        List<Auth> list;
        if (ChargingPropertyUtil.getInstance().isFacadeEncrypt()) {
            CryptEntity entity = JSON.parseObject(param, CryptEntity.class);
            list = JSON.parseArray(entity.getCipher(), Auth.class);
        } else {
            list = JSON.parseArray(param, Auth.class);
        }
        list = list != null ? list : new ArrayList<Auth>();
        return list;
    }

}
