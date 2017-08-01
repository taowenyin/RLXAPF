package cn.edu.siso.rlxapf;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Message;
import android.support.v4.content.SharedPreferencesCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;

import org.apache.commons.lang3.StringUtils;

import java.util.HashMap;
import java.util.Map;

import cn.edu.siso.rlxapf.bean.UserBean;
import cn.edu.siso.rlxapf.config.HTTPConfig;
import cn.edu.siso.rlxapf.util.http.OkHttpClientManager;

public class LoginActivity extends AppCompatActivity {

    // 登录按钮
    private Button loginFormLoginBtn = null;
    private EditText loginAccount = null;
    private EditText loginPassword = null;

    private ConnectDialogFragment dialogFragment = null;

    private Handler httpHandler = null;

    public static final String USER_DATA_KEY = "user_data";

    public static final String TAG = "LoginActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        SharedPreferences accountPref = getSharedPreferences(
                getResources().getString(R.string.account_pref_name), MODE_PRIVATE);

        loginFormLoginBtn = (Button) findViewById(R.id.login_form_login_btn);
        loginAccount = (EditText) findViewById(R.id.login_form_account);
        loginPassword = (EditText) findViewById(R.id.login_form_password);
        dialogFragment = new ConnectDialogFragment(); // 初始化通信对话框对象

        httpHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                Bundle data = msg.getData();
                String resultType = data.getString(OkHttpClientManager.HTTP_RESPONSE_TYPE);
                String resultData = data.getString(OkHttpClientManager.HTTP_RESPONSE_DATA);

                dialogFragment.dismiss();

                if (!TextUtils.isEmpty(resultData) && StringUtils.isNumeric(resultData)) {

                    String badMsg = "";
                    int resCode = Integer.parseInt(resultData);
                    switch (resCode) {
                        case HTTPConfig.HttpLoginError.ACCOUNT_NO_SET:
                            badMsg = getResources().getString(R.string.login_error_account_no_set);
                            break;
                        case HTTPConfig.HttpLoginError.ACCOUNT_EMPTY:
                            badMsg = getResources().getString(R.string.login_error_account_empty);
                            break;
                        case HTTPConfig.HttpLoginError.ACCOUNT_CHECK_FAIL:
                            badMsg = getResources().getString(R.string.login_error_account_check_fail);
                            break;
                    }

                    Toast toast = new ConnectToast(
                            getApplicationContext(),
                            ConnectToast.ConnectRes.BAD, badMsg, Toast.LENGTH_SHORT);
                    toast.show();
                } else {
                    UserBean userData = JSON.parseObject(resultData, UserBean.class);

                    // 账户验证成功后把信息保存在本地
                    SharedPreferences accountPref = getSharedPreferences(
                            getResources().getString(R.string.account_pref_name), MODE_PRIVATE);
                    SharedPreferences.Editor editor = accountPref.edit();
                    editor.putString(UserBean.ACCOUNT_KEY, userData.getAccount());
                    editor.putString(UserBean.PASSWORD_KEY, userData.getPassword());
                    editor.putString(UserBean.NAME_KEY, userData.getName());
                    editor.putString(UserBean.ADDRESS_KEY, userData.getAddress());
                    editor.putString(UserBean.CONTACT_KEY, userData.getContact());
                    editor.putString(UserBean.PHONE_KEY, userData.getPhone());
                    editor.putString(UserBean.MOBILE_ID_KEY, userData.getMobileId());
                    SharedPreferencesCompat.EditorCompat.getInstance().apply(editor);

                    Intent intent = new Intent(LoginActivity.this, DeviceListActivity.class);
                    intent.putExtra(USER_DATA_KEY, JSON.toJSONString(userData, SerializerFeature.WriteMapNullValue));
                    startActivity(intent);

                    LoginActivity.this.finish();
                }
            }
        };

        // 如果用户ID已经保存，则直接跳转
        if (accountPref.contains(UserBean.ACCOUNT_KEY)
                && accountPref.contains(UserBean.PASSWORD_KEY)
                && accountPref.contains(UserBean.NAME_KEY)
                && accountPref.contains(UserBean.ADDRESS_KEY)
                && accountPref.contains(UserBean.CONTACT_KEY)
                && accountPref.contains(UserBean.PHONE_KEY)
                && accountPref.contains(UserBean.MOBILE_ID_KEY)) {
            UserBean userData = new UserBean();
            userData.setAccount(accountPref.getString(UserBean.ACCOUNT_KEY, ""));
            userData.setPassword(accountPref.getString(UserBean.PASSWORD_KEY, ""));
            userData.setName(accountPref.getString(UserBean.NAME_KEY, ""));
            userData.setAddress(accountPref.getString(UserBean.ADDRESS_KEY, ""));
            userData.setContact(accountPref.getString(UserBean.CONTACT_KEY, ""));
            userData.setPhone(accountPref.getString(UserBean.PHONE_KEY, ""));
            userData.setMobileId(accountPref.getString(UserBean.MOBILE_ID_KEY, ""));

            Intent intent = new Intent(LoginActivity.this, DeviceListActivity.class);
            String data = JSON.toJSONString(userData, SerializerFeature.WriteMapNullValue);
            intent.putExtra(USER_DATA_KEY, data);
            startActivity(intent);

            LoginActivity.this.finish();
        }

        loginFormLoginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                RLXApplication application = (RLXApplication) getApplication();
                OkHttpClientManager httpManager = application.getHttpManager();

                Map<String, String> params = new HashMap<String, String>();
                params.put("u", loginAccount.getText().toString());
                params.put("p", loginPassword.getText().toString());
                httpManager.httpStrGetAsyn(HTTPConfig.API_URL_LOGIN, params, httpHandler);
                dialogFragment.show(getSupportFragmentManager(), LoginActivity.class.getName());
            }
        });
    }
}
