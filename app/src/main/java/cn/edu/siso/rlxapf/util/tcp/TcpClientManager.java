package cn.edu.siso.rlxapf.util.tcp;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.support.v4.os.AsyncTaskCompat;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;

import java.util.Timer;
import java.util.TimerTask;

import cn.edu.siso.rlxapf.R;
import cn.edu.siso.rlxapf.bean.HarmonicDatasBean;
import cn.edu.siso.rlxapf.bean.ParameterDatasBean;
import cn.edu.siso.rlxapf.bean.RealTimeDatasBean;
import cn.edu.siso.rlxapf.util.ProtocolUtil;
import cn.edu.siso.rlxapf.util.TCPUtil;

public class TcpClientManager {

    private static TcpClientManager instance = null;
    private static boolean isConnect = false;

    private TCPUtil tcpUtil = null;

    private Timer tcpTimer = null; // TCP超时定时器

    public static final String KEY_TCP_OPERATE_TYPE = "operate_type";
    public class TcpOperateType {
        public static final String CONNECT = "connect";
        public static final String OPERATE = "operate";
    }

    public static final String KEY_TCP_RES_TYPE = "res_type";
    public class TcpResType {
        public static final String SUCCESS = "success";
        public static final String ERROR = "error";
        public static final String CLOSE = "close";
        public static final String CRC = "crc";
        public static final String LENGTH = "length";
        public static final String TIMEOUT = "timeout";
    }

    public static final String KEY_TCP_RES_CODE = "res_code";
    public static final String KEY_TCP_RES_DATA = "res_data";

    public enum  TcpCmdType {
        EMPTY, CONNECT_DEVICE,
        LOAD_PARAMS, UPDATE_PARAM,
        STOP_DEVICE, START_DEVICE,
        REAL_DATA, SPECTRUM_DATA};
    public static final String KEY_TCP_CMD_TYPE = "cmd_type";

    private TcpClientManager() {
        tcpUtil = new TCPUtil();
    }

    public static TcpClientManager getInstance() {
        if (instance == null) {
            instance = new TcpClientManager();
        }

        return instance;
    }

    public void connect(Context context, String deviceId, String compass, Handler tcpHandler) {
        tcpUtil.connect(deviceId, compass, new OnTcpConnectListener(tcpHandler));

        // 执行任务时启动超时定时器
        tcpTimer = new Timer();
        tcpTimer.schedule(new TimeOutTask(tcpHandler, TcpCmdType.CONNECT_DEVICE),
                context.getResources().getInteger(R.integer.tcp_time_out));
    }

    /**
     * 发送TCP指令
     * @param type TcpCmdType 指令类型
     * @param params String[] 指令参数
     * @return int -1 设备未连接 0 设备已连接
     */
    public int sendCmd(Context context, TcpCmdType type, String[] params, Handler tcpHandler) {

        if (!isConnect) {
            return -1;
        }

        AsyncTaskCompat.executeParallel(
                new TcpAsyncTask(context, new OnTcpReceiveListener(tcpHandler, type)), params);

        // 因为启动和停止设备没有返回值，所以不启动定时器
        if (!(type == TcpCmdType.START_DEVICE) && !(type == TcpCmdType.STOP_DEVICE)) {
            // 执行任务时启动超时定时器
            tcpTimer = new Timer();
            tcpTimer.schedule(new TimeOutTask(tcpHandler, type),
                    context.getResources().getInteger(R.integer.tcp_time_out));
        }

        return 0;
    }

    public boolean getConnectStatus() {
        return isConnect;
    }

    public int close(String deviceId) {
        if (!isConnect) {
            return -1;
        }

        tcpUtil.close(deviceId);

        return 0;
    }

    private void clearTimer() {
        if (tcpTimer != null) {
            tcpTimer.cancel();
            tcpTimer = null;
        }
    }

    private class OnTcpConnectListener implements TCPUtil.OnConnectListener {

        private Handler tcpHandler = null;

        public OnTcpConnectListener (Handler tcpHandler) {
            this.tcpHandler = tcpHandler;
        }

