
import com.simple.core.common.entity.Flight;
import com.simple.core.common.entity.MsgBody;
import com.simple.core.common.facade.ParseMsgFacade;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/2/13.
 */
public class TestPwiStartup {

    private static final Log log = LogFactory.getLog(TestPwiStartup.class);
    public static void main(String[] args) {

        try {
            ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext(new String[]{
                    "classpath:spring/spring-context.xml"
            });
            context.start();
            /*PwiService  a=(PwiService) context.getBean("pwiService");

            a.getWindTemp("NJ305",226);
*/
        } catch (Exception e) {
            log.error("== TestFpnStartup context start error:", e);
        }
        synchronized (TestPwiStartup.class) {
            while (true) {
                try {
                    TestPwiStartup.class.wait();
                } catch (InterruptedException e) {
                    log.error("== synchronized error:", e);
                }
            }
        }
    }

    private static void testFpnFacade(ParseMsgFacade fpnFacade) {
        List<Flight> list = new ArrayList<>();
        Flight flight = new Flight();
        flight.setMainCompanyRoute("ZGSZZBAA-C1");
        flight.setAlterCompanyRoute1("aa");
        flight.setDepartureAirport("ZGSZ");
        flight.setArrivalAirport("ZBAA");
        flight.setFlightNo("ZH1111");
        flight.setMainRoutePoint("ZGSZ,SZ002,SZ012,SZ013,ZUH,KIBAS,BOKAT,ADBIN,MEPOG,ZUH75,BIGRO,GIVIV,SAMAS,NYB,ZJHK");
        flight.setAlterAirport1("ERT");
        flight.setMainSpecial(1);
        flight.setAlterRoute1Point("ZHCC,SHX,P63,P357,P338,P320,ZHCC");
        String msgContext = "\"Content\":\"2017-01-25 03:26:03\\n QU SHAQPHO\\n.BJSXCXA 250326\\n FML\\nFI HO1179/AN B-8407\\nDT BJS HRB 250326 F11A\\n-  REQFPN1493\"";
        list.add(flight);
        List<MsgBody> msgBody = fpnFacade.parseMessage(flight, msgContext);
        log.info("msgBody,1");
    }

}
