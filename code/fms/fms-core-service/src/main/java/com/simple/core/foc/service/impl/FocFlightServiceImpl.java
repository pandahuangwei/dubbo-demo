package com.simple.core.foc.service.impl;

import com.simple.common.persistence.page.Pagination;
import com.simple.common.utils.DateTimeUtil;
import com.simple.common.utils.ListUtil;
import com.simple.common.utils.StringUtil;
import com.simple.core.common.entity.FlightBase;
import com.simple.core.flightimport.dao.FlightImportDao;
import com.simple.core.flightimport.facade.FlightImportFacade;
import com.simple.core.foc.dao.FocFlightDao;
import com.simple.core.foc.entity.FocFlight;
import com.simple.core.foc.service.FocFlightService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**********************************************************************************
 * Copyright(c)2017 Dcits-air.com All rights reserved.
 * @Title: FocFlightServiceImpl.java.
 * @Package com.simple.core.foc.service.impl.
 * @Description: FocFlightServiceImpl.
 *
 * @author Administrator.
 * @version 1.0.
 * @created 2017/3/16 18:21.
 **********************************************************************************/
@Service("focFlightService")
public class FocFlightServiceImpl implements FocFlightService {
    private static final Logger logger = LoggerFactory.getLogger(FocFlightServiceImpl.class);

    private final static int FLIGHT_PAGE_SIZE = 50;
    private final static int CFP_PAGE_SIZE = 5;

    @Resource(name = "flightImportFacade")
    private FlightImportFacade flightImportFacade;

    @Resource(name = "focFlightDao")
    private FocFlightDao focFlightDao;

    @Override
    public void setCarrierIATA(String iata) {
        focFlightDao.setCarrierIATA(iata);
    }

    /**
     * 将获取到的foc航班发送到导入服务：通过最后更新时间
     *
     * @param lastUpdateTime
     * @return
     */
    @Override
    public boolean sendFocFlightToImporter(Date lastUpdateTime,boolean hasDispatchTime,boolean hasCrewArrivalTime) {
        //必须按更新时间升序排列
        int pageNo = 1;
        Pagination page = focFlightDao.selectFlightByTime(lastUpdateTime, pageNo, FLIGHT_PAGE_SIZE);
        logger.info("selectFlightByLastUpdateTime:total="+page.getTotalCount());
        if (CollectionUtils.isEmpty(page.getList())) return false;

        int order = 1;
        logger.info("#"+order+"-saveFocFlight:count="+page.getList().size());
        flightImportFacade.saveFocFlight(page.getList(),hasDispatchTime,hasCrewArrivalTime);

        int totalCount = page.getTotalCount();
        int count=page.getList().size();
        while(count<totalCount){
            order++;
            page = focFlightDao.selectFlightByTime(lastUpdateTime, ++pageNo, FLIGHT_PAGE_SIZE);
            if (CollectionUtils.isEmpty(page.getList())) break;

            logger.info("#"+order+"-saveFocFlight:count="+page.getList().size());
            flightImportFacade.saveFocFlight(page.getList(),hasDispatchTime,hasCrewArrivalTime);
            count +=page.getList().size();
        }
        logger.info("sendFocFlightToImporter done.");
        return true;
    }

    /**
     * 将获取到的foc飞行计划发送到导入服务：通过最后更新时间
     *
     * @param lastUpdateTime
     * @return
     */
    @Override
    public boolean sendFocCfpToImporter(Date lastUpdateTime) {
        //必须按更新时间升序排列
        int pageNo = 1;
        Pagination page = focFlightDao.selectCfpByTime(lastUpdateTime, pageNo, CFP_PAGE_SIZE);
        logger.info("selectCfpByLastUpdateTime:total="+page.getTotalCount());
        if (CollectionUtils.isEmpty(page.getList())) return false;

        int order = 1;
        logger.info("#"+order+"-saveFocCfp:count="+page.getList().size());
        flightImportFacade.saveFocCfp(page.getList());

        int totalCount = page.getTotalCount();
        int count=page.getList().size();
        while(count<totalCount){
            order++;
            page = focFlightDao.selectCfpByTime(lastUpdateTime, ++pageNo, CFP_PAGE_SIZE);
            if (CollectionUtils.isEmpty(page.getList())) break;

            logger.info("#"+order+"-saveFocCfp:count="+page.getList().size());
            flightImportFacade.saveFocCfp(page.getList());
            count +=page.getList().size();
        }

        logger.info("sendFocCfpToImporter done.");
        return true;
    }

    /**
     * 将获取到的foc航班发送到导入服务：通过起始时间，结束时间
     *
     * @param start
     * @param end
     * @return
     */
    @Override
    public boolean sendFocFlightToImporter(Date start, Date end,boolean hasDispatchTime,boolean hasCrewArrivalTime) {
        //得到foc的航班动态
        int pageNo = 1;
        Pagination page = focFlightDao.selectFlightByTime(start, end, pageNo, FLIGHT_PAGE_SIZE);
        logger.info("selectFlightByStartEnd:count="+page.getTotalCount());
        if (CollectionUtils.isEmpty(page.getList()))return false;

        int order = 1;
        logger.info("#"+order+"-saveFocFlight:count="+page.getList().size());
        flightImportFacade.saveFocFlight(page.getList(),hasDispatchTime,hasCrewArrivalTime);

        int totalCount = page.getTotalCount();
        int count=page.getList().size();
        while(count<totalCount){
            order++;
            page = focFlightDao.selectFlightByTime(start, end, ++pageNo, FLIGHT_PAGE_SIZE);
            if (CollectionUtils.isEmpty(page.getList())) break;

            logger.info("#"+order+"-saveFocFlight:count="+page.getList().size());
            flightImportFacade.saveFocFlight(page.getList(),hasDispatchTime,hasCrewArrivalTime);
            count +=page.getList().size();
        }

        logger.info("sendFocFlightToImporter done.");
        return true;
    }

