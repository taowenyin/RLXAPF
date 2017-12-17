package cn.edu.siso.rlxapf.bean;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;

public class ProvinceBean {

    private String id = StringUtils.EMPTY;
    public static String ID = "id";

    private String code = StringUtils.EMPTY;
    public static String CODE = "code";

    private String name = StringUtils.EMPTY;
    public static String NAME = "name";

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
}
