package cn.edu.siso.rlxapf;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import cn.edu.siso.rlxapf.bean.DataBean;
import cn.edu.siso.rlxapf.bean.DataGroupBean;
import cn.edu.siso.rlxapf.bean.RealTimeDatasBean;

public class RealCurveFragment extends Fragment implements IRealTimeData {

    private String TAG = "RealCurveFragment";

    private DataCurveRecycleAdapter adapter = null;

    // 系统电压
    private List<Map<String, Float>> systemAVoltageData = null;
    private List<Map<String, Float>> systemBVoltageData = null;
    private List<Map<String, Float>> systemCVoltageData = null;
    // 系统电流
    private List<Map<String, Float>> systemACurrentData = null;
    private List<Map<String, Float>> systemBCurrentData = null;
    private List<Map<String, Float>> systemCCurrentData = null;
    // 负载电流
    private List<Map<String, Float>> loadACurrentData = null;
    private List<Map<String, Float>> loadBCurrentData = null;
    private List<Map<String, Float>> loadCCurrentData = null;
    // 补偿电流
    private List<Map<String, Float>> compensationACurrentData = null;
    private List<Map<String, Float>> compensationBCurrentData = null;
    private List<Map<String, Float>> compensationCCurrentData = null;

    private Context context = null;

    public RealCurveFragment() {
        // Required empty public constructor
    }

    public static RealCurveFragment newInstance() {
        RealCurveFragment fragment = new RealCurveFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Log.i(TAG, "===onCreate===");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.i(TAG, "===onCreateView===");

        View rootView = inflater.inflate(R.layout.fragment_real_curve, container, false);
        RecyclerView curveDataRecyclerView = (RecyclerView) rootView.findViewById(R.id.curve_data_view);

        systemAVoltageData = new ArrayList<Map<String, Float>>();
        systemBVoltageData = new ArrayList<Map<String, Float>>();
        systemCVoltageData = new ArrayList<Map<String, Float>>();
        List<DataBean> systemVoltage = new ArrayList<DataBean>();
        DataBean systemAVoltageDataBean = new DataBean(
                getResources().getString(R.string.curve_a_voltage_title), systemAVoltageData);
        DataBean systemBVoltageDataBean = new DataBean(
                getResources().getString(R.string.curve_b_voltage_title), systemBVoltageData);
        DataBean systemCVoltageDataBean = new DataBean(
                getResources().getString(R.string.curve_c_voltage_title), systemCVoltageData);
        systemVoltage.add(systemAVoltageDataBean);
        systemVoltage.add(systemBVoltageDataBean);
        systemVoltage.add(systemCVoltageDataBean);
        DataGroupBean systemVoltageGroupBean = new DataGroupBean(
                getResources().getString(R.string.curve_voltage_title), systemVoltage);

        systemACurrentData = new ArrayList<Map<String, Float>>();
        systemBCurrentData = new ArrayList<Map<String, Float>>();
        systemCCurrentData = new ArrayList<Map<String, Float>>();
        List<DataBean> systemCurrent = new ArrayList<DataBean>();
        DataBean systemACurrentDataBean = new DataBean(
                getResources().getString(R.string.curve_a_ampere_title), systemACurrentData);
        DataBean systemBCurrentDataBean = new DataBean(
                getResources().getString(R.string.curve_b_ampere_title), systemBCurrentData);
        DataBean systemCCurrentDataBean = new DataBean(
                getResources().getString(R.string.curve_c_ampere_title), systemCCurrentData);
        systemCurrent.add(systemACurrentDataBean);
        systemCurrent.add(systemBCurrentDataBean);
        systemCurrent.add(systemCCurrentDataBean);
        DataGroupBean systemCurrentGroupBean = new DataGroupBean(
                getResources().getString(R.string.curve_ampere_title), systemCurrent);

        loadACurrentData = new ArrayList<Map<String, Float>>();
        loadBCurrentData = new ArrayList<Map<String, Float>>();
        loadCCurrentData = new ArrayList<Map<String, Float>>();
        List<DataBean> loadCurrent = new ArrayList<DataBean>();
        DataBean loadACurrentDataBean = new DataBean(
                getResources().getString(R.string.curve_a_load_title), loadACurrentData);
        DataBean loadBCurrentDataBean = new DataBean(
                getResources().getString(R.string.curve_b_load_title), loadBCurrentData);
        DataBean loadCCurrentDataBean = new DataBean(
                getResources().getString(R.string.curve_c_load_title), loadCCurrentData);
        loadCurrent.add(loadACurrentDataBean);
        loadCurrent.add(loadBCurrentDataBean);
        loadCurrent.add(loadCCurrentDataBean);
        DataGroupBean loadCurrentGroupBean = new DataGroupBean(
                getResources().getString(R.string.curve_load_title), loadCurrent);

        compensationACurrentData = new ArrayList<Map<String, Float>>();
        compensationBCurrentData = new ArrayList<Map<String, Float>>();
        compensationCCurrentData = new ArrayList<Map<String, Float>>();
        List<DataBean> compensationCurrent = new ArrayList<DataBean>();
        DataBean compensationACurrentDataBean = new DataBean(
                getResources().getString(R.string.curve_a_compensate_title), compensationACurrentData);
        DataBean compensationBCurrentDataBean = new DataBean(
                getResources().getString(R.string.curve_b_compensate_title), compensationBCurrentData);
        DataBean compensationCCurrentDataBean = new DataBean(
                getResources().getString(R.string.curve_c_compensate_title), compensationCCurrentData);
        compensationCurrent.add(compensationACurrentDataBean);
        compensationCurrent.add(compensationBCurrentDataBean);
        compensationCurrent.add(compensationCCurrentDataBean);
        DataGroupBean compensationCurrentGroupBean = new DataGroupBean(
                getResources().getString(R.string.curve_compensate_title), compensationCurrent);

        List<DataGroupBean> data = new ArrayList<DataGroupBean>();
        data.add(systemVoltageGroupBean);
        data.add(systemCurrentGroupBean);
        data.add(loadCurrentGroupBean);
        data.add(compensationCurrentGroupBean);

        adapter = new DataCurveRecycleAdapter(context, data);
        LinearLayoutManager layoutManager = new LinearLayoutManager(context);
        curveDataRecyclerView.setLayoutManager(layoutManager);
        curveDataRecyclerView.setAdapter(adapter);

        return rootView;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        this.context = context;

        Log.i(TAG, "===onAttachw===");
    }

