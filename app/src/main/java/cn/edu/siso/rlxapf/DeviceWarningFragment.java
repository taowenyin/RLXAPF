package cn.edu.siso.rlxapf;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;

import java.util.ArrayList;
import java.util.List;

import cn.edu.siso.rlxapf.bean.DeviceBean;
import cn.edu.siso.rlxapf.bean.NoticeDatasBean;
import cn.edu.siso.rlxapf.util.tcp.TcpClientManager;

import static cn.edu.siso.rlxapf.DeviceListActivity.DATA_KEY;
import static cn.edu.siso.rlxapf.DeviceListActivity.POSITION_KEY;


public class DeviceWarningFragment extends Fragment {

    private OnFragmentInteractionListener mListener = null;

    private TcpClientManager tcpClientManager = null;
    private Handler tcpHandler = null;
    private boolean isTimeout = false;
    private boolean isWarning = true;

    private Context context = null;

    private List<NoticeDatasBean> warningData = null;
    private NoticeDatasBean lastWarningBean = null;
    private WarningListRecyclerAdapter adapter = null;

    private List<DeviceBean> deviceData = null;
    private int currPosition = -1;
    private Uri signalUri = Uri.parse(
            UriCommunication.SchemeParams.Fragment
                    + "://"
                    + getClass().getName()
                    + "?"
                    + UriCommunication.Action
                    + "="
                    + UriCommunication.ActionParams.Signal);

