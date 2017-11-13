package cn.edu.siso.rlxapf.recycle.real;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import org.apache.commons.lang3.StringUtils;

import java.util.List;

import cn.edu.siso.rlxapf.R;
import cn.edu.siso.rlxapf.recycle.AbstractItem;
import eu.davidea.flexibleadapter.FlexibleAdapter;
import eu.davidea.flexibleadapter.items.ISectionable;
import eu.davidea.viewholders.FlexibleViewHolder;

public class RealDataSubItem extends AbstractItem<RealDataSubItem.RealDataSubViewHolder>
        implements ISectionable<RealDataSubItem.RealDataSubViewHolder, RealDataSubHeaderItem> {

    private RealDataSubHeaderItem header = null;

    private String value = StringUtils.EMPTY;

    public RealDataSubItem(String id, ITEM_TYPE tpye, RealDataSubHeaderItem header, Context context) {
        super(id, tpye, context);

        this.header = header;
    }

    @Override
    public int getLayoutRes() {
        return R.layout.data_sub_item_layout;
    }

    @Override
    public RealDataSubViewHolder createViewHolder(View view, FlexibleAdapter adapter) {
        return new RealDataSubViewHolder(view, adapter);
    }

    @Override
    public void bindViewHolder(FlexibleAdapter adapter, RealDataSubViewHolder holder, int position, List payloads) {
        holder.title.setText(getTitle());
        holder.value.setText(getValue());
    }

    @Override
    public RealDataSubHeaderItem getHeader() {
        return header;
    }

    @Override
    public void setHeader(RealDataSubHeaderItem header) {
        this.header = header;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public class RealDataSubViewHolder extends FlexibleViewHolder {

        private TextView title = null;
        private TextView value = null;

        public RealDataSubViewHolder(View view, FlexibleAdapter adapter) {
            super(view, adapter);

            title = view.findViewById(R.id.title);
            value = view.findViewById(R.id.value);
        }
    }

}
