package com.simple.core.route.flight.cache;

import com.simple.common.cache.ReloadableSpringBean;
import com.simple.common.utils.StringUtil;
import com.simple.core.route.flight.entity.MsgValidTm;
import com.simple.core.route.flight.service.FlightService;
import com.simple.core.common.enums.FlightEnum.FlightState;
import org.apache.commons.lang3.tuple.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 报文类型有效性验证
 *
 * @author Panda.HuangWei.
 * @version 1.0 .
 * @since 2017.02.15 18:54.
 */
public class MsgValidCache extends ReloadableSpringBean {
    private static Logger logger = LoggerFactory.getLogger(MsgValidCache.class);

    private static MsgValidCache instance;

    @Resource(name = "flightService")
    private FlightService flightService;
    /**
     * map(二字码_报文类型,实体)
     */
    private Map<String, MsgValidTm> typeEntityMap = new HashMap<>();

    public MsgValidCache() {
        super();
        instance = this;
    }

    @Override
    public void reload() {
        logger.info("Begin loading msgValidTms...");
        List<MsgValidTm> list = flightService.findMsgValidTms();
        if (list == null || list.isEmpty()) {
            logger.info("Loaded msgValidTms is null");
            return;
        }
        typeEntityMap = buildTypeMap(list);
        logger.info("loaded msgValidTms Result==========");
        for (Map.Entry<String, MsgValidTm> e :typeEntityMap.entrySet())  {
            logger.info("{}",e.toString());
        }
        logger.info("End loaded msgValidTms...");
    }

    /**
     * 获取配置中的报文配置有效时间范围
     *
     * @param carrierIata 公司二字码
     * @param msgType     航班类型
     * @return 报文的有效时间范围
     */
    public Pair<Integer, Integer> getRangeMinute(String carrierIata, String msgType) {
        MsgValidTm entity = getConfig(carrierIata, msgType);

        if (entity == null || !entity.isActive()) {
            return Pair.of(0, 0);
        }

        int begin = !entity.isDeptState() ? 0 : entity.getDeptBegin();
        int end = !entity.isArriveState() ? 0 : entity.getArriveEnd();
        return Pair.of(begin, end);
    }

    /**
     * 判断报文在各状态下是否允许请求
     *
     * @param carrierIata 二字码
     * @param msgType     航班类型
     * @param enumState   飞机状态(航前,空中,航后)
     * @return boolean
     */
    public boolean isPermit(String carrierIata, String msgType, FlightState enumState) {
        MsgValidTm entity = getConfig(carrierIata, msgType);
        if (entity == null || !entity.isActive()) {
            logger.info("Can't find config ,isPermit,carrierIata:{},msgType:{},enumState:{}", carrierIata, msgType, enumState.getValue());
            return false;
        }

        boolean state = false;
        switch (enumState) {
            case FLYING:
                state = entity.isFlyState();
                break;
            case LAND:
                state = entity.isArriveState();
                break;
            case PREPARING:
                state = entity.isDeptState();
                break;
        }
        logger.info("Get the config:{}",entity.toString());
        logger.info("carrierIata:{},msgType:{},enumState,{},the result:{}",carrierIata, msgType, enumState.getKey(),state);
        return state;
    }

    /**
     * 获取配置信息.
     *
     * @param carrierIata 二字码
     * @param msgType     报文类型
     * @return 配置实体
     */
    private MsgValidTm getConfig(String carrierIata, String msgType) {
        return typeEntityMap.get(StringUtil.genKey(carrierIata, msgType));
    }

    /**
     * 构建map(二字码_报文类型,实体)
     *
     * @param list 设置列表
     * @return map(二字码_报文类型, 实体)
     */
    private Map<String, MsgValidTm> buildTypeMap(List<MsgValidTm> list) {
        Map<String, MsgValidTm> map = new HashMap<>();
        if (list == null || list.isEmpty()) {
            return map;
        }

        for (MsgValidTm e : list) {
            map.put(StringUtil.genKey(e.getCarrierIata(), e.getMsgType()), e);
        }
        return map;
    }

    public static MsgValidCache getInstance() {
        return instance;
    }

}
