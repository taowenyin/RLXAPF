package cn.edu.siso.rlxapf.recycle.real;

import android.support.annotation.Nullable;

import java.util.List;

import eu.davidea.flexibleadapter.FlexibleAdapter;
import eu.davidea.flexibleadapter.items.AbstractFlexibleItem;

public class RealDataRecycleAdapter extends FlexibleAdapter<AbstractFlexibleItem> {

    public RealDataRecycleAdapter(@Nullable List<AbstractFlexibleItem> items) {
        super(items);
    }

}
