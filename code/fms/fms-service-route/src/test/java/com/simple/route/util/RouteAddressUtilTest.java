package com.simple.route.util;

import com.simple.core.route.util.RouteAddressUtil;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import static org.junit.Assert.*;

/**
 * @author Panda.HuangWei.
 * @version 1.0 .
 * @since 2017.02.08 16:13.
 */
public class RouteAddressUtilTest {
    public static void main(String[] args) {
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("classpath:spring/spring-context.xml");

        String fpn = RouteAddressUtil.instance.getAddress("FPN");
    }
}