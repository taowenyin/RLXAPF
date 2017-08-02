package cn.edu.siso.rlxapf;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import cn.edu.siso.rlxapf.bean.DeviceBean;
import cn.edu.siso.rlxapf.data.viewholder.DeviceItemViewHolder;


public class DeviceListRecyclerAdapter extends RecyclerView.Adapter<DeviceItemViewHolder> {

    public static String ID_KEY = "id";
    public static String PROVINCE_KEY = "province";
    public static String CITY_KEY = "city";
    public static String COUNTY_KEY = "county";
    public static String ACCOUNT_KEY = "account";
    public static String DEVICE_TYPE_KEY = "deviceType";
    public static String GPS_DEVICE_NO_KEY = "GPSDeviceNo";
    public static String DEVICE_NO_KEY = "deviceNo";
    public static String AUTHORITY_KEY = "authority";
    public static String DELETED_KEY = "deleted";

    private List<DeviceBean> data = null;

    private Context context = null;

    private OnItemClickListener onItemClickListener = null;
    private OnOperatorClickListener onOperatorClickListener = null;

    public DeviceListRecyclerAdapter(Context context, List<DeviceBean> data) {
        super();
        this.context = context;
        this.data = data;
    }

    @Override
    public DeviceItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.device_list_item_layout, parent, false);
        return new DeviceItemViewHolder(context, view);
    }

    @Override
    public void onBindViewHolder(final DeviceItemViewHolder holder, final int position) {
        holder.setId(data.get(position).getId());
        holder.setProvince(data.get(position).getProvince());
        holder.setCity(data.get(position).getCity());
        holder.setCounty(data.get(position).getCounty());
        holder.setDeviceType(data.get(position).getDeviceType());
        holder.setGpsNo(data.get(position).getGPSDeviceNo());
        holder.setDeviceNo(data.get(position).getDeviceNo());
//        holder.setDeleted(data.get(position).getDeleted());
        holder.setDeleted(data.get(position).getOnoff());

        if (onItemClickListener != null) {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onItemClickListener.onItemClick(holder.itemView, holder.getLayoutPosition());
                }
            });
        }
        if (onOperatorClickListener != null) {
            holder.setOperatorClickListner(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onOperatorClickListener.onOperatorClick(v, holder.getLayoutPosition());
                }
            });
        }

        // 为最后一行加入下边距
        if (position == getItemCount() - 1) {
            holder.itemView.setPadding(
                    0,
                    context.getResources().getDimensionPixelSize(R.dimen.device_list_item_padding),
                    0,
                    context.getResources().getDimensionPixelSize(R.dimen.device_list_item_padding));
        }
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public void setOnOperatorClickListener(OnOperatorClickListener onOperatorClickListener) {
        this.onOperatorClickListener = onOperatorClickListener;
    }

    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }

    public interface OnOperatorClickListener {
        void onOperatorClick(View view, int position);
    }
}
