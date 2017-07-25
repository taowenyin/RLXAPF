package cn.edu.siso.rlxapf.bean;

import java.util.List;
import java.util.Map;

public class DataBean<T> {

    public static final String TITLE_KEY = "title";
    public static final String DATA_KEY = "data";

    private String section = null;

    private List<Map<String, T>> data = null;

    public DataBean(String section, List<Map<String, T>> data) {
        this.section = section;
        this.data = data;
    }

    public String getSection() {
        return section;
    }

    public void setSection(String section) {
        this.section = section;
    }

    public List<Map<String, T>> getData() {
        return data;
    }

    public void setData(List<Map<String, T>> data) {
        this.data = data;
    }

    public int getDataSize() {
        return data.size();
    }

    public Map<String, T> getIndexData(int index) {
        return data.get(index);
    }

    public void modifyIndexData(int index, Map<String, T> value) {
        data.set(index, value);
    }

    public void addData(Map<String, T> value) {
        data.add(value);
    }
}
