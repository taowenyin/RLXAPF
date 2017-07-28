package cn.edu.siso.rlxapf.bean;

import cn.edu.siso.rlxapf.config.TCPConfig;
import cn.edu.siso.rlxapf.util.CRCUtil;

/**
 * Created by jason on 2017-7-26.
 * 实时数据解析，使用parse函数传入数据，getXXXX方法获取解析值
 */

public class RealTimeDatasBean {
    private double systemAVoltage; //系统A相电压
    private double systemBVoltage; //系统B相电压
    private double systemCVoltage; //系统C相电压
    private int systemACurrent; //系统A相电流
    private int systemBCurrent; //系统B相电流
    private int systemCCurrent; //系统C相电流
    private int loadACurrent; //负载A相电流
    private int loadBCurrent; //负载B相电流
    private int loadCCurrent; //负载C相电流
    private double compensationACurrent; //补偿A相电流
    private double compensationBCurrent; //补偿B相电流
    private double compensationCCurrent; //补偿C相电流
    private double voltageATotalDistortionRate; //A相电压总畸变率
    private double voltageBTotalDistortionRate; //B相电压总畸变率
    private double voltageCTotalDistortionRate; //C相电压总畸变率
    private double systemCurrentATotalDistortionRate; //A相系统电流总畸变率
    private double systemCurrentBTotalDistortionRate; //B相系统电流总畸变率
    private double systemCurrentCTotalDistortionRate; //C相系统电流总畸变率
    private double loadCurrentATotalDistortionRate; //A相负载电流总畸变率
    private double loadCurrentBTotalDistortionRate; //B相负载电流总畸变率
    private double loadCurrentCTotalDistortionRate; //C相负载电流总畸变率
    private double systemPowerFactor; //系统功率因数
    private double loadPowerFactor; //负载功率因数
    private double systemCurrentUnbalanceDegree; //系统电流不平衡度
    private double loadCurrentUnbalanceDegree; //负载电流不平衡度
    private int tempratureIGBT; //IGBT 温度
    private int stateAPF; //APF 运行状态
    private int voltageDC1; //直流电压 1
    private int voltageDC2; //直流电压 2
    private int version; //软件版本号

    /**
     * 低位高字节，高位低字节
     * @param b1
     * @param b2
     * @return
     */
    private int bytes2int(byte b1 , byte b2){
        return (int)(b1<<8|b2);
    }

    /**
     *
     * @param bytes 网络数据
     * @return -1 CRC校验错误；  -2长度解析出错； 0解析成功
     */
    public int parse(byte[] bytes){
        if(!CRCUtil.decode(bytes))
            return -1;
        if(bytes.length - 2 - 3 < bytes[2])
            return -2;

        int startLoc = 3;

        systemAVoltage = (double)bytes2int(bytes[startLoc],bytes[startLoc+1]) * TCPConfig.ZPO;
        systemBVoltage = (double)bytes2int(bytes[startLoc+2],bytes[startLoc+3]) * TCPConfig.ZPO;
        systemCVoltage = (double)bytes2int(bytes[startLoc+4],bytes[startLoc+5]) * TCPConfig.ZPO;
        systemACurrent = bytes2int(bytes[startLoc+6],bytes[startLoc+7]);
        systemBCurrent = bytes2int(bytes[startLoc+8],bytes[startLoc+9]);
        systemCCurrent = bytes2int(bytes[startLoc+10],bytes[startLoc+11]);
        loadACurrent = bytes2int(bytes[startLoc+12],bytes[startLoc+13]);
        loadBCurrent = bytes2int(bytes[startLoc+14],bytes[startLoc+15]);
        loadCCurrent = bytes2int(bytes[startLoc+16],bytes[startLoc+17]);
        compensationACurrent = (double)bytes2int(bytes[startLoc+18],bytes[startLoc+19]) * TCPConfig.ZPO;
        compensationBCurrent = (double)bytes2int(bytes[startLoc+20],bytes[startLoc+21]) * TCPConfig.ZPO;
        compensationCCurrent = (double)bytes2int(bytes[startLoc+22],bytes[startLoc+23]) * TCPConfig.ZPO;
        voltageATotalDistortionRate = (double)bytes2int(bytes[startLoc+24],bytes[startLoc+25]) * TCPConfig.ZPZZO;
        voltageBTotalDistortionRate = (double)bytes2int(bytes[startLoc+26],bytes[startLoc+27]) * TCPConfig.ZPZZO;
        voltageCTotalDistortionRate = (double)bytes2int(bytes[startLoc+28],bytes[startLoc+29]) * TCPConfig.ZPZZO;
        systemCurrentATotalDistortionRate = (double)bytes2int(bytes[startLoc+30],bytes[startLoc+31]) * TCPConfig.ZPZZO;
        systemCurrentBTotalDistortionRate = (double)bytes2int(bytes[startLoc+32],bytes[startLoc+33]) * TCPConfig.ZPZZO;
        systemCurrentCTotalDistortionRate = (double)bytes2int(bytes[startLoc+34],bytes[startLoc+35]) * TCPConfig.ZPZZO;
        loadCurrentATotalDistortionRate = (double)bytes2int(bytes[startLoc+36],bytes[startLoc+37]) * TCPConfig.ZPZZO;
        loadCurrentBTotalDistortionRate = (double)bytes2int(bytes[startLoc+38],bytes[startLoc+39]) * TCPConfig.ZPZZO;
        loadCurrentCTotalDistortionRate = (double)bytes2int(bytes[startLoc+40],bytes[startLoc+41]) * TCPConfig.ZPZZO;
        systemPowerFactor = (double)bytes2int(bytes[startLoc+42],bytes[startLoc+43]) * TCPConfig.ZPZZO;
        loadPowerFactor = (double)bytes2int(bytes[startLoc+44],bytes[startLoc+45]) * TCPConfig.ZPZZO;
        systemCurrentUnbalanceDegree = (double)bytes2int(bytes[startLoc+46],bytes[startLoc+47]) * TCPConfig.ZPO;
        loadCurrentUnbalanceDegree = (double)bytes2int(bytes[startLoc+48],bytes[startLoc+49]) * TCPConfig.ZPO;
        tempratureIGBT = bytes2int(bytes[startLoc+50],bytes[startLoc+51]);
        stateAPF = bytes2int(bytes[startLoc+52],bytes[startLoc+53]);
        voltageDC1 = bytes2int(bytes[startLoc+54],bytes[startLoc+55]);
        voltageDC2 = bytes2int(bytes[startLoc+56],bytes[startLoc+57]);
        version = bytes2int(bytes[startLoc+58],bytes[startLoc+59]);
        return 0;
    }

