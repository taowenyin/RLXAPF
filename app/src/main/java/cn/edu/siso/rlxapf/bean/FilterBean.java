package cn.edu.siso.rlxapf.bean;

import java.util.ArrayList;
import java.util.List;

public class FilterBean {

    private List<ProvinceBean> provinces = new ArrayList<ProvinceBean>();

    private List<String> devicesName = new ArrayList<String>();
    private List<String> devicesType = new ArrayList<String>();
    private List<String> devicesGprs = new ArrayList<String>();
    private List<String> devicesNo = new ArrayList<String>();

    public List<ProvinceBean> getProvinces() {
        return provinces;
    }

    public void setProvinces(List<ProvinceBean> provinces) {
        this.provinces = provinces;
    }

    public List<String> getDevicesType() {
        return devicesType;
    }

    public void setDevicesType(List<String> devicesType) {
        this.devicesType = devicesType;
    }

    public List<String> getDevicesGprs() {
        return devicesGprs;
    }

    public void setDevicesGprs(List<String> devicesGprs) {
        this.devicesGprs = devicesGprs;
    }

    public List<String> getDevicesNo() {
        return devicesNo;
    }

    public void setDevicesNo(List<String> devicesNo) {
        this.devicesNo = devicesNo;
    }

    public List<String> getDevicesName() {
        return devicesName;
    }

    public void setDevicesName(List<String> devicesName) {
        this.devicesName = devicesName;
    }

    public ProvinceBean getProvince(String name) {
        for (int i = 0; i < provinces.size(); i++) {
            ProvinceBean bean = provinces.get(i);
            if (bean.getName().equals(name)) {
                return bean;
            }
        }

        ProvinceBean bean = new ProvinceBean();
        bean.setName(name);
        provinces.add(bean);

        return bean;
    }

    public void addName(String name) {
        for (int i = 0; i < devicesName.size(); i++) {
            if (devicesName.get(i).equals(name)) {
                return;
            }
        }

        devicesName.add(name);
    }

    public void addType(String type) {
        for (int i = 0; i < devicesType.size(); i++) {
            if (devicesType.get(i).equals(type)) {
                return;
            }
        }

        devicesType.add(type);
    }

    public void addGprs(String gprs) {
        for (int i = 0; i < devicesGprs.size(); i++) {
            if (devicesGprs.get(i).equals(gprs)) {
                return;
            }
        }

        devicesGprs.add(gprs);
    }

    public void addNo(String no) {
        for (int i = 0; i < devicesNo.size(); i++) {
            if (devicesNo.get(i).equals(no)) {
                return;
            }
        }

        devicesNo.add(no);
    }
}
