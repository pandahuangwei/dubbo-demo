import com.alibaba.druid.filter.config.ConfigTools;

/**
 * @author Panda.HuangWei.
 * @version 1.0 .
 * @since 2017.04.17 15:42.
 */
public class DruidEncty {

    public static void main(String[] args) throws Exception{
        String encryStr = "fms";
        String fms = ConfigTools.encrypt(encryStr);
        System.out.println(encryStr+": ");
        System.out.println(fms);

        String decrypt = ConfigTools.decrypt(fms);
        System.out.println("解密:"+decrypt);
    }
}
