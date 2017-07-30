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
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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
import cn.edu.siso.rlxapf.bean.HarmonicDatasBean;
import cn.edu.siso.rlxapf.config.TCPConfig;
import cn.edu.siso.rlxapf.util.ProtocolUtil;
import cn.edu.siso.rlxapf.util.TCPUtil;

public class SpectrumFragment extends Fragment implements TCPUtil.OnConnectListener, TCPUtil.OnReceiveListener {

    private TCPUtil tcpUtil = null; // 和有人云通信的对象

    private String deviceParams;

    private RecyclerView spectrumRecyclerView = null;
    private DataSectionRecyclerAdapter adapter = null;

    private List<DataGroupBean> spectrumData = null;

    public static final String TAG = "SpectrumFragment";
    public static final String ARG_DEVICE_PARAM = "device_param";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            deviceParams = getArguments().getString(ARG_DEVICE_PARAM);
        }

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
    }

    @Override
    public void onResume() {
        super.onResume();

        Log.i(TAG, "===onResume===");

        // 建立和有人云的通信
        tcpUtil = new TCPUtil();
        tcpUtil.connect(TCPConfig.DEFAULT_DEVICE_ID, TCPConfig.DEFAULT_DEVICE_PWD, this);
    }

    @Override
    public void onPause() {
        super.onPause();

        Log.i(TAG, "===onPause===");

        // 关闭TCP连接
        tcpUtil.close(TCPConfig.DEFAULT_DEVICE_ID);
    }

    @Override
    public void onSuccess(String deviceId) {
        Log.i(TAG, "onSuccess Device Id = " + deviceId);

        // 发送指令后休息1000ms
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // 执行读取实时数据的指令
        AsyncTaskCompat.executeParallel(new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... params) {
                byte deviceAddr = 0x01;
                tcpUtil.send(ProtocolUtil.readHarmonicSpectrum(deviceAddr), SpectrumFragment.this);
                return null;
            }
        });
    }

    @Override
    public void onError(int errorCode, String errorMsg) {
        Log.i(TAG, "onError Error Code = " + errorCode + " Error Msg = " + errorMsg);
    }

    @Override
    public void onClose(String deviceId) {
        Log.i(TAG, "onClose Device Id = " + deviceId);
    }

    @Override
    public void onReceive(String deviceId, byte[] data) {
        Log.i(TAG, "onReceive Device Id = " + deviceId);
        Log.i(TAG, "onReceive Data = " + Arrays.toString(data));

        HarmonicDatasBean datasBean = new HarmonicDatasBean();
//        int res = datasBean.parse(data);
//
//        if (res == -1) {
//            Log.i(TAG, "CRC教研失败");
//        } else if (res == -2) {
//            Log.i(TAG, "长度解析出错");
//        } else {
//            Log.i(TAG, "数据解析成功");
//
//            updateHarmonicDatas(datasBean);
//
//            // 发送指令后休息100ms
//            try {
//                Thread.sleep(1000);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//
//            // 执行读取实时数据的指令
//            AsyncTaskCompat.executeParallel(new AsyncTask<Void, Void, Void>() {
//                @Override
//                protected Void doInBackground(Void... params) {
//                    byte deviceAddr = 0x01;
//                    tcpUtil.send(ProtocolUtil.readHarmonicSpectrum(deviceAddr), SpectrumFragment.this);
//                    return null;
//                }
//            });
//        }
    }

    private void updateHarmonicDatas(HarmonicDatasBean datasBean) {

    }
}
