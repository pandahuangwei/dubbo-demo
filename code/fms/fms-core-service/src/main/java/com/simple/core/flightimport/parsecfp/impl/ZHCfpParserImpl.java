package com.simple.core.flightimport.parsecfp.impl;

import com.simple.common.config.BufferConfigMgr;
import com.simple.common.config.ConfigBufferReader;
import com.simple.common.utils.StringUtil;
import com.simple.core.flightimport.constant.FlightConst;
import com.simple.core.flightimport.entity.CfpExt;
import com.simple.core.flightimport.entity.Cpt;
import com.simple.core.flightimport.parsecfp.CfpParser;
import com.simple.core.foc.service.impl.FocFlightServiceImpl;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.apache.commons.lang3.StringUtils;
import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**********************************************************************************
 * Copyright(c)2017 Dcits-air.com All rights reserved.
 * @Title: ZHCfpParserImpl.java.
 * @Package com.simple.core.flightimport.parsecfp.impl.
 * @Description: 深圳航空的飞行计划文件解析.
 *
 * @author Administrator.
 * @version 1.0.
 * @created 2017/3/16 18:22.
 **********************************************************************************/
@Service("ZHCfpParser")
public class ZHCfpParserImpl implements CfpParser {
    private static final Logger logger = LoggerFactory.getLogger(FocFlightServiceImpl.class);
    //常量定义
    private static final String ALT_END = "ENROUTE ALTERNATE";           //备降场信息结束位置
    private static final String ALT_START = "DESTINATION ALTERNATE";     //备降场信息开始位置
    private static final String MAIN_ROUTE_START = "ROUTE DESCRIPTION";  //主航路航信息开始位置
    private static final String MIAN_POINT_LAT_LONG_END = "DESTINATION";                //主航路点信息结束标记
    private static final String MIAN_POINT_LAT_LONG_START = "\nELEV";      //主航路点经纬度开始解析标记
    private static final String ROUTE_POINT_MARK = "CPT";                //主航路点信息开始标记
    private static final String ALT_INFO_MARK = "ALTERNATE";             //备降场基本信息开始标记
    private static final String START_N = "N";
    private static final String START_ALTER_ROUTE_NAME = "DIV RTE";               //备降航路名称开始标记
    private static final String START_MAINCOMPANY = "FOB";          //主公司航路开始标记
    private static final String START="PLAN";
    private static final String POA="POA";
    private static final String ALT="ALT";
    private static final String HLD="HLD";
    private static final String RES="RES";
    private static final String XTR="XTR";
    private static final String  APU="APU";
    private static final String TXO="TXO";
    private static final String TXI="TXI";
    private static final String ROUTE_AVG_WIND="ROUTE AVG WIND";
    private static final String ROUTE_AVG_TEMP="ROUTE AVG TEMP";
    private static final String M="M";
    private static final String P="P";

    /**
     * 解析飞行计划
     *
     * @param cfpText
     * @return
     */
    @Override
    public CfpExt parse(String cfpText) throws UnsupportedEncodingException {
        return parse(cfpText.getBytes("utf-8"));
    }

