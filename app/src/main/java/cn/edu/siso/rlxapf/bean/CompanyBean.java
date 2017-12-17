package cn.edu.siso.rlxapf.bean;

import org.apache.commons.lang3.StringUtils;

public class CompanyBean {

    private String account = StringUtils.EMPTY;
    public static String ACCOUNT = "account";

    private String name = StringUtils.EMPTY;
    public static String NAME = "name";

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
}
