package cn.edu.siso.rlxapf.bean;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;

public class ProvinceBean {

    private String province = StringUtils.EMPTY;
    public static String PROVINCE = "province";

    private String name = StringUtils.EMPTY;
    public static String NAME = "name";

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
