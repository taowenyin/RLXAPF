package cn.edu.siso.rlxapf;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

public class UserFragment extends Fragment implements View.OnClickListener {
    private OnFragmentInteractionListener mListener = null;

    public static final String TAG = "UserFragment";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_user, container, false);

        Button userPrefCancel = (Button) rootView.findViewById(R.id.user_pref_cancel);
        Button userPrefAbout = (Button) rootView.findViewById(R.id.user_preferences_about);

        userPrefCancel.setOnClickListener(this);
        userPrefAbout.setOnClickListener(this);

        return rootView;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onClick(View v) {
        Uri uri = Uri.parse(
                UriCommunication.SchemeParams.Fragment
                        + "://"
                        + getClass().getName()
                        + "?"
                        + UriCommunication.Action
                        + "="
                        + UriCommunication.ActionParams.Click
                        + "&&"
                        + UriCommunication.Data
                        + "="
                        + v.getId());
        mListener.onFragmentInteraction(uri);
    }

    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }

}
