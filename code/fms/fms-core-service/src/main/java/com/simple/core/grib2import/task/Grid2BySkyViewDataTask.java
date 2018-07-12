package com.simple.core.grib2import.task;

import com.alibaba.dubbo.config.annotation.Service;
import com.simple.common.utils.DateTimeUtil;
import com.simple.common.utils.TaskManager;
import com.simple.core.common.util.GribUtil;
import com.simple.core.grib2import.entity.Grib2CalcParam;
import com.simple.core.grib2import.service.Grib2ImportService;
import com.simple.core.grib2import.util.Grib2ImportPropertyUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.File;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

/**
 * 风温数据计算保存入口
 *
 * @author Panda.HuangWei.
 * @version 1.0 .
 * @since 2017.04.13 11:21.
 */
@Component("grid2BySkyViewDataTask")
public class Grid2BySkyViewDataTask {

    private static Logger logger = LoggerFactory.getLogger(Grid2BySkyViewDataTask.class);

    @Resource(name = "grib2ImportService")
    private Grib2ImportService service;

    // 公共目录名
    private final String publicDir = "us_avn";
    // 获取风温源数据目录
    private final String tempDirPath = "temper";
    // 获取风UV分量源数据目录
    private final String uvDirPath = "uv";

    // 系统录入的有效时段种类的数量
    private final int iValidDurationNum = 3;

    private boolean finishWork=true;

    public void calcAndSave() {
        //获取需要处理的文件路径
        List<Grib2CalcParam> list = new ArrayList<>();
        getCalcFilePath(list);

        if (list.isEmpty()) {
            logger.info("Can't get file path.");
            return;
        }
        Grid2BySkyViewDataTaskHelper taskHelper = new Grid2BySkyViewDataTaskHelper(service);
        TaskManager taskManager = TaskManager.getInstance();
        setFinishWork(false);
        for (Grib2CalcParam e : list) {
           /* taskManager.executeTaskWhileNoFull(new CalcTask(taskHelper,
                    e.getPathTemp(), e.getPathUV(), e.getiCurAtoms()));*/

            //单线程调试
            try {
                taskHelper.parseData(e.getPathTemp(), e.getPathUV(), e.getiCurAtoms());
            } catch (Exception e1) {
                logger.error(e1.getMessage());
            }
        }

        try {
            taskManager.waitTaskFinish();
        } catch (Exception e) {
            logger.error("Call Grid2BySkyView with error!", e);
        }
        setFinishWork(true);
        logger.info("end call Grid2BySkyView task!");
    }

    private void getCalcFilePath(List<Grib2CalcParam> list) {
        String grib2BySkyViewDirPath = Grib2ImportPropertyUtil.getInstance().getGrib2BySkyviewWindInfoDir();
        // 判断指定目录中是否存在文件
        if (!isVaildDir(grib2BySkyViewDirPath)) {
            return;
        }

        File fSacnDir = new File(grib2BySkyViewDirPath);
        if ((null == fSacnDir) || !fSacnDir.isDirectory()) {
            logger.error("无法打开指定数据源(风温or风分量)目录：" + grib2BySkyViewDirPath);
            return;
        }

        // 循环遍历日期目录
        String[] dateDirNameList = fSacnDir.list();
        for (int i = 0; i < dateDirNameList.length; i++) {
            logger.debug("扫描目录：" + dateDirNameList[i]);
            if (isNeedScanDir(dateDirNameList[i]) < 0) {
                continue;
            }
            logger.debug("扫描有效目录：" + dateDirNameList[i]);
            // 每个高度层目录要以“有效时段种类的数量”为遍历次数，每次处理一个有效时段的数据
            for (int iCount = 0; iCount < iValidDurationNum; iCount++) {
                logger.debug("已处理 " + iCount + " 份时效数据！");
                // 由于SKYVIEW使用的Grib2数据（风温、风分量）分别放置在两个不同目录，且这两个目录下存放着高度层目录列表，
                // 高度层目录中存放着风数据
                // 遍历标准气压高度层列表，按高度层来读取风数据，逐高度层入库
                // 目录中，有数据文件，则入库并删除；无数据，则略过
                for (int iAtomsNum = 0; iAtomsNum < GribUtil.atomsList.size(); iAtomsNum++) {
                    String pathTemp = grib2BySkyViewDirPath + File.separator + dateDirNameList[i] + File.separator + publicDir
                            + File.separator + tempDirPath + File.separator + GribUtil.atomsList.get(iAtomsNum);
                    String pathUV = grib2BySkyViewDirPath + File.separator + dateDirNameList[i] + File.separator + publicDir
                            + File.separator + uvDirPath + File.separator + GribUtil.atomsList.get(iAtomsNum);
                    list.add(new Grib2CalcParam(pathTemp, pathUV, GribUtil.atomsList.get(iAtomsNum)));
                }
            }
        }
    }

    /**
     * Function : 判断当前目录是否需要扫描并录入数据(只有文件名日期在20小时以内的才会录入)
     * param dirName : 以yyyyMMddHH表示的，时间字符串
     * return : < 0 目标目录数据是20小时之前的无效目录
     * = 0 目标目录数据是20小时那一时刻的有效目录
     * > 0 目标目录数据是20小时以内的有效目录
     */
    private int isNeedScanDir(String dirName) {

        try {
            // 目标目录转化称呼
            dirName = DateTimeUtil.formateToHour(dirName, 20);
        } catch (ParseException e) {
            logger.error("目录名异常无法解析：" + dirName);
            return -1;
        }
        String curUtcTimeStr = DateTimeUtil.getCurUTCHour();
        return dirName.compareTo(curUtcTimeStr);
    }

    /**
     * 判断是否有效的目录
     *
     * @param grib2BySkyViewDirPath
     * @return
     */
    private boolean isVaildDir(String grib2BySkyViewDirPath) {
        File fGrib2BySkyviewSacnDir = new File(grib2BySkyViewDirPath);
        if ((null == fGrib2BySkyviewSacnDir) || !fGrib2BySkyviewSacnDir.isDirectory()) {
            logger.error("无法打开指定数据源(风温or风UV分量)目录！");
            return false;
        }
        return true;
    }

    public boolean isFinishWork() {
        return finishWork;
    }

    public void setFinishWork(boolean finishWork) {
        this.finishWork = finishWork;
    }

    class CalcTask implements Runnable {
        Grid2BySkyViewDataTaskHelper taskHelper;
        String pathTemp;
        String pathUV;
        int iCurAtoms;

        CalcTask(Grid2BySkyViewDataTaskHelper taskHelper, String pathTemp, String pathUV, int iCurAtoms) {
            this.taskHelper = taskHelper;
            this.pathTemp = pathTemp;
            this.pathUV = pathUV;
            this.iCurAtoms = iCurAtoms;
        }

        @Override
        public void run() {
            try {
                taskHelper.parseData(pathTemp,pathUV,iCurAtoms);
            } catch (Exception e) {
                logger.error("Call calc task with error!", e);
            }
        }
    }
}
