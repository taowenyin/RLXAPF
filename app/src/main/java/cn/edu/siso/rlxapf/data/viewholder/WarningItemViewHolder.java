package cn.edu.siso.rlxapf.data.viewholder;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import cn.edu.siso.rlxapf.R;


public class WarningItemViewHolder extends RecyclerView.ViewHolder {

    private TextView isFpgaFault = null; //FPGA是否故障
    private TextView isFpgaCommunicationOutTime = null; //与 FPGA是否通信超时
    private TextView isFpgaCommnunicationError = null; //与 FPGA是否通信出错
    private TextView isStorageDataFault = null; //存储数据是否出错
    private TextView isSystemOverVoltage = null; //系统是否过压
    private TextView isSystemUnderVoltage = null; //系统是否欠压
    private TextView isThreePhaseVoltageUnbalance = null; //三相电压是否不平衡
    private TextView isOutputQuickBreak = null; //输出是否速断
    private TextView isOutputOverCurrent = null; //输出是否过流
    private TextView isUnitACHighVoltage = null; //单元直压是否过高
    private TextView isTempratureIGBTHigh = null; //IGBT是否温度过高
    private TextView isSystemPowerDown = null; //系统是否掉电
    private TextView isCloseResistanceFault = null; //合预充电电阻是否故障
    private TextView isCloseContactorFault = null; //合主接触器是否故障
    private TextView isOpenResistanceFault = null; //分预充电电阻是否故障
    private TextView isOpenContactorFault = null; //分主接触器是否故障
    private TextView warningTime = null; // 告警时间

    private Context context = null;

    public WarningItemViewHolder(Context context, View itemView) {
        super(itemView);

        this.isFpgaFault = (TextView) itemView.findViewById(R.id.warning_fpga_fault);
        this.isFpgaCommunicationOutTime = (TextView) itemView.findViewById(R.id.warning_fpga_communication_outTime);
        this.isFpgaCommnunicationError = (TextView) itemView.findViewById(R.id.warning_fpga_communication_error);
        this.isStorageDataFault = (TextView) itemView.findViewById(R.id.warning_storage_data_fault);
        this.isSystemOverVoltage = (TextView) itemView.findViewById(R.id.warning_system_over_voltage);
        this.isSystemUnderVoltage = (TextView) itemView.findViewById(R.id.warning_system_under_voltage);
        this.isThreePhaseVoltageUnbalance = (TextView) itemView.findViewById(R.id.warning_three_phase_voltage_unbalance);
        this.isOutputQuickBreak = (TextView) itemView.findViewById(R.id.warning_output_quick_break);
        this.isOutputOverCurrent = (TextView) itemView.findViewById(R.id.warning_output_over_current);
        this.isUnitACHighVoltage = (TextView) itemView.findViewById(R.id.warning_unit_ac_high_voltage);
        this.isTempratureIGBTHigh = (TextView) itemView.findViewById(R.id.warning_temprature_igbt_high);
        this.isSystemPowerDown = (TextView) itemView.findViewById(R.id.warning_system_power_down);
        this.isCloseResistanceFault = (TextView) itemView.findViewById(R.id.warning_close_resistance_fault);
        this.isCloseContactorFault = (TextView) itemView.findViewById(R.id.warning_close_contactor_fault);
        this.isOpenResistanceFault = (TextView) itemView.findViewById(R.id.warning_open_resistance_fault);
        this.isOpenContactorFault = (TextView) itemView.findViewById(R.id.warning_open_contactor_fault);
        this.warningTime = (TextView) itemView.findViewById(R.id.warning_time);

        this.context = context;
    }

    public void setFpgaFault(int fpgaFault) {
        if (fpgaFault == 0) {
            isFpgaFault.setTextColor(Color.GREEN);
            isFpgaFault.setText(context.getResources().getString(R.string.device_warning_ok));
        } else {
            isFpgaFault.setTextColor(Color.RED);
            isFpgaFault.setText(context.getResources().getString(R.string.device_warning_fail));
        }
    }

    public void setFpgaCommunicationOutTime(int fpgaCommunicationOutTime) {
        if (fpgaCommunicationOutTime == 0) {
            isFpgaCommunicationOutTime.setTextColor(Color.GREEN);
            isFpgaCommunicationOutTime.setText(context.getResources().getString(R.string.device_warning_ok));
        } else {
            isFpgaCommunicationOutTime.setTextColor(Color.RED);
            isFpgaCommunicationOutTime.setText(context.getResources().getString(R.string.device_warning_fail));
        }
    }

    public void setFpgaCommnunicationError(int fpgaCommnunicationError) {
        if (fpgaCommnunicationError == 0) {
            isFpgaCommnunicationError.setTextColor(Color.GREEN);
            isFpgaCommnunicationError.setText(context.getResources().getString(R.string.device_warning_ok));
        } else {
            isFpgaCommnunicationError.setTextColor(Color.RED);
            isFpgaCommnunicationError.setText(context.getResources().getString(R.string.device_warning_fail));
        }
    }

    public void setStorageDataFault(int storageDataFault) {
        if (storageDataFault == 0) {
            isStorageDataFault.setTextColor(Color.GREEN);
            isStorageDataFault.setText(context.getResources().getString(R.string.device_warning_ok));
        } else {
            isStorageDataFault.setTextColor(Color.RED);
            isStorageDataFault.setText(context.getResources().getString(R.string.device_warning_fail));
        }
    }

