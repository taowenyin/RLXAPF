package cn.edu.siso.rlxapf.bean;

import org.apache.commons.lang3.StringUtils;

public class DeviceBean {
    private String id = StringUtils.EMPTY;
    private String province = StringUtils.EMPTY;
    private String city = StringUtils.EMPTY;
    private String county = StringUtils.EMPTY;
    private String account = StringUtils.EMPTY;
    private String name = StringUtils.EMPTY;
    private String deviceType = StringUtils.EMPTY;
    private String GPSDeviceNo = StringUtils.EMPTY;
    private String GPRSComPw = StringUtils.EMPTY;
    private String GPRSMobileNo = StringUtils.EMPTY;
    private String GPRSChangePwd = StringUtils.EMPTY;
    private String deviceNo = StringUtils.EMPTY;
    private String authority = StringUtils.EMPTY;
    private String onoff = StringUtils.EMPTY;
    private String deleted = StringUtils.EMPTY;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCounty() {
        return county;
    }

    public void setCounty(String county) {
        this.county = county;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDeviceType() {
        return deviceType;
    }

    public void setDeviceType(String deviceType) {
        this.deviceType = deviceType;
    }

    public String getGPSDeviceNo() {
        return GPSDeviceNo;
    }

    public void setGPSDeviceNo(String GPSDeviceNo) {
        this.GPSDeviceNo = GPSDeviceNo;
    }

    public String getGPRSComPw() {
        return GPRSComPw;
    }

    public void setGPRSComPw(String GPRSComPw) {
        this.GPRSComPw = GPRSComPw;
    }

    public String getGPRSMobileNo() {
        return GPRSMobileNo;
    }

    public void setGPRSMobileNo(String GPRSMobileNo) {
        this.GPRSMobileNo = GPRSMobileNo;
    }

    public String getGPRSChangePwd() {
        return GPRSChangePwd;
    }

    public void setGPRSChangePwd(String GPRSChangePwd) {
        this.GPRSChangePwd = GPRSChangePwd;
    }

    public String getDeviceNo() {
        return deviceNo;
    }

    public void setDeviceNo(String deviceNo) {
        this.deviceNo = deviceNo;
    }

    public String getAuthority() {
        return authority;
    }

    public void setAuthority(String authority) {
        this.authority = authority;
    }

    public String getOnoff() {
        return onoff;
    }

    public void setOnoff(String onoff) {
        this.onoff = onoff;
    }

    public String getDeleted() {
        return deleted;
    }

    public void setDeleted(String deleted) {
        this.deleted = deleted;
    }
}
