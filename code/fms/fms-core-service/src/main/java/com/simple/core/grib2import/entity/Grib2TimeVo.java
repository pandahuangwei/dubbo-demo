package com.simple.core.grib2import.entity;

import com.simple.common.utils.DateTimeUtil;
import com.simple.common.utils.StringUtil;

import java.io.Serializable;
import java.text.ParseException;

/**
 * @author ZhuoMengLan.
 * @Desc: 用于存储Grib2数据发布时间信息
 * @since 2017.04.12 9:45.
 */
public class Grib2TimeVo implements Serializable {
    private static final long serialVersionUID = 1L;
    // 发布时间
    private String publicTime;
    // 失效时间
    private String endTime;
    // 有效时长 06、09、12、15、18、21、24、27、30、33、36
    private int validDuration;

    public Grib2TimeVo(String publicTime, String endTime, int validDuration) throws Exception {
        this.publicTime = publicTime;
        this.endTime = endTime;
        this.validDuration = validDuration;

        if (StringUtil.isBlank(this.endTime)) {
            try {
               this.endTime = DateTimeUtil.changeHour(this.publicTime, this.validDuration);
            } catch (Exception e) {
                String strException = "计算数据有效截止时间出错：起始时间(" + publicTime + "),有效时长(" + validDuration + ")";
                throw new Exception(strException);
            }
        }
    }

    public String getPublicTime() {
        return publicTime;
    }

    public void setPublicTime(String publicTime) {
        this.publicTime = publicTime;
    }

    public int getValidDuration() {
        return validDuration;
    }

    public void setValidDuration(int validDuration) {
        this.validDuration = validDuration;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

}
