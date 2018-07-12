package com.simple.core.grib2import.facade.impl;

import com.simple.core.grib2import.entity.Grib2TimeVo;
import com.simple.core.grib2import.facade.Grib2ImportFacade;
import com.simple.core.grib2import.service.Grib2ImportService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author Administrator.
 * @version 1.0 .
 * @since 2017.03.13 10:03.
 */
@Component("grib2ImportFacade")
public class Grib2ImportFacadeImpl implements Grib2ImportFacade {
    private static final Logger logger = LoggerFactory.getLogger(Grib2ImportFacadeImpl.class);

    @Resource(name = "grib2ImportService")
    Grib2ImportService grib2ImportService;

    @Override
    public void saveGrib2Data(List dataList, Grib2TimeVo grib2TimeVo, int iCurAtoms) {
        try {
            logger.info("storeGrib2Data  start...");
            grib2ImportService.storeGrib2Data(dataList,grib2TimeVo,iCurAtoms);
            logger.info("storeGrib2Data end....");
        } catch (Exception ex) {
            logger.error("storeGrib2Data error:"+ex.getMessage());
            return;
        }
    }
}
