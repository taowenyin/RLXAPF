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
        byte[] bytes = {deviceAddr,0x04,0x00,0x00,0x00,0x1A,0x00,0x00};
        byte[] crcBytes = CRCUtil.encode(bytes);
        return crcBytes;
    }
    /**
     * 获取参数协议
     * @param deviceAddr 设备地址
     * @return
     */
    public static byte[] readParamsDatas(byte deviceAddr){
        byte[] bytes = {deviceAddr,0x03,0x00,0x00,0x00,0x1A,0x00,0x00};
        byte[] crcBytes = CRCUtil.encode(bytes);
        return crcBytes;
    }
    /**
     * 写入参数数据协议
     * @param deviceAddr 设备地址
     * @return
     */
    public static byte[] writeParamsDatas(byte deviceAddr,byte[] wdata){
        byte[] bytes = {deviceAddr,0x06,0x00,0x00,wdata[0],wdata[1],0x00,0x00};
        byte[] crcBytes = CRCUtil.encode(bytes);
        return crcBytes;
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
