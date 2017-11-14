package cn.edu.siso.rlxapf.dialog;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import org.apache.commons.lang3.StringUtils;

import cn.edu.siso.rlxapf.R;

public class DeviceConfirmDialogFragment extends DialogFragment {

    private View rootView = null;
    private String titleText = StringUtils.EMPTY;
    private DIALOG_TYPE type = DIALOG_TYPE.EMPTY;

    private OnConfirmBtnClickListener mListener = null;

    private Context context = null;

    public enum DIALOG_TYPE {RUN_DEVICE, STOP_DEVICE, EMPTY};

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        setCancelable(false); // 不允许按键返回

        rootView = inflater.inflate(R.layout.fragment_confirm_dialog, container);

        TextView title = rootView.findViewById(R.id.confirm_dialog_title);
        Button okBtn = rootView.findViewById(R.id.confirm_dialog_btn_ok);
        Button cancelBtn = rootView.findViewById(R.id.confirm_dialog_btn_cancel);

        okBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mListener.onConfirmBtnClick(true, type);
            }
        });

        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mListener.onConfirmBtnClick(false, type);
            }
        });

        if (!titleText.isEmpty()) {
            title.setText(titleText);
        }


        return rootView;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        if (context instanceof DeviceConfirmDialogFragment.OnConfirmBtnClickListener) {
            mListener = (OnConfirmBtnClickListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }

        this.context = context;
    }

    public void setDialogType(DIALOG_TYPE type) {
        if (type == DIALOG_TYPE.RUN_DEVICE) {
            this.titleText = "启动设备?";
        } else {
            this.titleText = "停止设备?";
        }

        this.type = type;
    }

    public interface OnConfirmBtnClickListener {
        void onConfirmBtnClick(boolean isConfrim, DIALOG_TYPE type );
    }
}
