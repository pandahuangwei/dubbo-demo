package com.simple.route;

import com.simple.common.utils.DateTimeUtil;
import com.simple.core.common.facade.ParseMsgFacade;
import com.simple.core.route.flight.service.FlightService;
import com.simple.core.route.util.ReferenceBeanFactory;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * @author Panda.HuangWei.
 * @version 1.0 .
 * @since 2017.03.29 14:40.
 */
public class TestReferenceBeanFactory {

    private static final Log log = LogFactory.getLog(TestReferenceBeanFactory.class);

    public static void main(String[] args) {
        try {
            ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext(new String[]{
                    "classpath:spring/spring-context.xml"
            });

            //ReferenceBeanFactory factory = (ReferenceBeanFactory)context.getBean("referenceBeanFactory");
            //ParseMsgFacade reqfpn = factory.getFacade("REQFPN");
            ReferenceBeanFactory.getInstance().getFacade("REQFPN");
        } catch (Exception e) {
            log.error("== TestReferenceBeanFactory context start error:", e);
        } finally {
            System.exit(0);
        }
        synchronized (TestReferenceBeanFactory.class) {
            while (true) {
                try {
                    TestReferenceBeanFactory.class.wait();
                } catch (InterruptedException e) {
                    log.error("== synchronized error:", e);
                }
            }
        }
    }


}
