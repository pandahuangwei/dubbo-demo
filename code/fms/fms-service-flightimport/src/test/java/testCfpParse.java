import com.simple.common.persistence.db.MybatisTplDao;
import com.simple.core.flightimport.entity.CfpExt;
import com.simple.core.flightimport.parsecfp.CfpParser;
import com.simple.core.flightimport.parsecfp.impl.ZHCfpParserImpl;
import com.simple.core.foc.entity.FocCfp;
import com.simple.core.foc.entity.FocFlight;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import java.util.ArrayList;
import java.util.List;


/**
 * Created by Administrator on 2017/3/1.
 */
public class testCfpParse {



    public static void main(String[] args)  {
        Logger logger = LoggerFactory.getLogger(testCfpParse.class);

        try {
            ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext(new String[]{
                    "classpath:spring/spring-context.xml"
            });

            MybatisTplDao dao=(MybatisTplDao)context.getBean("mybatisTplDao");

            List<FocFlight>  listFlight=dao.findList("FocMapper.selectFlightId");
            logger.info("===========================取到总数据=============================="+listFlight.size());
            List<FocCfp>  listCfp=new ArrayList<>();
            for (FocFlight item:listFlight) {
                String id=item.getFocFlightId();
                FocCfp cfp= dao.findOne("FocMapper.selectCfpByFlightId",id);
                if(cfp==null)continue;
                cfp.setFlightId(id);
                listCfp.add(cfp);
            }
            //List<String> list2=new ArrayList<>();
            //遍历解析飞行计划文件
            for (FocCfp item:listCfp) {
                CfpParser parse = new ZHCfpParserImpl();
                CfpExt cfp = parse.parse(item.getCfpInfo());
               // list2.add(cfp.getMainRoutePoint());

                System.out.print(item.getFlightId()+"===="+cfp.getMainRoutePoint()+"\n");
            }
           // logger.info("=====================解析后结果==================="+list2.size());
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
    }


}
