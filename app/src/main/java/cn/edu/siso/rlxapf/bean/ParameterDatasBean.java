package cn.edu.siso.rlxapf.bean;

import cn.edu.siso.rlxapf.config.TCPConfig;
import cn.edu.siso.rlxapf.util.CRCUtil;

/**
 * Created by jason on 2017-7-27.
 * 参数数据解析，使用parse函数传入数据，getXXXX方法获取解析值
 */

public class ParameterDatasBean {
    private int unitCount; //单元数
    private int sampleMode; //采样模式
    private int compensationMode; //补偿模式
    private int systemCTChangeRate; //系统 CT 变比
    private int loadCTChangeRate; //负载 CT 变比
    private double objectPowerFactor; //目标功率因数
    private int harmonic3CompensationRate; //3 次谐波补偿率
    private int harmonic5CompensationRate; //5 次谐波补偿率
    private int harmonic7CompensationRate; //7 次谐波补偿率
    private int harmonic9CompensationRate; //9 次谐波补偿率
    private int harmonic11CompensationRate;//11 次谐波补偿率
    private int harmonic13CompensationRate;//13 次谐波补偿率
    private int harmonic15CompensationRate;//15 次谐波补偿率
    private int harmonic17CompensationRate;//17 次谐波补偿率
    private int harmonic19CompensationRate;//19 次谐波补偿率
    private int harmonic21CompensationRate;//21 次谐波补偿率
    private int harmonic23CompensationRate;//23 次谐波补偿率
    private int harmonic25CompensationRate;//25 次谐波补偿率
    private int harmonicEvenCompensationRate; //偶次及 26~50 次波补偿率
    private int uintRatedCapacity; //单元额定容量

    private int bytes2int(byte b1 , byte b2){
        return (int)((b1&0xff)<<8)|(b2 & 0xff);
    }

    /**
     *
     * @param bytes 网络数据
     * @return -1 CRC校验错误；  -2长度解析出错； 0解析成功
     */
    public int parse(byte[] bytes) {

        if(bytes == null || bytes.length != 45 || bytes[2] != 40)//指定参数一次读出的长度
            return -2;
        if (!CRCUtil.decode(bytes))
            return -1;

        int startLoc = 3;

        unitCount = bytes2int(bytes[startLoc],bytes[startLoc+1]);
        sampleMode = bytes2int(bytes[startLoc+2],bytes[startLoc+3]);
        compensationMode = bytes2int(bytes[startLoc+4],bytes[startLoc+5]);
        systemCTChangeRate = bytes2int(bytes[startLoc+6],bytes[startLoc+7]);
        loadCTChangeRate = bytes2int(bytes[startLoc+8],bytes[startLoc+9]);
        objectPowerFactor = (double)bytes2int(bytes[startLoc+10],bytes[startLoc+11]) * TCPConfig.ZPZO;
        harmonic3CompensationRate = bytes2int(bytes[startLoc+12],bytes[startLoc+13]);
        harmonic5CompensationRate = bytes2int(bytes[startLoc+14],bytes[startLoc+15]);
        harmonic7CompensationRate = bytes2int(bytes[startLoc+16],bytes[startLoc+17]);
        harmonic9CompensationRate = bytes2int(bytes[startLoc+18],bytes[startLoc+19]);
        harmonic11CompensationRate = bytes2int(bytes[startLoc+20],bytes[startLoc+21]);
        harmonic13CompensationRate = bytes2int(bytes[startLoc+22],bytes[startLoc+23]);
        harmonic15CompensationRate = bytes2int(bytes[startLoc+24],bytes[startLoc+25]);
        harmonic17CompensationRate = bytes2int(bytes[startLoc+26],bytes[startLoc+27]);
        harmonic19CompensationRate = bytes2int(bytes[startLoc+28],bytes[startLoc+29]);
        harmonic21CompensationRate = bytes2int(bytes[startLoc+30],bytes[startLoc+31]);
        harmonic23CompensationRate = bytes2int(bytes[startLoc+32],bytes[startLoc+33]);
        harmonic25CompensationRate = bytes2int(bytes[startLoc+34],bytes[startLoc+35]);
        harmonicEvenCompensationRate = bytes2int(bytes[startLoc+36],bytes[startLoc+37]);
        uintRatedCapacity = bytes2int(bytes[startLoc+38],bytes[startLoc+39]);

        return 0;

    }

    public void setUnitCount(int unitCount) {
        this.unitCount = unitCount;
    }