    public static final String TAG = "DeviceWarningFragment";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            Bundle bundle = getArguments();
            deviceData = JSON.parseArray(
                    bundle.getString(DATA_KEY),
                    DeviceBean.class);
            currPosition = bundle.getInt(POSITION_KEY);
        }

        warningData = new ArrayList<NoticeDatasBean>();

        tcpClientManager = TcpClientManager.getInstance();
        tcpHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);

                Bundle data = msg.getData();
                String operateType = data.getString(TcpClientManager.KEY_TCP_OPERATE_TYPE);
                if (operateType.equals(TcpClientManager.TcpOperateType.OPERATE)) {
                    int tcpCmdType = Integer.valueOf(data.getString(TcpClientManager.KEY_TCP_CMD_TYPE));

                    // 载入数据
                    if (TcpClientManager.TcpCmdType.DEVICE_WARNING.ordinal() == tcpCmdType) {
                        String resType = data.getString(TcpClientManager.KEY_TCP_RES_TYPE);

                        // 正常数据
                        if (!isTimeout && isWarning) {
                            if (resType.equals(TcpClientManager.TcpResType.CRC)) {
                                Log.e(TAG, getResources().getString(R.string.tcp_connect_device_warning_data_error_crc));

                                ConnectToast toast = new ConnectToast(context,
                                        ConnectToast.ConnectRes.BAD,
                                        getResources().getString(R.string.tcp_connect_device_warning_data_error_crc),
                                        Toast.LENGTH_LONG);
                                toast.show();
                            }
                            if (resType.equals(TcpClientManager.TcpResType.LENGTH)) {
                                Log.e(TAG, getResources().getString(R.string.tcp_connect_device_warning_data_error_length));

                                ConnectToast toast = new ConnectToast(context,
                                        ConnectToast.ConnectRes.BAD,
                                        getResources().getString(R.string.tcp_connect_device_warning_data_error_length),
                                        Toast.LENGTH_LONG);
                                toast.show();
                            }
                            if (resType.equals(TcpClientManager.TcpResType.SUCCESS)) {
                                Log.i(TAG, "获取告警数据");

                                // 警告数据
                                NoticeDatasBean warningBean = JSON.parseObject(
                                        data.getString(TcpClientManager.KEY_TCP_RES_DATA),
                                        NoticeDatasBean.class);

                                if (lastWarningBean != null) {
                                    if (lastWarningBean.getCloseContactorFault() != warningBean.getCloseContactorFault() ||
                                            lastWarningBean.getCloseResistanceFault() != warningBean.getCloseResistanceFault() ||
                                            lastWarningBean.getFpgaCommnunicationError() != warningBean.getFpgaCommnunicationError() ||
                                            lastWarningBean.getFpgaCommunicationOutTime() != warningBean.getFpgaCommunicationOutTime() ||
                                            lastWarningBean.getFpgaFault() != warningBean.getFpgaFault() ||
                                            lastWarningBean.getOpenContactorFault() != warningBean.getOpenContactorFault() ||
                                            lastWarningBean.getOpenResistanceFault() != warningBean.getOpenResistanceFault() ||
                                            lastWarningBean.getOutputOverCurrent() != warningBean.getOutputOverCurrent() ||
                                            lastWarningBean.getOutputQuickBreak() != warningBean.getOutputQuickBreak() ||
                                            lastWarningBean.getStorageDataFault() != warningBean.getStorageDataFault() ||
                                            lastWarningBean.getSystemOverVoltage() != warningBean.getSystemOverVoltage() ||
                                            lastWarningBean.getSystemPowerDown() != warningBean.getSystemPowerDown() ||
                                            lastWarningBean.getSystemUnderVoltage() != warningBean.getSystemUnderVoltage() ||
                                            lastWarningBean.getTempratureIGBTHigh() != warningBean.getTempratureIGBTHigh() ||
                                            lastWarningBean.getThreePhaseVoltageUnbalance() != warningBean.getThreePhaseVoltageUnbalance() ||
                                            lastWarningBean.getUnitACHighVoltage() != warningBean.getUnitACHighVoltage()) {

                                        // 更新当前的告警
                                        lastWarningBean = warningBean;

                                        // 如果数据不同，则加入队列，并更新列表
                                        warningData.add(warningBean);
                                        adapter.notifyDataSetChanged();
                                    }
                                } else {
                                    // 更新当前的告警
                                    lastWarningBean = warningBean;

                                    warningData.add(warningBean);
                                    adapter.notifyDataSetChanged();
                                }
                            }
                        }
                        // 超时状态
                        if (resType.equals(TcpClientManager.TcpResType.TIMEOUT) && isWarning) {
                            isTimeout = true;

                            Log.e(TAG, getResources().getString(R.string.tcp_connect_device_warning_data_time_out));

                            ConnectToast toast = new ConnectToast(getContext(),
                                    ConnectToast.ConnectRes.BAD,
                                    getResources().getString(R.string.tcp_connect_device_warning_data_time_out),
                                    Toast.LENGTH_LONG);
                            toast.show();
                        }

                        if (isWarning) {
                            // 发送指令后休息100ms
                            try {
                                Thread.sleep(context.getResources().getInteger(
                                        R.integer.tcp_real_time_delay));
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }

                            isTimeout = false; // 清空超时，允许进行TCP操作

                            // 发送实时数据指令
                            tcpClientManager.sendCmd(context, TcpClientManager.TcpCmdType.DEVICE_WARNING,
                                    new String[]{deviceData.get(currPosition).getDeviceNo()},
                                    tcpHandler);
                            // 发送信号指令
                            mListener.onFragmentInteraction(signalUri);
                        }
                    }
                }
            }
        };
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_device_warning, container, false);

        RecyclerView warningListView  = (RecyclerView) rootView.findViewById(R.id.warning_list_view);

        // 初始化列表数据
        LinearLayoutManager layoutManager = new LinearLayoutManager(
                context, LinearLayoutManager.VERTICAL, false);
        adapter = new WarningListRecyclerAdapter(context, warningData);
        warningListView.setLayoutManager(layoutManager);
        warningListView.setAdapter(adapter);

        return rootView;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }

        this.context = context;
    }

    @Override
    public void onResume() {
        super.onResume();

        isTimeout = false; // 清空超时，允许进行TCP操作

        tcpClientManager.sendCmd(context, TcpClientManager.TcpCmdType.DEVICE_WARNING,
                new String[]{deviceData.get(currPosition).getDeviceNo()},
                tcpHandler);
        // 发送信号指令
        mListener.onFragmentInteraction(signalUri);
    }

    @Override
    public void onPause() {
        super.onPause();
        isWarning = false; // 关闭实时数据
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }
}
