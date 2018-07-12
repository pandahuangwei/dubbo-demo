package com.simple.core.common.util;


import com.simple.common.utils.DateTimeUtil;
import com.simple.core.pwi.entity.PointVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.math.BigDecimal;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**********************************************************************************
 * Copyright(c)2017 Dcits-air.com All rights reserved.
 * @Title: GribUtil.java.
 * @Package com.simple.core.pwi.util.
 * @Description: grib2风温数据计算.
 *
 * @author Administrator.
 * @version 1.0.
 * @created 2017/3/17 11:01.
 **********************************************************************************/
public class GribUtil {

    // “民航数据公司切片Grib2数据接口标准v1.0”中说明的14个标准高度层，其中1000mb
    // 该变量在“Grib2数据计算”和“SKYVIEW格式的Grib2数据解析”中均有使用
    public static List<Integer> atomsList = new ArrayList<Integer>();

    static {
        atomsList.add(100);
        atomsList.add(150);
        atomsList.add(200);
        atomsList.add(225);
        atomsList.add(250);
        atomsList.add(275);
        atomsList.add(300);
        atomsList.add(350);
        atomsList.add(400);
        atomsList.add(500);
        atomsList.add(600);
        atomsList.add(700);
        atomsList.add(850);

        atomsList.add(1000); // 标准
    }

    /**
     * Takes the 1D coordinate for two points (sample1 and sample2) and the
     * value at each of these points (value and value2, respectively). You then
     * provide any 1D coordinate and the linearly interpolated result will be
     * returned.
     *
     * @param value1  : 线性顶点P1的值（U分量、V分量、风温）
     * @param value2  : 线性顶点P2的值（U分量、V分量、风温）
     * @param sample1 : 线性顶点P1的经度or纬度
     * @param sample2 : 线性顶点P2的经度or纬度
     * @param sample  : 待求点的经度or纬度
     * 2009-07-02 linearInterpolate(double value1, double value2, double
     * sample1, double sample2, double sample)
     * <p/>
     * <br>
     * <p/>
     * p1 +----+ p2<br>
     * <p/>
     * <br>
     * sample1、sample2、sample三者要同时使用经度or纬度值进行计算
     */
    private static BigDecimal linearInterpolate(BigDecimal value1, BigDecimal value2, BigDecimal sample1, BigDecimal sample2, BigDecimal sample) {
        // Math.abs(sample2 - sample1) < 0.000001
        if (((sample2.subtract(sample1)).abs()).compareTo(new BigDecimal(
                "0.000001")) == -1) {
            return value1;
        }
        // ratio = ((sample - sample1) / (sample2 - sample1));
        BigDecimal ratio = (sample.subtract(sample1)).divide(sample2.subtract(sample1), 3,
                BigDecimal.ROUND_HALF_EVEN);
        // value = (value2 - value1) * ratio + value1;
        BigDecimal value = (value2.subtract(value1).multiply(ratio)).add(value1);
        return value;
    }

    /**
     *
     * @param lineList list
     * @param t_x 待求点的经度
     * @param t_y 待求点的纬度
     *  p1/p2 经度不同，纬度相同；p3/p4经度不同，纬度相同, p1_x, p1_y, p1_value :pn点经度、pn点纬度、pn点值（U分量、V分量、风温）
     * @return BigDecimal
     */
    public static BigDecimal biLinearInterpolate(List lineList, BigDecimal t_x, BigDecimal t_y) {


        BigDecimal linei, result;
        List list = new ArrayList();
        BigDecimal p_y = null;

        List listy = new ArrayList();
        // 循环取出经度不同，纬度相同的两组点数据，计算两个经度与目标点经度相同，纬度与目标点纬度各不相同的两个点值
        for (int i = 0; i < lineList.size(); i++) {
            Map map = (Map) lineList.get(i);
            PointVo pointVo1 = (PointVo) map.get("P1");
            PointVo pointVo2 = (PointVo) map.get("P2");
            linei = linearInterpolate(new BigDecimal(pointVo1.getWindVal()),
                    new BigDecimal(pointVo2.getWindVal()), new BigDecimal(
                            pointVo1.getdLon()),
                    new BigDecimal(pointVo2.getdLon()), t_x);
            p_y = new BigDecimal(pointVo1.getdLat());
            listy.add(p_y);
            list.add(linei);
        }
        // 计算经度相同、纬度不同的两个点之间的目标点值
        result = linearInterpolate((BigDecimal) list.get(0),
                (BigDecimal) list.get(1), (BigDecimal) listy.get(0),
                (BigDecimal) listy.get(1), t_y);

        return result;
    }

