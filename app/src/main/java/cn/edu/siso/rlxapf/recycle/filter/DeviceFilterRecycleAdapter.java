package cn.edu.siso.rlxapf.recycle.filter;

import android.support.annotation.Nullable;

import java.util.List;

import cn.edu.siso.rlxapf.OnItemCheckedChangedListener;
import eu.davidea.flexibleadapter.FlexibleAdapter;
import eu.davidea.flexibleadapter.items.AbstractFlexibleItem;

public class DeviceFilterRecycleAdapter extends FlexibleAdapter<AbstractFlexibleItem> {

    private OnItemCheckedChangedListener onItemCheckedChangedListener = null;

    public DeviceFilterRecycleAdapter(@Nullable List<AbstractFlexibleItem> items, @Nullable Object listeners) {
        super(items, listeners, true);
    }

    public void setOnItemClickListener(OnItemCheckedChangedListener onItemCheckedChangedListener) {
        this.onItemCheckedChangedListener = onItemCheckedChangedListener;
    }

    public OnItemCheckedChangedListener getOnItemClickListener() {
        return onItemCheckedChangedListener;
    }

}
