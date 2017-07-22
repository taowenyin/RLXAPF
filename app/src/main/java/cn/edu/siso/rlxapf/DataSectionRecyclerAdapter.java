package cn.edu.siso.rlxapf;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.truizlop.sectionedrecyclerview.SectionedRecyclerViewAdapter;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import cn.edu.siso.rlxapf.data.viewholder.DataFooterViewHolder;
import cn.edu.siso.rlxapf.data.viewholder.DataItemViewHolder;
import cn.edu.siso.rlxapf.data.viewholder.DataSectionViewHolder;

public class DataSectionRecyclerAdapter extends SectionedRecyclerViewAdapter<
        DataSectionViewHolder,
        DataItemViewHolder,
        DataFooterViewHolder> {

    public static String TITLE_KEY = "title";
    public static String DATA_KEY = "data";

    private List<List<Map<String, String>>> dataData = null;
    private List<String> sectionData = null;

    private Context context = null;

    public DataSectionRecyclerAdapter(Context context, List<List<Map<String, String>>> dataData) {
        super();

        this.context = context;

        // 初始化实时数据分组标题
        this.sectionData = Arrays.asList(
                this.context.getResources().getStringArray(R.array.real_data_sections));
        // 初始化实时数据
        this.dataData = dataData;
    }

    @Override
    protected int getSectionCount() {
        return sectionData.size();
    }

    @Override
    protected int getItemCountForSection(int section) {
        return dataData.get(section).size();
    }

    @Override
    protected boolean hasFooterInSection(int section) {
        if (section == sectionData.size() - 1) {
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
        holder.setSection(sectionData.get(section));
    }

    @Override
    protected void onBindSectionFooterViewHolder(DataFooterViewHolder holder, int section) {

    }

    @Override
    protected void onBindItemViewHolder(DataItemViewHolder holder, int section, int position) {
        holder.setTitle(dataData.get(section).get(position).get(TITLE_KEY));
        holder.setValue(dataData.get(section).get(position).get(DATA_KEY));
    }
}
