package com.simple.core.charging.facade.impl;

import com.simple.core.charging.service.AuthService;
import com.simple.core.common.entity.Auth;
import com.simple.core.common.facade.ChargingFacade;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;

/**
 * 授权服务实现
 * @author Panda.HuangWei.
 * @version 1.0 .
 * @since 2017.03.02 9:25.
 */
@Service("chargingRestService")
public class ChargingRestFacadeImpl implements ChargingFacade {
    @Resource(name = "authService")
    private AuthService authService;


    @Override
    public boolean isAuth(String acrNo, String msgType, Date msgTm) {
        return authService.isAuth(acrNo, msgType, msgTm);
    }

    @Override
    public Auth getAuth(String acrNo, String msgType) {
        return authService.getAuth(acrNo, msgType);
    }
}