    /**
     * 获取给定坐标、高度层的周围四个坐标点的列表
     * 四个点的位置如下所示（对于立方体：p3\p4\p7\p8构成的平面在p1\p2\p5\p6的前侧，详细参见
     * ：<<风温上传SKYFMS内部处理流.vsd>>中的立体图） 上层坐标点 p1 +----+ p2<br>
     * | |<br>
     * p3 +----+ p4<br>
     * 下层坐标点 p5 +----+ p6<br>
     * | |<br>
     * p7 +----+ p8<br>
     * <p>
     * desc : 1.25度是Grib2数据间隙
     *
     * @param lon 给定坐标经度
     * @param lat 给定坐标纬度
     * @param alt 给定坐标高度层
     */
    public static List getPointList(double lon, double lat, double alt) {
        List points = new ArrayList();
        // 根据Grib2数据特点： -178.75 <= lon <= 180; -90 <= lat <= 90
        double lonLeft = -178.75;
        double lonRight = lonLeft + 1.25;
        double latDown = -90;
        double latUp = latDown + 1.25;

        List<Integer> altList = getTwoStandardAltitudes(alt);
        int altUp = altList.get(0);
        int altDown = altList.get(1);

        // 左右经度计算 lonRight < lon
        while (lonRight < lon) {
            lonLeft = lonRight;
            lonRight = lonLeft + 1.25;
        }
        // 上下纬度计算 latUp < lat
        while (latUp < lat) {
            latDown = latUp;
            latUp = latDown + 1.25;
        }
        Object p1 = new Object[]{lonLeft, latUp, altUp};
        Object p2 = new Object[]{lonRight, latUp, altUp};
        Object p3 = new Object[]{lonLeft, latDown, altUp};
        Object p4 = new Object[]{lonRight, latDown, altUp};
        Object p5 = new Object[]{lonLeft, latUp, altDown};
        Object p6 = new Object[]{lonRight, latUp, altDown};
        Object p7 = new Object[]{lonLeft, latDown, altDown};
        Object p8 = new Object[]{lonRight, latDown, altDown};

        points.add(p1);
        points.add(p2);
        points.add(p3);
        points.add(p4);
        points.add(p5);
        points.add(p6);
        points.add(p7);
        points.add(p8);
        return points;
    }

