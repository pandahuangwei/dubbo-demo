package com.simple.core.grib2import.util;

import org.springframework.beans.factory.InitializingBean;

/**********************************************************************************
 * Copyright(c)2017 Dcits-air.com All rights reserved.
 *
 * @author Administrator.
 * @version 1.0.
 * @Title: Grib2ImportPropertyUtil.java.
 * @Package com.simple.core.grib2import.util.
 * @Description: Grib2ImportPropertyUtil.
 * @created 2017/4/13 10:30.
 **********************************************************************************/
public class Grib2ImportPropertyUtil implements InitializingBean {

    private static Grib2ImportPropertyUtil instance = new Grib2ImportPropertyUtil();

    /**
     * 指定Grib2的目录
     */
    private String grib2Dir;
    /**
     * 存放无法解析的Grib2文件
     */
    private String backupGrib2Dir;

    /**
     * 存放解析成功的Grib2文件
     */
    private String backupGrib2SuccessDir;

    /**
     * 指定SKYVIEW使用的、经Grib2转换后数据存储目录
     */
    private String grib2BySkyviewWindInfoDir;

    /**
     * 存放无法解析的skyview类型的Grib2文件
     */
    private String backupGrib2BySkyviewDir;

    /**
     * 存放解析成功的skyview类型的Grib2文件
     */
    private String backupGrib2BySkyviewSuccessDir;

    /**
     * 获取实例
     *
     * @return
     */
    public static Grib2ImportPropertyUtil getInstance() {
        return instance;
    }


    @Override
    public void afterPropertiesSet() throws Exception {
        instance = this;
    }

    public String getGrib2Dir() {
        return grib2Dir;
    }

    public void setGrib2Dir(String grib2Dir) {
        this.grib2Dir = grib2Dir;
    }

    public String getBackupGrib2Dir() {
        return backupGrib2Dir;
    }

    public void setBackupGrib2Dir(String backupGrib2Dir) {
        this.backupGrib2Dir = backupGrib2Dir;
    }

    public String getBackupGrib2SuccessDir() {
        return backupGrib2SuccessDir;
    }

    public void setBackupGrib2SuccessDir(String backupGrib2SuccessDir) {
        this.backupGrib2SuccessDir = backupGrib2SuccessDir;
    }

    public String getGrib2BySkyviewWindInfoDir() {
        return grib2BySkyviewWindInfoDir;
    }

    public void setGrib2BySkyviewWindInfoDir(String grib2BySkyviewWindInfoDir) {
        this.grib2BySkyviewWindInfoDir = grib2BySkyviewWindInfoDir;
    }

    public String getBackupGrib2BySkyviewDir() {
        return backupGrib2BySkyviewDir;
    }

    public void setBackupGrib2BySkyviewDir(String backupGrib2BySkyviewDir) {
        this.backupGrib2BySkyviewDir = backupGrib2BySkyviewDir;
    }

    public String getBackupGrib2BySkyviewSuccessDir() {
        return backupGrib2BySkyviewSuccessDir;
    }

    public void setBackupGrib2BySkyviewSuccessDir(String backupGrib2BySkyviewSuccessDir) {
        this.backupGrib2BySkyviewSuccessDir = backupGrib2BySkyviewSuccessDir;
    }
}