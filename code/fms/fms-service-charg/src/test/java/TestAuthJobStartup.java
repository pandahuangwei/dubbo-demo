import com.simple.core.charging.job.SyncDataApplication;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.util.Scanner;

/**
 * @author Panda.HuangWei.
 * @version 1.0 .
 * @since 2017.04.06 9:44.
 */
public class TestAuthJobStartup {
    private static final Log log = LogFactory.getLog(TestAuthJobStartup.class);

    public static void main(String[] args) {
        try {
            ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext(new String[]{
                    "classpath:spring/spring-context.xml"
            });
            SyncDataApplication sync = (SyncDataApplication) context.getBean("syncDataApplication");
//            context.start();
            Scanner s = new Scanner(System.in);
            while (!s.next().equals("q")) {
                try {
                    sync.syncAuth();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            context.close();
            System.exit(-1);

        } catch (Exception e) {
            log.error("== TestAuthJobStartup context start error:", e);
        }
        synchronized (TestAuthJobStartup.class) {
            while (true) {
                try {
                    TestAuthJobStartup.class.wait();
                } catch (InterruptedException e) {
                    log.error("== synchronized error:", e);
                }
            }
        }
    }
}