    public void setSampleMode(int sampleMode) {
        this.sampleMode = sampleMode;
    }

    public void setCompensationMode(int compensationMode) {
        this.compensationMode = compensationMode;
    }

    public void setSystemCTChangeRate(int systemCTChangeRate) {
        this.systemCTChangeRate = systemCTChangeRate;
    }

    public void setLoadCTChangeRate(int loadCTChangeRate) {
        this.loadCTChangeRate = loadCTChangeRate;
    }

    public void setObjectPowerFactor(double objectPowerFactor) {
        this.objectPowerFactor = objectPowerFactor;
    }

    public void setHarmonic3CompensationRate(int harmonic3CompensationRate) {
        this.harmonic3CompensationRate = harmonic3CompensationRate;
    }

    public void setHarmonic5CompensationRate(int harmonic5CompensationRate) {
        this.harmonic5CompensationRate = harmonic5CompensationRate;
    }

    public void setHarmonic7CompensationRate(int harmonic7CompensationRate) {
        this.harmonic7CompensationRate = harmonic7CompensationRate;
    }

    public void setHarmonic9CompensationRate(int harmonic9CompensationRate) {
        this.harmonic9CompensationRate = harmonic9CompensationRate;
    }

    public void setHarmonic11CompensationRate(int harmonic11CompensationRate) {
        this.harmonic11CompensationRate = harmonic11CompensationRate;
    }

    public void setHarmonic13CompensationRate(int harmonic13CompensationRate) {
        this.harmonic13CompensationRate = harmonic13CompensationRate;
    }

    public void setHarmonic15CompensationRate(int harmonic15CompensationRate) {
        this.harmonic15CompensationRate = harmonic15CompensationRate;
    }

    public void setHarmonic17CompensationRate(int harmonic17CompensationRate) {
        this.harmonic17CompensationRate = harmonic17CompensationRate;
    }

    public void setHarmonic19CompensationRate(int harmonic19CompensationRate) {
        this.harmonic19CompensationRate = harmonic19CompensationRate;
    }

    public void setHarmonic21CompensationRate(int harmonic21CompensationRate) {
        this.harmonic21CompensationRate = harmonic21CompensationRate;
    }

    public void setHarmonic23CompensationRate(int harmonic23CompensationRate) {
        this.harmonic23CompensationRate = harmonic23CompensationRate;
    }

    public void setHarmonic25CompensationRate(int harmonic25CompensationRate) {
        this.harmonic25CompensationRate = harmonic25CompensationRate;
    }

    public void setHarmonicEvenCompensationRate(int harmonicEvenCompensationRate) {
        this.harmonicEvenCompensationRate = harmonicEvenCompensationRate;
    }

    public void setUintRatedCapacity(int uintRatedCapacity) {
        this.uintRatedCapacity = uintRatedCapacity;
    }

    public int getUnitCount() {
        return unitCount;
    }

    public int getSampleMode() {
        return sampleMode;
    }

    public int getCompensationMode() {
        return compensationMode;
    }

    public int getSystemCTChangeRate() {
        return systemCTChangeRate;
    }

    public int getLoadCTChangeRate() {
        return loadCTChangeRate;
    }

    public double getObjectPowerFactor() {
        return objectPowerFactor;
    }

    public int getHarmonic3CompensationRate() {
        return harmonic3CompensationRate;
    }

    public int getHarmonic5CompensationRate() {
        return harmonic5CompensationRate;
    }

    public int getHarmonic7CompensationRate() {
        return harmonic7CompensationRate;
    }

    public int getHarmonic9CompensationRate() {
        return harmonic9CompensationRate;
    }

    public int getHarmonic11CompensationRate() {
        return harmonic11CompensationRate;
    }

    public int getHarmonic13CompensationRate() {
        return harmonic13CompensationRate;
    }

    public int getHarmonic15CompensationRate() {
        return harmonic15CompensationRate;
    }

    public int getHarmonic17CompensationRate() {
        return harmonic17CompensationRate;
    }

    public int getHarmonic19CompensationRate() {
        return harmonic19CompensationRate;
    }

    public int getHarmonic21CompensationRate() {
        return harmonic21CompensationRate;
    }

    public int getHarmonic23CompensationRate() {
        return harmonic23CompensationRate;
    }

    public int getHarmonic25CompensationRate() {
        return harmonic25CompensationRate;
    }

    public int getHarmonicEvenCompensationRate() {
        return harmonicEvenCompensationRate;
    }

    public int getUintRatedCapacity() {
        return uintRatedCapacity;
    }
}
