package cn.edu.siso.rlxapf.expandable;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import org.apache.commons.lang3.StringUtils;

import java.util.List;

import cn.edu.siso.rlxapf.DeviceFilterRecycleAdapter;
import cn.edu.siso.rlxapf.R;
import eu.davidea.flexibleadapter.FlexibleAdapter;
import eu.davidea.flexibleadapter.items.IHeader;
import eu.davidea.flexibleadapter.items.ISectionable;
import eu.davidea.viewholders.FlexibleViewHolder;

public class FilterExpandableSubItem extends AbstractItem<FilterExpandableSubItem.FilterExpandableSubViewHolder>
        implements ISectionable<FilterExpandableSubItem.FilterExpandableSubViewHolder, IHeader> {

    private IHeader header = null;

    public static final String TAG = "FilterExpandableSubItem";

    public FilterExpandableSubItem(String id, ITEM_TYPE type, Context context) {
        super(id, type, context);
    }

    @Override
    public int getLayoutRes() {
        return R.layout.filter_expandable_sub_item;
    }

    @Override
    public FilterExpandableSubViewHolder createViewHolder(View view, FlexibleAdapter adapter) {
        return new FilterExpandableSubViewHolder(view, adapter);
    }

    @Override
    public void bindViewHolder(FlexibleAdapter adapter, FilterExpandableSubViewHolder holder, int position, List payloads) {
        holder.filterCB.setText(getTitle());
        holder.filterCB.setChecked(false);
        holder.id = getId(); // 解决ID不能传递的问题
    }

    @Override
    public IHeader getHeader() {
        return header;
    }

    @Override
    public void setHeader(IHeader header) {
        this.header = header;
    }

    public class FilterExpandableSubViewHolder extends FlexibleViewHolder {

        public String id = StringUtils.EMPTY; // 解决ID不能传递的问题

        public CheckBox filterCB = null;

        public FilterExpandableSubViewHolder(View view, final FlexibleAdapter adapter) {
            super(view, adapter);

            this.filterCB = (CheckBox) view.findViewById(R.id.filter_item);
            this.filterCB.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (adapter instanceof DeviceFilterRecycleAdapter) {
                        DeviceFilterRecycleAdapter filterRecycleAdapter = (DeviceFilterRecycleAdapter) adapter;
                        if (filterRecycleAdapter.getOnItemClickListener() != null) {
                            filterRecycleAdapter.getOnItemClickListener().onItemCheckedChanged(buttonView, id);
                        }
                    }
                }
            });
        }
    }

}
