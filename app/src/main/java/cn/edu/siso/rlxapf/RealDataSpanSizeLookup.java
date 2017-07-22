package cn.edu.siso.rlxapf;

import android.support.v7.widget.GridLayoutManager;

import com.truizlop.sectionedrecyclerview.SectionedRecyclerViewAdapter;

public class RealDataSpanSizeLookup extends GridLayoutManager.SpanSizeLookup {
    protected SectionedRecyclerViewAdapter<?, ?, ?> adapter = null;
    protected GridLayoutManager layoutManager = null;

    public RealDataSpanSizeLookup(SectionedRecyclerViewAdapter<?, ?, ?> adapter, GridLayoutManager layoutManager) {
        this.adapter = adapter;
        this.layoutManager = layoutManager;
    }

    @Override
    public int getSpanSize(int position) {
        // -10 因为最后10个数据需要一行一数据，
        if(adapter.isSectionHeaderPosition(position) ||
                adapter.isSectionFooterPosition(position) ||
                position >= (adapter.getItemCount() - 10)){
            return layoutManager.getSpanCount();
        }else {
            return 1;
        }
    }
}
