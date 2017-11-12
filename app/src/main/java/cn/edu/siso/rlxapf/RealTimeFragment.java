package cn.edu.siso.rlxapf;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;

import java.util.List;

import cn.edu.siso.rlxapf.bean.DeviceBean;
import cn.edu.siso.rlxapf.bean.RealTimeDatasBean;
import cn.edu.siso.rlxapf.util.tcp.TcpClientManager;

import static cn.edu.siso.rlxapf.DeviceListActivity.DATA_KEY;
import static cn.edu.siso.rlxapf.DeviceListActivity.POSITION_KEY;


public class RealTimeFragment extends Fragment implements
        TabLayout.OnTabSelectedListener {

    private OnFragmentInteractionListener mListener = null;

    private TcpClientManager tcpClientManager = null;
    private Handler tcpHandler = null;
    private boolean isTimeout = false;
    private boolean isRealTime = true;

    private Context context = null;

    private IRealTimeData iRealTimeData = null;

    private RealTimeFragmentAdapter adapter = null;
    private ViewPager realTimePage = null;

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

    private int[] realTimeIndicatorTitleArray = {
            R.string.main_top_tab_real_data_title,
            R.string.main_top_tab_real_curve_title
    };

    private Fragment[] realTimeTabItemFragmentArray = {
            RealDataFragment.newInstance(),
            RealCurveFragment.newInstance()
    };

    public static final String TAG = "RealTimeFragment";

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
                    if (TcpClientManager.TcpCmdType.REAL_DATA.ordinal() == tcpCmdType) {
                        String resType = data.getString(TcpClientManager.KEY_TCP_RES_TYPE);
                        if (!isTimeout && isRealTime) {
                            if (resType.equals(TcpClientManager.TcpResType.CRC)) {
                                Log.e(TAG, getResources().getString(R.string.tcp_connect_real_data_error_crc));

//                                ConnectToast toast = new ConnectToast(context,
//                                        ConnectToast.ConnectRes.BAD,
//                                        getResources().getString(R.string.tcp_connect_real_data_error_crc),
//                                        Toast.LENGTH_LONG);
//                                toast.show();
                            }
                            if (resType.equals(TcpClientManager.TcpResType.LENGTH)) {
                                Log.e(TAG, getResources().getString(R.string.tcp_connect_real_data_error_length));

//                                ConnectToast toast = new ConnectToast(context,
//                                        ConnectToast.ConnectRes.BAD,
//                                        getResources().getString(R.string.tcp_connect_real_data_error_length),
//                                        Toast.LENGTH_LONG);
//                                toast.show();
                            }
                            if (resType.equals(TcpClientManager.TcpResType.SUCCESS)) {
                                RealTimeDatasBean datasBean = JSON.parseObject(
                                        data.getString(TcpClientManager.KEY_TCP_RES_DATA),
                                        RealTimeDatasBean.class);

                                // 发送实时数据给子fragment
                                iRealTimeData.updateRealData(datasBean);
                            }
                        }
                        if (resType.equals(TcpClientManager.TcpResType.TIMEOUT) && isRealTime) {
                            // 标记当前为超时状态
                            isTimeout = true;

                            ConnectToast toast = new ConnectToast(getContext(),
                                    ConnectToast.ConnectRes.BAD,
                                    getResources().getString(R.string.tcp_connect_real_data_time_out),
                                    Toast.LENGTH_LONG);
                            toast.show();
                        }

                        if (isRealTime) {
                            // 发送指令后休息100ms
                            try {
                                Thread.sleep(context.getResources().getInteger(
                                        R.integer.tcp_real_time_delay));
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }

                            isTimeout = false; // 清空超时，允许进行TCP操作

                            // 发送实时数据指令
                            tcpClientManager.sendCmd(context, TcpClientManager.TcpCmdType.REAL_DATA,
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
        View rootView = inflater.inflate(R.layout.fragment_real_time, container, false);

        TabLayout realTimeTab = (TabLayout) rootView.findViewById(R.id.real_time_tab);
        realTimePage = (ViewPager) rootView.findViewById(R.id.real_time_page);

        adapter = new RealTimeFragmentAdapter(getChildFragmentManager(),
                realTimeTabItemFragmentArray, realTimeIndicatorTitleArray, getContext());
        realTimePage.setAdapter(adapter);
        realTimeTab.setupWithViewPager(realTimePage);
        realTimeTab.addOnTabSelectedListener(this);

        iRealTimeData = (IRealTimeData) adapter.getItem(0);

        Log.i(TAG, "===onCreateView===");

        return rootView;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Log.i(TAG, "===onAttach===");

        if (context instanceof UserFragment.OnFragmentInteractionListener) {
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
        isRealTime = true; // 打开实时数据

        tcpClientManager.sendCmd(context, TcpClientManager.TcpCmdType.REAL_DATA,
                new String[]{deviceData.get(currPosition).getDeviceNo()},
                tcpHandler);
        mListener.onFragmentInteraction(signalUri);
    }

    @Override
    public void onPause() {
        super.onPause();
        isRealTime = false; // 关闭实时数据
    }

    @Override
    public void onDetach() {
        super.onDetach();

        mListener = null;
    }

    @Override
    public void onTabSelected(TabLayout.Tab tab) {
        realTimePage.setCurrentItem(tab.getPosition());

        // 获取当前需要更新的数据
        iRealTimeData = adapter.getCurrentRealTimeObject();
    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {
        Log.i(TAG, "onTabUnselected");
    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {
        Log.i(TAG, "onTabUnselected");
    }

    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }
}
