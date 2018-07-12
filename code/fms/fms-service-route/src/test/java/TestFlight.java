import com.simple.common.utils.DateTimeUtil;
import com.simple.core.route.flight.service.FlightService;
import com.simple.route.TestRouteStartup;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * @author Panda.HuangWei.
 * @version 1.0 .
 * @since 2017.03.29 10:45.
 */
public class TestFlight {
    private static final Log log = LogFactory.getLog(TestFlight.class);

    public static void main(String[] args) {
        try {
            ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext(new String[]{
                    "classpath:spring/spring-context.xml"
            });
            //context.start();
            FlightService flightService = (FlightService)context.getBean("flightService");
            flightService.getFlight("ZH","B6571","REQFPN", DateTimeUtil.getNowDt());
        } catch (Exception e) {
            log.error("== TestFlight context start error:", e);
        } finally {
            System.exit(0);
        }
        synchronized (TestFlight.class) {
            while (true) {
                try {
                    TestFlight.class.wait();
                } catch (InterruptedException e) {
                    log.error("== synchronized error:", e);
                }
            }
        }
    }

}
