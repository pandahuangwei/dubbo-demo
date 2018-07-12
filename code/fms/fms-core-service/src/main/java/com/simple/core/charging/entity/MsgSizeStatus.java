package com.simple.core.charging.entity;

import java.io.Serializable;
import java.util.Date;

/**
 * @author Panda.HuangWei.
 * @version 1.0 .
 * @since 2017.04.14 19:10.
 */
public class MsgSizeStatus implements Serializable {
    private static final long serialVersionUID = 1L;

    private boolean syncStatus;
    /**
     * 上一次更新时间
     */
    private Date lastSyncDt;

    public boolean isSyncStatus() {
        return syncStatus;
    }

    public void setSyncStatus(boolean syncStatus) {
        this.syncStatus = syncStatus;
    }

    public Date getLastSyncDt() {
        return lastSyncDt;
    }

    public void setLastSyncDt(Date lastSyncDt) {
        this.lastSyncDt = lastSyncDt;
    }
}
