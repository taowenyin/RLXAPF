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
import android.widget.EditText;
import android.widget.TextView;

import org.apache.commons.lang3.StringUtils;

import cn.edu.siso.rlxapf.R;

public class PrefConfirmDialogFragment extends DialogFragment {

    private View rootView = null;

    private EditText prefConfirmPwd = null;
    private Button okBtn = null, cancelBtn = null;

    private OnConfirmBtnClickListener mListener = null;

    private Context context = null;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        setCancelable(false); // 不允许按键返回

        rootView = inflater.inflate(R.layout.pref_confirm_dialog, container);

        prefConfirmPwd = rootView.findViewById(R.id.pref_confirm_pwd);
        okBtn = rootView.findViewById(R.id.pref_confirm_btn_ok);
        cancelBtn = rootView.findViewById(R.id.confirm_dialog_btn_cancel);

        okBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mListener.onConfirmBtnClick(PrefConfirmDialogFragment.this, true, prefConfirmPwd.getText().toString());
                prefConfirmPwd.setText(StringUtils.EMPTY);
            }
        });

        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mListener.onConfirmBtnClick(PrefConfirmDialogFragment.this, false, StringUtils.EMPTY);
                prefConfirmPwd.setText(StringUtils.EMPTY);
            }
        });

        return rootView;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        if (context instanceof PrefConfirmDialogFragment.OnConfirmBtnClickListener) {
            mListener = (OnConfirmBtnClickListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }

        this.context = context;
    }

    public interface OnConfirmBtnClickListener {
        void onConfirmBtnClick(DialogFragment dialogFragment, boolean isConfrim, String pwd);
    }
}
