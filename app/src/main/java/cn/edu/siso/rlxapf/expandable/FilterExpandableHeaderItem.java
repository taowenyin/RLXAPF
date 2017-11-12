package cn.edu.siso.rlxapf.expandable;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;

import cn.edu.siso.rlxapf.R;
import eu.davidea.flexibleadapter.FlexibleAdapter;
import eu.davidea.flexibleadapter.items.IExpandable;
import eu.davidea.flexibleadapter.items.IHeader;
import eu.davidea.viewholders.ExpandableViewHolder;

public class FilterExpandableHeaderItem extends AbstractItem<FilterExpandableHeaderItem.FilterExpandableHeaderViewHolder>
        implements IExpandable<FilterExpandableHeaderItem.FilterExpandableHeaderViewHolder, FilterExpandableSubHeadItem>,
        IHeader<FilterExpandableHeaderItem.FilterExpandableHeaderViewHolder> {

    private boolean isExpanded = false;

    private List<FilterExpandableSubHeadItem> subItems = null;

    public FilterExpandableHeaderItem(String id, ITEM_TYPE type, Context context) {
        super(id, type, context);

        setExpanded(false);
    }

    @Override
    public int getLayoutRes() {
        return R.layout.filter_expandable_header_item;
    }

    @Override
    public FilterExpandableHeaderViewHolder createViewHolder(View view, FlexibleAdapter adapter) {
        return new FilterExpandableHeaderViewHolder(view, adapter);
    }

    @Override
    public void bindViewHolder(FlexibleAdapter adapter, FilterExpandableHeaderViewHolder holder, int position, List payloads) {
        holder.id = getId(); // 解决ID不能传递的问题
        holder.title.setText(getTitle());
//        holder.statusTitle.setText(isExpanded() ? "展开" : "折叠");
        holder.splitLine.setVisibility(isExpanded() ? View.GONE : View.VISIBLE);
        holder.statusImg.setImageResource(isExpanded() ? R.drawable.ic_arrow_up : R.drawable.ic_arrow_down);
    }

    @Override
    public boolean isExpanded() {
        return isExpanded;
    }

    @Override
    public void setExpanded(boolean expanded) {
        isExpanded = expanded;
    }

    @Override
    public int getExpansionLevel() {
        return 0;
    }

    @Override
    public List<FilterExpandableSubHeadItem> getSubItems() {
        return subItems;
    }

    public boolean hasSubItems() {
        return subItems != null && subItems.size() > 0;
    }

    public boolean removeSubItem(FilterExpandableSubHeadItem item) {
        return item != null && subItems.remove(item);
    }

    public boolean removeSubItem(int position) {
        if (subItems != null && position >= 0 && position < subItems.size()) {
            subItems.remove(position);
            return true;
        }
        return false;
    }

    public void addSubItem(FilterExpandableSubHeadItem item) {
        if (subItems == null)
            subItems = new ArrayList<>();
        subItems.add(item);
    }

    public void addSubItem(int position, FilterExpandableSubHeadItem item) {
        if (subItems != null && position >= 0 && position < subItems.size()) {
            subItems.add(position, item);
        } else
            addSubItem(item);
    }

    public class FilterExpandableHeaderViewHolder extends ExpandableViewHolder  {

        public String id = StringUtils.EMPTY; // 解决ID不能传递的问题

        public TextView title = null;
        public TextView statusTitle = null;
        public ImageView statusImg = null;
        public ImageView splitLine = null;

        public FilterExpandableHeaderViewHolder(View view, FlexibleAdapter adapter) {
            super(view, adapter);

            title = (TextView) view.findViewById(R.id.title);
//            statusTitle = (TextView) view.findViewById(R.id.status_title);
            statusImg = (ImageView) view.findViewById(R.id.status_img);
            splitLine = (ImageView) view.findViewById(R.id.split_line);
        }

        @Override
        protected boolean shouldNotifyParentOnClick() {
            return true;
        }
    }
}
