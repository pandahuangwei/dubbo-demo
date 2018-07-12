package com.simple.core.grib2import.service.impl;

import com.simple.common.utils.DateTimeUtil;
import com.simple.common.utils.StringUtil;
import com.simple.core.grib2import.dao.Grib2ImportDao;
import com.simple.core.grib2import.entity.Grib2TimeVo;
import com.simple.core.grib2import.service.Grib2ImportService;
import com.simple.core.pwi.entity.Grib2WdInfo;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author Administrator.
 * @version 1.0 .
 * @since 2017.03.13 10:04.
 */
@Service("grib2ImportService")
public class Grib2ImportServiceImpl implements Grib2ImportService {

    private static Logger logger = Logger.getLogger(Grib2ImportServiceImpl.class);
    //10000条commit一次
    private int CommitSize = 10000;
    private int BatchSaveSize = 1000;

    @Resource(name = "grib2ImportDao")
    private Grib2ImportDao grib2ImportDao;

    public void storeGrib2Data(List dataList, Grib2TimeVo grib2TimeVo, int iCurAtoms) {
        logger.info("(开始)入库LEVEL:"+iCurAtoms+",线程名:"+ Thread.currentThread() + DateTimeUtil.getCurrentDateTime("yyyy-MM-dd HH:mm:ss"));
        long hasRecordNum = grib2ImportDao.countByTimeAlt(grib2TimeVo.getPublicTime(), grib2TimeVo.getEndTime(), iCurAtoms);
        if (0 == hasRecordNum) {
            int count = storeDataByPage(dataList);
            logger.info("本次入库数据，库中不存在，入库(" + count+"/"+dataList.size() + ")条");
        } else if (125280 != hasRecordNum) {
            // 删除该时段、该高度层的不合法风温数据
            long delSize = grib2ImportDao.deleteByTimeAlt(grib2TimeVo.getPublicTime(), grib2TimeVo.getEndTime(), iCurAtoms);
            //保存grib2数据
            int count = storeDataByPage(dataList);
            logger.info("本次入库数据，库中不完整（多or少），已删除" + delSize + "条; 已入库(" + count+"/"+dataList.size() + ")条");
        } else if (125280 == hasRecordNum) {
            // 不用进行任何操作
            logger.debug("本次入库数据，库中已存在，不执行入库操作");
        }
        logger.info("(结束)入库线程名:" + Thread.currentThread() + DateTimeUtil.getCurrentDateTime("yyyy-MM-dd HH:mm:ss"));
    }

    /**
     * 分批量保存数据:事务控制提交
     * @param dataList
     */
    private int storeDataByPage(List dataList) {
        //保存grib2数据
        int count = 0;
        while(count<dataList.size()){
            int size = count+CommitSize;
            if(size>dataList.size()) size = dataList.size();
            List<Grib2WdInfo> subList = dataList.subList(count,size);
            long startTm = System.nanoTime();
            count += saveNewData(subList);
            logger.info("storeDataByPage size="+CommitSize+",time="+StringUtil.getSpentTm(startTm));
        }
        return count;
    }

    public int saveNewData(List dataList) {
        //保存grib2数据
        int count = 0;
        while(count<dataList.size()){
            int size = count+BatchSaveSize;
            if(size>dataList.size()) size = dataList.size();
            List<Grib2WdInfo> subList = dataList.subList(count,size);
            count += doSaveDataBatch(subList);
        }
        return count;
    }

    /**
     * 批量保存
     * @param dataList
     * @return
     */
    public int doSaveDataBatch(List<Grib2WdInfo> dataList) {
        return grib2ImportDao.saveGrib2Data(dataList);
    }
}
