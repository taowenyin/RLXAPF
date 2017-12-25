package cn.edu.siso.rlxapf.bean;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;


public class CityBean {

    private String city = StringUtils.EMPTY;
    public static String CITY = "city";

    private String name = StringUtils.EMPTY;
    public static String NAME = "name";

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }
}
