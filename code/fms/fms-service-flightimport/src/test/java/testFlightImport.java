import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * @author Panda.HuangWei.
 * @version 1.0 .
 * @since 2017.03.03 17:00.
 */
public class testFlightImport {
    private static Logger logger = LoggerFactory.getLogger(testFlightImport.class);

    public static void main(String[] args) {
        try {
            ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext(new String[]{

                    "classpath:spring/spring-context.xml"
            });
            context.start();
        } catch (Exception e) {
            logger.error("== testflightimport context start error:", e);
        }
        synchronized (testFlightImport.class) {
            while (true) {
                try {
                    testFlightImport.class.wait();
                } catch (InterruptedException e) {
                    logger.error("== synchronized error:", e);
                }
            }
        }
    }
}
