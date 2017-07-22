package cn.edu.siso.rlxapf;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.PopupWindow;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.edu.siso.rlxapf.bean.DeviceBean;
import cn.edu.siso.rlxapf.bean.UserBean;
import cn.edu.siso.rlxapf.util.HttpUtil;

public class DeviceListActivity extends AppCompatActivity implements
        DeviceListRecyclerAdapter.OnItemClickListener,
        DeviceListRecyclerAdapter.OnOperatorClickListener {

    private ImageButton toolbarBack = null;
    private RecyclerView deviceListView = null;
    private DeviceListRecyclerAdapter adapter = null;
    private PopupBottomMenu operateMenu = null;

    private WindowManager.LayoutParams wLP = null;

    private List<DeviceBean> deviceData = null;
    private UserBean userBean = null;

    public static String DATA_KEY = "data";
    public static String POSITION_KEY = "position";

    public static String TAG = "DeviceListActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_device_list);

        userBean = JSON.parseObject(
                getIntent().getStringExtra(LoginActivity.USER_KEY),
                UserBean.class);

        toolbarBack = (ImageButton) findViewById(R.id.toolbar_back);
        deviceListView  = (RecyclerView) findViewById(R.id.device_list_view);
        deviceData = new ArrayList<DeviceBean>();
//        DeviceBean bean = new DeviceBean();
//        bean.setId("57e7f306-5fb4-4ee0-b772-10deebfb6269");
//        bean.setProvince("330000");
//        bean.setCity("330200");
//        bean.setCounty("330204");
//        bean.setDeviceType("LXSVG");
//        bean.setGPSDeviceNo("GPRS设备编号3");
//        bean.setDeviceNo("设备编号3");
//        bean.setDeleted("0");
//        deviceData.add(bean);

        toolbarBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        LinearLayoutManager layoutManager = new LinearLayoutManager(
                getApplicationContext(), LinearLayoutManager.VERTICAL, false);
        adapter = new DeviceListRecyclerAdapter(getApplicationContext(), deviceData);
        adapter.setOnItemClickListener(this);
        adapter.setOnOperatorClickListener(this);
        deviceListView.setLayoutManager(layoutManager);
        deviceListView.setAdapter(adapter);

        wLP = getWindow().getAttributes();
        List<Map<String, String>> data = new ArrayList<Map<String, String>>();
        String[] operatePopupData = getResources().getStringArray(R.array.operate_popup_window);
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
                        switch (position) {
                            case 0:
                                break;
                            case 1:
                                break;
                            case 2:
                                Intent intent = new Intent(DeviceListActivity.this, ParamActivity.class);
                                startActivity(intent);
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

        // 获取设备列表
        HttpUtil http = HttpUtil.getInstance(getApplicationContext());
        Map<String, String> params = new HashMap<String, String>();
        params.put("mobileid", userBean.getMobileId());
        params.put("account", userBean.getAccount());
        http.getAsyn("JasonQueryDevice.aspx", params, new HttpUtil.OnReqCallBack() {
            @Override
            public void onReqSuccess(String result) {
                if (result.equals("1")) {

                } else if (result.equals("2")) {

                } else if (result.equals("3")) {

                } else if (result.equals("4")) {

                } else {
                    List<DeviceBean> devices = JSON.parseArray(result, DeviceBean.class);
                    deviceData.clear();
                    for (int i = 0; i < devices.size(); i++) {
                        deviceData.add(devices.get(i));
                    }
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onReqFailed(String errorMsg) {

            }
        });
    }

    @Override
    public void onItemClick(View view, int position) {
        Intent intent = new Intent(DeviceListActivity.this, MainActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString(DATA_KEY, JSON.toJSONString(
                deviceData, SerializerFeature.WriteMapNullValue));
        bundle.putInt(POSITION_KEY, position);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    @Override
    public void onOperatorClick(View view, int position) {
        wLP.alpha = 0.5f;
        DeviceListActivity.this.getWindow().setAttributes(wLP);
        operateMenu.showAtLocation(
                DeviceListActivity.this.findViewById(R.id.main),
                Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL,
                0, 0);
    }
}
