package cn.edu.siso.rlxapf;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import cn.edu.siso.rlxapf.bean.NoticeDatasBean;
import cn.edu.siso.rlxapf.data.viewholder.WarningItemViewHolder;

public class WarningListRecyclerAdapter extends RecyclerView.Adapter<WarningItemViewHolder> {

    private List<NoticeDatasBean> data = null;

    private Context context = null;

    public WarningListRecyclerAdapter(Context context, List<NoticeDatasBean> data) {
        super();

        this.context = context;
        this.data = data;
    }

    @Override
    public WarningItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.device_warning_list_item_layout, parent, false);
        return new WarningItemViewHolder(context, view);
    }

    @Override
    public void onBindViewHolder(WarningItemViewHolder holder, int position) {
        holder.setCloseContactorFault(data.get(position).getCloseContactorFault());
        holder.setCloseResistanceFault(data.get(position).getCloseResistanceFault());
        holder.setFpgaCommnunicationError(data.get(position).getFpgaCommnunicationError());
        holder.setFpgaCommunicationOutTime(data.get(position).getFpgaCommunicationOutTime());
        holder.setFpgaFault(data.get(position).getFpgaFault());
        holder.setOpenContactorFault(data.get(position).getOpenContactorFault());
        holder.setOpenResistanceFault(data.get(position).getOpenResistanceFault());
        holder.setOutputOverCurrent(data.get(position).getOutputOverCurrent());
        holder.setOutputQuickBreak(data.get(position).getOutputQuickBreak());
        holder.setStorageDataFault(data.get(position).getStorageDataFault());
        holder.setSystemOverVoltage(data.get(position).getSystemOverVoltage());
        holder.setSystemPowerDown(data.get(position).getSystemPowerDown());
        holder.setSystemUnderVoltage(data.get(position).getSystemUnderVoltage());
        holder.setTempratureIGBTHigh(data.get(position).getTempratureIGBTHigh());
        holder.setThreePhaseVoltageUnbalance(data.get(position).getThreePhaseVoltageUnbalance());
        holder.setUnitACHighVoltage(data.get(position).getUnitACHighVoltage());

        SimpleDateFormat sDateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss", Locale.CHINA);
        holder.setWarningTime(sDateFormat.format(new Date()));
    }

    @Override
    public int getItemCount() {
        return data.size();
    }
}
