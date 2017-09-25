package cn.edu.siso.rlxapf.expandable;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;

import cn.edu.siso.rlxapf.DeviceFilterRecycleAdapter;
import cn.edu.siso.rlxapf.R;
import eu.davidea.flexibleadapter.FlexibleAdapter;
import eu.davidea.flexibleadapter.items.IExpandable;
import eu.davidea.flexibleadapter.items.IHeader;
import eu.davidea.flexibleadapter.items.ISectionable;
import eu.davidea.viewholders.ExpandableViewHolder;

public class FilterExpandableSubHeadItem extends AbstractItem<FilterExpandableSubHeadItem.FilterExpandableSubHeadViewHolder>
        implements IExpandable<FilterExpandableSubHeadItem.FilterExpandableSubHeadViewHolder, FilterExpandableSubItem> {

    private boolean isExpandable = false;
    private List<FilterExpandableSubItem> subItems = null;

    public static final String TAG = "ExpandableSubHeadItem";

    public FilterExpandableSubHeadItem(String id, ITEM_TYPE tpye, Context context) {
        super(id, tpye, context);

        setExpanded(false);
    }

    @Override
    public int getLayoutRes() {
        if (getType() == ITEM_TYPE.CATEGORY) {
            return R.layout.filter_expandable_category_item;
        }

        return R.layout.filter_expandable_local_item;
    }

    @Override
    public FilterExpandableSubHeadViewHolder createViewHolder(View view, FlexibleAdapter adapter) {
        return new FilterExpandableSubHeadViewHolder(view, adapter, getType());
    }

    @Override
    public void bindViewHolder(FlexibleAdapter adapter, FilterExpandableSubHeadViewHolder holder, int position, List payloads) {
        if (getType() == ITEM_TYPE.CATEGORY) {
            holder.filterCB.setText(getTitle());
            holder.filterCB.setChecked(false);
            holder.id = getId(); // 解决ID不能传递的问题
        } else {
            holder.title.setText(getTitle());
            holder.statusTitle.setText(isExpanded() ? "展开" : "折叠");
            holder.splitLine.setVisibility(isExpanded() ? View.GONE : View.VISIBLE);
            holder.statusImg.setImageResource(isExpanded() ? R.drawable.ic_arrow_up : R.drawable.ic_arrow_down);
        }
    }

    @Override
    public boolean isExpanded() {
        return isExpandable;
    }

    @Override
    public void setExpanded(boolean expanded) {
        this.isExpandable = expanded;
    }

    @Override
    public int getExpansionLevel() {
        return 1;
    }

    @Override
    public List<FilterExpandableSubItem> getSubItems() {
        return subItems;
    }

    public boolean hasSubItems() {
        return subItems!= null && subItems.size() > 0;
    }

    public boolean removeSubItem(FilterExpandableSubItem item) {
        return item != null && subItems.remove(item);
    }

    public boolean removeSubItem(int position) {
        if (subItems != null && position >= 0 && position < subItems.size()) {
            subItems.remove(position);
            return true;
        }
        return false;
    }

    public void addSubItem(FilterExpandableSubItem subItem) {
        if (subItems == null)
            subItems = new ArrayList<FilterExpandableSubItem>();
        subItems.add(subItem);
    }

    public void addSubItem(int position, FilterExpandableSubItem subItem) {
        if (subItems != null && position >= 0 && position < subItems.size()) {
            subItems.add(position, subItem);
        } else
            addSubItem(subItem);
    }

    public class FilterExpandableSubHeadViewHolder extends ExpandableViewHolder {

        public String id = StringUtils.EMPTY; // 解决ID不能传递的问题

        public TextView title = null;
        public TextView statusTitle = null;
        public ImageView statusImg = null;
        public ImageView splitLine = null;

        public CheckBox filterCB = null;

        public FilterExpandableSubHeadViewHolder(View view, final FlexibleAdapter adapter, AbstractItem.ITEM_TYPE type) {
            super(view, adapter);

            if (type == AbstractItem.ITEM_TYPE.CATEGORY) {
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
            } else {
                title = (TextView) view.findViewById(R.id.title);
                statusTitle = (TextView) view.findViewById(R.id.status_title);
                statusImg = (ImageView) view.findViewById(R.id.status_img);
                splitLine = (ImageView) view.findViewById(R.id.split_line);
            }
        }

        @Override
        protected boolean shouldNotifyParentOnClick() {
            return true;
        }
    }

}
