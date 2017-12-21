package cn.edu.siso.rlxapf;

import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.PopupWindow;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.edu.siso.rlxapf.bean.DeviceBean;
import cn.edu.siso.rlxapf.dialog.PrefConfirmDialogFragment;

import static cn.edu.siso.rlxapf.DeviceListActivity.DATA_KEY;
import static cn.edu.siso.rlxapf.DeviceListActivity.POSITION_KEY;

public class ParamActivity extends AppCompatActivity implements PrefConfirmDialogFragment.OnConfirmBtnClickListener {

    private ImageButton toolbarNavBack = null;
    private ImageButton toolbarOperate = null;
    private PopupBottomMenu operateMenu = null;
    private Button deviceParamOk = null;
    private ParamPrefFragment paramPrefFragment = null;

    private WindowManager.LayoutParams wLP = null;

    private List<DeviceBean> deviceData = null;
    private int currPosition = -1;

    private String[] operatePopupData = null;
    private  String[] operateResultPopupData = null;

    public static final String TAG = "==ParamActivity==";

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
        toolbarOperate = (ImageButton) findViewById(R.id.toolbar_operate);
        deviceParamOk = (Button) findViewById(R.id.device_param_ok);

        wLP = getWindow().getAttributes();
        List<Map<String, String>> data = new ArrayList<Map<String, String>>();
        operatePopupData = getResources().getStringArray(R.array.param_operate_popup_window);
        operateResultPopupData = getResources().getStringArray(R.array.param_operate_result_popup_window);
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
                        ParamActivity.this.getWindow().setAttributes(wLP);
                        operateMenu.dismiss();
                        Bundle bundle = new Bundle();

                        // 获取参数设置的Fragment
                        ParamPrefFragment fragment = (ParamPrefFragment) getSupportFragmentManager()
                                .findFragmentById(R.id.param_content);

                        ConnectToast toast = null;
                        switch (position) {
                            case 0: // 参数召唤
                                fragment.downloadDeviceParams();
                                break;
                            case 1: // 参数保存
                                fragment.saveDeviceParams();
                                toast = new ConnectToast(getApplicationContext(),
                                        ConnectToast.ConnectRes.SUCCESS,
                                        operateResultPopupData[1],
                                        Toast.LENGTH_LONG);
                                toast.show();
                                break;
                            case 2: // 参数导出
                                fragment.exportDeviceParams();
                                toast = new ConnectToast(getApplicationContext(),
                                        ConnectToast.ConnectRes.SUCCESS,
                                        operateResultPopupData[2],
                                        Toast.LENGTH_LONG);
                                toast.show();
                                break;
                            case 3: // 恢复出厂设置
                                fragment.restoreFactoryParams();
                                toast = new ConnectToast(getApplicationContext(),
                                        ConnectToast.ConnectRes.SUCCESS,
                                        operateResultPopupData[3],
                                        Toast.LENGTH_LONG);
                                toast.show();
                                break;
                        }
                    }
                },
                new PopupWindow.OnDismissListener() {
                    @Override
                    public void onDismiss() {
                        wLP.alpha = 1f;
                        ParamActivity.this.getWindow().setAttributes(wLP);
                        operateMenu.dismiss();
                    }
                },
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        wLP.alpha = 1f;
                        ParamActivity.this.getWindow().setAttributes(wLP);
                        operateMenu.dismiss();
                    }
                });

        toolbarNavBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        toolbarOperate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                wLP.alpha = 0.5f;
                ParamActivity.this.getWindow().setAttributes(wLP);
                operateMenu.showAtLocation(
                        ParamActivity.this.findViewById(R.id.main),
                        Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL,
                        0, 0);
            }
        });

        deviceParamOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        paramPrefFragment = ParamPrefFragment.newInstance(JSON.toJSONString(
                deviceData.get(currPosition)));
        getSupportFragmentManager().beginTransaction().replace(
                R.id.param_content, paramPrefFragment).commit();
    }

    @Override
    public void onConfirmBtnClick(DialogFragment dialogFragment, boolean isConfrim, String pwd) {
        if (isConfrim && !StringUtils.isEmpty(deviceData.get(currPosition).getGPRSChangePwd()) &&
                !pwd.equals(deviceData.get(currPosition).getGPRSChangePwd())) {
            ConnectToast toast  = new ConnectToast(getApplicationContext(),
                    ConnectToast.ConnectRes.BAD,
                    getResources().getString(R.string.pref_fragment_dialog_pwd_error),
                    Toast.LENGTH_LONG);
            toast.show();
        } else {
            if (dialogFragment != null &&
                    dialogFragment.getDialog() != null &&
                    dialogFragment.getDialog().isShowing()) {
                dialogFragment.dismiss();
            }
            if (isConfrim) {
                paramPrefFragment.setCurrentParamStatus(true);
                Log.i(TAG, "修改密码成功");
            } else {
                paramPrefFragment.setCurrentParamStatus(false);
                Log.i(TAG, "取消修改密码");
            }
        }
    }
}
