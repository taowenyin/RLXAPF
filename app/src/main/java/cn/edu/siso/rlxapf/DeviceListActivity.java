package cn.edu.siso.rlxapf;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;

import org.apache.commons.lang3.StringUtils;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.edu.siso.rlxapf.bean.CityBean;
import cn.edu.siso.rlxapf.bean.CompanyBean;
import cn.edu.siso.rlxapf.bean.CountyBean;
import cn.edu.siso.rlxapf.bean.DeviceBean;
import cn.edu.siso.rlxapf.bean.GprsBean;
import cn.edu.siso.rlxapf.bean.ProvinceBean;
import cn.edu.siso.rlxapf.bean.TypeBean;
import cn.edu.siso.rlxapf.bean.UserBean;
import cn.edu.siso.rlxapf.config.HTTPConfig;
import cn.edu.siso.rlxapf.config.TCPConfig;
import cn.edu.siso.rlxapf.dialog.ConnectDialogFragment;
import cn.edu.siso.rlxapf.util.http.OkHttpClientManager;
import cn.edu.siso.rlxapf.util.tcp.TcpClientManager;

public class DeviceListActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    // HTTP请求类型
    public static enum HTTP_REQ_TYPE {
        REQ_EMPTY, REQ_DEVICE, REQ_PROVINCE, REQ_CITY,
        REQ_COUNTY, REQ_COMPANY, REQ_TYPE, REQ_GPRS
    };

    private ConnectDialogFragment dialogFragment = null;

    // 设备信息
    private List<DeviceBean> deviceData = null;

    private Spinner deviceListProvince = null;
    private SimpleAdapter deviceListProvinceAdapter = null;
    private List<Map<String, String>> deviceListProvinceData = null;
    private int currProvinceIndex = 0;

    private Spinner deviceListCity = null;
    private SimpleAdapter deviceListCityAdapter = null;
    private List<Map<String, String>> deviceListCityData = null;
    private int currCityIndex = 0;

    private Spinner deviceListCounty = null;
    private SimpleAdapter deviceListCountyAdapter = null;
    private List<Map<String, String>> deviceListCountyData = null;
    private int currCountyIndex = 0;

    private Spinner deviceListCompany = null;
    private SimpleAdapter deviceListCompanyAdapter = null;
    private List<Map<String, String>> deviceListCompanyData = null;
    private int currCompanyIndex = 0;

    private Spinner deviceListType = null;
    private SimpleAdapter deviceListTypeAdapter = null;
    private List<Map<String, String>> deviceListTypeData = null;
    private int currTypeIndex = 0;

    private Spinner deviceListGprs = null;
    private SimpleAdapter deviceListGprsAdapter = null;
    private List<Map<String, String>> deviceListGprsData = null;
    private int currGprsIndex = 0;

    private Button deviceConnectBtn = null;

    // 当前HTTP请求类型
    private HTTP_REQ_TYPE currHttpReqType = HTTP_REQ_TYPE.REQ_EMPTY;

    private Handler httpHandler = null;
    private OkHttpClientManager httpManager = null;
    private boolean isTimeout = false;

    private TcpClientManager tcpClientManager = null;
    private Handler tcpHandler = null;

    private UserBean userBean = null;

    private int currentDevicePosition = -1;

    public static final String USER_KEY = "user";
    public static final String DATA_KEY = "data";
    public static final String POSITION_KEY = "position";

    public static final String TAG = "==DeviceListActivity==";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_device_list);

        userBean = JSON.parseObject(
                getIntent().getStringExtra(LoginActivity.USER_DATA_KEY),
                UserBean.class);

        deviceData = new ArrayList<DeviceBean>();

        ImageButton toolbarBack = (ImageButton) findViewById(R.id.toolbar_back);
        dialogFragment = new ConnectDialogFragment(); // 初始化通信对话框对象

        deviceListProvince = (Spinner) findViewById(R.id.device_list_province);
        deviceListProvinceData = new ArrayList<Map<String, String>>();
        deviceListProvinceAdapter = new SimpleAdapter(this,
                deviceListProvinceData,
                R.layout.device_spinner_item_layout,
                new String[]{ProvinceBean.NAME},
                new int[]{R.id.device_spinner_item_name});
        deviceListProvince.setAdapter(deviceListProvinceAdapter);
        deviceListProvince.setOnItemSelectedListener(this);

        deviceListCity = (Spinner) findViewById(R.id.device_list_city);
        deviceListCityData = new ArrayList<Map<String, String>>();
        deviceListCityAdapter = new SimpleAdapter(this,
                deviceListCityData,
                R.layout.device_spinner_item_layout,
                new String[]{CityBean.NAME},
                new int[]{R.id.device_spinner_item_name});
        deviceListCity.setAdapter(deviceListCityAdapter);
        deviceListCity.setOnItemSelectedListener(this);

        deviceListCounty = (Spinner) findViewById(R.id.device_list_county);
        deviceListCountyData = new ArrayList<Map<String, String>>();
        deviceListCountyAdapter = new SimpleAdapter(this,
                deviceListCountyData,
                R.layout.device_spinner_item_layout,
                new String[]{CountyBean.NAME},
                new int[]{R.id.device_spinner_item_name});
        deviceListCounty.setAdapter(deviceListCountyAdapter);
        deviceListCounty.setOnItemSelectedListener(this);

        deviceListCompany = (Spinner) findViewById(R.id.device_list_company);
        deviceListCompanyData = new ArrayList<Map<String, String>>();
        deviceListCompanyAdapter = new SimpleAdapter(this,
                deviceListCompanyData,
                R.layout.device_spinner_item_layout,
                new String[]{CompanyBean.NAME},
                new int[]{R.id.device_spinner_item_name});
        deviceListCompany.setAdapter(deviceListCompanyAdapter);
        deviceListCompany.setOnItemSelectedListener(this);

        deviceListType = (Spinner) findViewById(R.id.device_list_type);
        deviceListTypeData = new ArrayList<Map<String, String>>();
        deviceListTypeAdapter = new SimpleAdapter(this,
                deviceListTypeData,
                R.layout.device_spinner_item_layout,
                new String[]{TypeBean.DEVICE_TYPE},
                new int[]{R.id.device_spinner_item_name});
        deviceListType.setAdapter(deviceListTypeAdapter);
        deviceListType.setOnItemSelectedListener(this);

        deviceListGprs = (Spinner) findViewById(R.id.device_list_gprs);
        deviceListGprsData = new ArrayList<Map<String, String>>();
        deviceListGprsAdapter = new SimpleAdapter(this,
                deviceListGprsData,
                R.layout.device_spinner_item_layout,
                new String[]{GprsBean.GPS_DEVICE_NO},
                new int[]{R.id.device_spinner_item_name});
        deviceListGprs.setAdapter(deviceListGprsAdapter);
        deviceListGprs.setOnItemSelectedListener(this);

        deviceConnectBtn = (Button) findViewById(R.id.device_connect_btn);

        httpHandler = new HttpHandler();
        tcpHandler = new TcpHandler();

        // 标题栏点击事件
        toolbarBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 清空用户数据
                SharedPreferences.Editor editor = getSharedPreferences(
                        getResources().getString(R.string.account_pref_name),
                        MODE_PRIVATE).edit();
                editor.clear();
                editor.apply();

                Intent intent = new Intent(DeviceListActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });

        deviceConnectBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Log.i(TAG, "GPS_DEVICE_NO:" + deviceListGprsData.get(currGprsIndex).get(GprsBean.GPS_DEVICE_NO));

                int position = -1; // 设备的索引

                for (int i = 0; i < deviceData.size(); i++) {
                    DeviceBean bean = deviceData.get(i);
                    if (bean.getGPSDeviceNo().equals(
                            deviceListGprsData.get(currGprsIndex).get(GprsBean.GPS_DEVICE_NO))) {
                        position = i;
                        break;
                    }
                }

                if (position != -1) {
                    currentDevicePosition = position;

                    if (tcpClientManager.getConnectStatus()) {
                        Log.i(TAG, "设备已经连接，进入监控设备");
                        Intent intent = new Intent(DeviceListActivity.this, MainActivity.class);
                        Bundle bundle = new Bundle();
                        bundle.putString(DATA_KEY, JSON.toJSONString(
                                deviceData, SerializerFeature.WriteMapNullValue));
                        bundle.putString(USER_KEY, JSON.toJSONString(
                                userBean, SerializerFeature.WriteMapNullValue));
                        bundle.putInt(POSITION_KEY, currentDevicePosition);
                        intent.putExtras(bundle);
                        startActivity(intent);
                    } else {
                        isTimeout = false; // 清空超时，允许进行TCP操作

                        tcpClientManager.connect(getApplicationContext(),
                                deviceData.get(position).getGPSDeviceNo(),
                                TCPConfig.DEFAULT_DEVICE_PWD, tcpHandler);
                        dialogFragment.show(getSupportFragmentManager(), DeviceListActivity.class.getName());
                    }
                } else {
                    ConnectToast toast = new ConnectToast(getApplicationContext(),
                            ConnectToast.ConnectRes.BAD,
                            getResources().getString(R.string.tcp_connect_gprs_error),
                            Toast.LENGTH_LONG);
                    toast.show();
                }
            }
        });

        // 获取设备列表
        RLXApplication application = (RLXApplication) getApplication();

        // 初始化HTTP链接对象
        httpManager = application.getHttpManager();
        // 初始化TCP连接对象
        tcpClientManager = TcpClientManager.getInstance();

        dialogFragment.show(getSupportFragmentManager(), DeviceListActivity.class.getName());
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

        // 解决Spinner同一索引不能触发点击事件的问题
        try {
            Field field = AdapterView.class.getDeclaredField("mOldSelectedPosition");
            field.setAccessible(true);
            if (adapterView.getId() == R.id.device_list_province) {
                field.setInt(deviceListProvince, AdapterView.INVALID_POSITION);
            }
            if (adapterView.getId() == R.id.device_list_city) {
                field.setInt(deviceListCity, AdapterView.INVALID_POSITION);
            }
            if (adapterView.getId() == R.id.device_list_county) {
                field.setInt(deviceListCounty, AdapterView.INVALID_POSITION);
            }
            if (adapterView.getId() == R.id.device_list_company) {
                field.setInt(deviceListCompany, AdapterView.INVALID_POSITION);
            }
            if (adapterView.getId() == R.id.device_list_type) {
                field.setInt(deviceListType, AdapterView.INVALID_POSITION);
            }
            if (adapterView.getId() == R.id.device_list_gprs) {
                field.setInt(deviceListGprs, AdapterView.INVALID_POSITION);
            }

        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

        if (adapterView.getId() == R.id.device_list_province) {
            currProvinceIndex = i;

            Log.i(TAG, "device_list_province");

            // 获取市级信息
            Map<String, String> params = new HashMap<String, String>();
            params.put("mobileid", userBean.getMobileId());
            params.put("province", deviceListProvinceData.get(i).get(ProvinceBean.ID));
            httpManager.httpStrGetAsyn(HTTPConfig.API_URL_QUERY_CITY, params, httpHandler);
            currHttpReqType = HTTP_REQ_TYPE.REQ_CITY;
        }
        if (adapterView.getId() == R.id.device_list_city) {
            currCityIndex = i;

            Log.i(TAG, "device_list_city");

            // 获取区级信息
            Map<String, String> params = new HashMap<String, String>();
            params.put("mobileid", userBean.getMobileId());
            params.put("city", deviceListCityData.get(i).get(CityBean.ID));
            httpManager.httpStrGetAsyn(HTTPConfig.API_URL_QUERY_COUNTY, params, httpHandler);
            currHttpReqType = HTTP_REQ_TYPE.REQ_COUNTY;
        }
        if (adapterView.getId() == R.id.device_list_county) {
            currCountyIndex = i;

            Log.i(TAG, "device_list_county");

            // 获取公司信息
            Map<String, String> params = new HashMap<String, String>();
            params.put("mobileid", userBean.getMobileId());
            params.put("province", deviceListProvinceData.get(currProvinceIndex).get(ProvinceBean.CODE));
            params.put("city", deviceListCityData.get(currCityIndex).get(CityBean.CODE));
            params.put("county", deviceListCountyData.get(currCountyIndex).get(CountyBean.CODE));
            httpManager.httpStrGetAsyn(HTTPConfig.API_URL_QUERY_CUSTOMER, params, httpHandler);
            currHttpReqType = HTTP_REQ_TYPE.REQ_COMPANY;
        }
        if (adapterView.getId() == R.id.device_list_company) {
            currCompanyIndex = i;

            Log.i(TAG, "device_list_company");

            // 获取类型信息
            Map<String, String> params = new HashMap<String, String>();
            params.put("mobileid", userBean.getMobileId());
            params.put("province", deviceListProvinceData.get(currProvinceIndex).get(ProvinceBean.CODE));
            params.put("city", deviceListCityData.get(currCityIndex).get(CityBean.CODE));
            params.put("county", deviceListCountyData.get(currCountyIndex).get(CountyBean.CODE));
            params.put("account", deviceListCompanyData.get(currCompanyIndex).get(CompanyBean.ACCOUNT));
            httpManager.httpStrGetAsyn(HTTPConfig.API_URL_QUERY_DEVICE_TYPE, params, httpHandler);
            currHttpReqType = HTTP_REQ_TYPE.REQ_TYPE;
        }
        if (adapterView.getId() == R.id.device_list_type) {
            currTypeIndex = i;

            Log.i(TAG, "device_list_type");

            // 获取GPRS信息
            Map<String, String> params = new HashMap<String, String>();
            params.put("mobileid", userBean.getMobileId());
            params.put("province", deviceListProvinceData.get(currProvinceIndex).get(ProvinceBean.CODE));
            params.put("city", deviceListCityData.get(currCityIndex).get(CityBean.CODE));
            params.put("county", deviceListCountyData.get(currCountyIndex).get(CountyBean.CODE));
            params.put("account", deviceListCompanyData.get(currCompanyIndex).get(CompanyBean.ACCOUNT));
            params.put("devicetype", deviceListTypeData.get(currTypeIndex).get(TypeBean.DEVICE_TYPE));
            httpManager.httpStrGetAsyn(HTTPConfig.API_URL_QUERY_GPRS, params, httpHandler);
            currHttpReqType = HTTP_REQ_TYPE.REQ_GPRS;
        }
        if (adapterView.getId() == R.id.device_list_gprs) {
            currGprsIndex = i;

            Log.i(TAG, "device_list_gprs");

            // 获取全部设备信息
            Map<String, String> params = new HashMap<String, String>();
            params.put("mobileid", userBean.getMobileId());
            params.put("account", userBean.getAccount());
            httpManager.httpStrGetAsyn(HTTPConfig.API_URL_QUERY_DEVICE, params, httpHandler);
            currHttpReqType = HTTP_REQ_TYPE.REQ_DEVICE;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    @Override
    protected void onResume() {
        super.onResume();

        Map<String, String> params = new HashMap<String, String>();
        params.put("mobileid", userBean.getMobileId());
        httpManager.httpStrGetAsyn(HTTPConfig.API_URL_QUERY_PROVINCE, params, httpHandler);
        currHttpReqType = HTTP_REQ_TYPE.REQ_PROVINCE;
    }

    private class HttpHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            Bundle data = msg.getData();
            String resultType = data.getString(OkHttpClientManager.HTTP_RESPONSE_TYPE);
            String resultData = data.getString(OkHttpClientManager.HTTP_RESPONSE_DATA);

            if (dialogFragment.getShowsDialog()) {
                dialogFragment.dismiss();
            }

            if (!TextUtils.isEmpty(resultData) && StringUtils.isNumeric(resultData)) {
                String badMsg = "";
                int resCode = Integer.parseInt(resultData);
                switch (resCode) {
                    case HTTPConfig.DeviceListError.MOBILE_ID_NO_SET:
                        badMsg = getResources().getString(R.string.device_list_error_mobile_id_no_set);
                        break;
                    case HTTPConfig.DeviceListError.SESSION_NO_SET:
                        badMsg = getResources().getString(R.string.device_list_error_session_no_set);

                        // 清空账户信息
                        SharedPreferences accountPref = getSharedPreferences(
                                getResources().getString(R.string.account_pref_name), MODE_PRIVATE);
                        SharedPreferences.Editor editor = accountPref.edit();
                        editor.clear();
                        editor.apply();

                        Intent intent = new Intent(DeviceListActivity.this, LoginActivity.class);
                        startActivity(intent);
                        finish();
                        break;
                    case HTTPConfig.DeviceListError.SESSION_ERROR:
                        badMsg = getResources().getString(R.string.device_list_error_session_error);
                        break;
                    case HTTPConfig.DeviceListError.NO_DATA:
                        badMsg = getResources().getString(R.string.device_list_error_no_data);

                        // 无数据时清空相应数据
                        if (currHttpReqType == HTTP_REQ_TYPE.REQ_PROVINCE) {
                            deviceListProvinceData.clear();
                            deviceListProvinceAdapter.notifyDataSetChanged();

                            deviceListCityData.clear();
                            deviceListCityAdapter.notifyDataSetChanged();

                            deviceListCountyData.clear();
                            deviceListCountyAdapter.notifyDataSetChanged();

                            deviceListCompanyData.clear();
                            deviceListCompanyAdapter.notifyDataSetChanged();

                            deviceListTypeData.clear();
                            deviceListTypeAdapter.notifyDataSetChanged();

                            deviceListGprsData.clear();
                            deviceListGprsAdapter.notifyDataSetChanged();
                        }
                        if (currHttpReqType == HTTP_REQ_TYPE.REQ_CITY) {
                            deviceListCityData.clear();
                            deviceListCityAdapter.notifyDataSetChanged();

                            deviceListCountyData.clear();
                            deviceListCountyAdapter.notifyDataSetChanged();

                            deviceListCompanyData.clear();
                            deviceListCompanyAdapter.notifyDataSetChanged();

                            deviceListTypeData.clear();
                            deviceListTypeAdapter.notifyDataSetChanged();

                            deviceListGprsData.clear();
                            deviceListGprsAdapter.notifyDataSetChanged();
                        }
                        if (currHttpReqType == HTTP_REQ_TYPE.REQ_COUNTY) {
                            deviceListCountyData.clear();
                            deviceListCountyAdapter.notifyDataSetChanged();

                            deviceListCompanyData.clear();
                            deviceListCompanyAdapter.notifyDataSetChanged();

                            deviceListTypeData.clear();
                            deviceListTypeAdapter.notifyDataSetChanged();

                            deviceListGprsData.clear();
                            deviceListGprsAdapter.notifyDataSetChanged();
                        }
                        if (currHttpReqType == HTTP_REQ_TYPE.REQ_COMPANY) {
                            deviceListCompanyData.clear();
                            deviceListCompanyAdapter.notifyDataSetChanged();

                            deviceListTypeData.clear();
                            deviceListTypeAdapter.notifyDataSetChanged();

                            deviceListGprsData.clear();
                            deviceListGprsAdapter.notifyDataSetChanged();
                        }
                        if (currHttpReqType == HTTP_REQ_TYPE.REQ_TYPE) {
                            deviceListTypeData.clear();
                            deviceListTypeAdapter.notifyDataSetChanged();

                            deviceListGprsData.clear();
                            deviceListGprsAdapter.notifyDataSetChanged();
                        }
                        if (currHttpReqType == HTTP_REQ_TYPE.REQ_GPRS) {
                            deviceListGprsData.clear();
                            deviceListGprsAdapter.notifyDataSetChanged();
                        }
                        break;
                }

                Toast toast = new ConnectToast(
                        getApplicationContext(),
                        ConnectToast.ConnectRes.BAD, badMsg, Toast.LENGTH_SHORT);
                toast.show();
            } else {
                if (currHttpReqType == HTTP_REQ_TYPE.REQ_DEVICE) {
                    deviceData = JSON.parseArray(resultData, DeviceBean.class);
                }

                if (currHttpReqType == HTTP_REQ_TYPE.REQ_PROVINCE) {
                    deviceListProvinceData.clear();
                    List<ProvinceBean> provinceData = JSON.parseArray(resultData, ProvinceBean.class);
                    for (ProvinceBean bean : provinceData) {
                        Map<String, String> item = new HashMap<String, String>();
                        item.put(ProvinceBean.ID, bean.getId());
                        item.put(ProvinceBean.CODE, bean.getCode());
                        item.put(ProvinceBean.NAME, bean.getName());

                        deviceListProvinceData.add(item);
                    }
                    deviceListProvinceAdapter.notifyDataSetChanged();
                    deviceListProvince.setSelection(0);
                }

                if (currHttpReqType == HTTP_REQ_TYPE.REQ_CITY) {
                    deviceListCityData.clear();
                    List<CityBean> cityData = JSON.parseArray(resultData, CityBean.class);
                    for (CityBean bean : cityData) {
                        Map<String, String> item = new HashMap<String, String>();
                        item.put(CityBean.ID, bean.getId());
                        item.put(CityBean.CODE, bean.getCode());
                        item.put(CityBean.NAME, bean.getName());
                        item.put(CityBean.PARENT_ID, bean.getParentId());
                        item.put(CityBean.FIRST_LETTER, bean.getFirstLetter());
                        item.put(CityBean.CITY_LEVEL, bean.getCityLevel());

                        deviceListCityData.add(item);
                    }
                    deviceListCityAdapter.notifyDataSetChanged();
                    deviceListCity.setSelection(0);
                }

                if (currHttpReqType == HTTP_REQ_TYPE.REQ_COUNTY) {
                    deviceListCountyData.clear();
                    List<CountyBean> countyData = JSON.parseArray(resultData, CountyBean.class);
                    for (CountyBean bean : countyData) {
                        Map<String, String> item = new HashMap<String, String>();
                        item.put(CountyBean.ID, bean.getId());
                        item.put(CountyBean.CODE, bean.getCode());
                        item.put(CountyBean.NAME, bean.getName());
                        item.put(CountyBean.PARENT_ID, bean.getParentId());
                        item.put(CountyBean.FIRST_LETTER, bean.getFirstLetter());
                        item.put(CountyBean.CITY_LEVEL, bean.getCityLevel());

                        deviceListCountyData.add(item);
                    }
                    deviceListCountyAdapter.notifyDataSetChanged();
                    deviceListCounty.setSelection(0);
                }

                if (currHttpReqType == HTTP_REQ_TYPE.REQ_COMPANY) {
                    deviceListCompanyData.clear();
                    List<CompanyBean> companyData = JSON.parseArray(resultData, CompanyBean.class);
                    for (CompanyBean bean : companyData) {
                        Map<String, String> item = new HashMap<String, String>();
                        item.put(CompanyBean.ACCOUNT, bean.getAccount());
                        item.put(CompanyBean.NAME, bean.getName());

                        deviceListCompanyData.add(item);
                    }
                    deviceListCompanyAdapter.notifyDataSetChanged();
                    deviceListCompany.setSelection(0);
                }

                if (currHttpReqType == HTTP_REQ_TYPE.REQ_TYPE) {
                    deviceListTypeData.clear();
                    List<TypeBean> typeData = JSON.parseArray(resultData, TypeBean.class);
                    for (TypeBean bean : typeData) {
                        Map<String, String> item = new HashMap<String, String>();
                        item.put(TypeBean.DEVICE_TYPE, bean.getDeviceType());

                        deviceListTypeData.add(item);
                    }
                    deviceListTypeAdapter.notifyDataSetChanged();
                    deviceListType.setSelection(0);
                }

                if (currHttpReqType == HTTP_REQ_TYPE.REQ_GPRS) {
                    deviceListGprsData.clear();
                    List<GprsBean> gprsData = JSON.parseArray(resultData, GprsBean.class);
                    for (GprsBean bean : gprsData) {
                        Map<String, String> item = new HashMap<String, String>();
                        item.put(GprsBean.GPS_DEVICE_NO, bean.getGPSDeviceNo());

                        deviceListGprsData.add(item);
                    }
                    deviceListGprsAdapter.notifyDataSetChanged();
                    deviceListGprs.setSelection(0);
                }

            }
        }
    }

    private class TcpHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            if (dialogFragment.getShowsDialog()) {
                dialogFragment.dismiss();
            }

            Bundle data = msg.getData();
            String tcpOperateType = data.getString(TcpClientManager.KEY_TCP_OPERATE_TYPE);
            String tcpResType = data.getString(TcpClientManager.KEY_TCP_RES_TYPE);

            if (tcpOperateType.equals(TcpClientManager.TcpOperateType.CONNECT)) {
                // 如果超时，则禁止更新数据
                if (!isTimeout) {
                    if (tcpResType.equals(TcpClientManager.TcpResType.SUCCESS)) {
                        Intent intent = new Intent(DeviceListActivity.this, MainActivity.class);
                        Bundle bundle = new Bundle();
                        bundle.putString(DATA_KEY, JSON.toJSONString(
                                deviceData, SerializerFeature.WriteMapNullValue));
                        bundle.putString(USER_KEY, JSON.toJSONString(
                                userBean, SerializerFeature.WriteMapNullValue));
                        bundle.putInt(POSITION_KEY, currentDevicePosition);
                        intent.putExtras(bundle);

                        startActivity(intent);
                    }
                    if (tcpResType.equals(TcpClientManager.TcpResType.ERROR)) {
                        String tcpResCode = data.getString(TcpClientManager.KEY_TCP_RES_CODE);
                        String tcpResData = data.getString(TcpClientManager.KEY_TCP_RES_DATA);

                        ConnectToast toast = new ConnectToast(getApplicationContext(),
                                ConnectToast.ConnectRes.BAD,
                                tcpResCode + " : " + tcpResData,
                                Toast.LENGTH_LONG);
                    }
                    if (tcpResType.equals(TcpClientManager.TcpResType.CLOSE)) {
                        ConnectToast toast = new ConnectToast(getApplicationContext(),
                                ConnectToast.ConnectRes.BAD,
                                getResources().getString(R.string.tcp_connect_close),
                                Toast.LENGTH_LONG);
                        toast.show();
                    }
                }
                if (tcpResType.equals(TcpClientManager.TcpResType.TIMEOUT)) {
                    // 标记当前为超时状态
                    isTimeout = true;

                    ConnectToast toast = new ConnectToast(getApplicationContext(),
                            ConnectToast.ConnectRes.BAD,
                            getResources().getString(R.string.tcp_connect_time_out),
                            Toast.LENGTH_LONG);
                    toast.show();
                }
            }
        }
    }
}
