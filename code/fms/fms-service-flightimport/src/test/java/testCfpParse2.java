import com.alibaba.druid.filter.config.ConfigTools;
import com.simple.common.utils.DateTimeUtil;
import com.simple.core.flightimport.entity.CfpExt;
import com.simple.core.flightimport.parsecfp.CfpParser;
import com.simple.core.flightimport.parsecfp.impl.ZHCfpParserImpl;

import java.io.*;
import java.text.ParseException;

/**
 * @author ZhuoMengLan.
 * @version 1.0 .
 * @since 2017.03.14 14:24.
 */
public class testCfpParse2 {
    public static void main(String[] args) throws Exception {
        try {
            String encoding = "UTF-8";
            File file = new File("d:\\test.txt");
            if (file.isFile() && file.exists()) { //判断文件是否存在
                InputStreamReader read = new InputStreamReader(
                        new FileInputStream(file), encoding);//考虑到编码格式
                BufferedReader bufferedReader = new BufferedReader(read);
                StringBuffer sb = new StringBuffer();
                String lineTxt = null;
                while ((lineTxt = bufferedReader.readLine()) != null) {
                    sb.append(lineTxt + "\n");
                }
                read.close();
                CfpParser parse = new ZHCfpParserImpl();
                CfpExt cfp = parse.parse(sb.toString());
                System.out.print(cfp.getAlterRoute1Point());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
