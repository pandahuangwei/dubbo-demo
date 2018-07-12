package com.simple.route;

import com.alibaba.fastjson.JSON;
import com.simple.core.common.entity.NoticeMsg;
import com.simple.core.route.util.RouteMsgAMQUtil;
import com.simple.core.route.util.RoutePropertyUtil;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.util.HashMap;
import java.util.Map;

/**
 * mq发送者测试
 * @author Panda.HuangWei.
 * @version 1.0 .
 * @since 2017.04.17 18:09.
 */
public class TestMqSender {
    private static final Log log = LogFactory.getLog(TestMqSender.class);

    public static void main(String[] args) {
        try {
            ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext(new String[]{
                    "classpath:spring/spring-context.xml"
            });
           // ObjectMessage objmsg = new ObjectMessage();

            NoticeMsg msg = new NoticeMsg();
            msg.setMsgType("1");
            msg.setAcReg("12");
           // objmsg.setObject(objmsg.);
            String jsonString = JSON.toJSONString(msg);
            Map<String,Object> map = new HashMap<>();
            map.put("1",jsonString);
            String mqQueueName = RoutePropertyUtil.getInstance().getMqQueueName();
            //RouteMsgAMQUtil.getInstance().sendMapMsg(map,mqQueueName, "Queue");

            for (int i=0;i<100;i++) {
                NoticeMsg e = new NoticeMsg();
                e.setMsgType(String.valueOf(i));
                e.setAcReg("12");
                RouteMsgAMQUtil.getInstance().sendObject(e);
                Thread.sleep(10000);
            }

        } catch (Exception e) {
            log.error("== TestRouteStartup context start error:", e);
        }
        synchronized (TestMqSender.class) {
            while (true) {
                try {
                    TestMqSender.class.wait();
                } catch (InterruptedException e) {
                    log.error("== synchronized error:", e);
                }
            }
        }
    }

}
