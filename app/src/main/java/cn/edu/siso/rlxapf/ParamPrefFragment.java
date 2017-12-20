package cn.edu.siso.rlxapf;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.preference.EditTextPreference;
import android.support.v7.preference.ListPreference;
import android.support.v7.preference.Preference;
import android.support.v7.preference.PreferenceFragmentCompat;
import android.support.v7.preference.PreferenceManager;
import android.util.Log;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;

import java.util.Timer;
import java.util.TimerTask;

import cn.edu.siso.rlxapf.bean.DeviceBean;
import cn.edu.siso.rlxapf.bean.ParameterDatasBean;
import cn.edu.siso.rlxapf.dialog.ConnectDialogFragment;
import cn.edu.siso.rlxapf.dialog.PrefConfirmDialogFragment;
import cn.edu.siso.rlxapf.util.tcp.TcpClientManager;

public class ParamPrefFragment extends PreferenceFragmentCompat implements
        SharedPreferences.OnSharedPreferenceChangeListener,
        Preference.OnPreferenceChangeListener,
        Preference.OnPreferenceClickListener {

    private TcpClientManager tcpClientManager = null;
    private Handler tcpHandler = null;
    private boolean isTimeout = false;
    private boolean correctParamPwd = false;

    private PrefConfirmDialogFragment confirmDialogFragment = null;

    private SharedPreferences paramPrefs = null;

    private ConnectDialogFragment dialogFragment = null;

    private Context context = null;

    private DeviceBean deviceBean = null;

    public static final String KEY_DEVICE_PARAM = "key_device_param";

    public static final String TAG = "===ParamPrefFragment===";

    public ParamPrefFragment() {
        tcpClientManager = TcpClientManager.getInstance();

        tcpHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);

                if (dialogFragment.getShowsDialog()) {
                    dialogFragment.dismiss();
                }

                Bundle data = msg.getData();
                String operateType = data.getString(TcpClientManager.KEY_TCP_OPERATE_TYPE);
                if (operateType.equals(TcpClientManager.TcpOperateType.OPERATE)) {
                    int tcpCmdType = Integer.valueOf(data.getString(TcpClientManager.KEY_TCP_CMD_TYPE));

                    // 载入数据
                    if (TcpClientManager.TcpCmdType.LOAD_PARAMS.ordinal() == tcpCmdType) {
                        String resType = data.getString(TcpClientManager.KEY_TCP_RES_TYPE);
                        if (!isTimeout) {
                            if (resType.equals(TcpClientManager.TcpResType.CRC)) {
                                ConnectToast toast = new ConnectToast(getContext(),
                                        ConnectToast.ConnectRes.BAD,
                                        getResources().getString(R.string.tcp_connect_data_error_crc),
                                        Toast.LENGTH_LONG);
                                toast.show();
                            }
                            if (resType.equals(TcpClientManager.TcpResType.LENGTH)) {
                                ConnectToast toast = new ConnectToast(getContext(),
                                        ConnectToast.ConnectRes.BAD,
                                        getResources().getString(R.string.tcp_connect_data_error_length),
                                        Toast.LENGTH_LONG);
                                toast.show();
                            }
                            if (resType.equals(TcpClientManager.TcpResType.SUCCESS)) {
                                ConnectToast toast = new ConnectToast(getContext(),
                                        ConnectToast.ConnectRes.SUCCESS,
                                        getResources().getString(R.string.tcp_connect_data_succ),
                                        Toast.LENGTH_SHORT);
                                toast.show();

                                ParameterDatasBean datasBean = JSON.parseObject(
                                        data.getString(TcpClientManager.KEY_TCP_RES_DATA),
                                        ParameterDatasBean.class);
                                writePreference(datasBean);
                            }
                        }
                        if (resType.equals(TcpClientManager.TcpResType.TIMEOUT)) {
                            // 标记当前为超时状态
                            isTimeout = true;

                            ConnectToast toast = new ConnectToast(getContext(),
                                    ConnectToast.ConnectRes.BAD,
                                    getResources().getString(R.string.tcp_connect_load_params_time_out),
                                    Toast.LENGTH_LONG);
                            toast.show();
                        }
                    }

                    // 上传参数
                    if (TcpClientManager.TcpCmdType.UPDATE_PARAMS.ordinal() == tcpCmdType) {
                        String resType = data.getString(TcpClientManager.KEY_TCP_RES_TYPE);

                        if (resType.equals(TcpClientManager.TcpResType.TIMEOUT)) {
                            // 标记当前为超时状态
                            isTimeout = true;

                            ConnectToast toast = new ConnectToast(getContext(),
                                    ConnectToast.ConnectRes.BAD,
                                    getResources().getString(R.string.tcp_connect_update_param_time_out),
                                    Toast.LENGTH_LONG);
                            toast.show();
                        }
                        if (!isTimeout) {
                            if (resType.equals(TcpClientManager.TcpResType.SUCCESS)) {
                                ConnectToast toast = new ConnectToast(getContext(),
                                        ConnectToast.ConnectRes.SUCCESS,
                                        getResources().getString(R.string.tcp_connect_param_update_succ),
                                        Toast.LENGTH_SHORT);
                                toast.show();
                            }
                        }
                    }

                    // 保存参数
                    if (TcpClientManager.TcpCmdType.SAVE_PARAMS.ordinal() == tcpCmdType) {
                        String resType = data.getString(TcpClientManager.KEY_TCP_RES_TYPE);

                        if (resType.equals(TcpClientManager.TcpResType.TIMEOUT)) {
                            // 标记当前为超时状态
                            isTimeout = true;

                            ConnectToast toast = new ConnectToast(getContext(),
                                    ConnectToast.ConnectRes.BAD,
                                    getResources().getString(R.string.tcp_connect_save_param_time_out),
                                    Toast.LENGTH_LONG);
                            toast.show();
                        }
                        if (!isTimeout) {
                            if (resType.equals(TcpClientManager.TcpResType.SUCCESS)) {
                                ConnectToast toast = new ConnectToast(getContext(),
                                        ConnectToast.ConnectRes.SUCCESS,
                                        getResources().getString(R.string.tcp_connect_param_save_succ),
                                        Toast.LENGTH_SHORT);
                                toast.show();
                            }
                        }
                    }

                    // 导出参数
                    if (TcpClientManager.TcpCmdType.EXPORT_PARAMS.ordinal() == tcpCmdType) {
                        String resType = data.getString(TcpClientManager.KEY_TCP_RES_TYPE);

                        if (resType.equals(TcpClientManager.TcpResType.TIMEOUT)) {
                            // 标记当前为超时状态
                            isTimeout = true;

                            ConnectToast toast = new ConnectToast(getContext(),
                                    ConnectToast.ConnectRes.BAD,
                                    getResources().getString(R.string.tcp_connect_export_param_time_out),
                                    Toast.LENGTH_LONG);
                            toast.show();
                        }
                        if (!isTimeout) {
                            if (resType.equals(TcpClientManager.TcpResType.SUCCESS)) {
                                ConnectToast toast = new ConnectToast(getContext(),
                                        ConnectToast.ConnectRes.SUCCESS,
                                        getResources().getString(R.string.tcp_connect_param_export_succ),
                                        Toast.LENGTH_SHORT);
                                toast.show();
                            }
                        }
                    }

                    // 恢复出厂设置参数
                    if (TcpClientManager.TcpCmdType.RESTORE_PARAMS.ordinal() == tcpCmdType) {
                        String resType = data.getString(TcpClientManager.KEY_TCP_RES_TYPE);

                        if (resType.equals(TcpClientManager.TcpResType.TIMEOUT)) {
                            // 标记当前为超时状态
                            isTimeout = true;

                            ConnectToast toast = new ConnectToast(getContext(),
                                    ConnectToast.ConnectRes.BAD,
                                    getResources().getString(R.string.tcp_connect_restore_param_time_out),
                                    Toast.LENGTH_LONG);
                            toast.show();
                        }
                        if (!isTimeout) {
                            if (resType.equals(TcpClientManager.TcpResType.SUCCESS)) {
                                ConnectToast toast = new ConnectToast(getContext(),
                                        ConnectToast.ConnectRes.SUCCESS,
                                        getResources().getString(R.string.tcp_connect_param_restore_succ),
                                        Toast.LENGTH_SHORT);
                                toast.show();
                            }
                        }
                    }
                }
            }
        };
    }

    public static ParamPrefFragment newInstance(String deviceParam) {
        ParamPrefFragment fragment = new ParamPrefFragment();
        Bundle data = new Bundle();
        data.putString(KEY_DEVICE_PARAM, deviceParam);
        fragment.setArguments(data);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        confirmDialogFragment = new PrefConfirmDialogFragment();

        if (getArguments() != null) {
            String device = getArguments().getString(KEY_DEVICE_PARAM);
            deviceBean = JSON.parseObject(device, DeviceBean.class);
        }
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
        editTextPreference.setOnPreferenceClickListener(this);

        ListPreference listPreference = null;

        listPreference = (ListPreference) findPreference(getResources().getString(R.string.param_preferences_sampling_model_key));
        listPreference.setSummary(listPreference.getEntry());
        listPreference.setOnPreferenceChangeListener(this);
        listPreference.setOnPreferenceClickListener(this);

        listPreference = (ListPreference) findPreference(getResources().getString(R.string.param_preferences_compensate_model_key));
        listPreference.setSummary(listPreference.getEntry());
        listPreference.setOnPreferenceChangeListener(this);
        listPreference.setOnPreferenceClickListener(this);

        // 系统CT
        editTextPreference = (EditTextPreference) findPreference(
                getResources().getString(R.string.param_preferences_system_ct_key));
        editTextPreference.setSummary(this.paramPrefs.getString(
                getResources().getString(R.string.param_preferences_system_ct_key), ""));
        editTextPreference.setOnPreferenceChangeListener(this);
        editTextPreference.setOnPreferenceClickListener(this);

        // 负载CT
        editTextPreference = (EditTextPreference) findPreference(
                getResources().getString(R.string.param_preferences_load_ct_key));
        editTextPreference.setSummary(this.paramPrefs.getString(
                getResources().getString(R.string.param_preferences_load_ct_key), ""));
        editTextPreference.setOnPreferenceChangeListener(this);
        editTextPreference.setOnPreferenceClickListener(this);

        // 目标功率因素
        editTextPreference = (EditTextPreference) findPreference(
                getResources().getString(R.string.param_preferences_target_power_key));
        editTextPreference.setSummary(this.paramPrefs.getString(
                getResources().getString(R.string.param_preferences_target_power_key), ""));
        editTextPreference.setOnPreferenceChangeListener(this);
        editTextPreference.setOnPreferenceClickListener(this);

        // N次谐波
        editTextPreference = (EditTextPreference) findPreference(
                getResources().getString(R.string.param_preferences_3_compensate_key));
        editTextPreference.setSummary(this.paramPrefs.getString(
                getResources().getString(R.string.param_preferences_3_compensate_key), ""));
        editTextPreference.setOnPreferenceChangeListener(this);
        editTextPreference.setOnPreferenceClickListener(this);

        editTextPreference = (EditTextPreference) findPreference(
                getResources().getString(R.string.param_preferences_5_compensate_key));
        editTextPreference.setSummary(this.paramPrefs.getString(
                getResources().getString(R.string.param_preferences_5_compensate_key), ""));
        editTextPreference.setOnPreferenceChangeListener(this);
        editTextPreference.setOnPreferenceClickListener(this);

        editTextPreference = (EditTextPreference) findPreference(
                getResources().getString(R.string.param_preferences_7_compensate_key));
        editTextPreference.setSummary(this.paramPrefs.getString(
                getResources().getString(R.string.param_preferences_7_compensate_key), ""));
        editTextPreference.setOnPreferenceChangeListener(this);
        editTextPreference.setOnPreferenceClickListener(this);

        editTextPreference = (EditTextPreference) findPreference(
                getResources().getString(R.string.param_preferences_9_compensate_key));
        editTextPreference.setSummary(this.paramPrefs.getString(
                getResources().getString(R.string.param_preferences_9_compensate_key), ""));
        editTextPreference.setOnPreferenceChangeListener(this);
        editTextPreference.setOnPreferenceClickListener(this);

        editTextPreference = (EditTextPreference) findPreference(
                getResources().getString(R.string.param_preferences_11_compensate_key));
        editTextPreference.setSummary(this.paramPrefs.getString(
                getResources().getString(R.string.param_preferences_11_compensate_key), ""));
        editTextPreference.setOnPreferenceChangeListener(this);
        editTextPreference.setOnPreferenceClickListener(this);

        editTextPreference = (EditTextPreference) findPreference(
                getResources().getString(R.string.param_preferences_13_compensate_key));
        editTextPreference.setSummary(this.paramPrefs.getString(
                getResources().getString(R.string.param_preferences_13_compensate_key), ""));
        editTextPreference.setOnPreferenceChangeListener(this);
        editTextPreference.setOnPreferenceClickListener(this);

        editTextPreference = (EditTextPreference) findPreference(
                getResources().getString(R.string.param_preferences_15_compensate_key));
        editTextPreference.setSummary(this.paramPrefs.getString(
                getResources().getString(R.string.param_preferences_15_compensate_key), ""));
        editTextPreference.setOnPreferenceChangeListener(this);
        editTextPreference.setOnPreferenceClickListener(this);

        editTextPreference = (EditTextPreference) findPreference(
                getResources().getString(R.string.param_preferences_17_compensate_key));
        editTextPreference.setSummary(this.paramPrefs.getString(
                getResources().getString(R.string.param_preferences_17_compensate_key), ""));
        editTextPreference.setOnPreferenceChangeListener(this);
        editTextPreference.setOnPreferenceClickListener(this);

        editTextPreference = (EditTextPreference) findPreference(
                getResources().getString(R.string.param_preferences_19_compensate_key));
        editTextPreference.setSummary(this.paramPrefs.getString(
                getResources().getString(R.string.param_preferences_19_compensate_key), ""));
        editTextPreference.setOnPreferenceChangeListener(this);
        editTextPreference.setOnPreferenceClickListener(this);

        editTextPreference = (EditTextPreference) findPreference(
                getResources().getString(R.string.param_preferences_21_compensate_key));
        editTextPreference.setSummary(this.paramPrefs.getString(
                getResources().getString(R.string.param_preferences_21_compensate_key), ""));
        editTextPreference.setOnPreferenceChangeListener(this);
        editTextPreference.setOnPreferenceClickListener(this);

        editTextPreference = (EditTextPreference) findPreference(
                getResources().getString(R.string.param_preferences_23_compensate_key));
        editTextPreference.setSummary(this.paramPrefs.getString(
                getResources().getString(R.string.param_preferences_23_compensate_key), ""));
        editTextPreference.setOnPreferenceChangeListener(this);
        editTextPreference.setOnPreferenceClickListener(this);

        editTextPreference = (EditTextPreference) findPreference(
                getResources().getString(R.string.param_preferences_25_compensate_key));
        editTextPreference.setSummary(this.paramPrefs.getString(
                getResources().getString(R.string.param_preferences_25_compensate_key), ""));
        editTextPreference.setOnPreferenceChangeListener(this);
        editTextPreference.setOnPreferenceClickListener(this);

        editTextPreference = (EditTextPreference) findPreference(
                getResources().getString(R.string.param_preferences_26_compensate_key));
        editTextPreference.setSummary(this.paramPrefs.getString(
                getResources().getString(R.string.param_preferences_26_compensate_key), ""));
        editTextPreference.setOnPreferenceChangeListener(this);
        editTextPreference.setOnPreferenceClickListener(this);

        // 打开网络通信进度框
        dialogFragment = new ConnectDialogFragment();
    }

    @Override
    public void onResume() {
        super.onResume();
        getPreferenceScreen().getSharedPreferences().registerOnSharedPreferenceChangeListener(this);

        isTimeout = false; // 清空超时，允许进行TCP操作

        tcpClientManager.sendCmd(context, TcpClientManager.TcpCmdType.LOAD_PARAMS,
                new String[]{deviceBean.getDeviceNo()}, tcpHandler);
        dialogFragment.show(getChildFragmentManager(), ParamPrefFragment.class.getName());
    }

    @Override
    public void onPause() {
        super.onPause();
        getPreferenceScreen().getSharedPreferences().unregisterOnSharedPreferenceChangeListener(this);
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
    public boolean onPreferenceChange(Preference preference, Object newValue) {

        isTimeout = false; // 清空超时，允许进行TCP操作

        if (correctParamPwd) {
            tcpClientManager.sendCmd(context, TcpClientManager.TcpCmdType.UPDATE_PARAMS,
                    new String[]{deviceBean.getDeviceNo(), preference.getKey(), newValue.toString()},
                    tcpHandler);

            dialogFragment.show(getFragmentManager(), ParamPrefFragment.class.getName());

            return true;
        }

        ConnectToast toast  = new ConnectToast(getContext(),
                ConnectToast.ConnectRes.BAD,
                getResources().getString(R.string.pref_fragment_dialog_pwd_error_double),
                Toast.LENGTH_SHORT);
        toast.show();

        return false;
    }

    @Override
    public boolean onPreferenceClick(Preference preference) {
        Log.i(TAG, "onPreferenceClick");

        confirmDialogFragment.show(getFragmentManager(), ParamPrefFragment.class.getName());

        return false;
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

    // 参数下载
    public void downloadDeviceParams() {
        isTimeout = false; // 清空超时，允许进行TCP操作

        // 执行参数下载的指令
        tcpClientManager.sendCmd(context, TcpClientManager.TcpCmdType.LOAD_PARAMS,
                new String[]{deviceBean.getDeviceNo()}, tcpHandler);

        dialogFragment.show(getFragmentManager(), ParamPrefFragment.class.getName());
    }

    // 参数保存
    public void saveDeviceParams() {
        isTimeout = false; // 清空超时，允许进行TCP操作

        // 执行参数保存的指令
        tcpClientManager.sendCmd(context, TcpClientManager.TcpCmdType.SAVE_PARAMS,
                new String[]{deviceBean.getDeviceNo()}, tcpHandler);
    }

    // 参数导出
    public void exportDeviceParams() {
        Log.i(TAG, "exportDeviceParams");
    }

    // 恢复出厂设置
    public void restoreFactoryParams() {
        isTimeout = false; // 清空超时，允许进行TCP操作

        // 执行参数恢复出厂设置的指令
        tcpClientManager.sendCmd(context, TcpClientManager.TcpCmdType.RESTORE_PARAMS,
                new String[]{deviceBean.getDeviceNo()}, tcpHandler);

        dialogFragment.show(getFragmentManager(), ParamPrefFragment.class.getName());

        // 延时指定时间后执行下载参数
        Timer restoreFactoryTimer = new Timer();
        restoreFactoryTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                isTimeout = false; // 清空超时，允许进行TCP操作

                // 执行参数下载的指令
                tcpClientManager.sendCmd(context, TcpClientManager.TcpCmdType.LOAD_PARAMS,
                        new String[]{deviceBean.getDeviceNo()}, tcpHandler);
            }
        }, context.getResources().getInteger(R.integer.tcp_restore_param_delay));
    }

    public void setCurrentParamStatus(boolean status) {
        Log.i(TAG, status ? "可以修改数据" : "不能修改数据");

        correctParamPwd = status;
    }
}
