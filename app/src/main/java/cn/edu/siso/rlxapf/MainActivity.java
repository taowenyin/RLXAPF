package cn.edu.siso.rlxapf;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentTabHost;
import android.support.v4.content.SharedPreferencesCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.edu.siso.rlxapf.bean.DeviceBean;
import cn.edu.siso.rlxapf.config.TCPConfig;
import cn.edu.siso.rlxapf.util.TCPUtil;
import cn.edu.siso.rlxapf.util.tcp.TcpClientManager;

import static cn.edu.siso.rlxapf.DeviceListActivity.DATA_KEY;
import static cn.edu.siso.rlxapf.DeviceListActivity.POSITION_KEY;
import static cn.edu.siso.rlxapf.util.tcp.TcpClientManager.KEY_TCP_CMD_TYPE;
import static cn.edu.siso.rlxapf.util.tcp.TcpClientManager.KEY_TCP_OPERATE_TYPE;
import static cn.edu.siso.rlxapf.util.tcp.TcpClientManager.KEY_TCP_RES_TYPE;

public class MainActivity extends AppCompatActivity implements
        TabHost.OnTabChangeListener,
        UserFragment.OnFragmentInteractionListener {

    private enum CurrOperate {NO_OPERATE, STOP_DEVICE, START_DEVICE, ENTER_PARAMS};

    private TextView toolbarTitle = null;
    private ImageButton toolbarOperate = null;
    private ImageButton toolbarBack = null;
    private PopupBottomMenu operateMenu = null;
    private ConnectDialogFragment dialogFragment = null;

    private WindowManager.LayoutParams wLP = null;

    private TcpClientManager tcpClientManager = null;
    private Handler tcpHandler = null;
    private boolean isTimeout = false;

    private List<DeviceBean> deviceData = null;
    private int currPosition = -1;

    // 当前的操作
    private CurrOperate currentOperate = CurrOperate.NO_OPERATE;

    // 主页底部标签的图片
    private int[] mainTabIndicatorImgArray = {
            R.drawable.ic_main_tab_real_time_item,
            R.drawable.ic_main_tab_spectrum_item,
            R.drawable.ic_main_tab_user_item
    };

    // 主页底部标签的文字
    private int[] mainTabIndicatorTitleArray = {
            R.string.main_tab_real_time_title,
            R.string.main_tab_spectrum_title,
            R.string.main_tab_user_title
    };

    // 主页底部标签对应的Fragment
    private Class[] mainTabItemFragmentArray = {
            RealTimeFragment.class,
            SpectrumFragment.class,
            UserFragment.class
    };

    public static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 得到设备数据和选择的设备索引
        Bundle deviceBundle = getIntent().getExtras();
        deviceData = JSON.parseArray(
                deviceBundle.getString(DATA_KEY),
                DeviceBean.class);
        currPosition = deviceBundle.getInt(POSITION_KEY);

        FragmentTabHost mainTabHost = (FragmentTabHost) findViewById(android.R.id.tabhost);
        toolbarTitle = (TextView) findViewById(R.id.toolbar_title);
        toolbarOperate = (ImageButton) findViewById(R.id.toolbar_operate);
        toolbarBack = (ImageButton) findViewById(R.id.toolbar_back);
        dialogFragment = new ConnectDialogFragment(); // 初始化通信对话框对象

        tcpHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);

                if (dialogFragment != null &&
                        dialogFragment.getDialog() != null &&
                        dialogFragment.getDialog().isShowing()) {
                    dialogFragment.dismiss();
                }

                Bundle data = msg.getData();
                String operateType = data.getString(KEY_TCP_OPERATE_TYPE);

                if (operateType.equals(TcpClientManager.TcpOperateType.OPERATE)) {
                    int tcpCmdType = Integer.valueOf(data.getString(KEY_TCP_CMD_TYPE));

                    if (TcpClientManager.TcpCmdType.START_DEVICE.ordinal() == tcpCmdType) {
                        String resType = data.getString(KEY_TCP_RES_TYPE);
                        if (resType.equals(TcpClientManager.TcpResType.TIMEOUT)) {
                            // 标记当前为超时状态
                            isTimeout = true;

                            ConnectToast toast = new ConnectToast(getApplicationContext(),
                                    ConnectToast.ConnectRes.BAD,
                                    getResources().getString(R.string.tcp_connect_start_device_time_out),
                                    Toast.LENGTH_LONG);
                            toast.show();
                        }
                        if (!isTimeout) {
                            ConnectToast toast = new ConnectToast(getApplicationContext(),
                                    ConnectToast.ConnectRes.SUCCESS,
                                    getResources().getString(R.string.tcp_connect_start_device_succ),
                                    Toast.LENGTH_SHORT);
                            toast.show();
                        }
                    }
                    if (TcpClientManager.TcpCmdType.STOP_DEVICE.ordinal() == tcpCmdType) {
                        String resType = data.getString(KEY_TCP_RES_TYPE);
                        if (resType.equals(TcpClientManager.TcpResType.TIMEOUT)) {
                            // 标记当前为超时状态
                            isTimeout = true;

                            ConnectToast toast = new ConnectToast(getApplicationContext(),
                                    ConnectToast.ConnectRes.BAD,
                                    getResources().getString(R.string.tcp_connect_stop_device_time_out),
                                    Toast.LENGTH_LONG);
                            toast.show();
                        }
                        if (!isTimeout) {
                            ConnectToast toast = new ConnectToast(getApplicationContext(),
                                    ConnectToast.ConnectRes.SUCCESS,
                                    getResources().getString(R.string.tcp_connect_stop_device_succ),
                                    Toast.LENGTH_SHORT);
                            toast.show();
                        }
                    }
                }
            }
        };

        wLP = getWindow().getAttributes();
        List<Map<String, String>> data = new ArrayList<Map<String, String>>();
        String[] operatePopupData = getResources().getStringArray(R.array.device_operate_popup_window);
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
                        MainActivity.this.getWindow().setAttributes(wLP);
                        operateMenu.dismiss();
                        Message msg = new Message();
                        Bundle bundle = new Bundle();
                        switch (position) {
                            case 0:
                                isTimeout = false; // 清空超时，允许进行TCP操作

                                // 执行停止设备的指令
                                tcpClientManager.sendCmd(getApplicationContext(),
                                        TcpClientManager.TcpCmdType.STOP_DEVICE,
                                        new String[]{deviceData.get(currPosition).getDeviceNo()},
                                        tcpHandler);

                                // 因为启动和停止设备没有返回值，所以不启动进度框，直接返回正确
                                bundle.putString(KEY_TCP_OPERATE_TYPE, TcpClientManager.TcpOperateType.OPERATE);
                                bundle.putString(KEY_TCP_CMD_TYPE, String.valueOf(TcpClientManager.TcpCmdType.STOP_DEVICE.ordinal()));
                                bundle.putString(KEY_TCP_RES_TYPE, TcpClientManager.TcpResType.SUCCESS);
                                msg.setData(bundle);
                                tcpHandler.sendMessage(msg);
//                                dialogFragment.show(getSupportFragmentManager(), MainActivity.class.getName());
                                break;
                            case 1:
                                isTimeout = false; // 清空超时，允许进行TCP操作

                                // 执行停止设备的指令
                                tcpClientManager.sendCmd(getApplicationContext(),
                                        TcpClientManager.TcpCmdType.START_DEVICE,
                                        new String[]{deviceData.get(currPosition).getDeviceNo()},
                                        tcpHandler);

                                // 因为启动和停止设备没有返回值，所以不启动进度框，直接返回正确
                                bundle.putString(KEY_TCP_OPERATE_TYPE, TcpClientManager.TcpOperateType.OPERATE);
                                bundle.putString(KEY_TCP_CMD_TYPE, String.valueOf(TcpClientManager.TcpCmdType.START_DEVICE.ordinal()));
                                bundle.putString(KEY_TCP_RES_TYPE, TcpClientManager.TcpResType.SUCCESS);
                                msg.setData(bundle);
                                tcpHandler.sendMessage(msg);
//                                dialogFragment.show(getSupportFragmentManager(), MainActivity.class.getName());
                                break;
                            case 2:
                                Intent intent = new Intent(MainActivity.this, ParamActivity.class);
                                bundle.putString(DATA_KEY, JSON.toJSONString(
                                        deviceData, SerializerFeature.WriteMapNullValue));
                                bundle.putInt(POSITION_KEY, currPosition);
                                intent.putExtras(bundle);
                                startActivity(intent);
                                break;
                        }
                    }
                },
                new PopupWindow.OnDismissListener() {
                    @Override
                    public void onDismiss() {
                        wLP.alpha = 1f;
                        MainActivity.this.getWindow().setAttributes(wLP);
                        operateMenu.dismiss();
                    }
                },
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        wLP.alpha = 1f;
                        MainActivity.this.getWindow().setAttributes(wLP);
                        operateMenu.dismiss();
                    }
                });

        mainTabHost.setup(getApplicationContext(), getSupportFragmentManager(), android.R.id.tabcontent);
        mainTabHost.getTabWidget().setDividerDrawable(null);
        mainTabHost.setOnTabChangedListener(this);

        for (int i = 0; i < mainTabItemFragmentArray.length; i++) {
            View tabIndicatorView = LayoutInflater.from(getApplicationContext()).inflate(
                    R.layout.main_tab_indicator_item_layout, null);

            ImageView indicatorImage = (ImageView) tabIndicatorView.findViewById(R.id.indicator_item_img);
            TextView indicatorTitle = (TextView) tabIndicatorView.findViewById(R.id.indicator_item_title);

            indicatorImage.setImageResource(mainTabIndicatorImgArray[i]);
            indicatorTitle.setText(mainTabIndicatorTitleArray[i]);

            TabHost.TabSpec newTab = mainTabHost.newTabSpec(getResources().getString(
                    mainTabIndicatorTitleArray[i])).setIndicator(tabIndicatorView);

            mainTabHost.addTab(newTab, mainTabItemFragmentArray[i], deviceBundle);
        }

        toolbarOperate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                wLP.alpha = 0.5f;
                MainActivity.this.getWindow().setAttributes(wLP);
                operateMenu.showAtLocation(
                        MainActivity.this.findViewById(R.id.main),
                        Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL,
                        0, 0);
            }
        });

        toolbarBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        // 初始化TCP连接对象
        tcpClientManager = TcpClientManager.getInstance();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        Log.i(TAG, "关闭设备");

        tcpClientManager.close(deviceData.get(currPosition).getGPSDeviceNo());
    }

    @Override
    public void onTabChanged(String tabId) {
        if (tabId.equals(getResources().getString(mainTabIndicatorTitleArray[0]))) {
            toolbarTitle.setText(getResources().getString(R.string.main_tab_real_time_title));
        }
        if (tabId.equals(getResources().getString(mainTabIndicatorTitleArray[1]))) {
            toolbarTitle.setText(getResources().getString(R.string.main_tab_spectrum_title));
        }
        if (tabId.equals(getResources().getString(mainTabIndicatorTitleArray[2]))) {
            toolbarTitle.setText(getResources().getString(R.string.main_tab_user_title));
        }
    }

    @Override
    public void onFragmentInteraction(Uri uri) {
        Log.i(TAG, uri.getScheme() + " " + uri.getAuthority() + " " + uri.getQueryParameter("action") + " " + uri.getQueryParameter("data"));

        if (uri.getQueryParameter(UriCommunication.Action).equals(UriCommunication.ActionParams.Click)) {
            int id = Integer.parseInt(uri.getQueryParameter(UriCommunication.Data));

            Intent intent = null;

            switch (id) {
                case R.id.user_pref_cancel:
                    // 清空账户信息
                    SharedPreferences accountPref = getSharedPreferences(
                            getResources().getString(R.string.account_pref_name), MODE_PRIVATE);
                    SharedPreferences.Editor editor = accountPref.edit();
                    editor.clear();
                    SharedPreferencesCompat.EditorCompat.getInstance().apply(editor);

                    intent = new Intent(MainActivity.this, LoginActivity.class);
                    startActivity(intent);
                    finish();
                    break;
                case R.id.user_preferences_about:
                    intent = new Intent(MainActivity.this, AboutActivity.class);
                    startActivity(intent);
                    break;
            }
        }
    }
}