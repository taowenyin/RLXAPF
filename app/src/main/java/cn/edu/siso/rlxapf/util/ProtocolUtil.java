package cn.edu.siso.rlxapf.util;

import java.util.Arrays;

import cn.edu.siso.rlxapf.bean.RealTimeDatasBean;

/**
 * Created by jason on 2017-7-25.
 * 生成发送请求协议
 */

public class ProtocolUtil {
    /**
     * 广播启动所有设备
     * @return 发送字节数组
     */
    public static byte[] startDevice(){
        byte[] bytes = {0x7F,0x05,0x00,0x05,0x00,0x01,0x00,0x00};
        byte[] crcBytes = CRCUtil.encode(bytes);
        return crcBytes;
    }
    /**
     * 广播停止所有设备
     * @return 发送字节数组
     */
    public static byte[] stopDevice(){
        byte[] bytes = {0x7F,0x05,0x00,0x04,0x00,0x01,0x00,0x00};
        byte[] crcBytes = CRCUtil.encode(bytes);
        return crcBytes;
    }
    /**
     * 广播所有设备配置
     * @return 发送字节数组
     */
    public static byte[] readParams(){
        byte[] bytes = {0x7F,0x05,0x00,0x03,0x00,0x01,0x00,0x00};
        byte[] crcBytes = CRCUtil.encode(bytes);
        return crcBytes;
    }
    /**
     * 广播恢复出厂所有参数
     * @return 发送字节数组
     */
    public static byte[] loadDefaultParams(){
        byte[] bytes = {0x7F,0x05,0x00,0x02,0x00,0x01,0x00,0x00};
        byte[] crcBytes = CRCUtil.encode(bytes);
        return crcBytes;
    }

    /**
     * 广播保存所有配置
     * @return 发送字节数组
     */
    public static byte[] saveParams(){
        byte[] bytes = {0x7F,0x05,0x00,0x01,0x00,0x01,0x00,0x00};
        byte[] crcBytes = CRCUtil.encode(bytes);
        return crcBytes;
    }

    /**
     * 启动设备
     * @param deviceAddr 设备地址
     * @return
     */
    public static byte[] startDevice(byte deviceAddr){
        byte[] bytes = {deviceAddr,0x05,0x00,0x05,0x00,0x01,0x00,0x00};
        byte[] crcBytes = CRCUtil.encode(bytes);
        return crcBytes;
    }
    /**
     * 停止设备
     * @param deviceAddr 设备地址
     * @return
     */
    public static byte[] stopDevice(byte deviceAddr){
        byte[] bytes = {deviceAddr,0x05,0x00,0x04,0x00,0x01,0x00,0x00};
        byte[] crcBytes = CRCUtil.encode(bytes);
        return crcBytes;
    }
    /**
     * 导出配置
     * @param deviceAddr 设备地址
     * @return
     */
    public static byte[] readParams(byte deviceAddr){
        byte[] bytes = {deviceAddr,0x05,0x00,0x03,0x00,0x01,0x00,0x00};
        byte[] crcBytes = CRCUtil.encode(bytes);
        return crcBytes;
    }
    /**
     * 恢复出厂参数
     * @param deviceAddr 设备地址
     * @return
     */
    public static byte[] loadDefaultParams(byte deviceAddr){
        byte[] bytes = {deviceAddr,0x05,0x00,0x02,0x00,0x01,0x00,0x00};
        byte[] crcBytes = CRCUtil.encode(bytes);
        return crcBytes;
    }
    /**
     * 保存配置
     * @param deviceAddr 设备地址
     * @return
     */
    public static byte[] saveParams(byte deviceAddr){
        byte[] bytes = {deviceAddr,0x05,0x00,0x01,0x00,0x01,0x00,0x00};
        byte[] crcBytes = CRCUtil.encode(bytes);
        return crcBytes;
    }
    /**
     * 获取实时数据协议
     * @param deviceAddr 设备地址
     * @return
     */
    public static byte[] readRealTimeDatas(byte deviceAddr){
        byte[] bytes = {deviceAddr,0x04,0x00,0x00,0x00,0x1E,0x00,0x00};
        byte[] crcBytes = CRCUtil.encode(bytes);
        return crcBytes;
    }
    public static byte[] readHarmonicSpectrum(byte deviceAddr){
        byte[] bytes = {deviceAddr,0x04,0x00,0x2E,0x00,0x4B,0x00,0x00};
        byte[] crcBytes = CRCUtil.encode(bytes);
        return crcBytes;
    }
    /**
     * 获取参数协议
     * @param deviceAddr 设备地址
     * @return
     */
    public static byte[] readParamsDatas(byte deviceAddr){
        byte[] bytes = {deviceAddr,0x03,0x00,0x00,0x00,0x14,0x00,0x00};
        byte[] crcBytes = CRCUtil.encode(bytes);
        return crcBytes;
    }
    /**
     * 写入参数数据协议
     * @param deviceAddr 设备地址
     * @return
     */
    public static byte[] writeParamsDatas(byte deviceAddr,byte regAddrH,byte regAddrL,byte wdataH,byte wdataL){
        byte[] bytes = {deviceAddr,0x06,regAddrH,regAddrL,wdataH,wdataL,0x00,0x00};
        byte[] crcBytes = CRCUtil.encode(bytes);
        return crcBytes;
    }
    public static byte[] writeParamsUnitCount(byte deviceAddr, int unitCount){
        byte dataH = (byte)((unitCount >> 8)&0xFF);
        byte dataL = (byte)(unitCount&0xFF);
        byte regH = 0x00;
        byte regL = 0x00;
        return ProtocolUtil.writeParamsDatas(deviceAddr,regH,regL,dataH,dataL);
    }
    public static byte[] writeParamsSampleMode(byte deviceAddr, int sampleMode){
        byte dataH = (byte)((sampleMode >> 8)&0xFF);
        byte dataL = (byte)(sampleMode&0xFF);
        byte regH = 0x00;
        byte regL = 0x01;
        return ProtocolUtil.writeParamsDatas(deviceAddr,regH,regL,dataH,dataL);
    }
    public static byte[] writeParamsCompensationMode(byte deviceAddr, int compensationMode){
        byte dataH = (byte)((compensationMode >> 8)&0xFF);
        byte dataL = (byte)(compensationMode&0xFF);
        byte regH = 0x00;
        byte regL = 0x02;
        return ProtocolUtil.writeParamsDatas(deviceAddr,regH,regL,dataH,dataL);
    }

