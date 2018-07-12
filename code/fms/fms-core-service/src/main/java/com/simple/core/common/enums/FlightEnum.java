package com.simple.core.common.enums;

/**
 * @author Panda.HuangWei.
 * @version 1.0 .
 * @since 2017.02.23 14:51.
 */
public interface FlightEnum {
    public final static String FLIGHT_CANCEL = "C";
    public final static String FLIGHT_ALTER = "V";
    public final static String FLIGHT_RETURN = "R";

    enum FlightState implements MessageEnum {
        LAND(0, "航后"), FLYING(1, "空中"), PREPARING(2, "航前");

        private int key;
        private String value;

        private FlightState(int itemKey, String itemValue) {
            this.key = itemKey;
            this.value = itemValue;
        }

        public boolean eq(int key) {
            return this.key == key;
        }

        public static boolean isFlying(int key) {
            return FLYING.eq(key);
        }

        public static boolean isLand(int key) {
            return LAND.eq(key);
        }

        public static boolean isPreparing(int key) {
            return PREPARING.eq(key);
        }

        public int getKey() {
            return key;
        }

        public String getValue() {
            return value;
        }
    }

    /**
     * 牵派放行状态
     */
    enum DispatchStatus implements MessageEnum {
        NO("0", "未放行"), YES("1", "已放行"), NULL("2", "取不到值");

        private String key;
        private String value;

        private DispatchStatus(String itemKey, String itemValue) {
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

    /**
     * 机组到位状态
     */
    enum CrewArrivalStatus implements MessageEnum {
        NO("0", "未到位"), YES("1", "已到位"), NULL("2", "取不到值");

        private String key;
        private String value;

        private CrewArrivalStatus(String itemKey, String itemValue) {
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

    /**
     * 截载状态
     */
    enum DisallowStatus implements MessageEnum {
        NO("0", "未截载"), YES("1", "已截载"), NULL("2", "取不到值");

        private String key;
        private String value;

        private DisallowStatus(String itemKey, String itemValue) {
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


}
