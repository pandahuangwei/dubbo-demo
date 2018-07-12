package com.simple.core.common.enums;

import com.simple.common.utils.StringUtil;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * 报文相关枚举
 *
 * @author Panda.HuangWei.
 * @version 1.0 .
 * @since 2017.01.12 9:46.
 */
public interface MessageEnum {

    /**
     * 报文类型
     */
    enum MessageType implements MessageEnum {
        REQFPN("REQFPN", "航路下行报"), FPN("FPN", "航路上行报"),
        REQPER("REQPER", "初始化下行报"), PER("PER", "初始化上行报"),
        REQPWI("REQPWI", "风温下行报"), PWI("PWI", "风温上行报"),
        REQLDI("REQLDI", "性能下行报"), LDI("LDI", "性能上行报"),
        FREETEXT("CMD", "自由报"),
        MAS("MAS", "通道响应"),

        REJFPN("REJFPN", "拒绝航路报"),
        REJPER("REJPER", "拒绝初始化报"),
        REJPWI("REJPWI", "拒绝风温报"),
        REJLDI("REJLDI", "拒绝性能报"),

        RESFPN("RESFPN", "飞机响应航路报"),
        RESPER("RESPER", "飞机响应初始化报"),
        RESPWI("RESPWI", "飞机响应风温报"),
        RESLDI("RESLDI", "飞机响应性能报");

        private String key;
        private String value;
        private static Map<String,String> map = new HashMap<>();
        static {
            for (MessageType e : MessageType.values()) {
                if (e.eq(FREETEXT.getKey())||e.eq(MAS.getKey())) {
                    continue;
                }
                String value2 = e.getKey();
                if (value2.length() >3) {
                    value2 = value2.substring(value2.length()-3);
                }
                map.put(e.getKey(),value2);
            }
        }

        private MessageType(String itemKey, String itemValue) {
            this.key = itemKey;
            this.value = itemValue;
        }

        public boolean eq(String key) {
            return this.key.equals(key);
        }

        public String getKey() {
            return key;
        }

        public String getValue() {
            return value;
        }

        public static boolean isConfirmType(String msgType) {
            return StringUtil.isEmpty(map.get(msgType)) ? false : true;
        }

        public static String getMapMsgType(String origMsgType) {
            return map.get(origMsgType);
        }

    }

    /**
     * 消息状态
     */
    enum MessageStatus implements MessageEnum {
        SUCC(true, "成功"), FAI(false, "失败");
        private boolean key;
        private String value;

        private MessageStatus(boolean itemKey, String itemValue) {
            this.key = itemKey;
            this.value = itemValue;
        }

        public boolean getKey() {
            return key;
        }

        public String getValue() {
            return value;
        }
    }

    /**
     * 报文状态
     */
    enum MsgReasonEnum implements MessageEnum {
        MSG_DOWNLINK_TIMEOUT(0, "飞机下行报文超时"),
        MSG_DOWNLINK_UNDEF(1, "飞机下行报文无定义"), MSG_DOWNLINK_SUC(2, "飞机下行报文处理成功"),

        REQ_ROUTE_FAIL(10, "请求前置系统接口失败"), REQ_ROUTE_TIMEOUT(11, "请求前置系统接口超时"), REQ_ROUTE_PARAMINVALID(12, "请求前置系统接口超时"),

        ROUTE_MSG_DOWNLINK_SUCC(20, "报文下行成功"), ROUTE_MSG_AUTH_UNDEF(21, "报文未授权", "no authority"), ROUTE_MSG_AUTH_EXPIRE(22, "报文授权失效", "authority expired"),
        ROUTE_MSG_FLIGHT_NOTFOUND(23, "获取不到航班", "no flight"), ROUTE_MSG_FLIGHTSTATUS_NOTPERMIT(24, "报文对应的航班状态不允许或者未配置", "not permit"),
        ROUTE_MSG_FLIGHT_OUTRANGE(25, "航班未在报文允许的时间范围", "time out of range"), ROUTE_NO_SERVICE(26, "无对应报文处理服务", "no service"),

        REQ_SERVICE_SUCC(40, "请求报文处理服务成功"), REQ_SERVICE_FAIL(41, "请求报文处理服务失败", "deal failed"), REQ_SERVICE_TIMEOUT(42, "请求报文处理服务超时", "deal timeout"),
        SERVICE_REQ_SUCC(43, "服务正常处理"),
        SERVICE_REJECT_FLIGHT_INFLYING(44, "拒绝飞行中的报文请求", "reject in air"), SERVICE_REJECT_FLIGHT_NOT_DISPACH(45, "拒绝航班未放行的报文请求", "not dispatch"),
        SERVICE_FLIGHT_PARAM_MISSING(46, "航班参数缺失","flight parameter missing"),
        SERVICE_UPLINK_MANUAL(47, "请联系签派进行手工上传","contact dispatcher"),

        MSG_UPLINK_PARSE_FAIL(60, "上行报文解析错误"),
        MSG_UPLINK_PACKAGE_SUCC(61, "上行报文组装成功"), MSG_UPLINK_PACKAGE_FAIL(62, "上行报文组装失败"),
        MSG_UPLINK_SEND_SUCC(63, "上行报文发送成功"), MSG_UPLINK_SEND_FAIL(64, "上行报文发送失败"),

