import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * @author Panda.HuangWei.
 * @version 1.0 .
 * @since 2017.03.03 13:59.
 */
public class TestFocStartup {
    private static Logger logger = LoggerFactory.getLogger(TestFocStartup.class);

    public static void main(String[] args) {
        try {
            ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext(new String[]{
                    "classpath:spring/spring-context.xml"
            });
            context.start();
        } catch (Exception e) {
            logger.error("== TestFocStartup context start error:", e);
        }
        synchronized (TestFocStartup.class) {
            while (true) {
                try {
                    TestFocStartup.class.wait();
                } catch (InterruptedException e) {
                    logger.error("== synchronized error:", e);
                }
            }
        }
    }
}
