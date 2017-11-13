package cn.edu.siso.rlxapf.recycle.real;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import java.util.List;

import cn.edu.siso.rlxapf.R;
import cn.edu.siso.rlxapf.recycle.AbstractItem;
import eu.davidea.flexibleadapter.FlexibleAdapter;
import eu.davidea.flexibleadapter.items.IHeader;
import eu.davidea.flexibleadapter.items.ISectionable;
import eu.davidea.viewholders.FlexibleViewHolder;

public class RealDataSubHeaderItem extends AbstractItem<RealDataSubHeaderItem.RealDataSubHeaderViewHolder>
        implements ISectionable<RealDataSubHeaderItem.RealDataSubHeaderViewHolder, RealDataHeaderItem>,
        IHeader<RealDataSubHeaderItem.RealDataSubHeaderViewHolder> {

    private RealDataHeaderItem header = null;

    public RealDataSubHeaderItem(String id, ITEM_TYPE tpye, RealDataHeaderItem header, Context context) {
        super(id, tpye, context);
        this.header = header;
    }

    @Override
    public int getLayoutRes() {
        return R.layout.data_sub_header_item_layout;
    }

    @Override
    public RealDataSubHeaderViewHolder createViewHolder(View view, FlexibleAdapter adapter) {
        return new RealDataSubHeaderViewHolder(view, adapter);
    }

    @Override
    public void bindViewHolder(FlexibleAdapter adapter, RealDataSubHeaderViewHolder holder, int position, List payloads) {
        holder.title.setText(getTitle());
    }

    @Override
    public RealDataHeaderItem getHeader() {
        return header;
    }

    @Override
    public void setHeader(RealDataHeaderItem header) {
        this.header = header;
    }

    public class RealDataSubHeaderViewHolder extends FlexibleViewHolder {

        public TextView title = null;

        public RealDataSubHeaderViewHolder(View view, FlexibleAdapter adapter) {
            super(view, adapter);

            title = view.findViewById(R.id.title);
        }
    }

}
