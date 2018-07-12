import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * @author Panda.HuangWei.
 * @version 1.0 .
 * @since 2017.02.07 15:54.
 */
public class TestAuthStartup {
    private static final Log log = LogFactory.getLog(TestAuthStartup.class);

    public static void main(String[] args) {
        try {
            ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext(new String[]{
                    "classpath:spring/spring-context.xml"
            });
            context.start();
        } catch (Exception e) {
            log.error("== TestAeroStartup context start error:", e);
        }
        synchronized (TestAuthStartup.class) {
            while (true) {
                try {
                    TestAuthStartup.class.wait();
                } catch (InterruptedException e) {
                    log.error("== synchronized error:", e);
                }
            }
        }
    }
}
