package cn.edu.siso.rlxapf.data.viewholder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import cn.edu.siso.rlxapf.R;

public class DataItemViewHolder extends RecyclerView.ViewHolder {

    private TextView titleView = null;
    private TextView dataView = null;

    public DataItemViewHolder(View itemView) {
        super(itemView);

        titleView = (TextView) itemView.findViewById(R.id.data_title);
        dataView = (TextView) itemView.findViewById(R.id.data_data);
    }

    public void setTitle(String title) {
        this.titleView.setText(title);
    }

    public void setValue(String value) {
        this.dataView.setText(value);
    }
}
