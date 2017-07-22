package cn.edu.siso.rlxapf.util;


import android.content.Context;
import android.os.Build;
import android.os.Handler;
import android.util.Log;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Headers;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

public class HttpUtil {

    private static HttpUtil ourInstance = null;

    private static Context ourContext = null;

    private OkHttpClient client = null;
    private Request.Builder builder = null;

    private Handler okHttpHandler;//全局处理子线程和M主线程通信

    private static final String BASE_URL = "http://xj.siso.edu.cn";

    // 超时时间
    public static final int TIMEOUT = 1000 * 60;

    //json请求
    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

    public static HttpUtil getInstance(Context context) {
        if (ourInstance == null) {
            ourContext = context;
            ourInstance = new HttpUtil();
        }

        return ourInstance;
    }

    private HttpUtil() {
        client = new OkHttpClient();
        client.newBuilder()
                .connectTimeout(TIMEOUT, TimeUnit.SECONDS)
                .writeTimeout(TIMEOUT, TimeUnit.SECONDS)
                .readTimeout(TIMEOUT, TimeUnit.SECONDS)
                .build();
        builder = new Request.Builder()
                .addHeader("Connection", "keep-alive")
                .addHeader("platform", "2")
                .addHeader("phoneModel", Build.MODEL)
                .addHeader("systemVersion", Build.VERSION.RELEASE)
                .addHeader("appVersion", "3.2.0");

        //初始化Handler
        okHttpHandler = new Handler(ourContext.getMainLooper());
    }

    public void getAsyn(String api, Map<String, String> params, final OnReqCallBack onReqCallBack) {
        StringBuilder tempParams = new StringBuilder();

        try {
            int pos = 0;
            for (String key : params.keySet()) {
                if (pos > 0) {
                    tempParams.append("&");
                }
                tempParams.append(String.format("%s=%s", key, URLEncoder.encode(params.get(key), "utf-8")));
                pos++;
            }
            String requestUrl = String.format("%s/%s?%s", BASE_URL, api, tempParams.toString());
            Request request = builder.url(requestUrl).build();
            Call call = client.newCall(request);
            call.enqueue(new Callback() {
                @Override
                public void onFailure(Call call, final IOException e) {
                    okHttpHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            if (onReqCallBack != null) {
                                onReqCallBack.onReqFailed(e.toString());
                            }
                        }
                    });
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    final String httpData = response.body().string();

                    // 添加Session
                    Headers headers = response.headers();
                    List<String> cookies = headers.values("Set-Cookie");
                    if (cookies.size() > 0) {
                        String sessionTmp = cookies.get(0);
                        String session = sessionTmp.substring(0, sessionTmp.indexOf(";"));
                        builder.addHeader("cookie", session);
                    }
                    okHttpHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            if (onReqCallBack != null) {
                                onReqCallBack.onReqSuccess(httpData);
                            }
                        }
                    });
                }
            });
        } catch (Exception e) {

        }
    }

    public interface OnReqCallBack {
        /**
         * 响应成功
         */
        void onReqSuccess(String result);

        /**
         * 响应失败
         */
        void onReqFailed(String errorMsg);
    }
}
