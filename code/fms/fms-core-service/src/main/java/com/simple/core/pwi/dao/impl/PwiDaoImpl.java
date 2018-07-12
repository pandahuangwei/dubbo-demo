package com.simple.core.pwi.dao.impl;

import com.simple.common.persistence.db.MybatisTplDao;
import com.simple.common.utils.DateTimeUtil;
import com.simple.core.common.util.GribUtil;
import com.simple.core.flightimport.entity.Cpt;
import com.simple.core.pwi.dao.PwiDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service("pwiDao")
public class PwiDaoImpl implements PwiDao {
    private static final Logger logger = LoggerFactory.getLogger(PwiDaoImpl.class);

    @Resource(name = "mybatisTplDao")
    private MybatisTplDao dao;
    private String NAMESPACE="PwiMapper.";


    /**
     * 取得航路点的经度、纬度:fms_way_point
     * @param cptName 航路点名称
     * @return 经纬度
     */
    @Override
    public Cpt findByCptnameForWayPoint(String cptName) {
        return (Cpt) dao.findOne(NAMESPACE+"findByCptnameForWayPoint", cptName);

    }

    /**
     * 取得航路点的经度、纬度:fms_naip
     * @param cptname
     * @return
     */
    @Override
    public Cpt findByCptnameForNaip(String cptname) {
        return (Cpt) dao.findOne(NAMESPACE+"findByCptnameForNaip", cptname);
    }

    /**
     * Function : 取得给定经、纬、高度的8个点的UGRD、VGRD、TMP信息
     * param lon : 指定经度的前后两个标准经度值
     * param lat : 指定纬度的前后两个标准纬度值
     * param alt : 指定高度层的上下两个标准气压高度层
     * return long : 取得给定经、纬、高度的8个点的UGRD、VGRD、TMP信息
     * 取值说明:
     * 1、获取当前UTC时间，并查询6小时内有效的Grib2数据，作为返回值；
     * 2、如果，6小时内的有效数据不存在or不全，则查询12小时的有效数据，作为返回值；
     * 3、如果，12小时内的有效数据不存在or不全，则查询18小时的有效数据，作为返回值；
     * 4、如果，18小时内的有效数据不存在or不全，则无法计算指定高度层、航路点的风信息；
     * eg: 当前UTC时间为7点，则系统优先选择6点发布的6小时预报、在选0点发布的12小时预报、最后选前一天18点发布的18小时预报
     */
    @Override
    public List findByLonLatAlt(List lon, List lat, List alt) {

        String hour = DateTimeUtil.getCurUTCtimeToHH("HH");
        List timelist = GribUtil.getTimes(Integer.valueOf(hour), 6);
        List list = dao.findList(NAMESPACE+"findByLonLatAlt", StrPara(lon, lat, alt, timelist));
        logger.info("使用6小时有效时长的GRID2数据，开始时间是：{},结束时间是：{}", timelist.get(0), timelist.get(1));

        if (list == null || list.size() != 24) {
            timelist = GribUtil.getTimes(Integer.valueOf(hour), 12);
            list = dao.findList(NAMESPACE+"findByLonLatAlt", StrPara(lon, lat, alt, timelist));
            logger.info("使用12小时有效时长的GRID2数据，开始时间是：{},结束时间是：{}", timelist.get(0), timelist.get(1));
        }

        if (list == null || list.size() != 24) {
            timelist = GribUtil.getTimes(Integer.valueOf(hour), 18);
            list = dao.findList(NAMESPACE+"findByLonLatAlt", StrPara(lon, lat, alt, timelist));
            logger.info("使用18小时有效时长的GRID2数据，开始时间是：{},结束时间是：{}", timelist.get(0), timelist.get(1));
        }
        return list;
    }

    /**
     * 给sql的参数赋值
     *
     * @param lon      经度
     * @param lat      纬度
     * @param alt      高度
     * @param timelist 时间集合
     * @return map参数
     */
    private Map StrPara(List lon, List lat, List alt, List timelist) {

        Map<String, Object> map = new HashMap<String, Object>();

        map.put("lon1", lon.get(0));
        map.put("alt1", alt.get(0));
        map.put("lat1", lat.get(0));
        map.put("stime1", timelist.get(0));
        map.put("etime1", timelist.get(1));

        map.put("lon2", lon.get(0));
        map.put("alt2", alt.get(1));
        map.put("lat2", lat.get(0));
        map.put("stime2", timelist.get(0));
        map.put("etime2", timelist.get(1));

        map.put("lon3", lon.get(0));
        map.put("alt3", alt.get(0));
        map.put("lat3", lat.get(1));
        map.put("stime3", timelist.get(0));
        map.put("etime3", timelist.get(1));

        map.put("lon4", lon.get(0));
        map.put("alt4", alt.get(1));
        map.put("lat4", lat.get(1));
        map.put("stime4", timelist.get(0));
        map.put("etime4", timelist.get(1));

        map.put("lon5", lon.get(1));
        map.put("alt5", alt.get(0));
        map.put("lat5", lat.get(0));
        map.put("stime5", timelist.get(0));
        map.put("etime5", timelist.get(1));

        map.put("lon6", lon.get(1));
        map.put("alt6", alt.get(1));
        map.put("lat6", lat.get(0));
        map.put("stime6", timelist.get(0));
        map.put("etime6", timelist.get(1));


        map.put("lon7", lon.get(1));
        map.put("alt7", alt.get(0));
        map.put("lat7", lat.get(1));
        map.put("stime7", timelist.get(0));
        map.put("etime7", timelist.get(1));

        map.put("lon8", lon.get(1));
        map.put("alt8", alt.get(1));
        map.put("lat8", lat.get(1));
        map.put("stime8", timelist.get(0));
        map.put("etime8", timelist.get(1));
        System.out.println("3333  " + map.toString());
        return map;
    }


}

