package com.simple.core.common.facade;

import com.alibaba.dubbo.rpc.protocol.rest.support.ContentType;
import com.simple.core.common.entity.Auth;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.Date;

/**
 * 授权服务接口
 * @author Panda.HuangWei.
 * @version 1.0 .
 * @since 2017.02.07 17:34.
 */
@Path("charging")
@Consumes({MediaType.APPLICATION_JSON, MediaType.TEXT_XML})
@Produces({ContentType.APPLICATION_JSON_UTF_8, ContentType.TEXT_XML_UTF_8})
public interface ChargingFacade {
    /**
     * 验证授权
     *
     * @param acReg   飞机号
     * @param msgType 报文类型
     * @param msgTm   报文时间
     * @return true/false
     */
    @POST
    boolean isAuth(String acReg, String msgType, Date msgTm);

    /**
     * 获取授权实体
     * @param acReg 飞机号
     * @param msgType 报文类型
     * @return 授权实体
     */
    @POST
    Auth getAuth(String acReg, String msgType);
}
