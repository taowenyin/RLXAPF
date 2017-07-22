package cn.edu.siso.rlxapf;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.preference.EditTextPreference;
import android.support.v7.preference.ListPreference;
import android.support.v7.preference.Preference;
import android.support.v7.preference.PreferenceFragmentCompat;
import android.support.v7.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.ListView;

public class DevicePrefFragment extends PreferenceFragmentCompat implements
        SharedPreferences.OnSharedPreferenceChangeListener {

    private SharedPreferences devicePrefs = null;

    public DevicePrefFragment() {
        // Required empty public constructor
    }

    public static DevicePrefFragment newInstance() {
        DevicePrefFragment fragment = new DevicePrefFragment();
        return fragment;
    }

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {

        getPreferenceManager().setSharedPreferencesName(
                getResources().getString(R.string.device_preferences_name));

        setPreferencesFromResource(R.xml.pref_device, rootKey);

        // 载入配置文件的默认值
        PreferenceManager.setDefaultValues(getActivity(), R.xml.pref_device, false);

        this.devicePrefs = getPreferenceScreen().getSharedPreferences();

        // 把每个Preference的设置到Summary中
        findPreference(getResources().getString(R.string.device_preferences_local_key))
                .setSummary(this.devicePrefs.getString(
                        getResources().getString(R.string.device_preferences_local_key), "设备区域设置错误"));
        findPreference(getResources().getString(R.string.device_preferences_address_key))
                .setSummary(this.devicePrefs.getString(
                        getResources().getString(R.string.device_preferences_address_key), "设备地址设置错误"));
        findPreference(getResources().getString(R.string.device_preferences_name_key))
                .setSummary(this.devicePrefs.getString(
                        getResources().getString(R.string.device_preferences_name_key), "客户名是什么？"));
        findPreference(getResources().getString(R.string.device_preferences_device_type_key))
                .setSummary(this.devicePrefs.getString(
                        getResources().getString(R.string.device_preferences_device_type_key), "未知的设备类型"));
        findPreference(getResources().getString(R.string.device_preferences_device_gps_key))
                .setSummary(this.devicePrefs.getString(
                        getResources().getString(R.string.device_preferences_device_gps_key), "GPS编号错误"));
        findPreference(getResources().getString(R.string.device_preferences_device_no_key))
                .setSummary(this.devicePrefs.getString(
                        getResources().getString(R.string.device_preferences_device_no_key), "设备号错误"));
        findPreference(getResources().getString(R.string.device_preferences_collection_freq_key))
                .setSummary(((ListPreference) findPreference(
                        getResources().getString(R.string.device_preferences_collection_freq_key))).getEntry());
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        // 移除默认的分割线
        setDivider(new ColorDrawable(Color.TRANSPARENT));
        setDividerHeight(0);

        // PreferenceFragment中移除默认的分割线
//        View rootView = getView();
//        ListView list = (ListView) rootView.findViewById(android.R.id.list);
//        list.setDivider(null);
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
