package com.simple.core.flightimport.facade.impl;

import com.simple.common.utils.DateTimeUtil;
import com.simple.core.flightimport.facade.FlightImportFacade;
import com.simple.core.flightimport.service.FlightImportService;
import com.simple.core.foc.entity.FocCfp;
import com.simple.core.foc.entity.FocFlight;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

/**********************************************************************************
 * Copyright(c)2017 Dcits-air.com All rights reserved.
 *
 * @author Administrator.
 * @version 1.0.
 * @Title: FlightImportFacadeImpl.java.
 * @Package com.simple.core.flightimport.facade.impl.
 * @Description: 航班导入服务接口.
 * @created 2017/2/28 21:10.
 **********************************************************************************/
@Component("flightImportFacade")
public class FlightImportFacadeImpl implements FlightImportFacade {
    private static final Logger logger = LoggerFactory.getLogger(FlightImportFacadeImpl.class);

    @Resource(name = "flightImportService")
    FlightImportService flightImportService;

    @Override
    public boolean saveFocFlight(List<FocFlight> list,boolean hasDispatchTime,boolean hasCrewArrivalTime) {
        try {
            logger.info("saveFocFlight:count="+(null==list?0:list.size())+",hasDispatchTime="+(hasDispatchTime?1:0)
                    +",hasCrewArrivalTime="+(hasCrewArrivalTime?1:0));
            return flightImportService.saveFocFlight(list,hasDispatchTime,hasCrewArrivalTime);
        } catch (Exception ex) {
            logger.error("saveFocFlight error:"+ex.getMessage());
            return false;
        }
    }

    @Override
    public boolean saveFocCfp(List<FocCfp> list) {
        try {
            logger.info("saveFocCfp:count="+(null==list?0:list.size()));
            return flightImportService.saveFocCfp(list);
        } catch (Exception ex) {
            logger.error("saveFocCfp error:"+ex.getMessage());
            return false;
        }
    }

    @Override
    public Date getMaxCfpModifyTime() {
        Date date;
        try {
            logger.info("getMaxCfpModifyTime...");
            date = flightImportService.getMaxCfpModifyTime();
            logger.info("getMaxCfpModifyTime date:{}", DateTimeUtil.toSting(date));
        } catch (Exception ex) {
            logger.error("getMaxCfpModifyTime error:"+ex.getMessage());
            return null;
        }
        return date;
    }

    @Override
    public Date getMaxFlightModifyTime() {
        Date date;
        try {
            logger.info("getMaxFlightModifyTime...");
            date = flightImportService.getMaxFlightModifyTime();
            logger.info("getMaxFlightModifyTime date:{}", DateTimeUtil.toSting(date));
        } catch (Exception ex) {
            logger.error("getMaxFlightModifyTime error:"+ex.getMessage());
            return null;
        }
        return date;
    }

    @Override
    public List<String> getFlightIds(Date flightDate) {
        try {
            logger.info("getFlightIds...");
            return flightImportService.getFlightIds(flightDate);
        } catch (Exception ex) {
            logger.error("getFlightIds error:"+ex.getMessage());
            return null;
        }
    }

    @Override
    public int deleteFlightByIds(List<String> ids) {
        try {
            logger.info("deleteFlightByIds...");
            int count = flightImportService.deleteFlightByIds(ids);
            logger.info("delete flight count="+count);
            return count;
        } catch (Exception ex) {
            logger.error("deleteFlightByIds error:"+ex.getMessage());
            return 0;
        }
    }

    @Override
    public int deleteCfpByIds(List<String> ids) {
        try {
            logger.info("deleteCfpByIds...");
            int count = flightImportService.deleteCfpByIds(ids);
            logger.info("delete flight count="+count);
            return count;
        } catch (Exception ex) {
            logger.error("deleteCfpByIds error:"+ex.getMessage());
            return 0;
        }
    }
}



