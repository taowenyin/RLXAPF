package cn.edu.siso.rlxapf.bean;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;


public class CityBean {

    private String id = StringUtils.EMPTY;
    public static String ID = "id";

    private String code = StringUtils.EMPTY;
    public static String CODE = "code";

    private String name = StringUtils.EMPTY;
    public static String NAME = "name";

    private String parentId = StringUtils.EMPTY;
    public static String PARENT_ID = "parentId";

    private String firstLetter = StringUtils.EMPTY;
    public static String FIRST_LETTER = "firstLetter";

    private String cityLevel = StringUtils.EMPTY;
    public static String CITY_LEVEL = "cityLevel";

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public String getFirstLetter() {
        return firstLetter;
    }

    public void setFirstLetter(String firstLetter) {
        this.firstLetter = firstLetter;
    }

    public String getCityLevel() {
        return cityLevel;
    }

    public void setCityLevel(String cityLevel) {
        this.cityLevel = cityLevel;
    }
}
