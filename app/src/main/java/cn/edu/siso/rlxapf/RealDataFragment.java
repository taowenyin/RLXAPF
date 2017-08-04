package cn.edu.siso.rlxapf;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
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

import cn.edu.siso.rlxapf.bean.DataBean;
import cn.edu.siso.rlxapf.bean.DataGroupBean;
import cn.edu.siso.rlxapf.bean.RealTimeDatasBean;

public class RealDataFragment extends Fragment implements IRealTimeData {

    private RecyclerView realDataRecyclerView = null;
    private DataSectionRecyclerAdapter adapter = null;

    private List<DataBean> voltageData = null;
    private List<DataBean> powerData = null;
    private List<DataBean> thdData = null;
    private List<DataGroupBean> realData = null;

    private String[] voltageTitles = null;
    private String[] powerTitles = null;
    private String[] thdTitles = null;

    public static final String TAG = "RealDataFragment";

    public RealDataFragment() {
        // Required empty public constructor
    }

    public static RealDataFragment newInstance() {
        RealDataFragment fragment = new RealDataFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        String[] realDataSections = getResources().getStringArray(R.array.real_data_sections);
        voltageTitles = getResources().getStringArray(R.array.real_data_voltage_titles);
        powerTitles = getResources().getStringArray(R.array.real_data_power_titles);
        thdTitles = getResources().getStringArray(R.array.real_data_thd_titles);

        // 数据列表
        this.realData = new ArrayList<DataGroupBean>();

        // 添加电压、电流数据
        voltageData = new ArrayList<DataBean>();
        for (int i = 0; i < voltageTitles.length; i++) {
            List<Map<String, Integer>> item = new ArrayList<Map<String, Integer>>();
            Map<String, Integer> value = new HashMap<String, Integer>();
            value.put(DataBean.DATA_KEY, 0);
            item.add(value);
            DataBean data = new DataBean(voltageTitles[i], item);
            voltageData.add(data);
        }
        DataGroupBean voltageGroupData = new DataGroupBean(realDataSections[0], voltageData);
        realData.add(voltageGroupData);

        // 添加功率数据
        powerData = new ArrayList<DataBean>();
        for (int i = 0; i < powerTitles.length; i++) {
            List<Map<String, Integer>> item = new ArrayList<Map<String, Integer>>();
            Map<String, Integer> value = new HashMap<String, Integer>();
            value.put(DataBean.DATA_KEY, 0);
            item.add(value);
            DataBean data = new DataBean(powerTitles[i], item);
            powerData.add(data);
        }
        DataGroupBean powerGroupData = new DataGroupBean(realDataSections[1], powerData);
        realData.add(powerGroupData);

        // 添加thd数据
        thdData = new ArrayList<DataBean>();
        for (int i = 0; i < thdTitles.length; i++) {
            List<Map<String, Integer>> item = new ArrayList<Map<String, Integer>>();
            Map<String, Integer> value = new HashMap<String, Integer>();
            value.put(DataBean.DATA_KEY, 0);
            item.add(value);
            DataBean data = new DataBean(thdTitles[i], item);
            thdData.add(data);
        }
        DataGroupBean thdGroupData = new DataGroupBean(realDataSections[2], thdData);
        realData.add(thdGroupData);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_real_data, container, false);

        realDataRecyclerView = (RecyclerView) rootView.findViewById(R.id.real_data_view);

        GridLayoutManager layoutManager = new GridLayoutManager(getContext(), 2);
        adapter = new DataSectionRecyclerAdapter(getContext(), realData);
        RealDataSpanSizeLookup lookup = new RealDataSpanSizeLookup(adapter, layoutManager);
        layoutManager.setSpanSizeLookup(lookup);
        realDataRecyclerView.setLayoutManager(layoutManager);
        realDataRecyclerView.setAdapter(adapter);