        DSP_UPLINK_L(70, "上行报文DSP设备确认"),DSP_UPLINK_S(71, "上行报文DSP设备收到"),
        DSP_UPLINK_X(72, "上行报文DSP不支持"),DSP_UPLINK_F(73, "上行报文DSP不能传输"),

        MSG_UPLINK_FMS_RECEIVE(80, "上行报文飞机FMS设备收到"),MSG_UPLINK_FMS_COMFIRM(81, "上行报文飞机FMS设备确认"),
        MSG_UPLINK_FMS_REJECT(82, "上行报文飞机FMS设备拒绝"),

        SERVICE_INNER_ERR(99, "服务内部错误");

        private int key;
        private String value;
        private String value2;
        private static Map<Integer,Integer> msgStepReasonMap = new HashMap<>();
        static {
            msgStepReasonMap.put(MsgStep.DSPAK.getKey(),DSP_UPLINK_L.getKey());
            msgStepReasonMap.put(MsgStep.DSPAC.getKey(),DSP_UPLINK_S.getKey());
            msgStepReasonMap.put(MsgStep.DSPX.getKey(),DSP_UPLINK_X.getKey());
            msgStepReasonMap.put(MsgStep.DSPF.getKey(),DSP_UPLINK_F.getKey());

            msgStepReasonMap.put(MsgStep.AK.getKey(),MSG_UPLINK_FMS_RECEIVE.getKey());
            msgStepReasonMap.put(MsgStep.AC.getKey(),MSG_UPLINK_FMS_COMFIRM.getKey());
            msgStepReasonMap.put(MsgStep.RJ.getKey(),MSG_UPLINK_FMS_REJECT.getKey());
        }

        private MsgReasonEnum(int itemKey, String itemValue) {
            this.key = itemKey;
            this.value = itemValue;
        }

        private MsgReasonEnum(int itemKey, String itemValue, String itemValue2) {
            this.key = itemKey;
            this.value = itemValue;
            this.value2 = itemValue2;
        }

        public boolean eq(int key) {
            return this.key == key;
        }

        public int getKey() {
            return key;
        }

        public String getValue() {
            return value;
        }

        public String getValue2() {
            return value2;
        }

        public static int getReason(int msgStep) {
            return msgStepReasonMap.get(msgStep);
        }
    }

    enum ChannelStatus implements MessageEnum {
        DEFAULT("0", "默认"), FORCE("1", "强制");

        private String key;
        private String value;

        private ChannelStatus(String itemKey, String itemValue) {
            this.key = itemKey;
            this.value = itemValue;
        }

        public boolean eq(String key) {
            return this.key.equals(key);
        }

        public String getKey() {
            return key;
        }

        public String getValue() {
            return value;
        }
    }

    enum MsgStep implements MessageEnum {
        REQ(0, "REQ", "请求"),REQ_FAIL(1, "REQFAIL", "请求失败"),
        SEND(2, "SEND", "已发送"), SEND_FAIL(3, "SENDFAIL", "发送失败"),
        DSPAK(4, "DSPAK", "DSP链路确认"), DSPAC(5, "DSPAC", "DSP报文已接收"), DSPX(6, "DSPX", "DSP不支持MA"), DSPF(7, "DSPF", "DSP不能传输"),
        AK(8, "AK", "飞机接收"), AC(9, "AC", "飞机确认"), RJ(10, "RJ", "飞机拒绝");

        private int key;
        private String value;
        private String desc;
        private static Set<Integer> needBuildReasonSet = new HashSet<>();
        static {
            needBuildReasonSet.add(DSPAK.getKey());
            needBuildReasonSet.add(DSPAC.getKey());
            needBuildReasonSet.add(DSPX.getKey());
            needBuildReasonSet.add(DSPF.getKey());
            needBuildReasonSet.add(AK.getKey());
            needBuildReasonSet.add(AC.getKey());
            needBuildReasonSet.add(RJ.getKey());
        }

        private MsgStep(int itemKey, String itemValue, String itemDesc) {
            this.key = itemKey;
            this.value = itemValue;
            this.desc = itemDesc;
        }

        public boolean eq(int key) {
            return this.key == key;
        }

        public int getKey() {
            return key;
        }

        public String getValue() {
            return value;
        }

        public String getDesc() {
            return desc;
        }

        public static boolean isNeedBuildReason(int step) {
            return needBuildReasonSet.contains(step);
        }
    }

    enum MsgGroup implements MessageEnum {
        REQ(0, "REQ"), SEND(1, "SEND"), AK(2, "AK"), AC(3, "AC"), RJ(4, "RJ");

        private int key;
        private String value;
        private String desc;

        private MsgGroup(int itemKey, String itemValue) {
            this.key = itemKey;
            this.value = itemValue;
        }

        public boolean eq(int key) {
            return this.key == key;
        }

        public int getKey() {
            return key;
        }

        public String getValue() {
            return value;
        }

    }
}
