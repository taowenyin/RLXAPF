package cn.edu.siso.rlxapf;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;

import java.util.List;

import cn.edu.siso.rlxapf.bean.DeviceBean;

import static cn.edu.siso.rlxapf.DeviceListActivity.DATA_KEY;
import static cn.edu.siso.rlxapf.DeviceListActivity.POSITION_KEY;

public class ParamActivity extends AppCompatActivity {

    private ImageButton toolbarNavBack = null;
    private ImageButton toolbarDownload = null;
    private Button deviceParamOk = null;

    private List<DeviceBean> deviceData = null;
    private int currPosition = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_param);

        // 得到设备数据和选择的设备索引
        Bundle deviceBundle = getIntent().getExtras();
        deviceData = JSON.parseArray(
                deviceBundle.getString(DATA_KEY),
                DeviceBean.class);
        currPosition = deviceBundle.getInt(POSITION_KEY);

        toolbarNavBack = (ImageButton) findViewById(R.id.toolbar_back);
        toolbarDownload = (ImageButton) findViewById(R.id.toolbar_operate);
        deviceParamOk = (Button) findViewById(R.id.device_param_ok);

        toolbarNavBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        toolbarDownload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ParamPrefFragment fragment = (ParamPrefFragment) getSupportFragmentManager()
                        .findFragmentById(R.id.param_content);
                fragment.pullDeviceParams(); // 下载参数
            }
        });

        deviceParamOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        getSupportFragmentManager().beginTransaction().replace(
                R.id.param_content, ParamPrefFragment.newInstance(
                        JSON.toJSONString(deviceData.get(currPosition)))).commit();
    }
}
