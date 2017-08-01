package cn.edu.siso.rlxapf;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;

import com.github.sahasbhop.apngview.ApngImageLoader;

public class ConnectDialogFragment extends DialogFragment {

    private View rootView = null;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        setCancelable(false); // 不允许按键返回

        rootView = inflater.inflate(R.layout.fragment_connect_dialog, container);

        ImageView loadingView = (ImageView) rootView.findViewById(R.id.loading_view);
        ApngImageLoader.getInstance().displayApng("assets://apng/loading.png", loadingView,
                new ApngImageLoader.ApngConfig(0, true));

        return rootView;
    }
}
