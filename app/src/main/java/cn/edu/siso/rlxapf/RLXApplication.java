package cn.edu.siso.rlxapf;

import android.app.Application;
import android.util.Log;

import com.github.sahasbhop.apngview.ApngImageLoader;
import com.tencent.bugly.crashreport.CrashReport;

import cn.edu.siso.rlxapf.util.TCPUtil;
import cn.edu.siso.rlxapf.util.http.OkHttpClientManager;

public class RLXApplication extends Application {

    private OkHttpClientManager okHttpClientManager = null;

    public static final String TAG = "RLXApplication";

    @Override
    public void onCreate() {
        super.onCreate();

        okHttpClientManager = OkHttpClientManager.getInstance(
                getApplicationContext(),
                getResources().getString(R.string.base_url));

        ApngImageLoader.getInstance().init(getApplicationContext());

        CrashReport.initCrashReport(getApplicationContext(), "a3591821ec", true);
    }

    public OkHttpClientManager getHttpManager() {
        return okHttpClientManager;
    }
}