        @Override
        public void onSuccess(String deviceId) {

            isConnect = true;

            Message msg = new Message();
            Bundle data = new Bundle();
            data.putString(KEY_TCP_OPERATE_TYPE, TcpOperateType.CONNECT);
            data.putString(KEY_TCP_RES_TYPE, TcpResType.SUCCESS);
            data.putString(KEY_TCP_RES_DATA, deviceId);
            msg.setData(data);

            // 清空定时器
            clearTimer();

            tcpHandler.sendMessage(msg);
        }

        @Override
        public void onError(int errorCode, String errorMsg) {
            Message msg = new Message();
            Bundle data = new Bundle();
            data.putString(KEY_TCP_OPERATE_TYPE, TcpOperateType.CONNECT);
            data.putString(KEY_TCP_RES_TYPE, TcpResType.ERROR);
            data.putString(KEY_TCP_RES_CODE, String.valueOf(errorCode));
            data.putString(KEY_TCP_RES_DATA, errorMsg);
            msg.setData(data);

            // 清空定时器
            clearTimer();

            tcpHandler.sendMessage(msg);
        }

        @Override
        public void onClose(String deviceId) {

            isConnect = false;

            Message msg = new Message();
            Bundle data = new Bundle();
            data.putString(KEY_TCP_OPERATE_TYPE, TcpOperateType.CONNECT);
            data.putString(KEY_TCP_RES_TYPE, TcpResType.CLOSE);
            data.putString(KEY_TCP_RES_DATA, deviceId);
            msg.setData(data);

            // 清空定时器
            clearTimer();

            tcpHandler.sendMessage(msg);
        }
    }

    private class OnTcpReceiveListener implements TCPUtil.OnReceiveListener {

        private Handler tcpHandler = null;
        private TcpCmdType type = TcpCmdType.EMPTY;

        public OnTcpReceiveListener(Handler tcpHandler, TcpCmdType type) {
            this.tcpHandler = tcpHandler;
            this.type = type;
        }

        public TcpCmdType getCmdType() {
            return type;
        }

        @Override
        public void onReceive(String deviceId, byte[] data) {
            Message msg = new Message();
            Bundle bundle = new Bundle();

            if (type == TcpCmdType.LOAD_PARAMS) {
                ParameterDatasBean datasBean = new ParameterDatasBean();
                int res = datasBean.parse(data);

                bundle.putString(KEY_TCP_OPERATE_TYPE, TcpOperateType.OPERATE);
                bundle.putString(KEY_TCP_CMD_TYPE, String.valueOf(TcpCmdType.LOAD_PARAMS.ordinal()));
                if (res == -1) {
                    bundle.putString(KEY_TCP_RES_TYPE, TcpResType.CRC);
                } else if (res == -2) {
                    bundle.putString(KEY_TCP_RES_TYPE, TcpResType.LENGTH);
                } else {
                    bundle.putString(KEY_TCP_RES_TYPE, TcpResType.SUCCESS);
                    String bean = JSON.toJSONString(datasBean, SerializerFeature.WriteMapNullValue);
                    bundle.putString(KEY_TCP_RES_DATA, bean);
                }
            }
            if (type == TcpCmdType.UPDATE_PARAM) {
                bundle.putString(KEY_TCP_OPERATE_TYPE, TcpOperateType.OPERATE);
                bundle.putString(KEY_TCP_CMD_TYPE, String.valueOf(TcpCmdType.UPDATE_PARAM.ordinal()));
                bundle.putString(KEY_TCP_RES_TYPE, TcpResType.SUCCESS);
            }
            if (type == TcpCmdType.START_DEVICE) {
                bundle.putString(KEY_TCP_OPERATE_TYPE, TcpOperateType.OPERATE);
                bundle.putString(KEY_TCP_CMD_TYPE, String.valueOf(TcpCmdType.START_DEVICE.ordinal()));
                bundle.putString(KEY_TCP_RES_TYPE, TcpResType.SUCCESS);
            }
            if (type == TcpCmdType.STOP_DEVICE) {
                bundle.putString(KEY_TCP_OPERATE_TYPE, TcpOperateType.OPERATE);
                bundle.putString(KEY_TCP_CMD_TYPE, String.valueOf(TcpCmdType.STOP_DEVICE.ordinal()));
                bundle.putString(KEY_TCP_RES_TYPE, TcpResType.SUCCESS);
            }
            if (type == TcpCmdType.REAL_DATA) {
                RealTimeDatasBean datasBean = new RealTimeDatasBean();

                int res = datasBean.parse(data);
                bundle.putString(KEY_TCP_OPERATE_TYPE, TcpOperateType.OPERATE);
                bundle.putString(KEY_TCP_CMD_TYPE, String.valueOf(TcpCmdType.REAL_DATA.ordinal()));
                if (res == -1) {
                    bundle.putString(KEY_TCP_RES_TYPE, TcpResType.CRC);
                } else if (res == -2) {
                    bundle.putString(KEY_TCP_RES_TYPE, TcpResType.LENGTH);
                } else {
                    bundle.putString(KEY_TCP_RES_TYPE, TcpResType.SUCCESS);
                    String bean = JSON.toJSONString(datasBean, SerializerFeature.WriteMapNullValue);
                    bundle.putString(KEY_TCP_RES_DATA, bean);
                }
            }
            if (type == TcpCmdType.SPECTRUM_DATA) {
                HarmonicDatasBean datasBean = new HarmonicDatasBean();
                int res = datasBean.parse(data);
                bundle.putString(KEY_TCP_OPERATE_TYPE, TcpOperateType.OPERATE);
                bundle.putString(KEY_TCP_CMD_TYPE, String.valueOf(TcpCmdType.SPECTRUM_DATA.ordinal()));
                if (res == -1) {
                    bundle.putString(KEY_TCP_RES_TYPE, TcpResType.CRC);
                } else if (res == -2) {
                    bundle.putString(KEY_TCP_RES_TYPE, TcpResType.LENGTH);
                } else {
                    bundle.putString(KEY_TCP_RES_TYPE, TcpResType.SUCCESS);
                    // 谐波数据还未定义
//                    String bean = JSON.toJSONString(datasBean, SerializerFeature.WriteMapNullValue);
//                    bundle.putString(KEY_TCP_RES_DATA, bean);
                }
            }

            // 清空定时器
            clearTimer();

            msg.setData(bundle);
            tcpHandler.sendMessage(msg);
        }
    }