    public void setSystemOverVoltage(int systemOverVoltage) {
        if (systemOverVoltage == 0) {
            isSystemOverVoltage.setTextColor(Color.GREEN);
            isSystemOverVoltage.setText(context.getResources().getString(R.string.device_warning_ok));
        } else {
            isSystemOverVoltage.setTextColor(Color.RED);
            isSystemOverVoltage.setText(context.getResources().getString(R.string.device_warning_fail));
        }
    }

    public void setSystemUnderVoltage(int systemUnderVoltage) {
        if (systemUnderVoltage == 0) {
            isSystemUnderVoltage.setTextColor(Color.GREEN);
            isSystemUnderVoltage.setText(context.getResources().getString(R.string.device_warning_ok));
        } else {
            isSystemUnderVoltage.setTextColor(Color.RED);
            isSystemUnderVoltage.setText(context.getResources().getString(R.string.device_warning_fail));
        }
    }

    public void setThreePhaseVoltageUnbalance(int threePhaseVoltageUnbalance) {
        if (threePhaseVoltageUnbalance == 0) {
            isThreePhaseVoltageUnbalance.setTextColor(Color.GREEN);
            isThreePhaseVoltageUnbalance.setText(context.getResources().getString(R.string.device_warning_ok));
        } else {
            isThreePhaseVoltageUnbalance.setTextColor(Color.RED);
            isThreePhaseVoltageUnbalance.setText(context.getResources().getString(R.string.device_warning_fail));
        }
    }

    public void setOutputQuickBreak(int outputQuickBreak) {
        if (outputQuickBreak == 0) {
            isOutputQuickBreak.setTextColor(Color.GREEN);
            isOutputQuickBreak.setText(context.getResources().getString(R.string.device_warning_ok));
        } else {
            isOutputQuickBreak.setTextColor(Color.RED);
            isOutputQuickBreak.setText(context.getResources().getString(R.string.device_warning_fail));
        }
    }

    public void setOutputOverCurrent(int outputOverCurrent) {
        if (outputOverCurrent == 0) {
            isOutputOverCurrent.setTextColor(Color.GREEN);
            isOutputOverCurrent.setText(context.getResources().getString(R.string.device_warning_ok));
        } else {
            isOutputOverCurrent.setTextColor(Color.RED);
            isOutputOverCurrent.setText(context.getResources().getString(R.string.device_warning_fail));
        }
    }

    public void setUnitACHighVoltage(int unitACHighVoltage) {
        if (unitACHighVoltage == 0) {
            isUnitACHighVoltage.setTextColor(Color.GREEN);
            isUnitACHighVoltage.setText(context.getResources().getString(R.string.device_warning_ok));
        } else {
            isUnitACHighVoltage.setTextColor(Color.RED);
            isUnitACHighVoltage.setText(context.getResources().getString(R.string.device_warning_fail));
        }
    }

    public void setTempratureIGBTHigh(int tempratureIGBTHigh) {
        if (tempratureIGBTHigh == 0) {
            isTempratureIGBTHigh.setTextColor(Color.GREEN);
            isTempratureIGBTHigh.setText(context.getResources().getString(R.string.device_warning_ok));
        } else {
            isTempratureIGBTHigh.setTextColor(Color.RED);
            isTempratureIGBTHigh.setText(context.getResources().getString(R.string.device_warning_fail));
        }
    }

    public void setSystemPowerDown(int systemPowerDown) {
        if (systemPowerDown == 0) {
            isSystemPowerDown.setTextColor(Color.GREEN);
            isSystemPowerDown.setText(context.getResources().getString(R.string.device_warning_ok));
        } else {
            isSystemPowerDown.setTextColor(Color.RED);
            isSystemPowerDown.setText(context.getResources().getString(R.string.device_warning_fail));
        }
    }

    public void setCloseResistanceFault(int closeResistanceFault) {
        if (closeResistanceFault == 0) {
            isCloseResistanceFault.setTextColor(Color.GREEN);
            isCloseResistanceFault.setText(context.getResources().getString(R.string.device_warning_ok));
        } else {
            isCloseResistanceFault.setTextColor(Color.RED);
            isCloseResistanceFault.setText(context.getResources().getString(R.string.device_warning_fail));
        }
    }

    public void setCloseContactorFault(int closeContactorFault) {
        if (closeContactorFault == 0) {
            isCloseContactorFault.setTextColor(Color.GREEN);
            isCloseContactorFault.setText(context.getResources().getString(R.string.device_warning_ok));
        } else {
            isCloseContactorFault.setTextColor(Color.RED);
            isCloseContactorFault.setText(context.getResources().getString(R.string.device_warning_fail));
        }
    }

    public void setOpenResistanceFault(int openResistanceFault) {
        if (openResistanceFault == 0) {
            isOpenResistanceFault.setTextColor(Color.GREEN);
            isOpenResistanceFault.setText(context.getResources().getString(R.string.device_warning_ok));
        } else {
            isOpenResistanceFault.setTextColor(Color.RED);
            isOpenResistanceFault.setText(context.getResources().getString(R.string.device_warning_fail));
        }
    }

    public void setOpenContactorFault(int openContactorFault) {
        if (openContactorFault == 0) {
            isOpenContactorFault.setTextColor(Color.GREEN);
            isOpenContactorFault.setText(context.getResources().getString(R.string.device_warning_ok));
        } else {
            isOpenContactorFault.setTextColor(Color.RED);
            isOpenContactorFault.setText(context.getResources().getString(R.string.device_warning_fail));
        }
    }

    public void setWarningTime(String warningTimeStr) {
        warningTime.setText(warningTimeStr);
    }
}
