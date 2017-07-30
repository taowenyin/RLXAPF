package cn.edu.siso.rlxapf.util.http;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Cookie;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class OkHttpClientManager {

    private static OkHttpClientManager instance = null;

    private OkHttpClient okHttpClient = null;
    private String baseUrl = "";

    private Context context = null;

    public static final String HTTP_RESPONSE_TYPE = "type";
    public static final String HTTP_RESPONSE_TYPE_FAIL = "fail";
    public static final String HTTP_RESPONSE_TYPE_SUCC = "succ";
    public static final String HTTP_RESPONSE_DATA = "data";
    public static final String TAG = "OkHttpClientManager";

    private OkHttpClientManager(Context context, String baseUrl, long httpTimeOut) {
        this.context = context;

        okHttpClient = new OkHttpClient.Builder()
                .connectTimeout(httpTimeOut, TimeUnit.MILLISECONDS)
                .readTimeout(httpTimeOut, TimeUnit.MILLISECONDS)
                .cookieJar(CookiesManager.getInstance(context))
                .build();

        this.baseUrl = baseUrl;
    }

    public static OkHttpClientManager getInstance(Context context, String baseUrl, long httpTimeOut) {
        if (instance == null) {
            synchronized (OkHttpClientManager.class) {
                if (instance == null) {
                    instance = new OkHttpClientManager(context, baseUrl, httpTimeOut);
                }
            }
        }

        return instance;
    }

    public static OkHttpClientManager getInstance(Context context, String baseUrl) {
        if (instance == null) {
            synchronized (OkHttpClientManager.class) {
                if (instance == null) {
                    instance = new OkHttpClientManager(context, baseUrl, 10000);
                }
            }
        }

        return instance;
    }

    // HTTP 同步 GET 操作
    private Response httpGetSync(String apiUrl, Map<String, String> params) {
        String requestUrl = String.format("%s/%s?%s", baseUrl, apiUrl, httpParams2String(params));

        Request request = new Request.Builder().url(requestUrl).build();

        Response response = null;
        try {
            response = okHttpClient.newCall(request).execute();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return response;
    }

    public String httpStrGetSync(String apiUrl, Map<String, String> params) {
        Response response = httpGetSync(apiUrl, params);
        String responseBody = null;

        try {
            responseBody = response.body().string();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return responseBody;
    }

    // HTTP 异步 GET 操作
    public void httpStrGetAsyn(String apiUrl, Map<String, String> params, Handler httpHandler) {
        String requestUrl = String.format("%s/%s?%s", baseUrl, apiUrl, httpParams2String(params));

        Request request = new Request.Builder().url(requestUrl).build();
        httpDeliveryHandler(request, httpHandler);
    }

    // HTTP 同步 Post 操作
    private Response httpPostSync(String apiUrl, Map<String, String> params) {
        String requestUrl = String.format("%s/%s", baseUrl, apiUrl);

        Request request = new Request.Builder().url(requestUrl).post(httpParams2Body(params)).build();

        Response response = null;
        try {
            response = okHttpClient.newCall(request).execute();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return response;
    }

    public String httpStrPostSync(String apiUrl, Map<String, String> params) {
        Response response = httpPostSync(apiUrl, params);
        String responseBody = null;

        try {
            responseBody = response.body().string();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return responseBody;
    }

    // HTTP 异步 Post 操作
    public void httpStrPostAsyn(String apiUrl, Map<String, String> params, Handler httpHandler) {
        String requestUrl = String.format("%s/%s", baseUrl, apiUrl);

        Request request = new Request.Builder().url(requestUrl).post(httpParams2Body(params)).build();
        httpDeliveryHandler(request, httpHandler);
    }

    // 把异步Http结果传递到UI
    private void httpDeliveryHandler(Request request, final Handler httpHandler) {
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Message message = new Message();

                Bundle data = new Bundle();
                data.putString(HTTP_RESPONSE_TYPE, HTTP_RESPONSE_TYPE_FAIL);
                data.putString(HTTP_RESPONSE_DATA, e.toString());

                message.setData(data);
                httpHandler.sendMessage(message);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Message message = new Message();

                Bundle data = new Bundle();
                data.putString(HTTP_RESPONSE_TYPE, HTTP_RESPONSE_TYPE_SUCC);
                data.putString(HTTP_RESPONSE_DATA, response.body().string());

                message.setData(data);
                httpHandler.sendMessage(message);
            }
        });
    }

    // 参数转化为String
    private String httpParams2String(Map<String, String> params) {
        if (params == null) {
            params = new HashMap<String, String>();
        }

        StringBuilder paramsBuilder = new StringBuilder();
        for (Map.Entry<String, String> entry : params.entrySet()) {
            try {
                paramsBuilder.append(String.format("%s=%s&", entry.getKey(),
                        URLEncoder.encode(entry.getValue(), "UTF-8")));
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
        paramsBuilder = paramsBuilder.delete(paramsBuilder.length() - 2,
                paramsBuilder.length() - 1);

        return paramsBuilder.toString();
    }

    // 参数转化为Body
    private RequestBody httpParams2Body(Map<String, String> params) {
        FormBody.Builder builder = new FormBody.Builder();
        if (params != null) {
            for (Map.Entry<String, String> entry : params.entrySet()) {
                builder.add(entry.getKey(), entry.getValue());
            }
        }

        return builder.build();
    }
}
