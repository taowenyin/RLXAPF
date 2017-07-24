package cn.edu.siso.rlxapf.bean;

import java.util.List;
import java.util.Map;

public class DataBean {

    private String section = null;

    private List<Float> data = null;

    public DataBean(String section, List<Float> data) {
        this.section = section;
        this.data = data;
    }

    public String getSection() {
        return section;
    }

    public void setSection(String section) {
        this.section = section;
    }

    public List<Float> getData() {
        return data;
    }

    public void setData(List<Float> data) {
        this.data = data;
    }

    public int getDataSize() {
        return data.size();
    }

    public Float getIndexData(int index) {
        return data.get(index);
    }

    public void modifyIndexData(int index, Float value) {
        data.set(index, value);
    }

    public void addData(Float value) {
        data.add(value);
    }
}
