import com.simple.core.charging.service.MsgSyncService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.util.Scanner;

/**
 * @author Panda.HuangWei.
 * @version 1.0 .
 * @since 2017.04.16 11:04.
 */
public class TestMsgSync {
    private static final Log log = LogFactory.getLog(TestMsgSync.class);

    public static void main(String[] args) {
        try {
            ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext(new String[]{
                    "classpath:spring/spring-context.xml"
            });
            MsgSyncService service = (MsgSyncService) context.getBean("msgSyncService");


            Scanner s = new Scanner(System.in);
            while (!s.next().equals("q")) {
                try {
                    service.syncMsgSize();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            context.close();
            System.exit(-1);

        } catch (Exception e) {
            log.error("== TestMsgSync context start error:", e);
        }
        synchronized (TestMsgSync.class) {
            while (true) {
                try {
                    TestMsgSync.class.wait();
                } catch (InterruptedException e) {
                    log.error("== synchronized error:", e);
                }
            }
        }
    }
}