    private class TcpAsyncTask extends AsyncTask<String, Void, Void> {

        private OnTcpReceiveListener listener = null;
        private Context context = null;

        public TcpAsyncTask(Context context, OnTcpReceiveListener listener) {
            super();
            this.listener = listener;
            this.context = context;
        }

        @Override
        protected Void doInBackground(String... params) {

            // 下载参数
            if (listener.getCmdType() == TcpCmdType.LOAD_PARAMS) {
                tcpUtil.send(ProtocolUtil.readParamsDatas(Integer.valueOf(params[0]).byteValue()),
                        listener);
            }

            // 更新参数
            if (listener.getCmdType() == TcpCmdType.UPDATE_PARAM) {
                byte deviceAddr = Integer.valueOf(params[0]).byteValue();
                String preferenceKey = params[1];
                String preferenceValue = params[2];
                byte[] bytesMsg = null;

                if (preferenceKey.equals(context.getResources().getString(R.string.param_preferences_unit_key))) {
                    bytesMsg = ProtocolUtil.writeParamsUnitCount(deviceAddr, Integer.valueOf(preferenceValue));
                }
                if (preferenceKey.equals(context.getResources().getString(R.string.param_preferences_system_ct_key))) {
                    bytesMsg = ProtocolUtil.writeParamsSystemCTChangeRate(deviceAddr, Integer.valueOf(preferenceValue));
                }
                if (preferenceKey.equals(context.getResources().getString(R.string.param_preferences_load_ct_key))) {
                    bytesMsg = ProtocolUtil.writeParamsLoadCTChangeRate(deviceAddr, Integer.valueOf(preferenceValue));
                }
                if (preferenceKey.equals(context.getResources().getString(R.string.param_preferences_target_power_key))) {
                    bytesMsg = ProtocolUtil.writeParamsObjectPowerFactor(deviceAddr, Double.valueOf(preferenceValue));
                }
                if (preferenceKey.equals(context.getResources().getString(R.string.param_preferences_3_compensate_key))) {
                    bytesMsg = ProtocolUtil.writeParamsHarmonic3CompensationRate(deviceAddr, Integer.valueOf(preferenceValue));
                }
                if (preferenceKey.equals(context.getResources().getString(R.string.param_preferences_5_compensate_key))) {
                    bytesMsg = ProtocolUtil.writeParamsHarmonic5CompensationRate(deviceAddr, Integer.valueOf(preferenceValue));
                }
                if (preferenceKey.equals(context.getResources().getString(R.string.param_preferences_7_compensate_key))) {
                    bytesMsg = ProtocolUtil.writeParamsHarmonic7CompensationRate(deviceAddr, Integer.valueOf(preferenceValue));
                }
                if (preferenceKey.equals(context.getResources().getString(R.string.param_preferences_9_compensate_key))) {
                    bytesMsg = ProtocolUtil.writeParamsHarmonic9CompensationRate(deviceAddr, Integer.valueOf(preferenceValue));
                }
                if (preferenceKey.equals(context.getResources().getString(R.string.param_preferences_11_compensate_key))) {
                    bytesMsg = ProtocolUtil.writeParamsHarmonic11CompensationRate(deviceAddr, Integer.valueOf(preferenceValue));
                }
                if (preferenceKey.equals(context.getResources().getString(R.string.param_preferences_13_compensate_key))) {
                    bytesMsg = ProtocolUtil.writeParamsHarmonic13CompensationRate(deviceAddr, Integer.valueOf(preferenceValue));
                }
                if (preferenceKey.equals(context.getResources().getString(R.string.param_preferences_15_compensate_key))) {
                    bytesMsg = ProtocolUtil.writeParamsHarmonic15CompensationRate(deviceAddr, Integer.valueOf(preferenceValue));
                }
                if (preferenceKey.equals(context.getResources().getString(R.string.param_preferences_17_compensate_key))) {
                    bytesMsg = ProtocolUtil.writeParamsHarmonic17CompensationRate(deviceAddr, Integer.valueOf(preferenceValue));
                }
                if (preferenceKey.equals(context.getResources().getString(R.string.param_preferences_19_compensate_key))) {
                    bytesMsg = ProtocolUtil.writeParamsHarmonic19CompensationRate(deviceAddr, Integer.valueOf(preferenceValue));
                }
                if (preferenceKey.equals(context.getResources().getString(R.string.param_preferences_21_compensate_key))) {
                    bytesMsg = ProtocolUtil.writeParamsHarmonic21CompensationRate(deviceAddr, Integer.valueOf(preferenceValue));
                }
                if (preferenceKey.equals(context.getResources().getString(R.string.param_preferences_23_compensate_key))) {
                    bytesMsg = ProtocolUtil.writeParamsHarmonic23CompensationRate(deviceAddr, Integer.valueOf(preferenceValue));
                }
                if (preferenceKey.equals(context.getResources().getString(R.string.param_preferences_25_compensate_key))) {
                    bytesMsg = ProtocolUtil.writeParamsHarmonic25CompensationRate(deviceAddr, Integer.valueOf(preferenceValue));
                }
                if (preferenceKey.equals(context.getResources().getString(R.string.param_preferences_26_compensate_key))) {
                    bytesMsg = ProtocolUtil.writeParamsHarmonicEvenCompensationRate(deviceAddr, Integer.valueOf(preferenceValue));
                }
                if (preferenceKey.equals(context.getResources().getString(R.string.param_preferences_sampling_model_key))) {
                    bytesMsg = ProtocolUtil.writeParamsSampleMode(deviceAddr, Integer.valueOf(preferenceValue));
                }
                if (preferenceKey.equals(context.getResources().getString(R.string.param_preferences_compensate_model_key))) {
                    bytesMsg = ProtocolUtil.writeParamsCompensationMode(deviceAddr, Integer.valueOf(preferenceValue));
                }

                tcpUtil.send(bytesMsg, listener);
            }

            // 停止设备
            if (listener.getCmdType() == TcpCmdType.STOP_DEVICE) {
//                tcpUtil.send(ProtocolUtil.stopDevice(Integer.valueOf(params[0]).byteValue()),
//                        listener);
                byte deviceAddr = 0x7F;
                tcpUtil.send(ProtocolUtil.stopDevice(deviceAddr), listener);
            }

            // 启动设备
            if (listener.getCmdType() == TcpCmdType.START_DEVICE) {
//                tcpUtil.send(ProtocolUtil.startDevice(Integer.valueOf(params[0]).byteValue()),
//                        listener);
                byte deviceAddr = 0x7F;
                tcpUtil.send(ProtocolUtil.startDevice(deviceAddr), listener);
            }

            // 实时数据
            if (listener.getCmdType() == TcpCmdType.REAL_DATA) {
                tcpUtil.send(ProtocolUtil.readRealTimeDatas(Integer.valueOf(params[0]).byteValue()),
                        listener);
            }

            // 谐波数据
            if (listener.getCmdType() == TcpCmdType.SPECTRUM_DATA) {
                tcpUtil.send(ProtocolUtil.readHarmonicSpectrum(Integer.valueOf(params[0]).byteValue()),
                        listener);
            }

            return null;
        }
    }

