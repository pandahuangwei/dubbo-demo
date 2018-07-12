package com.simple.core.grib2import.task;

import com.simple.common.utils.DateTimeUtil;
import com.simple.common.utils.StringUtil;
import com.simple.common.utils.UuidUtil;
import com.simple.core.grib2import.entity.Grib2BySkyviewFileVo;
import com.simple.core.grib2import.entity.Grib2TimeVo;
import com.simple.core.grib2import.service.Grib2ImportService;
import com.simple.core.pwi.entity.Grib2WdInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 风温数据计算保存辅助类
 *
 * @author Panda.HuangWei.
 * @version 1.0 .
 * @since 2017.04.13 11:21.
 */
public class Grid2BySkyViewDataTaskHelper {
    private static Logger logger = LoggerFactory.getLogger(Grid2BySkyViewDataTaskHelper.class);

    private Grib2ImportService service;
    // 以1.25度划分的全球坐标点总个数为 145(纬) * 288(经) = 41760
    private  int iCoordinatesSum = 41760;
    // 系统仅录入有效时长为6、12、18的Grib2数据文件
    private  String regexGrib2VaildTime = "\\d+\\.0(06|12|18)"; // 06|12|18

    public Grid2BySkyViewDataTaskHelper(Grib2ImportService service) {
        this.service = service;
    }

