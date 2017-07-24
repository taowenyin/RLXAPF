package cn.edu.siso.rlxapf.bean;

import java.util.List;

public class DataGroupBean {

    private String groupSection = null;

    private List<DataBean> data = null;

    public DataGroupBean(String groupSection, List<DataBean> data) {
        this.groupSection = groupSection;
        this.data = data;
    }

    public String getGroupSection() {
        return groupSection;
    }

    public void setGroupSection(String groupSection) {
        this.groupSection = groupSection;
    }

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    /**
     * 得到数据的数量
     */
    public int getDataSize() {
        return data.size();
    }

    /**
     * 获取指定位置的数据
     */
    public DataBean getIndexData(int index) {
        if (index >= data.size()) {
            return null;
        }

        return this.data.get(index);
    }

    /**
     * 修改某个索引位置的值
     */
    public int modifyIndexData(int index, DataBean value) {
        if (index >= data.size()) {
            return -1;
        }
        this.data.set(index, value);

        return 0;
    }

    /**
     * 添加数据
     */
    public void addData(DataBean value) {
        data.add(value);
    }
}
