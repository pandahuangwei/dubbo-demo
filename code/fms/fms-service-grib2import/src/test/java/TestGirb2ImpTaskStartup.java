import com.simple.core.charging.job.SyncDataApplication;
import com.simple.core.grib2import.task.Grid2BySkyViewDataTask;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.util.Scanner;

/**
 * @author Panda.HuangWei.
 * @version 1.0 .
 * @since 2017.04.13 15:01.
 */
public class TestGirb2ImpTaskStartup {
    private static final Log log = LogFactory.getLog(TestGirb2ImpTaskStartup.class);

    public static void main(String[] args) {


        try {
            ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext(new String[]{
                    "classpath:spring/spring-context.xml"
            });
            Grid2BySkyViewDataTask task = (Grid2BySkyViewDataTask) context.getBean("grid2BySkyViewDataTask");

            Scanner s = new Scanner(System.in);
            while (!s.next().equals("q")) {
                try {
                    task.calcAndSave();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            context.close();
            System.exit(-1);

        } catch (Exception e) {
            log.error("== TestGirb2ImpTaskStartup context start error:", e);
        }
        synchronized (TestGirb2ImpTaskStartup.class) {
            while (true) {
                try {
                    TestGirb2ImpTaskStartup.class.wait();
                } catch (InterruptedException e) {
                    log.error("== synchronized error:", e);
                }
            }
        }
    }
}
