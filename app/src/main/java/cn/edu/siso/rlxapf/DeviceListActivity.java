package cn.edu.siso.rlxapf;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Message;
import android.support.v4.content.SharedPreferencesCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.PopupWindow;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.edu.siso.rlxapf.bean.DeviceBean;
import cn.edu.siso.rlxapf.bean.UserBean;
import cn.edu.siso.rlxapf.config.HTTPConfig;
import cn.edu.siso.rlxapf.config.TCPConfig;
import cn.edu.siso.rlxapf.util.http.OkHttpClientManager;
import cn.edu.siso.rlxapf.util.tcp.TcpClientManager;

public class DeviceListActivity extends AppCompatActivity implements
        DeviceListRecyclerAdapter.OnItemClickListener,
        DeviceListRecyclerAdapter.OnOperatorClickListener {

    private enum CurrOperate {NO_OPERATE, ENTER_PARAMS, ENTER_REAL_DATA};

    private DeviceListRecyclerAdapter adapter = null;
    private List<DeviceBean> deviceData = null;

    private PopupBottomMenu operateMenu = null;
    private WindowManager.LayoutParams wLP = null;
    private ConnectDialogFragment dialogFragment = null;

    private Handler httpHandler = null;
    private boolean isTimeout = false;

    private TcpClientManager tcpClientManager = null;
    private Handler tcpHandler = null;

    // 当前的操作
    private CurrOperate currentOperate = CurrOperate.NO_OPERATE;

    private int currentDevicePosition = -1;

    public static final String DATA_KEY = "data";
    public static final String POSITION_KEY = "position";

    public static final String TAG = "DeviceListActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_device_list);

        UserBean userBean = JSON.parseObject(
                getIntent().getStringExtra(LoginActivity.USER_DATA_KEY),
                UserBean.class);

        ImageButton toolbarBack = (ImageButton) findViewById(R.id.toolbar_back);
        RecyclerView deviceListView  = (RecyclerView) findViewById(R.id.device_list_view);
        dialogFragment = new ConnectDialogFragment(); // 初始化通信对话框对象

        deviceData = new ArrayList<DeviceBean>();

        httpHandler = new Handler() {
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
                            SharedPreferencesCompat.EditorCompat.getInstance().apply(editor);

                            Intent intent = new Intent(DeviceListActivity.this, LoginActivity.class);
                            startActivity(intent);
                            finish();
                            break;
                        case HTTPConfig.DeviceListError.SESSION_ERROR:
                            badMsg = getResources().getString(R.string.device_list_error_session_error);
                            break;
                        case HTTPConfig.DeviceListError.NO_DATA:
                            badMsg = getResources().getString(R.string.device_list_error_no_data);
                            break;
                    }

                    Toast toast = new ConnectToast(
                            getApplicationContext(),
                            ConnectToast.ConnectRes.BAD, badMsg, Toast.LENGTH_SHORT);
                    toast.show();
                } else {
                    List<DeviceBean> devices = JSON.parseArray(resultData, DeviceBean.class);
                    deviceData.clear();
                    for (int i = 0; i < devices.size(); i++) {
                        deviceData.add(devices.get(i));
                    }
                    adapter.notifyDataSetChanged();
                }
            }
        };

        tcpHandler = new Handler() {
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
                            if (currentOperate == CurrOperate.ENTER_REAL_DATA) {
                                Intent intent = new Intent(DeviceListActivity.this, MainActivity.class);
                                Bundle bundle = new Bundle();
                                bundle.putString(DATA_KEY, JSON.toJSONString(
                                        deviceData, SerializerFeature.WriteMapNullValue));
                                bundle.putInt(POSITION_KEY, currentDevicePosition);
                                intent.putExtras(bundle);

                                currentOperate = CurrOperate.NO_OPERATE;
                                startActivity(intent);
                            }
                            if (currentOperate == CurrOperate.ENTER_PARAMS) {
                                Intent intent = new Intent(DeviceListActivity.this, ParamActivity.class);
                                Bundle bundle = new Bundle();
                                bundle.putString(DATA_KEY, JSON.toJSONString(
                                        deviceData, SerializerFeature.WriteMapNullValue));
                                bundle.putInt(POSITION_KEY, currentDevicePosition);
                                intent.putExtras(bundle);

                                currentOperate = CurrOperate.NO_OPERATE;
                                startActivity(intent);
                            }
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
        };

        // 初始化弹出框数据
        wLP = getWindow().getAttributes();
        List<Map<String, String>> data = new ArrayList<Map<String, String>>();
        String[] operatePopupData = getResources().getStringArray(
                R.array.device_operate_little_popup_window);
        for (int i = 0; i < operatePopupData.length; i++) {
            Map<String, String> item = new HashMap<String, String>();
            item.put(PopupBottomMenu.TITLE_KEY, operatePopupData[i]);
            data.add(item);
        }
        operateMenu = new PopupBottomMenu(getApplicationContext(),
                data,
                new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        wLP.alpha = 1f;
                        DeviceListActivity.this.getWindow().setAttributes(wLP);
                        operateMenu.dismiss();

                        DeviceBean deviceBean = deviceData.get(currentDevicePosition);
                        switch (position) {
                            case 0:
                                if (tcpClientManager.getConnectStatus()) {
                                    Log.i(TAG, "设备已经连接，进入参数配置");
                                    Intent intent = new Intent(DeviceListActivity.this, ParamActivity.class);
                                    Bundle bundle = new Bundle();
                                    bundle.putString(DATA_KEY, JSON.toJSONString(
                                            deviceData, SerializerFeature.WriteMapNullValue));
                                    bundle.putInt(POSITION_KEY, currentDevicePosition);
                                    intent.putExtras(bundle);
                                    startActivity(intent);
                                } else {
                                    isTimeout = false; // 清空超时，允许进行TCP操作

                                    tcpClientManager.connect(getApplicationContext(),
                                            deviceBean.getGPSDeviceNo(),
                                            TCPConfig.DEFAULT_DEVICE_PWD, tcpHandler);
                                    dialogFragment.show(getSupportFragmentManager(), DeviceListActivity.class.getName());
                                    // 即将进入参数配置页面
                                    currentOperate = CurrOperate.ENTER_PARAMS;
                                }
                                break;
                        }
                    }
                },
                new PopupWindow.OnDismissListener() {
                    @Override
                    public void onDismiss() {
                        wLP.alpha = 1f;
                        DeviceListActivity.this.getWindow().setAttributes(wLP);
                        operateMenu.dismiss();
                    }
                },
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        wLP.alpha = 1f;
                        DeviceListActivity.this.getWindow().setAttributes(wLP);
                        operateMenu.dismiss();
                    }
                });

        // 初始化列表数据
        LinearLayoutManager layoutManager = new LinearLayoutManager(
                getApplicationContext(), LinearLayoutManager.VERTICAL, false);
        adapter = new DeviceListRecyclerAdapter(getApplicationContext(), deviceData);
        adapter.setOnItemClickListener(this);
        adapter.setOnOperatorClickListener(this);
        deviceListView.setLayoutManager(layoutManager);
        deviceListView.setAdapter(adapter);

        toolbarBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 清空用户数据
                SharedPreferences.Editor editor = getSharedPreferences(
                        getResources().getString(R.string.account_pref_name),
                        MODE_PRIVATE).edit();
                editor.clear();
                SharedPreferencesCompat.EditorCompat.getInstance().apply(editor);

                Intent intent = new Intent(DeviceListActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });

        // 获取设备列表
        RLXApplication application = (RLXApplication) getApplication();
        OkHttpClientManager httpManager = application.getHttpManager();
        Map<String, String> params = new HashMap<String, String>();
        params.put("mobileid", userBean.getMobileId());
        params.put("account", userBean.getAccount());
        httpManager.httpStrGetAsyn(HTTPConfig.API_URL_QUERY_DEVICE, params, httpHandler);
        dialogFragment.show(getSupportFragmentManager(), DeviceListActivity.class.getName());

        // 初始化TCP连接对象
        tcpClientManager = TcpClientManager.getInstance();
    }

    @Override
    public void onItemClick(View view, int position) {
        currentDevicePosition = position;

        if (tcpClientManager.getConnectStatus()) {
            Log.i(TAG, "设备已经连接，进入监控设备");
            Intent intent = new Intent(DeviceListActivity.this, MainActivity.class);
            Bundle bundle = new Bundle();
            bundle.putString(DATA_KEY, JSON.toJSONString(
                    deviceData, SerializerFeature.WriteMapNullValue));
            bundle.putInt(POSITION_KEY, currentDevicePosition);
            intent.putExtras(bundle);
            startActivity(intent);
        } else {
            isTimeout = false; // 清空超时，允许进行TCP操作

            tcpClientManager.connect(getApplicationContext(),
                    deviceData.get(position).getGPSDeviceNo(),
                    TCPConfig.DEFAULT_DEVICE_PWD, tcpHandler);
            dialogFragment.show(getSupportFragmentManager(), DeviceListActivity.class.getName());

            // 即将进入参数配置页面
            currentOperate = CurrOperate.ENTER_REAL_DATA;
        }
    }

    @Override
    public void onOperatorClick(View view, int position) {
        currentDevicePosition = position;

        wLP.alpha = 0.5f;
        DeviceListActivity.this.getWindow().setAttributes(wLP);
        operateMenu.showAtLocation(
                DeviceListActivity.this.findViewById(R.id.main),
                Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL,
                0, 0);
    }
}
