package cn.edu.siso.rlxapf.bean;

import org.apache.commons.lang3.StringUtils;

public class CountyBean {

    private String county = StringUtils.EMPTY;
    public static String COUNTY = "county";

    private String name = StringUtils.EMPTY;
    public static String NAME = "name";

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCounty() {
        return county;
    }

    public void setCounty(String county) {
        this.county = county;
    }
}