    @Override
    public void onDetach() {
        super.onDetach();

        Log.i(TAG, "===onDetach===");
    }

    @Override
    public void onStart() {
        super.onStart();

        Log.i(TAG, "===onStart===");
    }

    @Override
    public void onResume() {
        super.onResume();

        Log.i(TAG, "===onResume===");

        // 系统电压
        systemAVoltageData.clear();
        systemBVoltageData.clear();
        systemCVoltageData.clear();
        // 系统电流
        systemACurrentData.clear();
        systemBCurrentData.clear();
        systemCCurrentData.clear();
        // 负载电流
        loadACurrentData.clear();
        loadBCurrentData.clear();
        loadCCurrentData.clear();
        // 补偿电流
        compensationACurrentData.clear();
        compensationBCurrentData.clear();
        compensationCCurrentData.clear();
    }

    @Override
    public void onPause() {
        super.onPause();

        Log.i(TAG, "===onPause===");
    }

    @Override
    public void onStop() {
        super.onStop();

        Log.i(TAG, "===onStop===");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        Log.i(TAG, "===onDestroyView===");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        Log.i(TAG, "===onDestroy===");
    }

    @Override
    public void updateRealData(RealTimeDatasBean datasBean) {
        // 系统电压A
        double systemAVoltage = datasBean.getSystemAVoltage();
        Map<String, Float> systemAVoltageValue = new HashMap<String, Float>();
        systemAVoltageValue.put(DataBean.DATA_KEY, (float) systemAVoltage);
        if (systemAVoltageData.size() == 10) {
            systemAVoltageData.remove(0);
        }
        systemAVoltageData.add(systemAVoltageValue);

        // 系统电压B
        double systemBVoltage = datasBean.getSystemBVoltage();
        Map<String, Float> systemBVoltageValue = new HashMap<String, Float>();
        systemBVoltageValue.put(DataBean.DATA_KEY, (float) systemBVoltage);
        if (systemBVoltageData.size() == 10) {
            systemBVoltageData.remove(0);
        }
        systemBVoltageData.add(systemBVoltageValue);

        // 系统电压C
        double systemCVoltage = datasBean.getSystemCVoltage();
        Map<String, Float> systemCVoltageValue = new HashMap<String, Float>();
        systemCVoltageValue.put(DataBean.DATA_KEY, (float) systemCVoltage);
        if (systemCVoltageData.size() == 10) {
            systemCVoltageData.remove(0);
        }
        systemCVoltageData.add(systemCVoltageValue);

        // 系统电流A
        int systemACurrent =  datasBean.getSystemACurrent();
        Map<String, Float> systemACurrentValue = new HashMap<String, Float>();
        systemACurrentValue.put(DataBean.DATA_KEY, (float) systemACurrent);
        if (systemACurrentData.size() == 10) {
            systemACurrentData.remove(0);
        }
        systemACurrentData.add(systemACurrentValue);

        // 系统电流B
        int systemBCurrent =  datasBean.getSystemBCurrent();
        Map<String, Float> systemBCurrentValue = new HashMap<String, Float>();
        systemBCurrentValue.put(DataBean.DATA_KEY, (float) systemBCurrent);
        if (systemBCurrentData.size() == 10) {
            systemBCurrentData.remove(0);
        }
        systemBCurrentData.add(systemBCurrentValue);

        // 系统电流C
        int systemCCurrent =  datasBean.getSystemCCurrent();
        Map<String, Float> systemCCurrentValue = new HashMap<String, Float>();
        systemCCurrentValue.put(DataBean.DATA_KEY, (float) systemCCurrent);
        if (systemCCurrentData.size() == 10) {
            systemCCurrentData.remove(0);
        }
        systemCCurrentData.add(systemCCurrentValue);

        // 负载电流A
        int loadACurrent = datasBean.getLoadACurrent();
        Map<String, Float> loadACurrentValue = new HashMap<String, Float>();
        loadACurrentValue.put(DataBean.DATA_KEY, (float) loadACurrent);
        if (loadACurrentData.size() == 10) {
            loadACurrentData.remove(0);
        }
        loadACurrentData.add(loadACurrentValue);

        // 负载电流B
        int loadBCurrent = datasBean.getLoadBCurrent();
        Map<String, Float> loadBCurrentValue = new HashMap<String, Float>();
        loadBCurrentValue.put(DataBean.DATA_KEY, (float) loadBCurrent);
        if (loadBCurrentData.size() == 10) {
            loadBCurrentData.remove(0);
        }
        loadBCurrentData.add(loadBCurrentValue);

        // 负载电流C
        int loadCCurrent = datasBean.getLoadCCurrent();
        Map<String, Float> loadCCurrentValue = new HashMap<String, Float>();
        loadCCurrentValue.put(DataBean.DATA_KEY, (float) loadCCurrent);
        if (loadCCurrentData.size() == 10) {
            loadCCurrentData.remove(0);
        }
        loadCCurrentData.add(loadCCurrentValue);

        // 补偿电流A
        double compensationACurrent =  datasBean.getCompensationACurrent();
        BigDecimal compensationACurrentBD = new BigDecimal(compensationACurrent);
        compensationACurrent = compensationACurrentBD.setScale(
                3, BigDecimal.ROUND_HALF_UP).doubleValue();
        Map<String, Float> compensationACurrentValue = new HashMap<String, Float>();
        compensationACurrentValue.put(DataBean.DATA_KEY, (float) compensationACurrent);
        if (compensationACurrentData.size() == 10) {
            compensationACurrentData.remove(0);
        }
        compensationACurrentData.add(compensationACurrentValue);

        // 补偿电流B
        double compensationBCurrent =  datasBean.getCompensationBCurrent();
        BigDecimal compensationBCurrentBD = new BigDecimal(compensationBCurrent);
        compensationBCurrent = compensationBCurrentBD.setScale(
                3, BigDecimal.ROUND_HALF_UP).doubleValue();
        Map<String, Float> compensationBCurrentValue = new HashMap<String, Float>();
        compensationBCurrentValue.put(DataBean.DATA_KEY, Float.valueOf(String.valueOf(compensationBCurrent)));
        if (compensationBCurrentData.size() == 10) {
            compensationBCurrentData.remove(0);
        }
        compensationBCurrentData.add(compensationBCurrentValue);

        // 补偿电流C
        double compensationCCurrent =  datasBean.getCompensationCCurrent();
        BigDecimal compensationCCurrentBD = new BigDecimal(compensationCCurrent);
        compensationCCurrent = compensationCCurrentBD.setScale(
                3, BigDecimal.ROUND_HALF_UP).doubleValue();
        Map<String, Float> compensationCCurrentValue = new HashMap<String, Float>();
        compensationCCurrentValue.put(DataBean.DATA_KEY, Float.valueOf(String.valueOf(compensationCCurrent)));
        if (compensationCCurrentData.size() == 10) {
            compensationCCurrentData.remove(0);
        }
        compensationCCurrentData.add(compensationCCurrentValue);

        adapter.notifyDataSetChanged();
    }
}