    /**
     * 解析飞行计划(主航路备降航路的航路点和描述)
     *
     * @param cfpArray
     * @return
     */
    public CfpExt parse(byte[] cfpArray) {
        if (cfpArray==null) return null;

        //处理航路点：过滤特殊航路点/经纬度航路点
        PropertiesConfiguration confMap = BufferConfigMgr.getInstance().getConfig("service");
        String filters = ConfigBufferReader.getString(confMap,"FILTERS.ZH","");
        String specialRegex = ConfigBufferReader.getString(confMap,"SPECIAL.ZH","");

        //将飞行计划转换成字符串
        String cfpArr = new String(cfpArray);
        CfpExt cfp = new CfpExt();

        //截取油量等信息
        try {
            String other=cfpArr.substring(0,cfpArr.indexOf(ALT_START));
            parseOther(other,cfp);
        } catch (Exception e) {
            logger.error(" parseAlterNate error：{}",e.getMessage());
        }
        try {
            //截取备降航路信息
            String alterRoute = cfpArr.substring(cfpArr.indexOf(ALT_START), cfpArr.indexOf(ALT_END));
            parseAlterNate(alterRoute, cfp,filters,specialRegex);
        } catch (Exception e) {
            logger.error(" parseOther error：{}",e.getMessage());
        }
        //截取主航路信息
        String mainRouteInfo = cfpArr.substring(cfpArr.indexOf(MAIN_ROUTE_START));
        //截取主航路中的航路描述
        String mainRouteDescription = mainRouteInfo.substring(mainRouteInfo.indexOf(MAIN_ROUTE_START), mainRouteInfo.indexOf(ROUTE_POINT_MARK));
        String[] description = mainRouteDescription.split("\n");
        String[] mianRoute = description[0].split(":");
        String[] mianRoutedes = StringUtils.split(mianRoute[1], " ");
        String mainroute = StringUtil.replaceRN(mianRoutedes[2]);
        cfp.setMainRoute(StringUtil.replaceRN(mainroute));
        int len = mainroute.length();
        String pod = mainroute.substring(0,(int)(len/2));
        String poa = mainroute.substring(len%2==0 ? (int)(len/2) : (int)(len/2)+1 );

        //截取主航路中的航路点信息
        String mainRoutePointInfo = mainRouteInfo.substring(mainRouteInfo.indexOf(MIAN_POINT_LAT_LONG_START),
                mainRouteInfo.indexOf(MIAN_POINT_LAT_LONG_END));
        String[] planInfo = mainRoutePointInfo.trim().split("\n");
        boolean bPointLine = false;
        boolean bMainSpecial = false;
        StringBuffer sb = new StringBuffer();
        for (int i = 0,size=planInfo.length; i <size ; i++) {
            String line = planInfo[i];
            if(line.contains(pod)){
                bPointLine = true;
            }
            if(!bPointLine){
               continue;
            }
            String[] arr = StringUtils.split(line, " ");
            for (int j = 1,length=arr.length; j <length ; j+=2) {
                if (arr[j].startsWith(START_N) && StringUtil.replaceRN(arr[j]).length() == 13) {  //说明是经纬度
                    String name = arr[j - 1];
                    if(filters.contains(name)) continue;
                    sb.append(name).append(",");

                    Cpt cpt = new Cpt();
                    String lngAndLat = StringUtil.replaceRN(arr[j]);
                    String lng = lngAndLat.substring(lngAndLat.indexOf("E"));  //经度
                    String lat = lngAndLat.substring(lngAndLat.indexOf("N"), lngAndLat.indexOf("E")); //纬度
                    cpt.setLat(StringUtil.replaceRN(lat));
                    cpt.setLng(StringUtil.replaceRN(lng));
                    cpt.setCptName(StringUtil.replaceRN(name));
                    cpt.setModifyTm(new Date());

                    //将特殊航路点转换成经纬度格式
                    if(matchSpecial(name,specialRegex)){
                        bMainSpecial = true;
                        cpt.setSpecial(true);
                        cpt.setCptName(lngAndLat);
                    }
                    cfp.addCpt(cpt);
                }
            }

            if(line.contains(poa)){
                break;
            }
        }
        cfp.setMainRoutePoint(StringUtil.replaceRN(StringUtil.trimEnd(sb.toString(),",")));
        cfp.setMainSpecial(bMainSpecial? FlightConst.ROUTE_SPECIAL:FlightConst.ROUTE_NORMAL);
        return cfp;
    }

