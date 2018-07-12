package com.simple.route;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * @author Panda.HuangWei.
 * @version 1.0 .
 * @since 2017.02.08 19:48.
 */
public class TestRouteStartup {
    private static final Log log = LogFactory.getLog(TestRouteStartup.class);

    public static void main(String[] args) {
        try {
            ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext(new String[]{
                    "classpath:spring/spring-context.xml"
            });
            context.start();
        } catch (Exception e) {
            log.error("== TestRouteStartup context start error:", e);
        }
        synchronized (TestRouteStartup.class) {
            while (true) {
                try {
                    TestRouteStartup.class.wait();
                } catch (InterruptedException e) {
                    log.error("== synchronized error:", e);
                }
            }
        }
    }
}
