package cn.edu.siso.rlxapf;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.truizlop.sectionedrecyclerview.SectionedRecyclerViewAdapter;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import cn.edu.siso.rlxapf.bean.DataBean;
import cn.edu.siso.rlxapf.bean.DataGroupBean;
import cn.edu.siso.rlxapf.data.viewholder.DataFooterViewHolder;
import cn.edu.siso.rlxapf.data.viewholder.DataItemViewHolder;
import cn.edu.siso.rlxapf.data.viewholder.DataSectionViewHolder;

public class DataSectionRecyclerAdapter extends SectionedRecyclerViewAdapter<
        DataSectionViewHolder,
        DataItemViewHolder,
        DataFooterViewHolder> {

    private List<DataGroupBean> dataData = null;

    private Context context = null;

    public DataSectionRecyclerAdapter(Context context, List<DataGroupBean> dataData) {
        super();

        this.context = context;
        this.dataData = dataData;
    }

    @Override
    protected int getSectionCount() {
        return dataData.size();
    }

    @Override
    protected int getItemCountForSection(int section) {
        return dataData.get(section).getDataSize();
    }

    @Override
    protected boolean hasFooterInSection(int section) {
        if (section == dataData.size() - 1) {
            return true;
        }
        return false;
    }

    @Override
    protected DataSectionViewHolder onCreateSectionHeaderViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.data_section_layout, parent, false);
        return new DataSectionViewHolder(view);
    }

    @Override
    protected DataFooterViewHolder onCreateSectionFooterViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.data_footer_layout, parent, false);
        return new DataFooterViewHolder(view);
    }

    @Override
    protected DataItemViewHolder onCreateItemViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.data_item_layout, parent, false);
        return new DataItemViewHolder(view);
    }

    @Override
    protected void onBindSectionHeaderViewHolder(DataSectionViewHolder holder, int section) {
        holder.setSection(dataData.get(section).getGroupSection());
    }

    @Override
    protected void onBindSectionFooterViewHolder(DataFooterViewHolder holder, int section) {

    }

    @Override
    protected void onBindItemViewHolder(DataItemViewHolder holder, int section, int position) {
        DataGroupBean groupBean = dataData.get(section);
        DataBean dataBean = groupBean.getIndexData(position);
        String title = dataBean.getSection();
        String value = String.valueOf(dataBean.getIndexData(0).get(DataBean.DATA_KEY));

        holder.setTitle(title);
        holder.setValue(value);
    }
}
