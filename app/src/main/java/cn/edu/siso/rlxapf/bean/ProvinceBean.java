package cn.edu.siso.rlxapf.bean;

import java.util.ArrayList;
import java.util.List;

public class ProvinceBean {

    private List<CityBean> cities = new ArrayList<CityBean>();

    private String name = "";

    public List<CityBean> getCities() {
        return cities;
    }

    public void setCities(List<CityBean> cities) {
        this.cities = cities;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public CityBean getCity(String name) {
        for (int i = 0; i < cities.size(); i++) {
            CityBean bean = cities.get(i);
            if (bean.getName().equals(name)) {
                return bean;
            }
        }

        CityBean bean = new CityBean();
        bean.setName(name);
        cities.add(bean);

        return bean;
    }
}
