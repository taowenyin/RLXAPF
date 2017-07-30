package cn.edu.siso.rlxapf;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.os.AsyncTaskCompat;
import android.support.v7.preference.EditTextPreference;
import android.support.v7.preference.ListPreference;
import android.support.v7.preference.Preference;
import android.support.v7.preference.PreferenceFragmentCompat;
import android.support.v7.preference.PreferenceManager;
import android.util.Log;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import cn.edu.siso.rlxapf.bean.ParameterDatasBean;
import cn.edu.siso.rlxapf.config.TCPConfig;
import cn.edu.siso.rlxapf.util.ProtocolUtil;
import cn.edu.siso.rlxapf.util.TCPUtil;


public class ParamPrefFragment extends PreferenceFragmentCompat implements
        SharedPreferences.OnSharedPreferenceChangeListener,
        TCPUtil.OnConnectListener,
        TCPUtil.OnReceiveListener, Preference.OnPreferenceChangeListener {

    private TCPUtil tcpUtil = null; // 和有人云通信的对象

    private SharedPreferences paramPrefs = null;

    private ConnectDialogFragment dialogFragment = null;

    private Context context = null;

    public static final String UPDATE_DATA_KEY = "data";
    public static final String UPDATE_PARAM_KEY = "param";

    public static final String TAG = "ParamPrefFragment";

    private enum NETWORK_TYPE {LOAD_PARAM, UPDATE_PARAM};

    private NETWORK_TYPE currentNetworkType;

    public ParamPrefFragment() {
        // Required empty public constructor
    }

    public static ParamPrefFragment newInstance() {
        ParamPrefFragment fragment = new ParamPrefFragment();
        return fragment;
    }

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        getPreferenceManager().setSharedPreferencesName(
                getResources().getString(R.string.param_preferences_name));

        setPreferencesFromResource(R.xml.pref_param, rootKey);

        // 载入配置文件的默认值
        PreferenceManager.setDefaultValues(getActivity(), R.xml.pref_param, false);

        this.paramPrefs = getPreferenceScreen().getSharedPreferences();

        // 把每个Preference的设置到Summary中
        EditTextPreference editTextPreference = null;

        // 单元数
        editTextPreference = (EditTextPreference) findPreference(
                getResources().getString(R.string.param_preferences_unit_key));
        editTextPreference.setSummary(this.paramPrefs.getString(
                getResources().getString(R.string.param_preferences_unit_key), ""));
        editTextPreference.setOnPreferenceChangeListener(this);

        ListPreference listPreference = null;

        listPreference = (ListPreference) findPreference(getResources().getString(R.string.param_preferences_sampling_model_key));
        listPreference.setSummary(listPreference.getEntry());
        listPreference.setOnPreferenceChangeListener(this);

        listPreference = (ListPreference) findPreference(getResources().getString(R.string.param_preferences_compensate_model_key));
        listPreference.setSummary(listPreference.getEntry());
        listPreference.setOnPreferenceChangeListener(this);

        // 系统CT
        editTextPreference = (EditTextPreference) findPreference(
                getResources().getString(R.string.param_preferences_system_ct_key));
        editTextPreference.setSummary(this.paramPrefs.getString(
                getResources().getString(R.string.param_preferences_system_ct_key), ""));
        editTextPreference.setOnPreferenceChangeListener(this);

        // 负载CT
        editTextPreference = (EditTextPreference) findPreference(
                getResources().getString(R.string.param_preferences_load_ct_key));
        editTextPreference.setSummary(this.paramPrefs.getString(
                getResources().getString(R.string.param_preferences_load_ct_key), ""));
        editTextPreference.setOnPreferenceChangeListener(this);

        // 目标功率因素
        editTextPreference = (EditTextPreference) findPreference(
                getResources().getString(R.string.param_preferences_target_power_key));
        editTextPreference.setSummary(this.paramPrefs.getString(
                getResources().getString(R.string.param_preferences_target_power_key), ""));
        editTextPreference.setOnPreferenceChangeListener(this);

        // N次谐波
        editTextPreference = (EditTextPreference) findPreference(
                getResources().getString(R.string.param_preferences_3_compensate_key));
        editTextPreference.setSummary(this.paramPrefs.getString(
                getResources().getString(R.string.param_preferences_3_compensate_key), ""));
        editTextPreference.setOnPreferenceChangeListener(this);

        editTextPreference = (EditTextPreference) findPreference(
                getResources().getString(R.string.param_preferences_5_compensate_key));
        editTextPreference.setSummary(this.paramPrefs.getString(
                getResources().getString(R.string.param_preferences_5_compensate_key), ""));
        editTextPreference.setOnPreferenceChangeListener(this);

        editTextPreference = (EditTextPreference) findPreference(
                getResources().getString(R.string.param_preferences_7_compensate_key));
        editTextPreference.setSummary(this.paramPrefs.getString(
                getResources().getString(R.string.param_preferences_7_compensate_key), ""));
        editTextPreference.setOnPreferenceChangeListener(this);

        editTextPreference = (EditTextPreference) findPreference(
                getResources().getString(R.string.param_preferences_9_compensate_key));
        editTextPreference.setSummary(this.paramPrefs.getString(
                getResources().getString(R.string.param_preferences_9_compensate_key), ""));
        editTextPreference.setOnPreferenceChangeListener(this);

        editTextPreference = (EditTextPreference) findPreference(
                getResources().getString(R.string.param_preferences_11_compensate_key));
        editTextPreference.setSummary(this.paramPrefs.getString(
                getResources().getString(R.string.param_preferences_11_compensate_key), ""));
        editTextPreference.setOnPreferenceChangeListener(this);

        editTextPreference = (EditTextPreference) findPreference(
                getResources().getString(R.string.param_preferences_13_compensate_key));
        editTextPreference.setSummary(this.paramPrefs.getString(
                getResources().getString(R.string.param_preferences_13_compensate_key), ""));
        editTextPreference.setOnPreferenceChangeListener(this);

        editTextPreference = (EditTextPreference) findPreference(
                getResources().getString(R.string.param_preferences_15_compensate_key));
        editTextPreference.setSummary(this.paramPrefs.getString(
                getResources().getString(R.string.param_preferences_15_compensate_key), ""));
        editTextPreference.setOnPreferenceChangeListener(this);

        editTextPreference = (EditTextPreference) findPreference(
                getResources().getString(R.string.param_preferences_17_compensate_key));
        editTextPreference.setSummary(this.paramPrefs.getString(
                getResources().getString(R.string.param_preferences_17_compensate_key), ""));
        editTextPreference.setOnPreferenceChangeListener(this);

        editTextPreference = (EditTextPreference) findPreference(
                getResources().getString(R.string.param_preferences_19_compensate_key));
        editTextPreference.setSummary(this.paramPrefs.getString(
                getResources().getString(R.string.param_preferences_19_compensate_key), ""));
        editTextPreference.setOnPreferenceChangeListener(this);

        editTextPreference = (EditTextPreference) findPreference(
                getResources().getString(R.string.param_preferences_21_compensate_key));
        editTextPreference.setSummary(this.paramPrefs.getString(
                getResources().getString(R.string.param_preferences_21_compensate_key), ""));
        editTextPreference.setOnPreferenceChangeListener(this);

        editTextPreference = (EditTextPreference) findPreference(
                getResources().getString(R.string.param_preferences_23_compensate_key));
        editTextPreference.setSummary(this.paramPrefs.getString(
                getResources().getString(R.string.param_preferences_23_compensate_key), ""));
        editTextPreference.setOnPreferenceChangeListener(this);

        editTextPreference = (EditTextPreference) findPreference(
                getResources().getString(R.string.param_preferences_25_compensate_key));
        editTextPreference.setSummary(this.paramPrefs.getString(
                getResources().getString(R.string.param_preferences_25_compensate_key), ""));
        editTextPreference.setOnPreferenceChangeListener(this);

        editTextPreference = (EditTextPreference) findPreference(
                getResources().getString(R.string.param_preferences_26_compensate_key));
        editTextPreference.setSummary(this.paramPrefs.getString(
                getResources().getString(R.string.param_preferences_26_compensate_key), ""));
        editTextPreference.setOnPreferenceChangeListener(this);

        // 打开网络通信进度框
        dialogFragment = new ConnectDialogFragment();
    }

    @Override
    public void onResume() {
        super.onResume();
        getPreferenceScreen().getSharedPreferences().registerOnSharedPreferenceChangeListener(this);

        // 建立和有人云的通信
        tcpUtil = new TCPUtil();
        tcpUtil.connect(TCPConfig.DEFAULT_DEVICE_ID, TCPConfig.DEFAULT_DEVICE_PWD, this);
        dialogFragment.show(getFragmentManager(),
                getResources().getString(R.string.connect_fragment_dialog_tag));

        currentNetworkType = NETWORK_TYPE.LOAD_PARAM;
    }

    @Override
    public void onPause() {
        super.onPause();
        getPreferenceScreen().getSharedPreferences().unregisterOnSharedPreferenceChangeListener(this);

        // 关闭与有人云的通信
        tcpUtil.close(TCPConfig.DEFAULT_DEVICE_ID);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        this.context = context;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        // 移除默认的分割线
        setDivider(new ColorDrawable(Color.TRANSPARENT));
        setDividerHeight(0);
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        Preference currPref = findPreference(key);
        if (currPref instanceof EditTextPreference) {
            // 当EditTextPreference的值发生变化时，把值设置到Summary中
            EditTextPreference etp = (EditTextPreference) currPref;
            currPref.setSummary(etp.getText());
        }
        if (currPref instanceof ListPreference) {
            // 当ListPreference的值发生变化时，把值设置到Summary中
            ListPreference ltp = (ListPreference) currPref;
            currPref.setSummary(ltp.getEntry());
        }
    }

    @Override
    public void onSuccess(String deviceId) {
        Log.i(TAG, "onSuccess Device Id = " + deviceId);

        // 发送指令后休息100ms
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // 执行读取参数的指令
        AsyncTaskCompat.executeParallel(new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... params) {
                byte deviceAddr = 0x01;
                tcpUtil.send(ProtocolUtil.readParamsDatas(deviceAddr), ParamPrefFragment.this);
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

        dialogFragment.dismiss(); // 关闭进度框

        if (currentNetworkType == NETWORK_TYPE.LOAD_PARAM) {
            Log.i(TAG, "读取数据成功");
            ParameterDatasBean datasBean = new ParameterDatasBean();
            int res = datasBean.parse(data);
            if (res == -1) {
                Log.i(TAG, "CRC教研失败");
            } else if (res == -2) {
                Log.i(TAG, "长度解析出错");
            } else {
                Log.i(TAG, "数据解析成功");
                writePreference(datasBean);
            }
        }
        if (currentNetworkType == NETWORK_TYPE.UPDATE_PARAM) {
            Log.i(TAG, "更新数据成功");
        }
    }

    private void writePreference(ParameterDatasBean datasBean) {
        // 把每个EditTextPreference的对象
        EditTextPreference editTextPreference = null;

        // 单元数
        editTextPreference = (EditTextPreference) findPreference(
                getResources().getString(R.string.param_preferences_unit_key));
        editTextPreference.setText(String.valueOf(datasBean.getUnitCount()));
        editTextPreference.setSummary(String.valueOf(datasBean.getUnitCount()));

        ListPreference listPreference = null;

        listPreference = (ListPreference) findPreference(getResources().getString(R.string.param_preferences_sampling_model_key));
        listPreference.setValueIndex(datasBean.getSampleMode());
        listPreference.setSummary(listPreference.getEntry());

        listPreference = (ListPreference) findPreference(getResources().getString(R.string.param_preferences_compensate_model_key));
        listPreference.setValueIndex(datasBean.getCompensationMode());
        listPreference.setSummary(listPreference.getEntry());

        // 系统CT
        editTextPreference = (EditTextPreference) findPreference(
                getResources().getString(R.string.param_preferences_system_ct_key));
        editTextPreference.setText(String.valueOf(datasBean.getSystemCTChangeRate()));
        editTextPreference.setSummary(String.valueOf(datasBean.getSystemCTChangeRate()));

        // 负载CT
        editTextPreference = (EditTextPreference) findPreference(
                getResources().getString(R.string.param_preferences_load_ct_key));
        editTextPreference.setText(String.valueOf(datasBean.getLoadCTChangeRate()));
        editTextPreference.setSummary(String.valueOf(datasBean.getLoadCTChangeRate()));

        // 目标功率因素
        editTextPreference = (EditTextPreference) findPreference(
                getResources().getString(R.string.param_preferences_target_power_key));
        editTextPreference.setText(String.valueOf(datasBean.getObjectPowerFactor()));
        editTextPreference.setSummary(String.valueOf(datasBean.getObjectPowerFactor()));

        // N次谐波
        editTextPreference = (EditTextPreference) findPreference(
                getResources().getString(R.string.param_preferences_3_compensate_key));
        editTextPreference.setText(String.valueOf(datasBean.getHarmonic3CompensationRate()));
        editTextPreference.setSummary(String.valueOf(datasBean.getHarmonic3CompensationRate()));

        editTextPreference = (EditTextPreference) findPreference(
                getResources().getString(R.string.param_preferences_5_compensate_key));
        editTextPreference.setText(String.valueOf(datasBean.getHarmonic5CompensationRate()));
        editTextPreference.setSummary(String.valueOf(datasBean.getHarmonic5CompensationRate()));

        editTextPreference = (EditTextPreference) findPreference(
                getResources().getString(R.string.param_preferences_7_compensate_key));
        editTextPreference.setText(String.valueOf(datasBean.getHarmonic7CompensationRate()));
        editTextPreference.setSummary(String.valueOf(datasBean.getHarmonic7CompensationRate()));

        editTextPreference = (EditTextPreference) findPreference(
                getResources().getString(R.string.param_preferences_9_compensate_key));
        editTextPreference.setText(String.valueOf(datasBean.getHarmonic9CompensationRate()));
        editTextPreference.setSummary(String.valueOf(datasBean.getHarmonic9CompensationRate()));

        editTextPreference = (EditTextPreference) findPreference(
                getResources().getString(R.string.param_preferences_11_compensate_key));
        editTextPreference.setText(String.valueOf(datasBean.getHarmonic11CompensationRate()));
        editTextPreference.setSummary(String.valueOf(datasBean.getHarmonic11CompensationRate()));

        editTextPreference = (EditTextPreference) findPreference(
                getResources().getString(R.string.param_preferences_13_compensate_key));
        editTextPreference.setText(String.valueOf(datasBean.getHarmonic13CompensationRate()));
        editTextPreference.setSummary(String.valueOf(datasBean.getHarmonic13CompensationRate()));

        editTextPreference = (EditTextPreference) findPreference(
                getResources().getString(R.string.param_preferences_15_compensate_key));
        editTextPreference.setText(String.valueOf(datasBean.getHarmonic15CompensationRate()));
        editTextPreference.setSummary(String.valueOf(datasBean.getHarmonic15CompensationRate()));

        editTextPreference = (EditTextPreference) findPreference(
                getResources().getString(R.string.param_preferences_17_compensate_key));
        editTextPreference.setText(String.valueOf(datasBean.getHarmonic17CompensationRate()));
        editTextPreference.setSummary(String.valueOf(datasBean.getHarmonic17CompensationRate()));

        editTextPreference = (EditTextPreference) findPreference(
                getResources().getString(R.string.param_preferences_19_compensate_key));
        editTextPreference.setText(String.valueOf(datasBean.getHarmonic19CompensationRate()));
        editTextPreference.setSummary(String.valueOf(datasBean.getHarmonic19CompensationRate()));

        editTextPreference = (EditTextPreference) findPreference(
                getResources().getString(R.string.param_preferences_21_compensate_key));
        editTextPreference.setText(String.valueOf(datasBean.getHarmonic21CompensationRate()));
        editTextPreference.setSummary(String.valueOf(datasBean.getHarmonic21CompensationRate()));

        editTextPreference = (EditTextPreference) findPreference(
                getResources().getString(R.string.param_preferences_23_compensate_key));
        editTextPreference.setText(String.valueOf(datasBean.getHarmonic23CompensationRate()));
        editTextPreference.setSummary(String.valueOf(datasBean.getHarmonic23CompensationRate()));

        editTextPreference = (EditTextPreference) findPreference(
                getResources().getString(R.string.param_preferences_25_compensate_key));
        editTextPreference.setText(String.valueOf(datasBean.getHarmonic25CompensationRate()));
        editTextPreference.setSummary(String.valueOf(datasBean.getHarmonic25CompensationRate()));

        editTextPreference = (EditTextPreference) findPreference(
                getResources().getString(R.string.param_preferences_26_compensate_key));
        editTextPreference.setText(String.valueOf(datasBean.getHarmonicEvenCompensationRate()));
        editTextPreference.setSummary(String.valueOf(datasBean.getHarmonicEvenCompensationRate()));
    }

    public void pullDeviceParams() {
        // 执行读取参数的指令
        AsyncTaskCompat.executeParallel(new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... params) {
                byte deviceAddr = 0x01;
                tcpUtil.send(ProtocolUtil.readParamsDatas(deviceAddr), ParamPrefFragment.this);
                return null;
            }
        });
        currentNetworkType = NETWORK_TYPE.LOAD_PARAM;

        dialogFragment.show(getFragmentManager(),
                getResources().getString(R.string.connect_fragment_dialog_tag));
    }

    @Override
    public boolean onPreferenceChange(Preference preference, Object newValue) {

        Map<String, String> param = new HashMap<String, String>();
        param.put(UPDATE_PARAM_KEY, preference.getKey());
        param.put(UPDATE_DATA_KEY, newValue.toString());

        AsyncTaskCompat.executeParallel(new ParamUpdateTask(), param);
        dialogFragment.show(getFragmentManager(),
                getResources().getString(R.string.connect_fragment_dialog_tag));

        currentNetworkType = NETWORK_TYPE.UPDATE_PARAM;

        return true;
    }

    private class ParamUpdateTask extends AsyncTask<Map<String, String>, Void, Void> {

        @Override
        protected Void doInBackground(Map<String, String>... params) {
            Map<String, String> param = params[0];

            String paramKey = param.get(UPDATE_PARAM_KEY);
            String paramData = param.get(UPDATE_DATA_KEY);

            Log.i(TAG, "ParamUpdateTask Param Key = " + paramKey);
            Log.i(TAG, "ParamUpdateTask Param Data = " + paramData);

            byte[] bytesMsg = null;
            byte deviceAddr = 0x01;

            if (paramKey.equals(getResources().getString(R.string.param_preferences_unit_key))) {
                bytesMsg = ProtocolUtil.writeParamsUnitCount(deviceAddr, Integer.valueOf(paramData));
            }
            if (paramKey.equals(getResources().getString(R.string.param_preferences_system_ct_key))) {
                bytesMsg = ProtocolUtil.writeParamsSystemCTChangeRate(deviceAddr, Integer.valueOf(paramData));
            }
            if (paramKey.equals(getResources().getString(R.string.param_preferences_load_ct_key))) {
                bytesMsg = ProtocolUtil.writeParamsLoadCTChangeRate(deviceAddr, Integer.valueOf(paramData));
            }
            if (paramKey.equals(getResources().getString(R.string.param_preferences_target_power_key))) {
                bytesMsg = ProtocolUtil.writeParamsObjectPowerFactor(deviceAddr, Double.valueOf(paramData));
            }
            if (paramKey.equals(getResources().getString(R.string.param_preferences_3_compensate_key))) {
                bytesMsg = ProtocolUtil.writeParamsHarmonic3CompensationRate(deviceAddr, Integer.valueOf(paramData));
            }
            if (paramKey.equals(getResources().getString(R.string.param_preferences_5_compensate_key))) {
                bytesMsg = ProtocolUtil.writeParamsHarmonic5CompensationRate(deviceAddr, Integer.valueOf(paramData));
            }
            if (paramKey.equals(getResources().getString(R.string.param_preferences_7_compensate_key))) {
                bytesMsg = ProtocolUtil.writeParamsHarmonic7CompensationRate(deviceAddr, Integer.valueOf(paramData));
            }
            if (paramKey.equals(getResources().getString(R.string.param_preferences_9_compensate_key))) {
                bytesMsg = ProtocolUtil.writeParamsHarmonic9CompensationRate(deviceAddr, Integer.valueOf(paramData));
            }
            if (paramKey.equals(getResources().getString(R.string.param_preferences_11_compensate_key))) {
                bytesMsg = ProtocolUtil.writeParamsHarmonic11CompensationRate(deviceAddr, Integer.valueOf(paramData));
            }
            if (paramKey.equals(getResources().getString(R.string.param_preferences_13_compensate_key))) {
                bytesMsg = ProtocolUtil.writeParamsHarmonic13CompensationRate(deviceAddr, Integer.valueOf(paramData));
            }
            if (paramKey.equals(getResources().getString(R.string.param_preferences_15_compensate_key))) {
                bytesMsg = ProtocolUtil.writeParamsHarmonic15CompensationRate(deviceAddr, Integer.valueOf(paramData));
            }
            if (paramKey.equals(getResources().getString(R.string.param_preferences_17_compensate_key))) {
                bytesMsg = ProtocolUtil.writeParamsHarmonic17CompensationRate(deviceAddr, Integer.valueOf(paramData));
            }
            if (paramKey.equals(getResources().getString(R.string.param_preferences_19_compensate_key))) {
                bytesMsg = ProtocolUtil.writeParamsHarmonic19CompensationRate(deviceAddr, Integer.valueOf(paramData));
            }
            if (paramKey.equals(getResources().getString(R.string.param_preferences_21_compensate_key))) {
                bytesMsg = ProtocolUtil.writeParamsHarmonic21CompensationRate(deviceAddr, Integer.valueOf(paramData));
            }
            if (paramKey.equals(getResources().getString(R.string.param_preferences_23_compensate_key))) {
                bytesMsg = ProtocolUtil.writeParamsHarmonic23CompensationRate(deviceAddr, Integer.valueOf(paramData));
            }
            if (paramKey.equals(getResources().getString(R.string.param_preferences_25_compensate_key))) {
                bytesMsg = ProtocolUtil.writeParamsHarmonic25CompensationRate(deviceAddr, Integer.valueOf(paramData));
            }
            if (paramKey.equals(getResources().getString(R.string.param_preferences_26_compensate_key))) {
                bytesMsg = ProtocolUtil.writeParamsHarmonicEvenCompensationRate(deviceAddr, Integer.valueOf(paramData));
            }
            if (paramKey.equals(getResources().getString(R.string.param_preferences_sampling_model_key))) {
                bytesMsg = ProtocolUtil.writeParamsSampleMode(deviceAddr, Integer.valueOf(paramData));
            }
            if (paramKey.equals(getResources().getString(R.string.param_preferences_compensate_model_key))) {
                bytesMsg = ProtocolUtil.writeParamsCompensationMode(deviceAddr, Integer.valueOf(paramData));
            }

            tcpUtil.send(bytesMsg, ParamPrefFragment.this);

            return null;
        }
    }
}
