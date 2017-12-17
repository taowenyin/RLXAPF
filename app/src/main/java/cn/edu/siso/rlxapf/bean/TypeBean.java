package cn.edu.siso.rlxapf.bean;

import org.apache.commons.lang3.StringUtils;

public class TypeBean {

    private String deviceType = StringUtils.EMPTY;
    public static String DEVICE_TYPE = "deviceType";

    public String getDeviceType() {
        return deviceType;
    }

    public void setDeviceType(String deviceType) {
        this.deviceType = deviceType;
    }
}
