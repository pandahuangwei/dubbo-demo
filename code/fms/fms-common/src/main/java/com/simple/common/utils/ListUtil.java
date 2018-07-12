package com.simple.common.utils;

import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * @author ZhuoMengLan.
 * @version 1.0 .
 * @since 2017.03.23 10:51.
 */
public class ListUtil {

    /**
     * 取两集合的交集
     * @param list1
     * @param list2
     * @return
     */
    public static List<String> intersect(List<String> list1, List<String> list2) {
        List<String> list = new ArrayList<String>(list2.size());
        for (String t : list2) {
            if (list1.contains(t)) {
                list.add(t);
            }
        }
        return list;
    }

    /**
     * 取两集合的差集
     * @param list1
     * @param list2
     * @return
     */
    public static List<String> diff(List<String> list1, List<String> list2) {
        list1.removeAll(list2);
        return list1;
    }

    /**
     * 集合是否为空
     * @param list
     * @return
     */
    public static boolean isStringListBlank(List<String> list){
        if(CollectionUtils.isEmpty(list)) return true;

        for(String str : list){
            if(!StringUtil.isBlank(str)) return true;
        }

        return  false;
    }


}
