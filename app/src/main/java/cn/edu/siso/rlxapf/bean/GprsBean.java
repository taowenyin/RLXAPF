package cn.edu.siso.rlxapf.bean;

import org.apache.commons.lang3.StringUtils;

public class GprsBean {

    private String GPSDeviceNo = StringUtils.EMPTY;
    public static String GPS_DEVICE_NO = "GPSDeviceNo";

    public String getGPSDeviceNo() {
        return GPSDeviceNo;
    }

    public void setGPSDeviceNo(String GPSDeviceNo) {
        this.GPSDeviceNo = GPSDeviceNo;
    }
}
