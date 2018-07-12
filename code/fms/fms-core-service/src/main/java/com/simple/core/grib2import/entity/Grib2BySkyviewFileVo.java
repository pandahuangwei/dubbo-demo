package com.simple.core.grib2import.entity;

import java.io.Serializable;

/**
 * Created with IntelliJ IDEA.
 * Desc: 该对象对应着SKYVIEW项目中使用Grib2数据文件
 */
public class Grib2BySkyviewFileVo implements Serializable {
    private static final long serialVersionUID = 1L;

    //文件路径
    private String filePath;
    // 发布时间
    private Grib2TimeVo grib2Time;
    // 保留，文件格式描述（即，每个待解析文件的第二行内容，一旦该行内容会经常性的发生变化，就需要对格式进行分析，并修改代码）
    private String strFileFormatDesc;
    // 实际内容
    private String grib2Context;

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public Grib2TimeVo getGrib2Time() {
        return grib2Time;
    }

    public void setGrib2Time(Grib2TimeVo grib2Time) {
        this.grib2Time = grib2Time;
    }

    public String getStrFileFormatDesc() {
        return strFileFormatDesc;
    }

    public void setStrFileFormatDesc(String strFileFormatDesc) {
        this.strFileFormatDesc = strFileFormatDesc;
    }

    public String getGrib2Context() {
        return grib2Context;
    }

    public void setGrib2Context(String grib2Context) {
        this.grib2Context = grib2Context;
    }
}