    public static byte[] writeParamsSystemCTChangeRate(byte deviceAddr, int systemCTChangeRate){
        byte dataH = (byte)((systemCTChangeRate >> 8)&0xFF);
        byte dataL = (byte)(systemCTChangeRate&0xFF);
        byte regH = 0x00;
        byte regL = 0x03;
        return ProtocolUtil.writeParamsDatas(deviceAddr,regH,regL,dataH,dataL);
    }
    public static byte[] writeParamsLoadCTChangeRate(byte deviceAddr, int loadCTChangeRate){
        byte dataH = (byte)((loadCTChangeRate >> 8)&0xFF);
        byte dataL = (byte)(loadCTChangeRate&0xFF);
        byte regH = 0x00;
        byte regL = 0x04;
        return ProtocolUtil.writeParamsDatas(deviceAddr,regH,regL,dataH,dataL);
    }

    public static byte[] writeParamsObjectPowerFactor(byte deviceAddr, double objectPowerFactor){
        int opfInt = (int)(objectPowerFactor * 100);
        byte dataH = (byte)((opfInt >> 8)&0xFF);
        byte dataL = (byte)(opfInt & 0xFF);
        byte regH = 0x00;
        byte regL = 0x05;
        return ProtocolUtil.writeParamsDatas(deviceAddr,regH,regL,dataH,dataL);
    }

    public static byte[] writeParamsHarmonic3CompensationRate(byte deviceAddr, int harmonic3CompensationRate){
        byte dataH = (byte)((harmonic3CompensationRate >> 8)&0xFF);
        byte dataL = (byte)(harmonic3CompensationRate&0xFF);
        byte regH = 0x00;
        byte regL = 0x06;
        return ProtocolUtil.writeParamsDatas(deviceAddr,regH,regL,dataH,dataL);
    }

    public static byte[] writeParamsHarmonic5CompensationRate(byte deviceAddr, int harmonic5CompensationRate){
        byte dataH = (byte)((harmonic5CompensationRate >> 8)&0xFF);
        byte dataL = (byte)(harmonic5CompensationRate&0xFF);
        byte regH = 0x00;
        byte regL = 0x07;
        return ProtocolUtil.writeParamsDatas(deviceAddr,regH,regL,dataH,dataL);
    }

    public static byte[] writeParamsHarmonic7CompensationRate(byte deviceAddr, int harmonic7CompensationRate){
        byte dataH = (byte)((harmonic7CompensationRate >> 8)&0xFF);
        byte dataL = (byte)(harmonic7CompensationRate&0xFF);
        byte regH = 0x00;
        byte regL = 0x08;
        return ProtocolUtil.writeParamsDatas(deviceAddr,regH,regL,dataH,dataL);
    }

    public static byte[] writeParamsHarmonic9CompensationRate(byte deviceAddr, int harmonic9CompensationRate){
        byte dataH = (byte)((harmonic9CompensationRate >> 8)&0xFF);
        byte dataL = (byte)(harmonic9CompensationRate&0xFF);
        byte regH = 0x00;
        byte regL = 0x09;
        return ProtocolUtil.writeParamsDatas(deviceAddr,regH,regL,dataH,dataL);
    }

