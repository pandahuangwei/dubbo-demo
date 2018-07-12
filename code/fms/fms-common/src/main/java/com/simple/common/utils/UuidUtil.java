package com.simple.common.utils;

import java.util.UUID;

/**
 * @author Panda.HuangWei.
 * @version 1.0 .
 * @since 2017.01.18 17:39.
 */
public class UuidUtil {

    public static String get32UUID() {
        return UUID.randomUUID().toString().trim().replaceAll("-", "");
    }
}
