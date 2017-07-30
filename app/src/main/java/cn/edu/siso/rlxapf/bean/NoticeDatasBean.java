package cn.edu.siso.rlxapf.bean;

import cn.edu.siso.rlxapf.util.CRCUtil;

/**
 * Created by jason on 2017-7-27.
 * 告警巡检数据解析，使用parse函数传入数据，getXXXX方法获取解析值
 */

public class NoticeDatasBean {
    private int fpgaFault; //FPGA 故障
    private int fpgaCommunicationOutTime; //与 FPGA 通信超时
    private int fpgaCommnunicationError; //与 FPGA 通信出错
    private int storageDataFault; //存储数据出错
    private int systemOverVoltage; //系统过压
    private int systemUnderVoltage; //系统欠压
    private int threePhaseVoltageUnbalance; //三相电压不平衡
    private int outputQuickBreak; //输出速断
    private int outputOverCurrent; //输出过流
    private int unitACHighVoltage; //单元直压过高
    private int tempratureIGBTHigh; //IGBT 温度过高
    private int systemPowerDown; //系统掉电
    private int closeResistanceFault; //合预充电电阻故障
    private int closeContactorFault; //合主接触器故障
    private int openResistanceFault; //分预充电电阻故障
    private int openContactorFault; //分主接触器故障

    /**
     *
     * @param bytes 网络数据
     * @return -1 CRC校验错误；  -2长度解析出错； 0解析成功
     */
    public int parse(byte[] bytes) {

        if(bytes==null || bytes.length !=7)
            return -2;

        if (!CRCUtil.decode(bytes))
            return -1;

        if((int)(bytes[4]&0x01) == 0)
            fpgaFault = 0;
        else
            fpgaFault = 1;

        if((int)(bytes[4]&0x02) == 0)
            fpgaCommunicationOutTime = 0;
        else
            fpgaCommunicationOutTime = 1;

        if((int)(bytes[4]&0x04) == 0)
            fpgaCommnunicationError = 0;
        else
            fpgaCommnunicationError = 1;

        if((int)(bytes[4]&0x08) == 0)
            storageDataFault = 0;
        else
            storageDataFault = 1;

        if((int)(bytes[4]&0x10) == 0)
            systemOverVoltage = 0;
        else
            systemOverVoltage = 1;

        if((int)(bytes[4]&0x20) == 0)
            systemUnderVoltage = 0;
        else
            systemUnderVoltage = 1;

        if((int)(bytes[4]&0x40) == 0)
            threePhaseVoltageUnbalance = 0;
        else
            threePhaseVoltageUnbalance = 1;

        if((int)(bytes[4]&0x80) == 0)
            outputQuickBreak = 0;
        else
            outputQuickBreak = 1;

////////////////////////////////////////////////////////
        if((int)(bytes[3]&0x01) == 0)
            outputOverCurrent = 0;
        else
            outputOverCurrent = 1;
//
        if((int)(bytes[3]&0x02) == 0)
            unitACHighVoltage = 0;
        else
            unitACHighVoltage = 1;
//
        if((int)(bytes[3]&0x04) == 0)
            tempratureIGBTHigh = 0;
        else
            tempratureIGBTHigh = 1;
//
        if((int)(bytes[3]&0x08) == 0)
            systemPowerDown = 0;
        else
            systemPowerDown = 1;
//
        if((int)(bytes[3]&0x10) == 0)
            closeResistanceFault = 0;
        else
            closeResistanceFault = 1;
//
        if((int)(bytes[3]&0x20) == 0)
            closeContactorFault = 0;
        else
            closeContactorFault = 1;
//
        if((int)(bytes[3]&0x40) == 0)
            openResistanceFault = 0;
        else
            openResistanceFault = 1;

        if((int)(bytes[3]&0x80) == 0)
            openContactorFault = 0;
        else
            openContactorFault = 1;



        return 0;
    }

    public int getFpgaFault() {
        return fpgaFault;
    }

    public int getFpgaCommunicationOutTime() {
        return fpgaCommunicationOutTime;
    }

    public int getFpgaCommnunicationError() {
        return fpgaCommnunicationError;
    }

    public int getStorageDataFault() {
        return storageDataFault;
    }

    public int getSystemOverVoltage() {
        return systemOverVoltage;
    }

    public int getSystemUnderVoltage() {
        return systemUnderVoltage;
    }

    public int getThreePhaseVoltageUnbalance() {
        return threePhaseVoltageUnbalance;
    }

    public int getOutputQuickBreak() {
        return outputQuickBreak;
    }

    public int getOutputOverCurrent() {
        return outputOverCurrent;
    }

    public int getUnitACHighVoltage() {
        return unitACHighVoltage;
    }

    public int getTempratureIGBTHigh() {
        return tempratureIGBTHigh;
    }

    public int getSystemPowerDown() {
        return systemPowerDown;
    }

    public int getCloseResistanceFault() {
        return closeResistanceFault;
    }

    public int getCloseContactorFault() {
        return closeContactorFault;
    }

    public int getOpenResistanceFault() {
        return openResistanceFault;
    }

    public int getOpenContactorFault() {
        return openContactorFault;
    }
}