    public double getSystemAVoltage() {
        return systemAVoltage;
    }

    public double getSystemBVoltage() {
        return systemBVoltage;
    }

    public double getSystemCVoltage() {
        return systemCVoltage;
    }

    public int getSystemACurrent() {
        return systemACurrent;
    }

    public int getSystemBCurrent() {
        return systemBCurrent;
    }

    public int getSystemCCurrent() {
        return systemCCurrent;
    }

    public int getLoadACurrent() {
        return loadACurrent;
    }

    public int getLoadBCurrent() {
        return loadBCurrent;
    }

    public int getLoadCCurrent() {
        return loadCCurrent;
    }

    public double getCompensationACurrent() {
        return compensationACurrent;
    }

    public double getCompensationBCurrent() {
        return compensationBCurrent;
    }

    public double getCompensationCCurrent() {
        return compensationCCurrent;
    }

    public double getVoltageATotalDistortionRate() {
        return voltageATotalDistortionRate;
    }

    public double getVoltageBTotalDistortionRate() {
        return voltageBTotalDistortionRate;
    }

    public double getVoltageCTotalDistortionRate() {
        return voltageCTotalDistortionRate;
    }

    public double getSystemCurrentATotalDistortionRate() {
        return systemCurrentATotalDistortionRate;
    }

    public double getSystemCurrentBTotalDistortionRate() {
        return systemCurrentBTotalDistortionRate;
    }

    public double getSystemCurrentCTotalDistortionRate() {
        return systemCurrentCTotalDistortionRate;
    }

    public double getLoadCurrentATotalDistortionRate() {
        return loadCurrentATotalDistortionRate;
    }

    public double getLoadCurrentBTotalDistortionRate() {
        return loadCurrentBTotalDistortionRate;
    }

    public double getLoadCurrentCTotalDistortionRate() {
        return loadCurrentCTotalDistortionRate;
    }

    public double getSystemPowerFactor() {
        return systemPowerFactor;
    }

    public double getLoadPowerFactor() {
        return loadPowerFactor;
    }

    public double getSystemCurrentUnbalanceDegree() {
        return systemCurrentUnbalanceDegree;
    }

    public double getLoadCurrentUnbalanceDegree() {
        return loadCurrentUnbalanceDegree;
    }

    public int getTempratureIGBT() {
        return tempratureIGBT;
    }

    public int getStateAPF() {
        return stateAPF;
    }

    public int getVoltageDC1() {
        return voltageDC1;
    }

    public int getVoltageDC2() {
        return voltageDC2;
    }

    public int getVersion() {
        return version;
    }
}