    /**
     * 将获取到的foc飞行计划发送到导入服务：：通过起始时间，结束时间
     *
     * @param start
     * @param end
     * @return
     */
    @Override
    public boolean sendFocCfpToImporter(Date start, Date end) {
        //通过分页
        int pageNo = 1;
        Pagination page = focFlightDao.selectCfpByTime(start,end, pageNo, CFP_PAGE_SIZE);
        logger.info("selectCfpByTime:total="+page.getTotalCount());
        if (CollectionUtils.isEmpty(page.getList())) return false;

        int order = 1;
        logger.info("#"+order+"-saveFocCfp:count="+page.getList().size());
        flightImportFacade.saveFocCfp(page.getList());
        int totalCount = page.getTotalCount();
        int count=page.getList().size();
        while(count<totalCount){
            order++;
            page = focFlightDao.selectCfpByTime(start,end,++pageNo, CFP_PAGE_SIZE);
            if (CollectionUtils.isEmpty(page.getList())) break;

            logger.info("#"+order+"-saveFocCfp:count="+page.getList().size());
            flightImportFacade.saveFocCfp(page.getList());
            count +=page.getList().size();
        }

        logger.info("sendFocCfpToImporter done.");
        return true;
    }


    /**
     * 取得本地航班动态的最后更新时间
     * @param updateTimeDefault
     * @return
     */
    @Override
    public Date getMaxFlightModifyTimeFromImporter(int updateTimeDefault) {
        Date dt = flightImportFacade.getMaxFlightModifyTime();
        if (null == dt || dt.after(new Date())) {
            logger.info("getMaxFlightModifyTimeFromImporter: date is null or after now,"+DateTimeUtil.toSting(dt));
            Date dtToday = new Date();
            dt = DateTimeUtil.addHours(dtToday, -updateTimeDefault);
        }
        logger.info("getMaxFlightModifyTimeFromImporter:"+DateTimeUtil.toSting(dt));
        return dt;
    }

    /**
     * 取得本地cfp的最后更新时间
     * @param updateTimeDefault
     * @return
     */
    @Override
    public Date getMaxCfpModifyTimeFromImporter(int updateTimeDefault) {
        Date dt = flightImportFacade.getMaxCfpModifyTime();
        if (null == dt || dt.after(new Date())) {
            logger.info("getMaxCfpModifyTimeFromImporter: date is null or after now,"+DateTimeUtil.toSting(dt));
            Date dtToday = new Date();
            dt = DateTimeUtil.addHours(dtToday, -updateTimeDefault);
        }
        logger.info("getMaxCfpModifyTimeFromImporter:"+DateTimeUtil.toSting(dt));
        return dt;
    }

    /**
     * 同步FOC中删除的航班
     * @return
     */
    @Override
    public void syncDeletedFlights() {
        //查找本地flight当天航班
        List<String> fmsids=flightImportFacade.getFlightIds(dateToFlightDate(new Date()));
        if(CollectionUtils.isEmpty(fmsids)) return;

        //查询ids中在Foc存在的航班记录
        List<String> diff = null;
        List<Integer> focids = focFlightDao.getFocFlightIds(fmsids);
        //防止清空数据
        if(CollectionUtils.isEmpty(focids)) return;

        List<String> focids1=new ArrayList<>();
        for (Integer item:focids) {
            focids1.add(String.valueOf(item));
        }
        //取差值
        diff = ListUtil.diff(fmsids, focids1);
        if(CollectionUtils.isEmpty(diff))return ;

        String idsDelete = null;
        for(String id : diff){
            if(null==idsDelete) idsDelete = id;
            else idsDelete +=(","+id);
        }
        logger.info("DeletedFlight ids="+idsDelete);

        //删除本地不存在的航班
        flightImportFacade.deleteFlightByIds(diff);
        //删除本地不存在的飞行计划
        flightImportFacade.deleteCfpByIds(diff);
        logger.info("syncDeletedFlights done.");
    }

    /**
     * 05:00~次日05:00
     * @param dt
     * @return 航班日期
     */
    public Date dateToFlightDate(Date dt) {
        if(null==dt) return null;

        try {
            Calendar calendar= Calendar.getInstance();
            calendar.setTime(dt);
            int hour = calendar.get(Calendar.HOUR_OF_DAY);
            if(hour<5) calendar.add(Calendar.DAY_OF_YEAR, -1);
            calendar.set(Calendar.HOUR,0);
            calendar.set(Calendar.MINUTE,0);
            calendar.set(Calendar.SECOND,0);
            calendar.set(Calendar.MILLISECOND,0);
            return DateTimeUtil.truncDate(calendar.getTime());
        } catch (Exception e) {
        }
        return null;
    }
}

