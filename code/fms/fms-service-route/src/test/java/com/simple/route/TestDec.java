package com.simple.route;

import com.alibaba.druid.filter.config.ConfigTools;

/**
 * @author Panda.HuangWei.
 * @version 1.0 .
 * @since 2017.03.16 14:17.
 */
public class TestDec {

    public static void main(String[] args) throws Exception {
        //密码明文
        String password = "root";

        System.out.println("密码[ "+password+" ]的加密信息如下：\n");

        password = ConfigTools.encrypt(password);

        System.out.println("password:"+password);
        String decryptPassword=ConfigTools.decrypt(password);
        System.out.println("decryptPassword："+decryptPassword);


    }

}
