package cn.edu.siso.rlxapf.data.viewholder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import cn.edu.siso.rlxapf.R;

public class DataSectionViewHolder extends RecyclerView.ViewHolder {

    private TextView sectionView = null;

    public DataSectionViewHolder(View itemView) {
        super(itemView);

        sectionView = (TextView) itemView.findViewById(R.id.title);
    }

    public void setSection(String title) {
        this.sectionView.setText(title);
    }
}