    /**
     * 解析备降航路信息
     * @param alterRoute 备降航路
     * @param cfp  飞行计划
     * @param filters 过滤特殊点
     * @param specialRegex  特殊点标记
     */
    public void parseAlterNate(String alterRoute, CfpExt cfp,String filters,String specialRegex) {

        String[] alterInfo = alterRoute.split("\n");
        StringBuffer sb1 = new StringBuffer();
        StringBuffer sb2 = new StringBuffer();
        StringBuffer sb3 = new StringBuffer();
        for (int i = 0,size=alterInfo.length; i < size; i++) {
            String str = alterInfo[i];

            //alternate - 1  zggg   055  328   156  118  m001  00.33  1427
            //解析备降机场名称，备降航路的高度层，备降航路的风分量
            if (str.startsWith(ALT_INFO_MARK)) {
                String[] strInfo = StringUtils.split(str, " ");
                if (strInfo[2].trim().equals("1")) {
                    //备降机场1
                    cfp.setAlterAirport1(StringUtil.replaceRN(strInfo[3]));
                    //备降航路1高度层
                    cfp.setAlterRoute1Fl(StringUtil.trimstart(StringUtil.replaceRN(strInfo[7])));
                    //备降航路1风分量
                    cfp.setAlterRoute1Wc(StringUtil.replaceRN(strInfo[8]));
                    //备降航路1油耗
                    cfp.setAlterRoute1Fuel(StringUtil.replaceRN(strInfo[10]));
                } else if (strInfo[2].trim().equals("2")) {
                    //备降机场2
                    cfp.setAlterAirport2(StringUtil.replaceRN(strInfo[3]));
                    //备降航路2高度层
                    cfp.setAlterRoute2Fl(StringUtil.trimstart(StringUtil.replaceRN(strInfo[7])));
                    //备降航路2风分量
                    cfp.setAlterRoute2Wc(StringUtil.replaceRN(strInfo[8]));
                    //备降航路1油耗
                    cfp.setAlterRoute2Fuel(StringUtil.replaceRN(strInfo[10]));
                } else if (strInfo[2].trim().equals("3")) {
                    //备降机场3
                    cfp.setAlterAirport3(StringUtil.replaceRN(strInfo[3]));
                    //备降航路3高度层
                    cfp.setAlterRoute3Fl(StringUtil.trimstart(StringUtil.replaceRN(strInfo[7])));
                    //备降航路3风分量
                    cfp.setAlterRoute3Wc(StringUtil.replaceRN(strInfo[8]));
                    //备降航路1油耗
                    cfp.setAlterRoute3Fuel(StringUtil.replaceRN(strInfo[10]));
                }
            }

            //解析备降机场描述(3个)
            if (str.startsWith(START_ALTER_ROUTE_NAME)) {
                String[] strInfo = StringUtils.split(alterInfo[i-1], " ");
                String next=alterInfo[i+1];
                if (strInfo[2].trim().equals("1")) {
                    String[] alterde1 = StringUtils.split(str, "-");
                    //避免备降机场描述有两行的情况
                    if(next!=null && next.length()>0 && !next.startsWith(ROUTE_POINT_MARK)){
                        cfp.setAlterRoute1(StringUtil.replaceRN(alterde1[1]+" "+next.trim()));
                    }else{
                        cfp.setAlterRoute1(StringUtil.replaceRN(alterde1[1]));
                    }
                } else if (strInfo[2].trim().equals("2")) {
                    String[] alterde2 = StringUtils.split(str, "-");
                    //避免备降机场描述有两行的情况
                    if(next!=null && next.length()>0 && !next.startsWith(ROUTE_POINT_MARK)){
                        cfp.setAlterRoute2(StringUtil.replaceRN(alterde2[1]+" "+next.trim()));
                    }else {
                        cfp.setAlterRoute2(StringUtil.replaceRN(alterde2[1]));
                    }
                } else if(strInfo[2].trim().equals("3")) {
                    String[] alterde3 = StringUtils.split(str, "-");
                    //避免备降机场描述有两行的情况
                    if(next!=null && next.length()>0 && !next.startsWith(ROUTE_POINT_MARK)){
                        cfp.setAlterRoute3(StringUtil.replaceRN(alterde3[1]+" "+next.trim()));
                    }else {
                        cfp.setAlterRoute3(StringUtil.replaceRN(alterde3[1]));
                    }
                }
            }

            //解析备降航路点（3个）
            if (str.startsWith(ROUTE_POINT_MARK)) {
                if (cfp.getAlterAirport1() != null && cfp.getAlterAirport2() == null && cfp.getAlterAirport3() == null) {
                    i++;        //从关键字的下一行开始解析备降场航路点信息
                    boolean bAlterSpecial = false;
                    for (; i < alterInfo.length; i++) {
                        str = alterInfo[i].trim();
                        if (str.isEmpty()) break;
                        if(addAlternateCpt(cfp,sb1,str,filters,specialRegex)) bAlterSpecial = true;
                    }
                    cfp.setAlterRoute1Point(StringUtil.replaceRN(cfp.getAlterAirport1()+","+StringUtil.trimEnd(sb1.toString(),",")));
                    cfp.setAlter1Special(bAlterSpecial? FlightConst.ROUTE_SPECIAL:FlightConst.ROUTE_NORMAL);
                } else if (cfp.getAlterAirport1() != null && cfp.getAlterAirport2() != null && cfp.getAlterAirport3() == null) {
                    i++;        //从关键字的下一行开始解析备降场航路点信息
                    boolean bAlterSpecial = false;
                    for (; i < alterInfo.length; i++) {
                        str = alterInfo[i].trim();
                        if (str.isEmpty()) break;
                        if(addAlternateCpt(cfp,sb2,str,filters,specialRegex)) bAlterSpecial = true;
                    }
                    cfp.setAlterRoute2Point(StringUtil.replaceRN(cfp.getAlterAirport2()+","+StringUtil.trimEnd(sb2.toString(),",")));
                    cfp.setAlter2Special(bAlterSpecial? FlightConst.ROUTE_SPECIAL:FlightConst.ROUTE_NORMAL);
                } else {
                    if(cfp.getAlterRoute3Point()!=null) break;
                    i++;        //从关键字的下一行开始解析备降场航路点信息
                    boolean bAlterSpecial = false;
                    for (; i < alterInfo.length; i++) {
                        str = alterInfo[i].trim();
                        if (str.isEmpty()) break;
                        if(addAlternateCpt(cfp,sb3,str,filters,specialRegex)) bAlterSpecial = true;
                    }
                    cfp.setAlterRoute3Point(StringUtil.replaceRN(cfp.getAlterAirport3()+","+StringUtil.trimEnd(sb3.toString(),",")));
                    cfp.setAlter3Special(bAlterSpecial? FlightConst.ROUTE_SPECIAL:FlightConst.ROUTE_NORMAL);
                }
            }
        }
    }

