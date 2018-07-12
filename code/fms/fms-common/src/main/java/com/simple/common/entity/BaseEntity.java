package com.simple.common.entity;

import com.simple.common.utils.UuidUtil;

import java.io.Serializable;
import java.util.Date;

/**
 * 基础实体类，包含各实体公用属性 .
 *
 * @author Panda.HuangWei.
 * @Create 2016.11.17 15:32.
 * @Version 1.0 .
 */
public class BaseEntity implements Serializable {
    /**
     * 版本号
     **/
    private Integer version = 0;
    private String id;
    private String createBy;
    private String modifyBy;
    private boolean del = false;
    /**
     * 创建时间
     **/
    private Date createTm;
    /**
     * 修改时间
     **/
    private Date modifyTm;

    public void init() {
        this.id = UuidUtil.get32UUID();
        this.createBy = "Admin";
        this.modifyBy = "Admin";
        this.createTm = new Date();
        this.modifyTm = getCreateTm();
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCreateBy() {
        return createBy;
    }

    public void setCreateBy(String createBy) {
        this.createBy = createBy;
    }

    public String getModifyBy() {
        return modifyBy;
    }

    public void setModifyBy(String modifyBy) {
        this.modifyBy = modifyBy;
    }

    public boolean isDel() {
        return del;
    }

    public void setDel(boolean del) {
        this.del = del;
    }

    public Date getCreateTm() {
        return createTm;
    }

    public void setCreateTm(Date createTm) {
        this.createTm = createTm;
    }

    public Date getModifyTm() {
        return modifyTm;
    }

    public void setModifyTm(Date modifyTm) {
        this.modifyTm = modifyTm;
    }
}