    /**
     * Function : 将指定日期、指定高度层、一个时效（6 or 12 or 18）的一组Grib2（对应的“风温、UV分量”）数据入库
     * param dirName : 以yyyyMMddHH表示的，时间字符串
     */
    public void parseData(final String pathTemp, final String pathUV, final int iCurAtoms) throws Exception {
        // 存入GRIB2_WD_INFO表的记录列表数据
        ArrayList grib2InfoList = new ArrayList();

        String[] arrGlobalPointTemp = null;
        String[] arrGlobalPointUV = null;
        // 处理一个存放温度分量的Grib2数据文件
        Grib2BySkyviewFileVo grib2TempVo = null;
        // 处理一个存放UV分量的Grib2数据文件
        Grib2BySkyviewFileVo grib2UVVo = null;

        logger.debug("  处理目录：" + pathTemp);
        logger.debug("  处理目录：" + pathUV);
        grib2TempVo = getGrib2FileObj(pathTemp, 3);
        if (null != grib2TempVo) {
            // 将经纬度点的温度数据存入数组
            arrGlobalPointTemp = grib2TempVo.getGrib2Context().split(" +");
            logger.debug("  当前风温文件含有温度数据个数：" + arrGlobalPointTemp.length);
        }

        grib2UVVo = getGrib2FileObj(pathUV, 3);
        if (null != grib2UVVo) {
            // 将经纬度点的UV分量数据存入数组;
            // 根据SKYVIEW的Grib2数据说明，数组前半部分为全球经纬度点U分量，后半部分为V分量
            arrGlobalPointUV = grib2UVVo.getGrib2Context().split(" +");
            logger.debug("  当前风温文件含有UV数据个数：" + arrGlobalPointUV.length);
        }

        // 没有解析到（同一高度层、同一起止日期）一组（温度、UV分量）数据 ==> 防止指定高度层、经纬度点风温数据不完整
        if ((null == grib2TempVo) || (null == grib2UVVo)
                || ((null != grib2TempVo) && (iCoordinatesSum != arrGlobalPointTemp.length))
                || ((null != grib2UVVo) && (iCoordinatesSum * 2 != arrGlobalPointUV.length))) {
            logger.error("  grib2TempVo：" + grib2TempVo);
            logger.error("  grib2UVVo：" + grib2UVVo);
            return;
        }

        /*
        * 值与经纬度的匹配算法说明：
        *  从SKYVIEW项目组获取到的Grib2数据格式会影响，经纬度点与数据的对应关系（对于SKYFMS1期基于气象局的Grib2数据解析服务实现而言）
        *  年 月 日 时 时效  高度层(mb) 经度格距  纬度格距  起始经度  终止经度  起始纬度  终止纬度（均为浮点数）
        *  14 10 22 18 006    100       1.250000 -1.250000   0.00      358.75    90.00    -90.00
        *  由上面数据说明可知：数据依次为
        *     纬度
        *  -90 |
        *      |
        *      |
        *  90  |
        *      ---------------------------------------  经度
        *    0            180   -178.75            -1.25    (刘佳修正)
        *    先变经度（以1.25为增量）、再变纬度（以-1.25为增量）
        *    eg：第一个数据表示 (0, 90)；第二个点数据表示（1.25, 90）.....
        *
        *    注意：下面for循环中涉及的数值，是根据数据源文件中的头两行说明得来的
        * */
        int iCoordinatesPointNum = 0;
        // 以1.25为间隔，遍历纬度范围
        for (double lat = 90; lat >= -90; lat -= 1.25) {

            // 以1.25为间隔，遍历经度度范围
            for (double lon = 0; lon <= 180; lon += 1.25) {

                Grib2WdInfo grib2WdTmp = new Grib2WdInfo();
                //开始时间
                grib2WdTmp.setStartTime(DateTimeUtil.formatTime(grib2TempVo.getGrib2Time().getPublicTime()));
                //结束时间
                grib2WdTmp.setEndTime(DateTimeUtil.formatTime(grib2TempVo.getGrib2Time().getEndTime()));
                //大气压强
                grib2WdTmp.setAtmosPressure(new BigInteger(String.valueOf(iCurAtoms)));
                //经度
                grib2WdTmp.setLon(new BigDecimal(lon));
                //纬度
                grib2WdTmp.setLat(new BigDecimal(lat));
                grib2WdTmp.setWindType("TMP");
                grib2WdTmp.setWdId(UuidUtil.get32UUID());
                grib2WdTmp.setWindVel(new BigDecimal(arrGlobalPointTemp[iCoordinatesPointNum]));

                grib2InfoList.add(grib2WdTmp);
                Grib2WdInfo grib2WdU = new Grib2WdInfo();
                //开始时间
                grib2WdU.setStartTime(DateTimeUtil.formatTime(grib2UVVo.getGrib2Time().getPublicTime()));
                //结束时间
                grib2WdU.setEndTime(DateTimeUtil.formatTime(grib2UVVo.getGrib2Time().getEndTime()));
                //大气压强
                grib2WdU.setAtmosPressure(new BigInteger(String.valueOf(iCurAtoms)));
                //经度
                grib2WdU.setLon(new BigDecimal(lon));
                //纬度
                grib2WdU.setLat(new BigDecimal(lat));

                Grib2WdInfo grib2WdV = null;
                grib2WdV = (Grib2WdInfo) grib2WdU.clone();

                // 风U分量
                grib2WdU.setWindType("UGRD");
                grib2WdU.setWdId(UuidUtil.get32UUID());
                grib2WdU.setWindVel(new BigDecimal(arrGlobalPointUV[iCoordinatesPointNum]));

                // 风V分量
                // 根据Skyview项目中的Grib2数据UV文件特点：先存U，后存V；  iCoordinatesSum
                grib2WdV.setWindType("VGRD");
                grib2WdV.setWdId(UuidUtil.get32UUID());
                grib2WdV.setWindVel(new BigDecimal(arrGlobalPointUV[iCoordinatesPointNum + iCoordinatesSum]));

                grib2InfoList.add(grib2WdU);
                grib2InfoList.add(grib2WdV);

                // 重新赋初值(赋完初值后，循环立即进行 -180 + 1.25 = -178.75)
                if (180 == lon) {
                    lon = -180;
                }
                // 重新设置内循环退出条件（赋值后，仍满足循环条件）
                if (-1.25 == lon) {
                    iCoordinatesPointNum++;
                    break;
                }
                // 这个值是的大小对for循环执行次数的统计呦~~这个执行次数对应着数组中坐标点值的位置
                iCoordinatesPointNum++;
            }
        }
        if (0 != grib2InfoList.size()) {
            service.storeGrib2Data(grib2InfoList, grib2TempVo.getGrib2Time(), iCurAtoms);
        }

        //删除文件
        try {
            File fDataSource = new File(grib2TempVo.getFilePath());
            fDataSource.delete();
            logger.info("delete file:"+grib2TempVo.getFilePath());
        } catch (Exception e) {
        }

        try {
            File fDataSource = new File(grib2UVVo.getFilePath());
            fDataSource.delete();
            logger.info("delete file:"+grib2UVVo.getFilePath());
        } catch (Exception e) {
        }
    }

