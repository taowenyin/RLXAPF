package cn.edu.siso.rlxapf;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class ConnectToast extends Toast {

    public enum ConnectRes {SUCCESS, BAD};

    public ConnectToast(Context context, ConnectRes res, String msg, int duration) {
        super(context);

        View rootView = LayoutInflater.from(context).inflate(R.layout.toast_connect_layout, null);

        ImageView toastImg = (ImageView) rootView.findViewById(R.id.toast_img);
        TextView toastMsg = (TextView) rootView.findViewById(R.id.toast_message);

        // 设置Toast的文字
        toastMsg.setText(msg);
        // 设置Toast的图片
        if (res == ConnectRes.SUCCESS) {
            toastImg.setImageResource(R.drawable.ic_connect_succ);
        } else {
            toastImg.setImageResource(R.drawable.ic_connect_bad);
        }

        // 设置自定义样式
        setView(rootView);
        // 设置Toast居中
        setGravity(Gravity.CENTER, 0, 0);
        setDuration(Toast.LENGTH_LONG);
        // 设置延时
        setDuration(duration);
    }

}