    /**
     * grid2时间有效范围 3个小时为一个段 expTime 6,9,12,15,18,21,24
     * @param now 时间
     * @return list
     */
    public static List getTimes(int now, int expTime) {
        List<String> list = new ArrayList<String>();
        int startTime = 0;
        int endTime = startTime + 6;
        while (endTime < now) {
            startTime = endTime;
            endTime = startTime + 6;
        }
        String stime = null;
        String etime = null;
        try {
            stime = DateTimeUtil.getUTCTime("yyyy-MM-dd HH:mm:ss",
                    (endTime - expTime));
            etime = DateTimeUtil.getUTCTime("yyyy-MM-dd HH:mm:ss", endTime);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        list.add(stime);
        list.add(etime);
        return list;

    }

    /**
     * 获取给定经度的前后两个标准经度值
     * @return list
     */
    public static List<Double> getTwoStandardLons(double lon) {
        List<Double> lonList = new ArrayList<Double>();
        double lonLeft = -178.75;
        double lonRight = lonLeft + 1.25;
        // 左右经度计算 lonRight < lon
        while (lonRight < lon) {
            lonLeft = lonRight;
            lonRight = lonLeft + 1.25;
        }
        lonList.add(lonLeft);
        lonList.add(lonRight);
        return lonList;
    }

    /**
     * 获取给定纬度的前后两个标准纬度值
     * @param lat 给定纬度
     * @return list
     */
    public static List<Double> getTwoStandardLats(double lat) {
        List<Double> latList = new ArrayList<Double>();
        double latDown = -90;
        double latUp = latDown + 1.25;
        // 上下纬度计算
        while (latUp < lat) {
            latDown = latUp;
            latUp = latDown + 1.25;
        }
        latList.add(latUp);
        latList.add(latDown);
        return latList;
    }

    /**
     * 获取给定高度层的上下两个标准气压高度层
     * @param alt 给定高度层
     * @return list中第一个，第二个值分别为上下高度层
     */
    public static List<Integer> getTwoStandardAltitudes(double alt) {
        List<Integer> altList = new ArrayList<Integer>();
        int altDown = atomsList.get(0);
        int altUp = atomsList.get(1);

        // 高度层计算
        int i = 0;
        while (alt > altUp && i < atomsList.size()) {
            altDown = atomsList.get(i);
            altUp = atomsList.get(i + 1);
            i++;
        }
        altList.add(altUp);
        altList.add(altDown);
        return altList;
    }

    /**
     * 经纬度数字格式转换
     * @param str
     * @param flag 标识前面参数str的表示格式
     *             1：str “经度 或 纬度”字符串，且以“2/3位度、2位分、2位秒”的格式表示，对应NAIP表
     *             2：str “经度 或 纬度”字符串，且以“2/3位度、2位分、1位分（秒的小数表示）”的格式表示
     * @return 以度为单位的数值
     */
    public static double convertLonLat(String str, int flag) {
        double value = 0.0;
        StringBuffer s = new StringBuffer();
        // E1123248 后四位是分秒各两位，转换成为度。
        BigDecimal second = null;
        BigDecimal min = null;
        BigDecimal sufixdotvalue = null;
        if (1 == flag) {
            second = new BigDecimal(str.substring(str.length() - 2,
                    str.length())); // 截取后两位
            // 分*60转换成“秒”
            min = new BigDecimal(str.substring(str.length() - 4,
                    str.length() - 2)).multiply(new BigDecimal("60"));
            // “分和秒”转换成“度”
            sufixdotvalue = (min.add(second)).divide(BigDecimal.valueOf(3600),
                    3, BigDecimal.ROUND_HALF_EVEN);
            // 后四位是分秒
            s.append(str.substring(0, str.length() - 4));
        } else {
            second = new BigDecimal(str.substring(str.length() - 1,
                    str.length()));
            // “秒的分钟表示”转为为“秒”，先除10，再乘60
            second = second.multiply(new BigDecimal("6"));
            // 分*60转换成“秒”
            min = new BigDecimal(str.substring(str.length() - 3,
                    str.length() - 1)).multiply(new BigDecimal("60"));
            // “分和秒”转换成“度”
            sufixdotvalue = (min.add(second)).divide(BigDecimal.valueOf(3600),
                    3, BigDecimal.ROUND_HALF_EVEN);
            // 后三位是分秒
            s.append(str.substring(0, str.length() - 3));
        }

        if (s.toString().toUpperCase().startsWith("E")
                || s.toString().toUpperCase().startsWith("N")) {
            value = Double.valueOf(s.substring(1, s.length()))
                    + sufixdotvalue.doubleValue();
        } else if (s.toString().toUpperCase().startsWith("W")
                || s.toString().toUpperCase().startsWith("S")) {
            value = -Double.valueOf(s.substring(1, s.length()))
                    + sufixdotvalue.doubleValue();
        }
        return value;
    }

    /**
     * 经纬度数字格式转换
     * @param str
     * str “经度 或 纬度”字符串，且以“2/3位度.3小数”的格式表示
     * @return
     */
    public static double convertLonLat2(String str) {
        double value = 0.0;
        StringBuffer s = new StringBuffer(str);
        s.insert(str.length() - 3, ".");
        str = s.toString().toUpperCase();
        if (str.startsWith("E") || str.startsWith("N")) {
            value = Double.valueOf(str.substring(1, str.length()));
        } else if (str.startsWith("W") || str.startsWith("S")) {
            value = -Double.valueOf(str.substring(1, str.length()));
        }
        return value;

    }

	/*
     * Po = 1.01325×105 Pascals Kf = 0.3048m/ft α = lapse rate(6.5×10-3Km-1 when
	 * PALT < 36,089ft) 1. MEASUREMENT OF PRESSURE ALTITUDE 7 Figure 1
	 * Pitot-static probe Figure 2 ICAO International Standard Atmosphere ICAO
	 * Atmosphere -10000 0 10000 20000 30000 40000 50000 60000 -60 -40 -20 0 20
	 * 40 60 80 100 120 Temperature(C), Pressure(hPa/10) Height(ft) Temp(C)
	 * Pressure(hPa/10) go = Acceleration due to gravity at sea level =
	 * 9.80665ms-2 at 45°N ρo = Density of dry air at sea level = 1.225kgm-3 To
	 * = 288.15K And β= go ρo To/ α Po wujianjun(武建军) 13:00:06 Po = 1.01325×105
	 * Pascals Kf = 0.3048m/ft α = lapse rate(6.5×10-3Km-1 when PALT < 36,089ft)
	 * go = Acceleration due to gravity at sea level = 9.80665ms-2 at 45°N ρo =
	 * Density of dry air at sea level = 1.225kgm-3 To = 288.15K And β= go ρo
	 * To/ α Po
	 */

    /**
     * @param palt : 飞行高度值
     * @return : 风速,风向
     * @Desc : 将飞行高度转换为气象高度
     */
    public static BigDecimal fl2hpa(BigDecimal palt) {

        BigDecimal P_hPa = null;

        // palt <= 36089
        if (palt.compareTo(new BigDecimal(36089)) <= 0) {
            BigDecimal P0 = new BigDecimal(1.01325e5);
            BigDecimal Kf = new BigDecimal(0.3048);
            BigDecimal g0 = new BigDecimal(9.80665);
            BigDecimal rho0 = new BigDecimal(1.225);
            BigDecimal T0 = new BigDecimal(288.15);
            BigDecimal alpha = new BigDecimal(6.5e-3);
            // g0 * rho0 * T0 / (alpha * P0);
            BigDecimal beta = new BigDecimal(9.80665)
                    .multiply(new BigDecimal(1.225))  //multiply 返回其值为 (this * val) 的 BigInteger
                    .multiply(new BigDecimal(288.15))
                    .divide(new BigDecimal(6.5e-3).multiply(new BigDecimal(
                            1.01325e5)), 3, BigDecimal.ROUND_HALF_EVEN);
            P_hPa = new BigDecimal(1e-2).multiply(new BigDecimal(1.01325e5)).multiply(new BigDecimal(Math.pow((new BigDecimal("1").subtract(new BigDecimal(0.3048).multiply(new BigDecimal(6.5e-3)).multiply(palt).divide(new BigDecimal(288.15), 3, BigDecimal.ROUND_HALF_EVEN))).doubleValue(), beta.doubleValue())));
        } else {
            P_hPa = (new BigDecimal(226.32)).multiply(new BigDecimal(Math
                    .exp(-(palt.subtract(new BigDecimal(36089)).divide(
                            new BigDecimal(20805), 3,
                            BigDecimal.ROUND_HALF_EVEN).doubleValue()))));
        }

        return P_hPa;
    }

    /**
     * @param uParam: U分量
     * @param vParam: V分量
     * @return : 风速,风向
     * @Desc: 通过U/V分量计算某个点风速/风向，另外，指定高度层、经度、纬度三者确定一个点
     */
    public static String ComputeWindVelDirByUV(BigDecimal uParam, BigDecimal vParam) {
        // Math.sqrt(uParam * uParam + vParam * vParam)
        BigDecimal windVel = new BigDecimal(Math.sqrt((uParam.multiply(uParam)
                .add(vParam.multiply(vParam))).doubleValue()));
        BigDecimal windDir = new BigDecimal("0");
        // windVel > 0.0
        if (windVel.compareTo(new BigDecimal("0.0")) == 1) {
            // vParam / windVel
            windDir = vParam.divide(windVel, 10, BigDecimal.ROUND_HALF_EVEN);
            // windDir = (Math.asin(windDir) * 180.0 / 3.1415926536);
            windDir = (new BigDecimal(Math.asin(windDir.doubleValue()))
                    .multiply(new BigDecimal("180.0")).divide(new BigDecimal("3.1415926536"), 10, BigDecimal.ROUND_HALF_EVEN));
            // vParam >= 0.0 && uParam < 0.0
            if (vParam.compareTo(new BigDecimal("0.0")) >= 0
                    && uParam.compareTo(new BigDecimal("0.0")) == -1)
                // windDir = (180.0 - windDir);
                windDir = ((new BigDecimal("180.0")).subtract(windDir));
            // (vParam < 0.0 && uParam < 0.0)
            if (vParam.compareTo(new BigDecimal("0.0")) == -1 && uParam.compareTo(new BigDecimal("0.0")) == -1)
                // -180.0 - windDir;
                windDir = new BigDecimal("-180.0").subtract(windDir); // subtract
            // 270.0 - windDir
            windDir = (new BigDecimal("270.0")).subtract(windDir);

            // windDir > 0.0
            if (windDir.compareTo(new BigDecimal("360.0")) == 1) {
                // windDir = windDir - 360.0
                windDir = windDir.subtract(new BigDecimal("360.0"));
            }
        }
        // else {
        // windDir = new BigDecimal(9999);
        // }
        return String.valueOf(GribUtil.MeV2EnV(windVel).setScale(0, BigDecimal.ROUND_HALF_EVEN))
                + ","
                + String.valueOf(windDir.setScale(0, BigDecimal.ROUND_HALF_EVEN));
    }

    /**
     * @param ppin  : 来自Grib2的标准高度层数组，单位为mb，必须将标准高度层由高至低放入数组中
     * @param npin  : ppin数据大小
     * @param xxin  : 本次计算所需的实际数据（U/V分量、风温）
     * @param ppout : 需要计算的高度层数组，单位为mb
     * @param npout : 需要计算的高度层个数
     * @return xxout: 作为返回值，返回与ppout所求高度层对应的值（U/V分量、风温）
     * 本代码从fortran代码翻译而来，主要参考p_interp.F90的第24~26页
     * @Desc: 已知三个经纬度相同、高度层不同的点，上下两个高度层（U分量/V分量/风温）已知，求中间任意高度层点的（U分量/V分量/风温）；
     */
    public static BigDecimal[] ComputeLogarithmValue(BigDecimal ppin[], int npin, BigDecimal xxin[], BigDecimal ppout[], int npout) {
        int linlog = 1; // 为1线性插值计算，为2对数插值计算
        // (线性插值法是指使用连接两个已知量的直线来确定在这两个已知量之间的一个未知量的值的方法。)
        BigDecimal slope, pa, pb, pc; // 临时变量
        BigDecimal xxout[] = new BigDecimal[npout];
        if (1 == linlog) {
            for (int np = 1; np <= npout; np++) {
                for (int n1 = 1; n1 <= npin - 1; n1++) {
                    // (ppout[np - 1] < ppin[n1 - 1]) && (ppout[np - 1] >
                    // ppin[n1])) {
                    if ((ppout[np - 1].compareTo(ppin[n1 - 1]) == -1)
                            && (ppout[np - 1].compareTo(ppin[n1]) == 1)) {
                        // slope = (xxin[n1 - 1] - xxin[n1]) / (ppin[n1 - 1] -
                        // ppin[n1]);
                        slope = (xxin[n1 - 1].subtract(xxin[n1])).divide(
                                ppin[n1 - 1].subtract(ppin[n1]), 3,
                                BigDecimal.ROUND_HALF_EVEN);
                        // xxin[n1] + slope * (ppout[np - 1] - ppin[n1]);
                        xxout[np - 1] = xxin[n1].add(slope
                                .multiply(ppout[np - 1].subtract(ppin[n1])));
                    }
                }
            }
        } else if (2 == linlog) {
            for (int np = 1; np <= npout; np++) {
                for (int n1 = 1; n1 <= npin - 1; n1++) {
                    // if (ppout[np - 1] < ppin[n1 - 1] && ppout[np - 1] >
                    // ppin[n1])
                    if (ppout[np - 1].compareTo(ppin[n1 - 1]) == -1
                            && ppout[np - 1].compareTo(ppin[n1]) == 1) {
                        pa = new BigDecimal(
                                Math.log(ppin[n1 - 1].doubleValue()));
                        pb = new BigDecimal(Math.log(ppout[np - 1]
                                .doubleValue()));
                        // fortran的real类型数组，默认初始化为双浮点型0值
                        if (ppin[n1].compareTo(new BigDecimal("0.0")) == 1) {
                            pc = new BigDecimal(
                                    Math.log(ppin[n1].doubleValue()));
                        } else {
                            pc = new BigDecimal(Math.log(1.0 - 4.0));
                        }
                        // (xxin[n1 - 1] - xxin[n1]) / (pa - pc);
                        slope = (xxin[n1 - 1].subtract(xxin[n1])).divide(
                                pa.subtract(pc), 3, BigDecimal.ROUND_HALF_EVEN);
                        xxout[np - 1] = xxin[n1].add(slope.multiply(pb
                                .subtract(pc)));
                    }
                }
            }
        }
        return xxout;
    }

    /**
     * @param kRef : 以开尔文为单位的温度值（Grib2数据中使用）
     * @return xxout: 作为返回值，返回以摄氏度为单位的温度值
     * @Desc: 将温度单位为开尔文的转化为摄氏度
     */
    public static BigDecimal TempK2C(BigDecimal kRef) {
        return kRef.subtract(new BigDecimal(273.15));
    }

    /**
     * @param meV :公制速度，
     * @return enV: 英制速度
     * @Desc: 将公制速度转换成英制速度
     */
    public static BigDecimal MeV2EnV(BigDecimal meV) {
        return meV.multiply(new BigDecimal(1.852));
    }

    /**
     * 取含有经纬度的航路点中的经纬度
     * @param name 航路点
     * @return 经纬度集合
     */
    public static List getLonLatList(String name) {
        String lonReg = "(E|W)\\d+";
        String latReg = "(N|S)\\d+";

        String lon=null;
        Pattern pattern = Pattern.compile(lonReg); // [a-zA-Z_0-9]
        Matcher matcher = pattern.matcher(name);
        List list = new ArrayList();
        while (matcher.find()) {
            lon = matcher.group();
            if (lon.length() == 8) {
                list.add(GribUtil.convertLonLat(lon, 1));
            } else {
                list.add(GribUtil.convertLonLat2(lon));
            }
        }

        String lat=null;
        pattern = Pattern.compile(latReg);
        matcher = pattern.matcher(name);
        while (matcher.find()) {
            lat = matcher.group();
            if (lat.length() == 7) {
                list.add(GribUtil.convertLonLat(lat, 1));
            } else {
                list.add(GribUtil.convertLonLat2(lat));
            }
        }

        //补充原始格式的经纬度
        list.add(lon);
        list.add(lat);
        return list;
    }

    /**
     * @param name
     * @return
     * @Desc 判断航路点是不是经纬度数据组成
     */
    public static boolean nameIsLonLat(String name) {
        String patternStr = "(N|S)\\d+(E|W)\\d+";
        return Pattern.matches(patternStr, name);

    }

    /**
     * 去掉高度层中重复的高度层
     */
    public static String[] removeRepeatAlts(String[] alts) {
        List<String> list = new ArrayList<String>();
        for (int i = 0; i < alts.length; i++) {
            if (!list.contains(alts[i])) {
                list.add(alts[i]);
            }
        }
        return list.toArray(new String[0]);

    }

    public static void main(String arg[]) {
        //测试
         //double aaa = convertLonLat("W10233",2);
         //System.out.println(aaa);

         //List aa = getAltitudes(310);
         //System.out.println(aa.get(0));
        // System.out.println(aa.get(1));

        // 测试 fl2hpa 飞行高度转换为气象高度
        /* BigDecimal palt = new BigDecimal(30000);
         BigDecimal p_hPa = fl2hpa(palt);
         System.out.println(p_hPa);
        int testArr[] = {12000, 18000, 24000, 30000, 34000, 39000, 45000,
                50000};
        for (int i = 0; i < testArr.length; i++) {
            BigDecimal palt2 = new BigDecimal(testArr[i]);
            BigDecimal p_hPa2 = fl2hpa(palt2);
            System.out.println(p_hPa2);
        }*/

        //
        // // 测试 ComputeWindVelDirFromLayer
         //double ppin[] = new double[3];
         //ppin[0] = 600;
         //ppin[1] = 500;
         //ppin[2] = 300;
         //int npin = 3;
        // double xxin[] = new double[3];
         //xxin[0] = 2;
         //xxin[1] = 3;
         //xxin[2] = 4;
        // double ppout[] = new double[1];
        // ppout[0] = 400;
        // int npout = 1;
        // double [] xxout = ComputeWindVelDirFromLayer(ppin, npin, xxin, ppout,
        // npout);
        // for (int i = 0; i < npout; i++) {
        // System.out.println(xxout[i]);
        // }
        // for (int i = 280; i <= 450; i = i + 10) {
        // int temp = i * 100;
        // System.out.println(fl2hpa(new BigDecimal(temp)).setScale(3,
        // BigDecimal.ROUND_HALF_EVEN));
        // }

         /*String regEx = "(E|W)\\d+";
         String regEx1 = "(N|S)\\d+";
         String s = "N12314W12312";
         String add[] = s.split(regEx);
         for (int i = 0; i < add.length; i++) {
         System.out.println(add[i]);
         }*/
        // Pattern pattern = Pattern.compile(regEx); //[a-zA-Z_0-9]
        // Matcher matcher = pattern.matcher(s);
        // List<String> list = new ArrayList<String>();
        // while (matcher.find()) {
        // String temp = matcher.group();
        // list.add(temp);
        // }
        // System.out.println(list);
        // pattern = Pattern.compile(regEx1);
        // matcher = pattern.matcher(s);
        //
        // while (matcher.find()) {
        // String temp = matcher.group();
        // list.add(temp);
        // }
        // System.out.println(list);

        // String str = "no pains . no Gains.";
        // Pattern pattern = Pattern.compile("(\\w)*"); //[a-zA-Z_0-9]
        // Matcher matcher = pattern.matcher(str);
        // List<String> list = new ArrayList<String>();
        // int size = 0,length;
        // String temp;
        // String result = "" ;
        // while(matcher.find()){
        // temp = matcher.group();
        // list.add(temp);
        // length = temp.length();
        // if(length > size){
        // size = length ;
        // result = temp;
        // }
        // }
        // System.out.println(list);
        // for(int i=0;i<list.size();i++){
        // System.out.println(list.get(i));
        // }
        // System.out.println(result);

        // 测试java访问FTP文件

    }

}
