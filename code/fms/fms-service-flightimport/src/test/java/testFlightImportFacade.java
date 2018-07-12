import com.simple.core.flightimport.service.FlightImportService;
import com.simple.core.foc.entity.FocFlight;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Administrator on 2017/3/6.
 */
public class testFlightImportFacade {
    private static Logger logger = LoggerFactory.getLogger(testFlightImportFacade.class);

    public static void main(String[] args) {
        try {
            ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext(new String[]{
                    "classpath:spring/spring-context.xml"
            });
            // context.start();
            FlightImportService service = (FlightImportService) context.getBean("flightImportService");

            testSaveFocFlight(service);

        } catch (Exception e) {
            logger.error("== testFlightImportFacade context start error:", e);
        }
        synchronized (testFlightImportFacade.class) {
            while (true) {
                try {
                    TestFlightImportStartup.class.wait();
                } catch (InterruptedException e) {
                    logger.error("== synchronized error:", e);
                }
            }
        }
    }

    private static void testSaveFocFlight(FlightImportService service) {
        List<FocFlight> list = new ArrayList<>();
        FocFlight t1 = new FocFlight();
        t1.setFocFlightId("1");
        t1.setFlightDate(new Date());
        list.add(t1);

      /*  service.saveFocFlight(list);*/
    }

}