    private class TimeOutTask extends TimerTask {

        private Handler tcpHandler = null;
        private TcpCmdType tcpType = TcpCmdType.EMPTY;

        public TimeOutTask(Handler tcpHandler, TcpCmdType tcpType) {
            super();

            this.tcpHandler = tcpHandler;
            this.tcpType = tcpType;
        }

        @Override
        public void run() {
            Message msg = new Message();
            Bundle bundle = new Bundle();

            if (tcpType == TcpCmdType.CONNECT_DEVICE) {
                bundle.putString(KEY_TCP_OPERATE_TYPE, TcpOperateType.CONNECT);
                bundle.putString(KEY_TCP_RES_TYPE, TcpResType.TIMEOUT);
            }
            if (tcpType == TcpCmdType.LOAD_PARAMS) {
                bundle.putString(KEY_TCP_OPERATE_TYPE, TcpOperateType.OPERATE);
                bundle.putString(KEY_TCP_CMD_TYPE, String.valueOf(TcpCmdType.LOAD_PARAMS.ordinal()));
                bundle.putString(KEY_TCP_RES_TYPE, TcpResType.TIMEOUT);
            }
            if (tcpType == TcpCmdType.UPDATE_PARAM) {
                bundle.putString(KEY_TCP_OPERATE_TYPE, TcpOperateType.OPERATE);
                bundle.putString(KEY_TCP_CMD_TYPE, String.valueOf(TcpCmdType.UPDATE_PARAM.ordinal()));
                bundle.putString(KEY_TCP_RES_TYPE, TcpResType.TIMEOUT);
            }
            if (tcpType == TcpCmdType.START_DEVICE) {
                bundle.putString(KEY_TCP_OPERATE_TYPE, TcpOperateType.OPERATE);
                bundle.putString(KEY_TCP_CMD_TYPE, String.valueOf(TcpCmdType.START_DEVICE.ordinal()));
                bundle.putString(KEY_TCP_RES_TYPE, TcpResType.TIMEOUT);
            }
            if (tcpType == TcpCmdType.STOP_DEVICE) {
                bundle.putString(KEY_TCP_OPERATE_TYPE, TcpOperateType.OPERATE);
                bundle.putString(KEY_TCP_CMD_TYPE, String.valueOf(TcpCmdType.STOP_DEVICE.ordinal()));
                bundle.putString(KEY_TCP_RES_TYPE, TcpResType.TIMEOUT);
            }
            if (tcpType == TcpCmdType.REAL_DATA) {
                bundle.putString(KEY_TCP_OPERATE_TYPE, TcpOperateType.OPERATE);
                bundle.putString(KEY_TCP_CMD_TYPE, String.valueOf(TcpCmdType.REAL_DATA.ordinal()));
                bundle.putString(KEY_TCP_RES_TYPE, TcpResType.TIMEOUT);
            }
            if (tcpType == TcpCmdType.SPECTRUM_DATA) {
                bundle.putString(KEY_TCP_OPERATE_TYPE, TcpOperateType.OPERATE);
                bundle.putString(KEY_TCP_CMD_TYPE, String.valueOf(TcpCmdType.SPECTRUM_DATA.ordinal()));
                bundle.putString(KEY_TCP_RES_TYPE, TcpResType.TIMEOUT);
            }

            msg.setData(bundle);
            tcpHandler.sendMessage(msg);
        }
    }
}
