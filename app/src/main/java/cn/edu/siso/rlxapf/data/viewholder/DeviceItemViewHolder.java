package cn.edu.siso.rlxapf.data.viewholder;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import cn.edu.siso.rlxapf.R;

public class DeviceItemViewHolder extends RecyclerView.ViewHolder {

    private TextView province = null;
    private TextView city = null;
    private TextView county = null;
    private TextView deviceType = null;
    private TextView gpsNo = null;
    private TextView deviceNo = null;
    private TextView deleted = null;

    private ImageButton deviceOperator = null;

    private Context context = null;

    public DeviceItemViewHolder(Context context, View itemView) {
        super(itemView);

        this.province = (TextView) itemView.findViewById(R.id.device_province);
        this.city = (TextView) itemView.findViewById(R.id.device_city);
        this.county = (TextView) itemView.findViewById(R.id.device_county);
        this.deviceType = (TextView) itemView.findViewById(R.id.device_type);
        this.gpsNo = (TextView) itemView.findViewById(R.id.device_gps_no);
        this.deviceNo = (TextView) itemView.findViewById(R.id.device_no);
        this.deviceOperator = (ImageButton) itemView.findViewById(R.id.device_operator);

        this.context = context;
    }

    public void setProvince(String province) {
        this.province.setText(province);
    }

    public void setCity(String city) {
        this.city.setText(city);
    }

    public void setCounty(String county) {
        this.county.setText(county);
    }

    public void setDeviceType(String deviceType) {
        this.deviceType.setText(deviceType);
    }

    public void setGpsNo(String gpsNo) {
        this.gpsNo.setText(gpsNo);
    }

    public void setDeviceNo(String deviceNo) {
        this.deviceNo.setText(deviceNo);
    }

    public void setOperatorClickListner(View.OnClickListener onClickListener) {
        this.deviceOperator.setOnClickListener(onClickListener);
    }
}
