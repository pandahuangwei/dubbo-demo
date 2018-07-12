package com.simple.core.charging.cache;

import com.simple.common.cache.ReloadableSpringBean;
import com.simple.common.utils.StringUtil;
import com.simple.core.charging.service.AuthService;

import com.simple.core.common.entity.Auth;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Resource;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 权限数据缓存
 *
 * @author Panda.HuangWei.
 * @version 1.0 .
 * @since 2017.02.21 15:57.
 */
public class AuthCache extends ReloadableSpringBean {
    private static Logger logger = LoggerFactory.getLogger(AuthCache.class);

    private static AuthCache instance;
    private Map<String, Auth> acrNoMsgTypeMap = new HashMap<>();

    @Resource(name = "authService")
    private AuthService authService;

    public AuthCache() {
        super();
        instance = this;
    }

    @Override
    public void reload() {
        logger.info("Begin Load Auth Cache Data....");
        List<Auth> authList = authService.findAuthList();
        Map<String, Auth> map = buildAcrNoMsgTypeMap(authList);

        if (map == null || map.isEmpty()) {
            logger.info("Can not Load Auth Cache Data....");
            return;
        }
        acrNoMsgTypeMap = map;
        logger.info("End Load Auth Cache Data....");
    }

    public Auth getAuth(String acrNo, String msgType) {
        return acrNoMsgTypeMap.get(StringUtil.genKey(acrNo, msgType));
    }

    /**
     * 获取缓存实例
     *
     * @return 缓存
     */
    public static AuthCache getInstance() {
        return instance;
    }

    /**
     * 构建acrNo_msgType为key的map
     *
     * @param list 授权列表
     * @return map
     */
    private Map<String, Auth> buildAcrNoMsgTypeMap(List<Auth> list) {
        if (list == null || list.isEmpty()) {
            return new HashMap<>();
        }
        Map<String, Auth> map = new HashMap<>(list.size());
        for (Auth e : list) {
            String key = StringUtil.genKey(e.getAcReg(), e.getMsgType());
            if (e.getEffectiveDt() == null) {
                e.setEffectiveDt(new Date());
            }
            if (e.getExpireDt() == null) {
                e.setExpireDt(new Date());
            }
            map.put(key, e);
        }
        return map;
    }
}
