package cn.edu.siso.rlxapf.bean;

import java.util.ArrayList;
import java.util.List;


public class CityBean {
    private List<CountyBean> counties = new ArrayList<CountyBean>();

    private String name = "";

    public List<CountyBean> getCounties() {
        return counties;
    }

    public void setCounties(List<CountyBean> counties) {
        this.counties = counties;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public CountyBean getCounty(String name) {
        for (int i = 0; i < counties.size(); i++) {
            CountyBean bean = counties.get(i);
            if (bean.getName().equals(name)) {
                return bean;
            }
        }

        CountyBean bean = new CountyBean();
        bean.setName(name);
        counties.add(bean);

        return bean;
    }
}
