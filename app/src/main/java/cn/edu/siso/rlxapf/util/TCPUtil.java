package cn.edu.siso.rlxapf.util;

import android.util.Log;

import com.android.usrcloudlibrary.USRConnect;
import com.android.usrcloudlibrary.USRConnectListener;
import com.android.usrcloudlibrary.USRConnectManager;
import com.android.usrcloudlibrary.Utils.USRConfig;

/**
 * 有人GPRS网络操作接口
 * Created by jason on 2017-7-24.
 */

public class TCPUtil {
    //USRConfig.
    private USRConnectManager connectManager;
    private USRConnect connect;
    private OnReceiveListener onReceiveListener;

    private String TAG = "TCPUtil";

    public TCPUtil(){
        connectManager = USRConnectManager.getInstance();
    }

    public void close(String deviceId){
        if(connect != null)
            connect.breakConnect();
    }

    public void send(byte[] bytesMsg, OnReceiveListener onReceiveListener){
        if(connect != null){
            connect.send(bytesMsg);
            this.onReceiveListener = onReceiveListener;
        }
    }

    public void connect(String did, String compass, final OnConnectListener onConnectListener){
        connect = connectManager.createConnect(did,compass, USRConfig.CLOUD_LONG_PORT, new USRConnectListener() {
            @Override
            public void onConnectSuccess(String deviceId) {
                //连接成功
                Log.i(TAG,"GORS连接成功");
                if(onConnectListener != null)
                    onConnectListener.onSuccess(deviceId);
            }

            @Override
            public void onRegisterSuccess(String deviceId) {
                //注册成功
                Log.i(TAG,"GORS注册成功");
            }

            @Override
            public void onConnectBreak(String deviceId) {
                //连接断开
                Log.i(TAG,"GORS连接断开");
                if(onConnectListener != null)
                    onConnectListener.onClose(deviceId);

            }

            @Override
            public void onReceviceData(String deviceId, byte[] data) {
                if(onReceiveListener != null){
                    if(data[0]==0x77&&data[1]==0x77&&data[2]==0x77){
                        Log.i(TAG,"有人云数据，TCPUtil已过滤");
                    }else{
                        onReceiveListener.onReceive(deviceId,data);
                    }

                }
            }

            @Override
            public void onError(String s, int errorCode) {
                String error ="";
                switch (errorCode) {
                    case 0x31:
                        error = "注册包不合法";
                        break;
                    case 0x32:
                        error = "通讯密码错误";
                        break;
                    case 0x33:
                        error = "设备不存在";
                        break;
                    case 0x34:
                        error = "设备被顶掉";
                        break;
                    case 0x25:
                        error = "设备不在线";
                        break;
                    case 0x26:
                        error = "目标组不存在或无权限";
                        break;
                    case 0x27:
                        error = "临时会话类型错误";
                        break;
                }
                if(onConnectListener != null)
                    onConnectListener.onError(errorCode,error);
            }
        });
    }

    /**
     * 连接设备回调接口
     */
    public interface OnConnectListener {
        /**
         * 连接成功操作
         */
        void onSuccess(String deviceId);

        /**
         * 连接失败
         */
        void onError(int errorCode,String errorMsg);

        /**
         * 连接断开时的操作
         */
        void onClose(String deviceId);
    }

    /**
     * 发送数据请求接收回调接口
     */
    public interface OnReceiveListener {
        /**
         * 接收回调
         */
        void onReceive(String deviceId,byte[] data);
    }
}
