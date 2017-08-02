package cn.edu.siso.rlxapf;

import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.os.AsyncTaskCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.truizlop.sectionedrecyclerview.SectionedSpanSizeLookup;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import cn.edu.siso.rlxapf.bean.DataBean;
import cn.edu.siso.rlxapf.bean.DataGroupBean;
import cn.edu.siso.rlxapf.bean.DeviceBean;
import cn.edu.siso.rlxapf.bean.HarmonicDatasBean;
import cn.edu.siso.rlxapf.config.TCPConfig;
import cn.edu.siso.rlxapf.util.ProtocolUtil;
import cn.edu.siso.rlxapf.util.TCPUtil;
import cn.edu.siso.rlxapf.util.tcp.TcpClientManager;

import static cn.edu.siso.rlxapf.DeviceListActivity.DATA_KEY;
import static cn.edu.siso.rlxapf.DeviceListActivity.POSITION_KEY;

public class SpectrumFragment extends Fragment {

    private OnFragmentInteractionListener mListener = null;

    private RecyclerView spectrumRecyclerView = null;
    private DataSectionRecyclerAdapter adapter = null;

    private List<DataGroupBean> spectrumData = null;

    private TcpClientManager tcpClientManager = null;
    private Handler tcpHandler = null;
    private boolean isTimeout = false;
    private boolean isSpectrum = true;

    private Context context = null;

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

    public static final String TAG = "SpectrumFragment";

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
                    if (TcpClientManager.TcpCmdType.SPECTRUM_DATA.ordinal() == tcpCmdType) {
                        String resType = data.getString(TcpClientManager.KEY_TCP_RES_TYPE);
                        if (!isTimeout && isSpectrum) {
                            if (resType.equals(TcpClientManager.TcpResType.CRC)) {
                                Log.e(TAG, getResources().getString(R.string.tcp_connect_spectrum_data_error_crc));

//                                ConnectToast toast = new ConnectToast(context,
//                                        ConnectToast.ConnectRes.BAD,
//                                        getResources().getString(R.string.tcp_connect_spectrum_data_error_crc),
//                                        Toast.LENGTH_LONG);
//                                toast.show();
                            }
                            if (resType.equals(TcpClientManager.TcpResType.LENGTH)) {
                                Log.e(TAG, getResources().getString(R.string.tcp_connect_spectrum_data_error_length));

//                                ConnectToast toast = new ConnectToast(context,
//                                        ConnectToast.ConnectRes.BAD,
//                                        getResources().getString(R.string.tcp_connect_spectrum_data_error_length),
//                                        Toast.LENGTH_LONG);
//                                toast.show();
                            }
                            if (resType.equals(TcpClientManager.TcpResType.SUCCESS)) {
                                Log.i(TAG, "更新谐波数据");

                                // 谐波数据还未定义
//                                HarmonicDatasBean datasBean = JSON.parseObject(
//                                        data.getString(TcpClientManager.KEY_TCP_RES_DATA),
//                                        HarmonicDatasBean.class);
//
//                                // 更新谐波数据
//                                updateHarmonicDates(datasBean);
                            }
                        }
                        if (resType.equals(TcpClientManager.TcpResType.TIMEOUT) && isSpectrum) {
                            // 标记当前为超时状态
                            isTimeout = true;

                            Log.e(TAG, getResources().getString(R.string.tcp_connect_spectrum_data_time_out));

//                            ConnectToast toast = new ConnectToast(getContext(),
//                                    ConnectToast.ConnectRes.BAD,
//                                    getResources().getString(R.string.tcp_connect_spectrum_data_time_out),
//                                    Toast.LENGTH_LONG);
//                            toast.show();
                        }

                        if (isSpectrum) {
                            // 发送指令后休息100ms
                            try {
                                Thread.sleep(context.getResources().getInteger(
                                        R.integer.tcp_real_time_delay));
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }

                            isTimeout = false; // 清空超时，允许进行TCP操作

                            // 发送实时数据指令
                            tcpClientManager.sendCmd(context, TcpClientManager.TcpCmdType.SPECTRUM_DATA,
                                    new String[]{deviceData.get(currPosition).getDeviceNo()},
                                    tcpHandler);
                            // 发送信号指令
                            mListener.onFragmentInteraction(signalUri);
                        }
                    }
                }
            }
        };

        // 初始化谐波数据
        spectrumData = new ArrayList<DataGroupBean>();
        String[] spectrumSectionData = getResources().getStringArray(R.array.specturm_sections);
        for (int i = 0; i < spectrumSectionData.length; i++) {

            List<DataBean> data = new ArrayList<DataBean>();
            for (int j = 0; j < 25; j++) {
                List<Map<String, Integer>> item = new ArrayList<Map<String, Integer>>();

                Map<String, Integer> value = new HashMap<String, Integer>();
                value.put(DataBean.DATA_KEY, 0);
                item.add(value);

                DataBean itemData = null;
                if (j == 0) {
                    switch (i) {
                        case 0:
                            itemData = new DataBean("A相基波", item);
                            break;
                        case 1:
                            itemData = new DataBean("B相基波", item);
                            break;
                        case 2:
                            itemData = new DataBean("C相基波", item);
                            break;
                    }
                } else {
                    itemData = new DataBean(j + 1 + "次", item);
                }

                data.add(itemData);
            }

            DataGroupBean spectrumGroupData = new DataGroupBean(spectrumSectionData[i], data);
            spectrumData.add(spectrumGroupData);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_spectrum, container, false);

        spectrumRecyclerView = (RecyclerView) rootView.findViewById(R.id.spectrum_view);

        GridLayoutManager layoutManager = new GridLayoutManager(getContext(), 2);
        adapter = new DataSectionRecyclerAdapter(getContext(), spectrumData);
        SectionedSpanSizeLookup lookup = new SectionedSpanSizeLookup(adapter, layoutManager);
        layoutManager.setSpanSizeLookup(lookup);
        spectrumRecyclerView.setLayoutManager(layoutManager);
        spectrumRecyclerView.setAdapter(adapter);

        return rootView;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

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
        isSpectrum = true; // 打开实时数据

        tcpClientManager.sendCmd(context, TcpClientManager.TcpCmdType.SPECTRUM_DATA,
                new String[]{deviceData.get(currPosition).getDeviceNo()},
                tcpHandler);
        // 发送信号指令
        mListener.onFragmentInteraction(signalUri);
    }

    @Override
    public void onPause() {
        super.onPause();
        isSpectrum = false; // 关闭实时数据
    }

    @Override
    public void onDetach() {
        super.onDetach();

        mListener = null;
    }

    private void updateHarmonicDates(HarmonicDatasBean datesBean) {

    }

    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }
}