    /**
     * 解析备降航路点
     * @param cfp
     * @param sb
     * @param line
     * @param filters
     * @param specialRegex
     * @return 是否特殊航路点
     */
    private static boolean addAlternateCpt(CfpExt cfp,StringBuffer sb,String line,String filters,String specialRegex){
        String[] strArr = StringUtils.split(line, " ");
        if(strArr.length<3) return false;

        String name=strArr[0];
        String lat=strArr[1];
        String lng=strArr[2];
        if(filters.contains(name)) return false;

        //将航路点对应的经纬度写入本地库
        Cpt cpt = new Cpt();
        cpt.setLat(StringUtil.replaceRN(lat));
        cpt.setLng(StringUtil.replaceRN(lng));
        cpt.setCptName(StringUtil.replaceRN(name));
        cpt.setModifyTm(new Date());
        if(matchSpecial(name,specialRegex)) cpt.setSpecial(true);
        cfp.addCpt(cpt);

        sb.append(name).append(",");
        return cpt.isSpecial();
    }

    private static boolean matchSpecial(String name,String regEx){
        try {
            Pattern pattern = Pattern.compile(regEx);
            Matcher matcher = pattern.matcher(name);
            return matcher.matches();
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * 解析油量等信息
     * @param Other
     * @param cfp
     */
    public void parseOther(String Other, CfpExt cfp){
        String [] otherMsg=StringUtils.split(Other.trim(),"\n");
        for(int i=0;i<otherMsg.length;i++){
            String name=otherMsg[i];
            //解析成本指数,飞行计划编号
            if(name.startsWith(START)){
                String [] costInd=StringUtils.split(name, " ");
                cfp.setPlanNo(costInd[1]);
                String costInd2=costInd[costInd.length-2];
                String [] costInd3=StringUtils.split(costInd2, "/");
                cfp.setCostIndex(StringUtil.replaceRN(costInd3[0]));
            }
            //解析 航程油量，起飞重量，着陆重量，可用业载，使用空量
            if(name.startsWith(POA)){
                String [] poa=StringUtils.split(name, " ");
                cfp.setTraFuel(StringUtil.replaceRN(StringUtil.trimstart(poa[2])));
                cfp.setTakeoffWeight(StringUtil.trimstart(StringUtil.replaceRN(poa[6])));
                cfp.setLandWeight(StringUtil.trimstart(StringUtil.replaceRN(poa[7])));
                cfp.setAvPayload(StringUtil.trimstart(StringUtil.replaceRN(poa[8])));
                cfp.setOpnlWeight(StringUtil.trimstart(StringUtil.replaceRN(poa[9])));
            }
            //解析备降油量
            if(name.startsWith(ALT)&&cfp.getAltFuel()==null){
                String [] altfuel=StringUtils.split(name, " ");
                cfp.setAltFuel(StringUtil.trimstart(StringUtil.replaceRN(altfuel[2])));
            }
            //解析等待油量
            if(name.startsWith(HLD)&&cfp.getHldFuel()==null){
                String [] hld=StringUtils.split(name, " ");
                cfp.setHldFuel(StringUtil.trimstart(StringUtil.replaceRN(hld[1])));
            }
            //解析备份油量
            if(name.startsWith(RES)&&cfp.getResFuel()==null){
                String [] res=StringUtils.split(name, " ");
                cfp.setResFuel(StringUtil.trimstart(StringUtil.replaceRN(res[1])));
            }
            //解析额外油量
            if(name.startsWith(XTR)&&cfp.getXtrFuel()==null){
                String [] xtr=StringUtils.split(name, " ");
                cfp.setXtrFuel(StringUtil.trimstart(StringUtil.replaceRN(xtr[1])));
            }
            //解析地面apu油量
            if(name.startsWith(APU)&&cfp.getApuFuel()==null){
                String [] apu=StringUtils.split(name, " ");
                cfp.setApuFuel(StringUtil.trimstart(StringUtil.replaceRN(apu[1])));
            }
            //解析滑出油量
            if(name.startsWith(TXO)&&cfp.getTxoFuel()==null){
                String [] txo=StringUtils.split(name, " ");
                cfp.setTxoFuel(StringUtil.trimstart(StringUtil.replaceRN(txo[1])));
            }
            //解析滑入油量
            if(name.startsWith(TXI)&&cfp.getTxiFuel()==null){
                String [] txi=StringUtils.split(name, " ");
                cfp.setTxiFuel(StringUtil.trimstart(StringUtil.replaceRN(txi[1])));
            }
            //解析总油量 ,主公司航路
            if(name.startsWith(START_MAINCOMPANY)&&cfp.getFobFuel()==null){
                String [] fob=StringUtils.split(name, " ");
                cfp.setFobFuel(StringUtil.trimstart(StringUtil.replaceRN(fob[1])));
                //判断主公司航路是否存在
                for (int j=0;j<fob.length;j++){
                    if("TRK".equals(fob[j])){
                        if(j!=fob.length-1){
                            cfp.setMainCompanyRoute(StringUtil.replaceRN(fob[j+1]));
                            break;
                        }
                    }
                }

            }
            //解析航路平均分风
            if(name.startsWith(ROUTE_AVG_WIND)&&cfp.getRouteAvgWind()==null){
                String [] wind=StringUtils.split(name, " ");
                cfp.setRouteAvgWind(StringUtil.replaceRN(wind[3]));
            }
            //解析航路平均温度,巡航高度
            if(name.startsWith(ROUTE_AVG_TEMP)&&cfp.getRouteAvgTemp()==null){
                String [] temp=StringUtils.split(name, " ");
                cfp.setRouteAvgTemp(StringUtil.replaceRN(temp[3]));
                String [] cruiseAltitude=temp[6].split("/");
                cfp.setCruiseAltitude(StringUtil.replaceRN(cruiseAltitude[0]));
            }
        }
    }

    /*
    /**
     * 替换航路平均风，温度的 m，p
     * @param value  替换航路平均风，温度的 m，p
     * @return
     */
    /*public String replaceMP(String value){
        if(value.startsWith(M)){
            String str=value.substring(1,value.length());
            return "-"+StringUtil.trimstart(str);
        }
        if(value.startsWith(P)){
            String str=value.substring(1,value.length());
            return ""+StringUtil.trimstart(str);
        }
    return StringUtil.trimstart(value);
    }*/

    //N21581E112493
    public static void main(String[] args) {
        boolean bmatch = matchSpecial("N21581E112493","^(N|S)\\d{5}(E|W)\\d{6}$");
        System.out.print(bmatch?"true":"false");
    }

}
