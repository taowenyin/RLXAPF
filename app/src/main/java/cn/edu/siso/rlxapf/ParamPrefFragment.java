package cn.edu.siso.rlxapf;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.preference.EditTextPreference;
import android.support.v7.preference.ListPreference;
import android.support.v7.preference.Preference;
import android.support.v7.preference.PreferenceFragmentCompat;
import android.support.v7.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


public class ParamPrefFragment extends PreferenceFragmentCompat implements
        SharedPreferences.OnSharedPreferenceChangeListener {

    private SharedPreferences paramPrefs = null;

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

        findPreference(getResources().getString(R.string.param_preferences_sampling_model_key))
                .setSummary(((ListPreference) findPreference(
                        getResources().getString(R.string.param_preferences_sampling_model_key))).getEntry());
        findPreference(getResources().getString(R.string.param_preferences_compensate_model_key))
                .setSummary(((ListPreference) findPreference(
                        getResources().getString(R.string.param_preferences_compensate_model_key))).getEntry());

        // 系统CT
        editTextPreference = (EditTextPreference) findPreference(
                getResources().getString(R.string.param_preferences_system_ct_key));
        editTextPreference.setSummary(this.paramPrefs.getString(
                getResources().getString(R.string.param_preferences_system_ct_key), ""));

        // 负载CT
        editTextPreference = (EditTextPreference) findPreference(
                getResources().getString(R.string.param_preferences_load_ct_key));
        editTextPreference.setSummary(this.paramPrefs.getString(
                getResources().getString(R.string.param_preferences_load_ct_key), ""));

        // 目标功率因素
        editTextPreference = (EditTextPreference) findPreference(
                getResources().getString(R.string.param_preferences_target_power_key));
        editTextPreference.setSummary(this.paramPrefs.getString(
                getResources().getString(R.string.param_preferences_target_power_key), ""));

        // N次谐波
        editTextPreference = (EditTextPreference) findPreference(
                getResources().getString(R.string.param_preferences_3_compensate_key));
        editTextPreference.setSummary(this.paramPrefs.getString(
                getResources().getString(R.string.param_preferences_3_compensate_key), ""));
        editTextPreference = (EditTextPreference) findPreference(
                getResources().getString(R.string.param_preferences_5_compensate_key));
        editTextPreference.setSummary(this.paramPrefs.getString(
                getResources().getString(R.string.param_preferences_5_compensate_key), ""));
        editTextPreference = (EditTextPreference) findPreference(
                getResources().getString(R.string.param_preferences_7_compensate_key));
        editTextPreference.setSummary(this.paramPrefs.getString(
                getResources().getString(R.string.param_preferences_7_compensate_key), ""));
        editTextPreference = (EditTextPreference) findPreference(
                getResources().getString(R.string.param_preferences_9_compensate_key));
        editTextPreference.setSummary(this.paramPrefs.getString(
                getResources().getString(R.string.param_preferences_9_compensate_key), ""));
        editTextPreference = (EditTextPreference) findPreference(
                getResources().getString(R.string.param_preferences_11_compensate_key));
        editTextPreference.setSummary(this.paramPrefs.getString(
                getResources().getString(R.string.param_preferences_11_compensate_key), ""));
        editTextPreference = (EditTextPreference) findPreference(
                getResources().getString(R.string.param_preferences_13_compensate_key));
        editTextPreference.setSummary(this.paramPrefs.getString(
                getResources().getString(R.string.param_preferences_13_compensate_key), ""));
        editTextPreference = (EditTextPreference) findPreference(
                getResources().getString(R.string.param_preferences_15_compensate_key));
        editTextPreference.setSummary(this.paramPrefs.getString(
                getResources().getString(R.string.param_preferences_15_compensate_key), ""));
        editTextPreference = (EditTextPreference) findPreference(
                getResources().getString(R.string.param_preferences_17_compensate_key));
        editTextPreference.setSummary(this.paramPrefs.getString(
                getResources().getString(R.string.param_preferences_17_compensate_key), ""));
        editTextPreference = (EditTextPreference) findPreference(
                getResources().getString(R.string.param_preferences_19_compensate_key));
        editTextPreference.setSummary(this.paramPrefs.getString(
                getResources().getString(R.string.param_preferences_19_compensate_key), ""));
        editTextPreference = (EditTextPreference) findPreference(
                getResources().getString(R.string.param_preferences_21_compensate_key));
        editTextPreference.setSummary(this.paramPrefs.getString(
                getResources().getString(R.string.param_preferences_21_compensate_key), ""));
        editTextPreference = (EditTextPreference) findPreference(
                getResources().getString(R.string.param_preferences_23_compensate_key));
        editTextPreference.setSummary(this.paramPrefs.getString(
                getResources().getString(R.string.param_preferences_23_compensate_key), ""));
        editTextPreference = (EditTextPreference) findPreference(
                getResources().getString(R.string.param_preferences_25_compensate_key));
        editTextPreference.setSummary(this.paramPrefs.getString(
                getResources().getString(R.string.param_preferences_25_compensate_key), ""));
        editTextPreference = (EditTextPreference) findPreference(
                getResources().getString(R.string.param_preferences_26_compensate_key));
        editTextPreference.setSummary(this.paramPrefs.getString(
                getResources().getString(R.string.param_preferences_26_compensate_key), ""));
    }

    @Override
    public void onResume() {
        super.onResume();
        getPreferenceScreen().getSharedPreferences().registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        getPreferenceScreen().getSharedPreferences().unregisterOnSharedPreferenceChangeListener(this);
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
}
