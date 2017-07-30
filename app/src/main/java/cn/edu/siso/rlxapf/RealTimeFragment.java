package cn.edu.siso.rlxapf;

import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.os.AsyncTaskCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.Arrays;
import java.util.Timer;
import java.util.TimerTask;

import cn.edu.siso.rlxapf.bean.RealTimeDatasBean;
import cn.edu.siso.rlxapf.config.TCPConfig;
import cn.edu.siso.rlxapf.util.ProtocolUtil;
import cn.edu.siso.rlxapf.util.TCPUtil;


public class RealTimeFragment extends Fragment implements
        TabLayout.OnTabSelectedListener,
        TCPUtil.OnReceiveListener,
        TCPUtil.OnConnectListener {

    private TCPUtil tcpUtil = null; // 和有人云通信的对象

    private IRealTimeData iRealTimeData = null;

    private RealTimeFragmentAdapter adapter = null;

    public static final String TAG = "RealTimeFragment";

    private int[] realTimeIndicatorTitleArray = {
            R.string.main_top_tab_real_curve_title,
            R.string.main_top_tab_real_data_title
    };

    private Fragment[] realTimeTabItemFragmentArray = {
            RealCurveFragment.newInstance(),
            RealDataFragment.newInstance()
    };

    private TabLayout realTimeTab = null;
    private ViewPager realTimePage = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        tcpUtil = new TCPUtil();

        Log.i(TAG, "===onCreate===");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_real_time, container, false);

        realTimeTab = (TabLayout) rootView.findViewById(R.id.real_time_tab);
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
    }

    @Override
    public void onResume() {
        super.onResume();

        Log.i(TAG, "===onResume===");

        tcpUtil.connect(TCPConfig.DEFAULT_DEVICE_ID, TCPConfig.DEFAULT_DEVICE_PWD, this);
    }

    @Override
    public void onPause() {
        super.onPause();

        tcpUtil.close(TCPConfig.DEFAULT_DEVICE_ID);
    }

    @Override
    public void onTabSelected(TabLayout.Tab tab) {
        Log.i(TAG, "Tab Position = " + tab.getPosition());

        realTimePage.setCurrentItem(tab.getPosition());

        // 获取当前需要更新的数据
        iRealTimeData = adapter.getCurrentRealTime();
    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {
        Log.i(TAG, "onTabUnselected");
    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {
        Log.i(TAG, "onTabUnselected");
    }

    @Override
    public void onReceive(String deviceId, byte[] data) {
        Log.i(TAG, "onReceive Device Id = " + deviceId);
        Log.i(TAG, "onReceive Data = " + Arrays.toString(data));

        RealTimeDatasBean datasBean = new RealTimeDatasBean();
        int res = datasBean.parse(data);

        if (res == -1) {
            Log.i(TAG, "CRC教研失败");
        } else if (res == -2) {
            Log.i(TAG, "长度解析出错");
        } else {
            Log.i(TAG, "数据解析成功");

            iRealTimeData.updateRealData(datasBean);
        }

        // 发送指令后休息100ms
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // 执行读取实时数据的指令
        AsyncTaskCompat.executeParallel(new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... params) {
                byte deviceAddr = 0x01;
                tcpUtil.send(ProtocolUtil.readRealTimeDatas(deviceAddr), RealTimeFragment.this);
                return null;
            }
        });
    }

    @Override
    public void onSuccess(String deviceId) {
        Log.i(TAG, "onSuccess Device Id = " + deviceId);

        // 执行读取实时数据的指令
        AsyncTaskCompat.executeParallel(new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... params) {
                byte deviceAddr = 0x01;
                tcpUtil.send(ProtocolUtil.readRealTimeDatas(deviceAddr), RealTimeFragment.this);
                return null;
            }
        });
    }

    @Override
    public void onError(int errorCode, String errorMsg) {
        Log.i(TAG, "onError errorCode = " + errorCode + " errorMsg = " + errorMsg);

        tcpUtil.close(TCPConfig.DEFAULT_DEVICE_ID);
    }

    @Override
    public void onClose(String deviceId) {
        Log.i(TAG, "onClose Device Id = " + deviceId);
    }
}
