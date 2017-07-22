package cn.edu.siso.rlxapf;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

public class AboutFragment extends Fragment implements View.OnClickListener {

    private Button aboutDescription = null;

    private OnFragmentInteractionListener mListener = null;

    public static String TAG = "AboutFragment";

    public AboutFragment() {
        // Required empty public constructor
    }

    public static AboutFragment newInstance() {
        AboutFragment fragment = new AboutFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_about, container, false);

        aboutDescription = (Button) rootView.findViewById(R.id.about_description);
        aboutDescription.setOnClickListener(this);

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
