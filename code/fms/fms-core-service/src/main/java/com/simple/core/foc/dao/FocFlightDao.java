package com.simple.core.foc.dao;

import com.simple.common.persistence.page.Pagination;
import com.simple.core.foc.entity.FocFlight;
import java.util.Date;
import java.util.List;

/**
 * Created by Administrator on 2017/2/23.
 */
public interface FocFlightDao {

    void setCarrierIATA(String iata);

    Pagination selectFlightByTime(Date start, Date end, int pageNo, int pageSize);
    Pagination selectFlightByTime(Date lastUpdateTime, int pageNo, int pageSize);

    Pagination selectCfpByTime(Date start, Date end, int pageNo, int pageSize);
    Pagination selectCfpByTime(Date lastUpdateTime, int pageNo, int pageSize);

    List<Integer> getFocFlightIds(List<String> list);
}