    public static byte[] writeParamsHarmonic11CompensationRate(byte deviceAddr, int harmonic11CompensationRate){
        byte dataH = (byte)((harmonic11CompensationRate >> 8)&0xFF);
        byte dataL = (byte)(harmonic11CompensationRate&0xFF);
        byte regH = 0x00;
        byte regL = 0x0A;
        return ProtocolUtil.writeParamsDatas(deviceAddr,regH,regL,dataH,dataL);
    }

    public static byte[] writeParamsHarmonic13CompensationRate(byte deviceAddr, int harmonic13CompensationRate){
        byte dataH = (byte)((harmonic13CompensationRate >> 8)&0xFF);
        byte dataL = (byte)(harmonic13CompensationRate&0xFF);
        byte regH = 0x00;
        byte regL = 0x0B;
        return ProtocolUtil.writeParamsDatas(deviceAddr,regH,regL,dataH,dataL);
    }

    public static byte[] writeParamsHarmonic15CompensationRate(byte deviceAddr, int harmonic15CompensationRate){
        byte dataH = (byte)((harmonic15CompensationRate >> 8)&0xFF);
        byte dataL = (byte)(harmonic15CompensationRate&0xFF);
        byte regH = 0x00;
        byte regL = 0x0C;
        return ProtocolUtil.writeParamsDatas(deviceAddr,regH,regL,dataH,dataL);
    }

    public static byte[] writeParamsHarmonic17CompensationRate(byte deviceAddr, int harmonic17CompensationRate){
        byte dataH = (byte)((harmonic17CompensationRate >> 8)&0xFF);
        byte dataL = (byte)(harmonic17CompensationRate&0xFF);
        byte regH = 0x00;
        byte regL = 0x0D;
        return ProtocolUtil.writeParamsDatas(deviceAddr,regH,regL,dataH,dataL);
    }

    public static byte[] writeParamsHarmonic19CompensationRate(byte deviceAddr, int harmonic19CompensationRate){
        byte dataH = (byte)((harmonic19CompensationRate >> 8)&0xFF);
        byte dataL = (byte)(harmonic19CompensationRate&0xFF);
        byte regH = 0x00;
        byte regL = 0x0E;
        return ProtocolUtil.writeParamsDatas(deviceAddr,regH,regL,dataH,dataL);
    }

    public static byte[] writeParamsHarmonic21CompensationRate(byte deviceAddr, int harmonic21CompensationRate){
        byte dataH = (byte)((harmonic21CompensationRate >> 8)&0xFF);
        byte dataL = (byte)(harmonic21CompensationRate&0xFF);
        byte regH = 0x00;
        byte regL = 0x0F;
        return ProtocolUtil.writeParamsDatas(deviceAddr,regH,regL,dataH,dataL);
    }

    public static byte[] writeParamsHarmonic23CompensationRate(byte deviceAddr, int harmonic23CompensationRate){
        byte dataH = (byte)((harmonic23CompensationRate >> 8)&0xFF);
        byte dataL = (byte)(harmonic23CompensationRate&0xFF);
        byte regH = 0x00;
        byte regL = 0x10;
        return ProtocolUtil.writeParamsDatas(deviceAddr,regH,regL,dataH,dataL);
    }

    public static byte[] writeParamsHarmonic25CompensationRate(byte deviceAddr, int harmonic25CompensationRate){
        byte dataH = (byte)((harmonic25CompensationRate >> 8)&0xFF);
        byte dataL = (byte)(harmonic25CompensationRate&0xFF);
        byte regH = 0x00;
        byte regL = 0x11;
        return ProtocolUtil.writeParamsDatas(deviceAddr,regH,regL,dataH,dataL);
    }

    public static byte[] writeParamsHarmonicEvenCompensationRate(byte deviceAddr, int harmonicEvenCompensationRate){
        byte dataH = (byte)((harmonicEvenCompensationRate >> 8)&0xFF);
        byte dataL = (byte)(harmonicEvenCompensationRate&0xFF);
        byte regH = 0x00;
        byte regL = 0x12;
        return ProtocolUtil.writeParamsDatas(deviceAddr,regH,regL,dataH,dataL);
    }

    public static byte[] writeParamsUintRatedCapacity(byte deviceAddr, int uintRatedCapacity){
        byte dataH = (byte)((uintRatedCapacity >> 8)&0xFF);
        byte dataL = (byte)(uintRatedCapacity&0xFF);
        byte regH = 0x00;
        byte regL = 0x12;
        return ProtocolUtil.writeParamsDatas(deviceAddr,regH,regL,dataH,dataL);
    }
    /**
     * 获取告警巡检数据协议
     * @param deviceAddr 设备地址
     * @return
     */
    public static  byte[] readNoticeCheck(byte deviceAddr){
        byte[] bytes = {deviceAddr,0x02,0x00,0x00,0x00,0x01,0x00,0x00};
        byte[] crcBytes = CRCUtil.encode(bytes);
        return crcBytes;
    }



}