    /**
     * @param filePath      : 带解析文件路径（文件中除开始几行为字段说明行外，其余应该是由空白分隔的数据项）
     * @param iStartLineNum : 从第几行开始为实际数据
     * @return Grib2BySkyviewFileVo : 符合条件Grib2文件对象
     * @desc : 找到一个，进行解析（日期、数据排列格式、数据）后删除
     */
    private Grib2BySkyviewFileVo getGrib2FileObj(String filePath, int iStartLineNum) {
        Grib2BySkyviewFileVo grib2BySkyviewFileVo = null;

        try {
            File fSacnDir = new File(filePath);
            if ((null == fSacnDir) || !fSacnDir.isDirectory()) {
                logger.error("无法打开指定数据源(风温or风分量)目录：" + filePath);
                return null;
            }

            String[] fileNameList = fSacnDir.list();
            for (int i = 0; i < fileNameList.length; i++) {
                Grib2TimeVo grib2TimeVo = fileNameToGrib2Time(fileNameList[i]);
                if (null == grib2TimeVo) {
                    continue;
                }
                grib2BySkyviewFileVo = new Grib2BySkyviewFileVo();
                grib2BySkyviewFileVo.setGrib2Time(grib2TimeVo);

                String targetFilePath = fSacnDir + File.separator + fileNameList[i];
                File fDataSource = new File(targetFilePath);
                if (!fDataSource.exists() || fDataSource.isDirectory()) {
                    logger.error("文件不存在：" + fDataSource.getName());
                } else {
                    StringBuffer sb = new StringBuffer();
                    BufferedReader bufReader = new BufferedReader(new FileReader(fDataSource));
                    String strLineTemp = bufReader.readLine();
                    int count = 0;
                    while (!StringUtil.isBlank(strLineTemp)) {
                        count++;
                        if (count < iStartLineNum) {
                            strLineTemp = bufReader.readLine();
                            if (2 == iStartLineNum) {
                                grib2BySkyviewFileVo.setStrFileFormatDesc(strLineTemp);
                            }
                            continue;
                        }
                        sb.append(" ");
                        sb.append(strLineTemp);
                        strLineTemp = bufReader.readLine();
                    }
                    grib2BySkyviewFileVo.setGrib2Context(sb.toString().trim());
                    grib2BySkyviewFileVo.setFilePath(targetFilePath);
                    try {
                        if (bufReader != null)
                            bufReader.close();
                    } catch (IOException ex) {
                        logger.error("流关闭失败");
                        return null;
                    }
                    // 一次只处理一个文件
                    break;
                }
            }
        } catch (Exception e) {
            logger.error(e.getMessage());
            return null;
        }

        return grib2BySkyviewFileVo;
    }

    /**
     * @param fileName : 带解析文件路径
     * @return String : 时间字符串 2014-10-16 18:00:00
     * @desc : 解析符合正则表达式的目标数据源文件名
     */
    private Grib2TimeVo fileNameToGrib2Time(String fileName) {
        Grib2TimeVo grib2TimeVo = null;

        Pattern pattern = Pattern.compile(regexGrib2VaildTime);
        Matcher matcher = pattern.matcher(fileName.trim()); // rawMsg
        if (matcher.matches()) {
            logger.debug("    符合处理条件解析文件：" + fileName);
            String publicTime = "20" + fileName.substring(0, 2) + "-" + fileName.substring(2, 4) + "-" + fileName.substring(4, 6)
                    + " " + fileName.substring(6, 8) + ":00:00";
            try {
                grib2TimeVo = new Grib2TimeVo(publicTime, "", Integer.valueOf(matcher.group(1)));
            } catch (Exception e) {
                logger.error("Grib2文件名中的有效时间解析 Integer.parseInt(strVaildTime) 失败：" + fileName);
                return null;
            }
        }
        return grib2TimeVo;
    }
}
