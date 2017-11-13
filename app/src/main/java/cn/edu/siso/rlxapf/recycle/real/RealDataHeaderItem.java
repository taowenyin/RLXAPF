package cn.edu.siso.rlxapf.recycle.real;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import java.util.List;

import cn.edu.siso.rlxapf.R;
import cn.edu.siso.rlxapf.recycle.AbstractItem;
import eu.davidea.flexibleadapter.FlexibleAdapter;
import eu.davidea.flexibleadapter.items.IHeader;
import eu.davidea.viewholders.FlexibleViewHolder;

public class RealDataHeaderItem extends AbstractItem<RealDataHeaderItem.RealDataHeaderViewHolder>
        implements IHeader<RealDataHeaderItem.RealDataHeaderViewHolder> {

    public RealDataHeaderItem(String id, ITEM_TYPE tpye, Context context) {
        super(id, tpye, context);
    }

    @Override
    public int getLayoutRes() {
        return R.layout.data_header_item_layout;
    }

    @Override
    public RealDataHeaderViewHolder createViewHolder(View view, FlexibleAdapter adapter) {
        return new RealDataHeaderViewHolder(view, adapter);
    }

    @Override
    public void bindViewHolder(FlexibleAdapter adapter, RealDataHeaderViewHolder holder, int position, List payloads) {
        holder.title.setText(getTitle());
    }

    public class RealDataHeaderViewHolder extends FlexibleViewHolder {

        public TextView title = null;

        public RealDataHeaderViewHolder(View view, FlexibleAdapter adapter) {
            super(view, adapter);

            title = view.findViewById(R.id.title);
        }
    }

}
