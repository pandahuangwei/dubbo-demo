package com.simple.core.common.util;

import com.simple.core.charging.util.ChargingPropertyUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

/**
 * rest客户端访问
 *
 * @author Panda.HuangWei.
 * @version 1.0 .
 * @since 2017.04.05 18:19.
 */
public class RestClientUtil {
    private static Logger logger = LoggerFactory.getLogger(RestClientUtil.class);
    private static String serverIpPort;
   // private static Client client;
    private static String urlHead;
    private static String urlPath;
    private static char urlSplit;

    private RestClientUtil() {
        //client = ClientBuilder.newClient();
        serverIpPort = ChargingPropertyUtil.getInstance().getChargingIpPort();
        urlHead = "http://";
        urlPath = "/services/charging/";
        urlSplit = '.';
    }

    public static RestClientUtil getInstance() {
        return RestClientUtil.SingletonHolder.instance;
    }

    /**
     * 获取json格式的数据
     * @param entity 请求参数实体
     * @param method 请求方法
     * @return 返回xml格式的字符串
     * @throws Exception 异常
     */
    public String getJson(Object entity, String method) {
        return post(entity, method, MediaType.APPLICATION_JSON_TYPE);
    }

    /**
     * 发送json格式的数据
     * @param entity 请求参数实体
     * @param method 请求方法
     * @return 返回xml格式的字符串
     * @throws Exception 异常
     */
    public String postJson(Object entity, String method) {
        return post(entity, method, MediaType.APPLICATION_JSON_TYPE);
    }

    /**
     * 发送xml格式的数据
     * @param entity 请求参数实体
     * @param method 请求方法
     * @return 返回xml格式的字符串
     * @throws Exception 异常
     */
    public String postXml(Object entity, String method) {
        return post(entity, method, MediaType.TEXT_XML_TYPE);
    }

    private String post(Object entity, String method, MediaType mediaType) {
        String url = genUrl(method, mediaType);
        Client client = ClientBuilder.newClient();
        WebTarget target = client.target(url);
        Response response = target.request().post(Entity.entity(entity, mediaType));
        String rs;
        try {
            if (response.getStatus() != 200) {
                throw new RuntimeException("Failed with HTTP error code : " + response.getStatus());
            }
            rs = response.readEntity(String.class);
        } finally {
            response.close();
            client.close();
        }
        return rs;
    }



    private static class SingletonHolder {
        static RestClientUtil instance = new RestClientUtil();
    }


    private String genUrl(String method, MediaType mediaType) {
        StringBuffer sb = new StringBuffer();
        sb.append(urlHead).append(serverIpPort).append(urlPath).append(method).append(urlSplit).append(mediaType.getSubtype());
        return sb.toString();
    }

}
