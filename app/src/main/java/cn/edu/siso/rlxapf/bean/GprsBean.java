package cn.edu.siso.rlxapf.bean;

import org.apache.commons.lang3.StringUtils;

public class GprsBean {
    private String id = StringUtils.EMPTY;
    public static String ID = "id";

    private String GPSDeviceNo = StringUtils.EMPTY;
    public static String GPS_DEVICE_NO = "GPSDeviceNo";

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getGPSDeviceNo() {
        return GPSDeviceNo;
    }

    public void setGPSDeviceNo(String GPSDeviceNo) {
        this.GPSDeviceNo = GPSDeviceNo;
    }
}
