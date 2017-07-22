package cn.edu.siso.rlxapf;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.edu.siso.rlxapf.bean.UserBean;
import cn.edu.siso.rlxapf.util.HttpUtil;

public class LoginActivity extends AppCompatActivity {

    // 登录按钮
    private Button loginFormLoginBtn = null;
    private EditText loginAccount = null;
    private EditText loginPassword = null;

    public static final String USER_KEY = "user_data";

    public static final String TAG = "LoginActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        loginFormLoginBtn = (Button) findViewById(R.id.login_form_login_btn);
        loginAccount = (EditText) findViewById(R.id.login_form_account);
        loginPassword = (EditText) findViewById(R.id.login_form_password);

        loginFormLoginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                HttpUtil http = HttpUtil.getInstance(getApplicationContext());
                Map<String, String> params = new HashMap<String, String>();
                params.put("u", loginAccount.getText().toString());
                params.put("p", loginPassword.getText().toString());
                http.getAsyn("Jasonlogin.aspx", params, new HttpUtil.OnReqCallBack() {
                    @Override
                    public void onReqSuccess(String result) {
                        if (result.equals("1")) {

                        } else if (result.equals("2")) {

                        } else if (result.equals("3")) {

                        } else {
                            UserBean userData = JSON.parseObject(result, UserBean.class);
                            Intent intent = new Intent(LoginActivity.this, DeviceListActivity.class);
                            intent.putExtra(USER_KEY, JSON.toJSONString(userData, SerializerFeature.WriteMapNullValue));
                            startActivity(intent);
                        }
                    }

                    @Override
                    public void onReqFailed(String errorMsg) {

                    }
                });
            }
        });
    }
}
