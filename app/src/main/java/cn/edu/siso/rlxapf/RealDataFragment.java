package cn.edu.siso.rlxapf;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

import cn.edu.siso.rlxapf.bean.DataBean;
import cn.edu.siso.rlxapf.bean.DataGroupBean;
import cn.edu.siso.rlxapf.bean.RealTimeDatasBean;
import cn.edu.siso.rlxapf.recycle.AbstractItem;
import cn.edu.siso.rlxapf.recycle.real.RealDataHeaderItem;
import cn.edu.siso.rlxapf.recycle.real.RealDataRecycleAdapter;
import cn.edu.siso.rlxapf.recycle.real.RealDataSubHeaderItem;
import cn.edu.siso.rlxapf.recycle.real.RealDataSubItem;
import eu.davidea.flexibleadapter.common.SmoothScrollGridLayoutManager;
import eu.davidea.flexibleadapter.items.AbstractFlexibleItem;

public class RealDataFragment extends Fragment implements IRealTimeData {

    private RecyclerView realDataRecyclerView = null;

    private List<AbstractFlexibleItem> realDataItems = null;
    private RealDataRecycleAdapter adapter = null;

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

        realDataItems = new ArrayList<AbstractFlexibleItem>();
        JSONArray realDataTitleArray = null;
        try {
            realDataTitleArray = JSONArray.parseArray(IOUtils.toString(
                   getResources().getAssets().open("json/real.json")));
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (realDataTitleArray != null) {
            for (int i = 0; i < realDataTitleArray.size(); i++) {
                JSONObject headerItem = realDataTitleArray.getJSONObject(i);

                // 添加一级标题
                String headerTitle = headerItem.getString("name");
                RealDataHeaderItem header = new RealDataHeaderItem(headerTitle, AbstractItem.ITEM_TYPE.REAL, getContext());
                header.setTitle(headerTitle);
                realDataItems.add(header);

                JSONArray subHeaderItems = headerItem.getJSONArray("data");
                if (subHeaderItems != null) {
                    for (int j = 0; j < subHeaderItems.size(); j++) {
                        JSONObject subHeaderItem = subHeaderItems.getJSONObject(j);

                        // 添加二级标题
                        String subHeaderTitle = subHeaderItem.getString("name");
                        RealDataSubHeaderItem subHeader = new RealDataSubHeaderItem(headerTitle + "-" + subHeaderTitle, AbstractItem.ITEM_TYPE.REAL, header, getContext());
                        subHeader.setTitle(subHeaderTitle);
                        realDataItems.add(subHeader);

                        JSONArray subItems = subHeaderItem.getJSONArray("data");
                        if (subItems != null) {
                            for (int k = 0; k < subItems.size(); k++) {
                                JSONObject subItem = subItems.getJSONObject(k);

                                // 添加三级标题
                                String subTitle = subItem.getString("name");
                                RealDataSubItem sub = new RealDataSubItem(headerTitle + "-" + subHeaderTitle + "-" + subTitle, AbstractItem.ITEM_TYPE.REAL, subHeader, getContext());
                                sub.setTitle(subTitle);
                                sub.setValue(String.valueOf(0));
                                realDataItems.add(sub);
                            }
                        }
                    }
                }
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_real_data, container, false);

        realDataRecyclerView = (RecyclerView) rootView.findViewById(R.id.real_data_view);

        adapter = new RealDataRecycleAdapter(realDataItems);
        GridLayoutManager gridLayoutManager = new SmoothScrollGridLayoutManager(getContext(), 3);
        gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                AbstractItem item = (AbstractItem) adapter.getItem(position);
                switch (adapter.getItemViewType(position)) {
                    case R.layout.data_header_item_layout:
                    case R.layout.data_sub_header_item_layout:
                        return 3;
                    default:
                        return 1;
                }
            }
        });

        realDataRecyclerView.setLayoutManager(gridLayoutManager);
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

        // 系统电压A
        double systemAVoltage = datasBean.getSystemAVoltage();
        BigDecimal systemAVoltageBD = new BigDecimal(systemAVoltage);
        systemAVoltage = systemAVoltageBD.setScale(1, BigDecimal.ROUND_HALF_UP).doubleValue();

        // 系统电压B
        double systemBVoltage = datasBean.getSystemBVoltage();
        BigDecimal systemBVoltageBD = new BigDecimal(systemBVoltage);
        systemBVoltage = systemBVoltageBD.setScale(1, BigDecimal.ROUND_HALF_UP).doubleValue();

        // 系统电压C
        double systemCVoltage = datasBean.getSystemCVoltage();
        BigDecimal systemCVoltageBD = new BigDecimal(systemCVoltage);
        systemCVoltage = systemCVoltageBD.setScale(1, BigDecimal.ROUND_HALF_UP).doubleValue();

        // 系统电流A
        int systemACurrent =  datasBean.getSystemACurrent();

        // 系统电流B
        int systemBCurrent =  datasBean.getSystemBCurrent();

        // 系统电流C
        int systemCCurrent =  datasBean.getSystemCCurrent();

        // 负载电流A
        int loadACurrent = datasBean.getLoadACurrent();

        // 负载电流B
        int loadBCurrent = datasBean.getLoadBCurrent();

        // 负载电流C
        int loadCCurrent = datasBean.getLoadCCurrent();

        // 补偿电流A
        double compensationACurrent =  datasBean.getCompensationACurrent();
        BigDecimal compensationACurrentBD = new BigDecimal(compensationACurrent);
        compensationACurrent = compensationACurrentBD.setScale(1, BigDecimal.ROUND_HALF_UP).doubleValue();

        // 补偿电流B
        double compensationBCurrent =  datasBean.getCompensationBCurrent();
        BigDecimal compensationBCurrentBD = new BigDecimal(compensationBCurrent);
        compensationBCurrent = compensationBCurrentBD.setScale(1, BigDecimal.ROUND_HALF_UP).doubleValue();

        // 补偿电流C
        double compensationCCurrent =  datasBean.getCompensationCCurrent();
        BigDecimal compensationCCurrentBD = new BigDecimal(compensationCCurrent);
        compensationCCurrent = compensationCCurrentBD.setScale(1, BigDecimal.ROUND_HALF_UP).doubleValue();

        // 直流电压1
        int voltageDC1 = datasBean.getVoltageDC1();

        // 直流电压2
        int voltageDC2 = datasBean.getVoltageDC2();

        // IGBT温度
        int tempratureIGBT = datasBean.getTempratureIGBT();

        // 设备软件版本
        String version = datasBean.getVersion();

        // 设备运行状态
        int stateAPF = datasBean.getStateAPF();

        // A相电压总畸变率
        double voltageATotalDistortionRate = datasBean.getVoltageATotalDistortionRate();
        BigDecimal voltageATotalDistortionRateBD = new BigDecimal(voltageATotalDistortionRate);
        voltageATotalDistortionRate = voltageATotalDistortionRateBD.setScale(1, BigDecimal.ROUND_HALF_UP).doubleValue();

        // B相电压总畸变率
        double voltageBTotalDistortionRate = datasBean.getVoltageBTotalDistortionRate();
        BigDecimal voltageBTotalDistortionRateBD = new BigDecimal(voltageBTotalDistortionRate);
        voltageBTotalDistortionRate = voltageBTotalDistortionRateBD.setScale(1, BigDecimal.ROUND_HALF_UP).doubleValue();

        // C相电压总畸变率
        double voltageCTotalDistortionRate = datasBean.getVoltageCTotalDistortionRate();
        BigDecimal voltageCTotalDistortionRateBD = new BigDecimal(voltageCTotalDistortionRate);
        voltageCTotalDistortionRate = voltageCTotalDistortionRateBD.setScale(1, BigDecimal.ROUND_HALF_UP).doubleValue();

        // 系统A相电流总畸变率
        double systemCurrentATotalDistortionRate = datasBean.getSystemCurrentATotalDistortionRate();
        BigDecimal systemCurrentATotalDistortionRateBD = new BigDecimal(systemCurrentATotalDistortionRate);
        systemCurrentATotalDistortionRate = systemCurrentATotalDistortionRateBD.setScale(1, BigDecimal.ROUND_HALF_UP).doubleValue();

        // 系统B相电流总畸变率
        double systemCurrentBTotalDistortionRate = datasBean.getSystemCurrentBTotalDistortionRate();
        BigDecimal systemCurrentBTotalDistortionRateBD = new BigDecimal(systemCurrentBTotalDistortionRate);
        systemCurrentBTotalDistortionRate = systemCurrentBTotalDistortionRateBD.setScale(1, BigDecimal.ROUND_HALF_UP).doubleValue();

        // 系统C相电流总畸变率
        double systemCurrentCTotalDistortionRate = datasBean.getSystemCurrentCTotalDistortionRate();
        BigDecimal systemCurrentCTotalDistortionRateBD = new BigDecimal(systemCurrentCTotalDistortionRate);
        systemCurrentCTotalDistortionRate = systemCurrentCTotalDistortionRateBD.setScale(1, BigDecimal.ROUND_HALF_UP).doubleValue();

        // 负载A相电流总畸变率
        double loadCurrentATotalDistortionRate = datasBean.getLoadCurrentATotalDistortionRate();
        BigDecimal loadCurrentATotalDistortionRateBD = new BigDecimal(loadCurrentATotalDistortionRate);
        loadCurrentATotalDistortionRate = loadCurrentATotalDistortionRateBD.setScale(1, BigDecimal.ROUND_HALF_UP).doubleValue();

        // 负载B相电流总畸变率
        double loadCurrentBTotalDistortionRate = datasBean.getLoadCurrentBTotalDistortionRate();
        BigDecimal loadCurrentBTotalDistortionRateBD = new BigDecimal(loadCurrentBTotalDistortionRate);
        loadCurrentBTotalDistortionRate = loadCurrentBTotalDistortionRateBD.setScale(1, BigDecimal.ROUND_HALF_UP).doubleValue();

        // 负载C相电流总畸变率
        double loadCurrentCTotalDistortionRate = datasBean.getLoadCurrentCTotalDistortionRate();
        BigDecimal loadCurrentCTotalDistortionRateBD = new BigDecimal(loadCurrentCTotalDistortionRate);
        loadCurrentCTotalDistortionRate = loadCurrentCTotalDistortionRateBD.setScale(1, BigDecimal.ROUND_HALF_UP).doubleValue();

        // 系统功率因素
        double systemPowerFactor = datasBean.getSystemPowerFactor();
        BigDecimal systemPowerFactorBD = new BigDecimal(systemPowerFactor);
        systemPowerFactor = systemPowerFactorBD.setScale(1, BigDecimal.ROUND_HALF_UP).doubleValue();

        // 负载功率因素
        double loadPowerFactor = datasBean.getLoadPowerFactor();
        BigDecimal loadPowerFactorBD = new BigDecimal(loadPowerFactor);
        loadPowerFactor = loadPowerFactorBD.setScale(1, BigDecimal.ROUND_HALF_UP).doubleValue();

        // 系统电流不平衡度
        double systemCurrentUnbalanceDegree = datasBean.getSystemCurrentUnbalanceDegree();
        BigDecimal systemCurrentUnbalanceDegreeBD = new BigDecimal(systemCurrentUnbalanceDegree);
        systemCurrentUnbalanceDegree = systemCurrentUnbalanceDegreeBD.setScale(1, BigDecimal.ROUND_HALF_UP).doubleValue();

        // 负载电流不平衡度
        double loadCurrentUnbalanceDegree = datasBean.getLoadCurrentUnbalanceDegree();
        BigDecimal loadCurrentUnbalanceDegreeBD = new BigDecimal(loadCurrentUnbalanceDegree);
        loadCurrentUnbalanceDegree = loadCurrentUnbalanceDegreeBD.setScale(1, BigDecimal.ROUND_HALF_UP).doubleValue();

        for (int i = 0; i < realDataItems.size(); i++) {
            AbstractFlexibleItem item = realDataItems.get(i);
            if (item instanceof RealDataSubItem) {
                RealDataSubItem subItem = (RealDataSubItem) item;

                if (subItem.getId().equals("电压电流-系统电压(V)-UA")) {
                    subItem.setValue(String.valueOf(systemAVoltage));
                }
                if (subItem.getId().equals("电压电流-系统电压(V)-UB")) {
                    subItem.setValue(String.valueOf(systemBVoltage));
                }
                if (subItem.getId().equals("电压电流-系统电压(V)-UC")) {
                    subItem.setValue(String.valueOf(systemCVoltage));
                }
                if (subItem.getId().equals("电压电流-系统电流(A)-IA")) {
                    subItem.setValue(String.valueOf(systemACurrent));
                }
                if (subItem.getId().equals("电压电流-系统电流(A)-IB")) {
                    subItem.setValue(String.valueOf(systemBCurrent));
                }
                if (subItem.getId().equals("电压电流-系统电流(A)-IC")) {
                    subItem.setValue(String.valueOf(systemCCurrent));
                }
                if (subItem.getId().equals("电压电流-负载电流(A)-IA")) {
                    subItem.setValue(String.valueOf(loadACurrent));
                }
                if (subItem.getId().equals("电压电流-负载电流(A)-IB")) {
                    subItem.setValue(String.valueOf(loadBCurrent));
                }
                if (subItem.getId().equals("电压电流-负载电流(A)-IC")) {
                    subItem.setValue(String.valueOf(loadCCurrent));
                }
                if (subItem.getId().equals("电压电流-补偿电流(A)-IA")) {
                    subItem.setValue(String.valueOf(compensationACurrent));
                }
                if (subItem.getId().equals("电压电流-补偿电流(A)-IB")) {
                    subItem.setValue(String.valueOf(compensationBCurrent));
                }
                if (subItem.getId().equals("电压电流-补偿电流(A)-IC")) {
                    subItem.setValue(String.valueOf(compensationCCurrent));
                }
                if (subItem.getId().equals("电压电流-直流电压及温度-UDC1")) {
                    subItem.setValue(String.valueOf(voltageDC1));
                }
                if (subItem.getId().equals("电压电流-直流电压及温度-UDC2")) {
                    subItem.setValue(String.valueOf(voltageDC2));
                }
                if (subItem.getId().equals("电压电流-直流电压及温度-TEM")) {
                    subItem.setValue(String.valueOf(tempratureIGBT));
                }
                if (subItem.getId().equals("电压电流-设备信息-设备软件版本")) {
                    subItem.setValue("V" + String.valueOf(version));
                }
                if (subItem.getId().equals("电压电流-设备信息-设备运行状态")) {
                    subItem.setValue(String.valueOf(stateAPF));
                }
                if (subItem.getId().equals("总畸变率-电压总畸变率(%)-A相")) {
                    subItem.setValue(String.valueOf(voltageATotalDistortionRate));
                }
                if (subItem.getId().equals("总畸变率-电压总畸变率(%)-B相")) {
                    subItem.setValue(String.valueOf(voltageBTotalDistortionRate));
                }
                if (subItem.getId().equals("总畸变率-电压总畸变率(%)-C相")) {
                    subItem.setValue(String.valueOf(voltageCTotalDistortionRate));
                }
                if (subItem.getId().equals("总畸变率-系统电流总畸变率(%)-A相")) {
                    subItem.setValue(String.valueOf(systemCurrentATotalDistortionRate));
                }
                if (subItem.getId().equals("总畸变率-系统电流总畸变率(%)-B相")) {
                    subItem.setValue(String.valueOf(systemCurrentBTotalDistortionRate));
                }
                if (subItem.getId().equals("总畸变率-系统电流总畸变率(%)-C相")) {
                    subItem.setValue(String.valueOf(systemCurrentCTotalDistortionRate));
                }
                if (subItem.getId().equals("总畸变率-负载电流总畸变率(%)-A相")) {
                    subItem.setValue(String.valueOf(loadCurrentATotalDistortionRate));
                }
                if (subItem.getId().equals("总畸变率-负载电流总畸变率(%)-B相")) {
                    subItem.setValue(String.valueOf(loadCurrentBTotalDistortionRate));
                }
                if (subItem.getId().equals("总畸变率-负载电流总畸变率(%)-C相")) {
                    subItem.setValue(String.valueOf(loadCurrentCTotalDistortionRate));
                }
                if (subItem.getId().equals("总畸变率-功率因数-系统COS")) {
                    subItem.setValue(String.valueOf(systemPowerFactor));
                }
                if (subItem.getId().equals("总畸变率-功率因数-负载COS")) {
                    subItem.setValue(String.valueOf(loadPowerFactor));
                }
                if (subItem.getId().equals("总畸变率-不平衡度(%)-系统电流")) {
                    subItem.setValue(String.valueOf(systemCurrentUnbalanceDegree));
                }
                if (subItem.getId().equals("总畸变率-不平衡度(%)-负载电流")) {
                    subItem.setValue(String.valueOf(loadCurrentUnbalanceDegree));
                }
            }
        }

        // 更新数据
        adapter.notifyDataSetChanged();
    }
}
