package com.simple.core.flightimport.constant;

/**
 * @author Administrator.
 * @version 1.0 .
 * @since 2017.03.09 14:58.
 */
public class FlightConst {
    //签派放行1是,0否,2取不到值
    public static final String DISPACH_NO = "0";
    public static final String DISPACH_YES = "1";
    public static final String DISPACH_NULL = "2";

    //机组是否到位1是,0否,2取不到值
    public static final String CREWARRIVAL_NO = "0";
    public static final String CREWARRIVAL_YES = "1";
    public static final String CREWARRIVAL_NULL = "2";

    //主备航路变化1主2备3主备,缺省其他变化0,9参数缺省
    public static final String CHANGE_OTHER = "0";
    public static final String CHANGE_MAIN = "1";
    public static final String CHANGE_ALTER = "2";
    public static final String CHANGE_MAIN_ALTER = "3";
    public static final String NO_PARAM = "9";

    //V-备降 R-返航,C-取消航班
    public static final String STATUS_V = "V";
    public static final String STATUS_R = "R";
    public static final String STATUS_C = "C";

    //是否特殊航路1是0否
    public static final int ROUTE_NORMAL = 0;
    public static final int ROUTE_SPECIAL = 1;

    //主计划-0，二计划-1
    public static final int MAIN_PLAN = 0;
    public static final int MAIN_SECONDPLAN = 1;


}