        return rootView;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void updateRealData(RealTimeDatasBean datasBean) {
        voltageData.clear();
        // 系统电压A
        double systemAVoltage = datasBean.getSystemAVoltage();
        BigDecimal systemAVoltageBD = new BigDecimal(systemAVoltage);
        systemAVoltage = systemAVoltageBD.setScale(1, BigDecimal.ROUND_HALF_UP).doubleValue();
        List<Map<String, Double>> systemAVoltageItem = new ArrayList<Map<String, Double>>();
        Map<String, Double> systemAVoltageValue = new HashMap<String, Double>();
        systemAVoltageValue.put(DataBean.DATA_KEY, systemAVoltage);
        systemAVoltageItem.add(systemAVoltageValue);
        DataBean systemAVoltageData = new DataBean(voltageTitles[0], systemAVoltageItem);
        voltageData.add(systemAVoltageData);

        // 系统电压B
        double systemBVoltage = datasBean.getSystemBVoltage();
        BigDecimal systemBVoltageBD = new BigDecimal(systemBVoltage);
        systemBVoltage = systemBVoltageBD.setScale(1, BigDecimal.ROUND_HALF_UP).doubleValue();
        List<Map<String, Double>> systemBVoltageItem = new ArrayList<Map<String, Double>>();
        Map<String, Double> systemBVoltageValue = new HashMap<String, Double>();
        systemBVoltageValue.put(DataBean.DATA_KEY, systemBVoltage);
        systemBVoltageItem.add(systemBVoltageValue);
        DataBean systemBVoltageData = new DataBean(voltageTitles[1], systemBVoltageItem);
        voltageData.add(systemBVoltageData);

        // 系统电压C
        double systemCVoltage = datasBean.getSystemCVoltage();
        BigDecimal systemCVoltageBD = new BigDecimal(systemCVoltage);
        systemCVoltage = systemCVoltageBD.setScale(1, BigDecimal.ROUND_HALF_UP).doubleValue();
        List<Map<String, Double>> systemCVoltageItem = new ArrayList<Map<String, Double>>();
        Map<String, Double> systemCVoltageValue = new HashMap<String, Double>();
        systemCVoltageValue.put(DataBean.DATA_KEY, systemCVoltage);
        systemCVoltageItem.add(systemCVoltageValue);
        DataBean systemCVoltageData = new DataBean(voltageTitles[2], systemCVoltageItem);
        voltageData.add(systemCVoltageData);

        // 系统电流A
        int systemACurrent =  datasBean.getSystemACurrent();
        List<Map<String, Integer>> systemACurrentItem = new ArrayList<Map<String, Integer>>();
        Map<String, Integer> systemACurrentValue = new HashMap<String, Integer>();
        systemACurrentValue.put(DataBean.DATA_KEY, systemACurrent);
        systemACurrentItem.add(systemACurrentValue);
        DataBean systemACurrentData = new DataBean(voltageTitles[3], systemACurrentItem);
        voltageData.add(systemACurrentData);

        // 系统电流B
        int systemBCurrent =  datasBean.getSystemBCurrent();
        List<Map<String, Integer>> systemBCurrentItem = new ArrayList<Map<String, Integer>>();
        Map<String, Integer> systemBCurrentValue = new HashMap<String, Integer>();
        systemBCurrentValue.put(DataBean.DATA_KEY, systemBCurrent);
        systemBCurrentItem.add(systemBCurrentValue);
        DataBean systemBCurrentData = new DataBean(voltageTitles[4], systemBCurrentItem);
        voltageData.add(systemBCurrentData);

        // 系统电流C
        int systemCCurrent =  datasBean.getSystemCCurrent();
        List<Map<String, Integer>> systemCCurrentItem = new ArrayList<Map<String, Integer>>();
        Map<String, Integer> systemCCurrentValue = new HashMap<String, Integer>();
        systemCCurrentValue.put(DataBean.DATA_KEY, systemCCurrent);
        systemCCurrentItem.add(systemCCurrentValue);
        DataBean systemCCurrentData = new DataBean(voltageTitles[5], systemCCurrentItem);
        voltageData.add(systemCCurrentData);

        // 负载电流A
        int loadACurrent = datasBean.getLoadACurrent();
        List<Map<String, Integer>> loadACurrentItem = new ArrayList<Map<String, Integer>>();
        Map<String, Integer> loadACurrentValue = new HashMap<String, Integer>();
        loadACurrentValue.put(DataBean.DATA_KEY, loadACurrent);
        loadACurrentItem.add(loadACurrentValue);
        DataBean loadACurrentData = new DataBean(voltageTitles[6], loadACurrentItem);
        voltageData.add(loadACurrentData);

        // 负载电流B
        int loadBCurrent = datasBean.getLoadBCurrent();
        List<Map<String, Integer>> loadBCurrentItem = new ArrayList<Map<String, Integer>>();
        Map<String, Integer> loadBCurrentValue = new HashMap<String, Integer>();
        loadBCurrentValue.put(DataBean.DATA_KEY, loadBCurrent);
        loadBCurrentItem.add(loadBCurrentValue);
        DataBean loadBCurrentData = new DataBean(voltageTitles[7], loadBCurrentItem);
        voltageData.add(loadBCurrentData);

        // 负载电流C
        int loadCCurrent = datasBean.getLoadCCurrent();
        List<Map<String, Integer>> loadCCurrentItem = new ArrayList<Map<String, Integer>>();
        Map<String, Integer> loadCCurrentValue = new HashMap<String, Integer>();
        loadCCurrentValue.put(DataBean.DATA_KEY, loadCCurrent);
        loadCCurrentItem.add(loadCCurrentValue);
        DataBean loadCCurrentData = new DataBean(voltageTitles[8], loadCCurrentItem);
        voltageData.add(loadCCurrentData);

        // 补偿电流A
        double compensationACurrent =  datasBean.getCompensationACurrent();
        BigDecimal compensationACurrentBD = new BigDecimal(compensationACurrent);
        compensationACurrent = compensationACurrentBD.setScale(1, BigDecimal.ROUND_HALF_UP).doubleValue();
        List<Map<String, Double>> compensationACurrentItem = new ArrayList<Map<String, Double>>();
        Map<String, Double> compensationACurrentValue = new HashMap<String, Double>();
        compensationACurrentValue.put(DataBean.DATA_KEY, compensationACurrent);
        compensationACurrentItem.add(compensationACurrentValue);
        DataBean compensationACurrentData = new DataBean(voltageTitles[9], compensationACurrentItem);
        voltageData.add(compensationACurrentData);

        // 补偿电流B
        double compensationBCurrent =  datasBean.getCompensationBCurrent();
        BigDecimal compensationBCurrentBD = new BigDecimal(compensationBCurrent);
        compensationBCurrent = compensationBCurrentBD.setScale(1, BigDecimal.ROUND_HALF_UP).doubleValue();
        List<Map<String, Double>> compensationBCurrentItem = new ArrayList<Map<String, Double>>();
        Map<String, Double> compensationBCurrentValue = new HashMap<String, Double>();
        compensationBCurrentValue.put(DataBean.DATA_KEY, compensationBCurrent);
        compensationBCurrentItem.add(compensationBCurrentValue);
        DataBean compensationBCurrentData = new DataBean(voltageTitles[10], compensationBCurrentItem);
        voltageData.add(compensationBCurrentData);

        // 补偿电流C
        double compensationCCurrent =  datasBean.getCompensationCCurrent();
        BigDecimal compensationCCurrentBD = new BigDecimal(compensationCCurrent);
        compensationCCurrent = compensationCCurrentBD.setScale(1, BigDecimal.ROUND_HALF_UP).doubleValue();
        List<Map<String, Double>> compensationCCurrentItem = new ArrayList<Map<String, Double>>();
        Map<String, Double> compensationCCurrentValue = new HashMap<String, Double>();
        compensationCCurrentValue.put(DataBean.DATA_KEY, compensationCCurrent);
        compensationCCurrentItem.add(compensationCCurrentValue);
        DataBean compensationCCurrentData = new DataBean(voltageTitles[11], compensationCCurrentItem);
        voltageData.add(compensationCCurrentData);

        powerData.clear();
        // 系统功率因素
        double systemPowerFactor = datasBean.getSystemPowerFactor();
        BigDecimal systemPowerFactorBD = new BigDecimal(systemPowerFactor);
        systemPowerFactor = systemPowerFactorBD.setScale(1, BigDecimal.ROUND_HALF_UP).doubleValue();
        List<Map<String, Double>> systemPowerFactorItem = new ArrayList<Map<String, Double>>();
        Map<String, Double> systemPowerFactorValue = new HashMap<String, Double>();
        systemPowerFactorValue.put(DataBean.DATA_KEY, systemPowerFactor);
        systemPowerFactorItem.add(systemPowerFactorValue);
        DataBean systemPowerFactorData = new DataBean(powerTitles[0], systemPowerFactorItem);
        powerData.add(systemPowerFactorData);

        // 系统电流不平衡度
        double systemCurrentUnbalanceDegree = datasBean.getSystemCurrentUnbalanceDegree();
        BigDecimal systemCurrentUnbalanceDegreeBD = new BigDecimal(systemCurrentUnbalanceDegree);
        systemCurrentUnbalanceDegree = systemCurrentUnbalanceDegreeBD.setScale(1, BigDecimal.ROUND_HALF_UP).doubleValue();
        List<Map<String, Double>> systemCurrentUnbalanceDegreeItem = new ArrayList<Map<String, Double>>();
        Map<String, Double> systemCurrentUnbalanceDegreeValue = new HashMap<String, Double>();
        systemCurrentUnbalanceDegreeValue.put(DataBean.DATA_KEY, systemCurrentUnbalanceDegree);
        systemCurrentUnbalanceDegreeItem.add(systemCurrentUnbalanceDegreeValue);
        DataBean systemCurrentUnbalanceDegreeData = new DataBean(powerTitles[1], systemCurrentUnbalanceDegreeItem);
        powerData.add(systemCurrentUnbalanceDegreeData);

        // 负载功率因素
        double loadPowerFactor = datasBean.getLoadPowerFactor();
        BigDecimal loadPowerFactorBD = new BigDecimal(loadPowerFactor);
        loadPowerFactor = loadPowerFactorBD.setScale(1, BigDecimal.ROUND_HALF_UP).doubleValue();
        List<Map<String, Double>> loadPowerFactorItem = new ArrayList<Map<String, Double>>();
        Map<String, Double> loadPowerFactorValue = new HashMap<String, Double>();
        loadPowerFactorValue.put(DataBean.DATA_KEY, loadPowerFactor);
        loadPowerFactorItem.add(loadPowerFactorValue);
        DataBean loadPowerFactorData = new DataBean(powerTitles[2], loadPowerFactorItem);
        powerData.add(loadPowerFactorData);

        // 负载电流不平衡度
        double loadCurrentUnbalanceDegree = datasBean.getLoadCurrentUnbalanceDegree();
        BigDecimal loadCurrentUnbalanceDegreeBD = new BigDecimal(loadCurrentUnbalanceDegree);
        loadCurrentUnbalanceDegree = loadCurrentUnbalanceDegreeBD.setScale(1, BigDecimal.ROUND_HALF_UP).doubleValue();
        List<Map<String, Double>> loadCurrentUnbalanceDegreeItem = new ArrayList<Map<String, Double>>();
        Map<String, Double> loadCurrentUnbalanceDegreeValue = new HashMap<String, Double>();
        loadCurrentUnbalanceDegreeValue.put(DataBean.DATA_KEY, loadCurrentUnbalanceDegree);
        loadCurrentUnbalanceDegreeItem.add(loadCurrentUnbalanceDegreeValue);
        DataBean loadCurrentUnbalanceDegreeData = new DataBean(powerTitles[3], loadCurrentUnbalanceDegreeItem);
        powerData.add(loadCurrentUnbalanceDegreeData);

        // IGBT温度
        int tempratureIGBT = datasBean.getTempratureIGBT();
        List<Map<String, Integer>> tempratureIGBTItem = new ArrayList<Map<String, Integer>>();
        Map<String, Integer> tempratureIGBTValue = new HashMap<String, Integer>();
        tempratureIGBTValue.put(DataBean.DATA_KEY, tempratureIGBT);
        tempratureIGBTItem.add(tempratureIGBTValue);
        DataBean tempratureIGBTData = new DataBean(powerTitles[4], tempratureIGBTItem);
        powerData.add(tempratureIGBTData);

        thdData.clear();
        // A相电压总畸变率
        double voltageATotalDistortionRate = datasBean.getVoltageATotalDistortionRate();
        BigDecimal voltageATotalDistortionRateBD = new BigDecimal(voltageATotalDistortionRate);
        voltageATotalDistortionRate = voltageATotalDistortionRateBD.setScale(1, BigDecimal.ROUND_HALF_UP).doubleValue();
        List<Map<String, Double>> voltageATotalDistortionRateItem = new ArrayList<Map<String, Double>>();
        Map<String, Double> voltageATotalDistortionRateValue = new HashMap<String, Double>();
        voltageATotalDistortionRateValue.put(DataBean.DATA_KEY, voltageATotalDistortionRate);
        voltageATotalDistortionRateItem.add(voltageATotalDistortionRateValue);
        DataBean voltageATotalDistortionRateData = new DataBean(thdTitles[0], voltageATotalDistortionRateItem);
        thdData.add(voltageATotalDistortionRateData);

        // B相电压总畸变率
        double voltageBTotalDistortionRate = datasBean.getVoltageBTotalDistortionRate();
        BigDecimal voltageBTotalDistortionRateBD = new BigDecimal(voltageBTotalDistortionRate);
        voltageBTotalDistortionRate = voltageBTotalDistortionRateBD.setScale(1, BigDecimal.ROUND_HALF_UP).doubleValue();
        List<Map<String, Double>> voltageBTotalDistortionRateItem = new ArrayList<Map<String, Double>>();
        Map<String, Double> voltageBTotalDistortionRateValue = new HashMap<String, Double>();
        voltageBTotalDistortionRateValue.put(DataBean.DATA_KEY, voltageBTotalDistortionRate);
        voltageBTotalDistortionRateItem.add(voltageBTotalDistortionRateValue);
        DataBean voltageBTotalDistortionRateData = new DataBean(thdTitles[1], voltageBTotalDistortionRateItem);
        thdData.add(voltageBTotalDistortionRateData);

        // C相电压总畸变率
        double voltageCTotalDistortionRate = datasBean.getVoltageCTotalDistortionRate();
        BigDecimal voltageCTotalDistortionRateBD = new BigDecimal(voltageCTotalDistortionRate);
        voltageCTotalDistortionRate = voltageCTotalDistortionRateBD.setScale(1, BigDecimal.ROUND_HALF_UP).doubleValue();
        List<Map<String, Double>> voltageCTotalDistortionRateItem = new ArrayList<Map<String, Double>>();
        Map<String, Double> voltageCTotalDistortionRateValue = new HashMap<String, Double>();
        voltageCTotalDistortionRateValue.put(DataBean.DATA_KEY, voltageCTotalDistortionRate);
        voltageCTotalDistortionRateItem.add(voltageCTotalDistortionRateValue);
        DataBean voltageCTotalDistortionRateData = new DataBean(thdTitles[2], voltageCTotalDistortionRateItem);
        thdData.add(voltageCTotalDistortionRateData);

        // 系统A相电流总畸变率
        double systemCurrentATotalDistortionRate = datasBean.getSystemCurrentATotalDistortionRate();
        BigDecimal systemCurrentATotalDistortionRateBD = new BigDecimal(systemCurrentATotalDistortionRate);
        systemCurrentATotalDistortionRate = systemCurrentATotalDistortionRateBD.setScale(1, BigDecimal.ROUND_HALF_UP).doubleValue();
        List<Map<String, Double>> systemCurrentATotalDistortionRateItem = new ArrayList<Map<String, Double>>();
        Map<String, Double> systemCurrentATotalDistortionRateValue = new HashMap<String, Double>();
        systemCurrentATotalDistortionRateValue.put(DataBean.DATA_KEY, systemCurrentATotalDistortionRate);
        systemCurrentATotalDistortionRateItem.add(systemCurrentATotalDistortionRateValue);
        DataBean systemCurrentATotalDistortionRateData = new DataBean(thdTitles[3], systemCurrentATotalDistortionRateItem);
        thdData.add(systemCurrentATotalDistortionRateData);

        // 系统B相电流总畸变率
        double systemCurrentBTotalDistortionRate = datasBean.getSystemCurrentBTotalDistortionRate();
        BigDecimal systemCurrentBTotalDistortionRateBD = new BigDecimal(systemCurrentBTotalDistortionRate);
        systemCurrentBTotalDistortionRate = systemCurrentBTotalDistortionRateBD.setScale(1, BigDecimal.ROUND_HALF_UP).doubleValue();
        List<Map<String, Double>> systemCurrentBTotalDistortionRateItem = new ArrayList<Map<String, Double>>();
        Map<String, Double> systemCurrentBTotalDistortionRateValue = new HashMap<String, Double>();
        systemCurrentBTotalDistortionRateValue.put(DataBean.DATA_KEY, systemCurrentBTotalDistortionRate);
        systemCurrentBTotalDistortionRateItem.add(systemCurrentBTotalDistortionRateValue);
        DataBean systemCurrentBTotalDistortionRateData = new DataBean(thdTitles[4], systemCurrentBTotalDistortionRateItem);
        thdData.add(systemCurrentBTotalDistortionRateData);

        // 系统C相电流总畸变率
        double systemCurrentCTotalDistortionRate = datasBean.getSystemCurrentCTotalDistortionRate();
        BigDecimal systemCurrentCTotalDistortionRateBD = new BigDecimal(systemCurrentCTotalDistortionRate);
        systemCurrentCTotalDistortionRate = systemCurrentCTotalDistortionRateBD.setScale(1, BigDecimal.ROUND_HALF_UP).doubleValue();
        List<Map<String, Double>> systemCurrentCTotalDistortionRateItem = new ArrayList<Map<String, Double>>();
        Map<String, Double> systemCurrentCTotalDistortionRateValue = new HashMap<String, Double>();
        systemCurrentCTotalDistortionRateValue.put(DataBean.DATA_KEY, systemCurrentCTotalDistortionRate);
        systemCurrentCTotalDistortionRateItem.add(systemCurrentCTotalDistortionRateValue);
        DataBean systemCurrentCTotalDistortionRateData = new DataBean(thdTitles[5], systemCurrentCTotalDistortionRateItem);
        thdData.add(systemCurrentCTotalDistortionRateData);

        // 负载A相电流总畸变率
        double loadCurrentATotalDistortionRate = datasBean.getLoadCurrentATotalDistortionRate();
        BigDecimal loadCurrentATotalDistortionRateBD = new BigDecimal(loadCurrentATotalDistortionRate);
        loadCurrentATotalDistortionRate = loadCurrentATotalDistortionRateBD.setScale(1, BigDecimal.ROUND_HALF_UP).doubleValue();
        List<Map<String, Double>> loadCurrentATotalDistortionRateItem = new ArrayList<Map<String, Double>>();
        Map<String, Double> loadCurrentATotalDistortionRateValue = new HashMap<String, Double>();
        loadCurrentATotalDistortionRateValue.put(DataBean.DATA_KEY, loadCurrentATotalDistortionRate);
        loadCurrentATotalDistortionRateItem.add(loadCurrentATotalDistortionRateValue);
        DataBean loadCurrentATotalDistortionRateData = new DataBean(thdTitles[6], loadCurrentATotalDistortionRateItem);
        thdData.add(loadCurrentATotalDistortionRateData);

        // 负载B相电流总畸变率
        double loadCurrentBTotalDistortionRate = datasBean.getLoadCurrentBTotalDistortionRate();
        BigDecimal loadCurrentBTotalDistortionRateBD = new BigDecimal(loadCurrentBTotalDistortionRate);
        loadCurrentBTotalDistortionRate = loadCurrentBTotalDistortionRateBD.setScale(1, BigDecimal.ROUND_HALF_UP).doubleValue();
        List<Map<String, Double>> loadCurrentBTotalDistortionRateItem = new ArrayList<Map<String, Double>>();
        Map<String, Double> loadCurrentBTotalDistortionRateValue = new HashMap<String, Double>();
        loadCurrentBTotalDistortionRateValue.put(DataBean.DATA_KEY, loadCurrentBTotalDistortionRate);
        loadCurrentBTotalDistortionRateItem.add(loadCurrentBTotalDistortionRateValue);
        DataBean loadCurrentBTotalDistortionRateData = new DataBean(thdTitles[7], loadCurrentBTotalDistortionRateItem);
        thdData.add(loadCurrentBTotalDistortionRateData);

        // 负载C相电流总畸变率
        double loadCurrentCTotalDistortionRate = datasBean.getLoadCurrentCTotalDistortionRate();
        BigDecimal loadCurrentCTotalDistortionRateBD = new BigDecimal(loadCurrentCTotalDistortionRate);
        loadCurrentCTotalDistortionRate = loadCurrentCTotalDistortionRateBD.setScale(1, BigDecimal.ROUND_HALF_UP).doubleValue();
        List<Map<String, Double>> loadCurrentCTotalDistortionRateItem = new ArrayList<Map<String, Double>>();
        Map<String, Double> loadCurrentCTotalDistortionRateValue = new HashMap<String, Double>();
        loadCurrentCTotalDistortionRateValue.put(DataBean.DATA_KEY, loadCurrentCTotalDistortionRate);
        loadCurrentCTotalDistortionRateItem.add(loadCurrentCTotalDistortionRateValue);
        DataBean loadCurrentCTotalDistortionRateData = new DataBean(thdTitles[8], loadCurrentCTotalDistortionRateItem);
        thdData.add(loadCurrentCTotalDistortionRateData);

        adapter.notifyDataSetChanged();
    }
}
