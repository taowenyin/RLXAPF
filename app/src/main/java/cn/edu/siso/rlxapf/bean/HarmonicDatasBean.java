package cn.edu.siso.rlxapf.bean;

import android.util.Log;

import cn.edu.siso.rlxapf.util.CRCUtil;

/**
 * Created by jason on 2017-7-28.
 * 谐波数据解析，使用parse函数传入数据，getXXXX方法获取解析值
 */

public class HarmonicDatasBean {
    public static final String PARAM_DATA_KEY = "paramsData";

    private int[] paramsData;

    private int bytes2int(byte b1 , byte b2){
        return (int)((b1&0xff)<<8)|(b2 & 0xff);
    }

    /**
     *
     * @param bytes 网络数据
     * @return -1 CRC校验错误；  -2长度解析出错； 0解析成功
     */
    public int parse(byte[] bytes) {
        if(bytes == null || bytes.length != 155 || bytes[2] != (byte) 0x96)//指定参数一次读出的长度
            return -2;

        if (!CRCUtil.decode(bytes))
            return -1;

        int startLoc = 3;

        int[] datas = new int[75];

        for (int i = 0; i < 75 ; i++) {
            datas[i] = bytes2int(bytes[startLoc + 2*i],bytes[startLoc + 2*i +1]);
        }

        this.paramsData = datas;
        return 0;
    }

    public int[] getParamsData() {
        return paramsData;
    }

    public void setParamsData(int[] paramsData) {
        this.paramsData = paramsData;
    }
}
