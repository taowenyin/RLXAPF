package cn.edu.siso.rlxapf;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


public class RealTimeFragment extends Fragment implements TabLayout.OnTabSelectedListener {
    private static final String ARG_DEVICE_PARAM = "device_param";

    private String deviceParams;

    private String TAG = "===RealTimeFragment===";

    private int[] realTimeIndicatorTitleArray = {
            R.string.main_top_tab_real_curve_title,
            R.string.main_top_tab_real_data_title
    };

    private Fragment[] realTimeTabItemFragmentArray = {
            RealCurveFragment.newInstance("", ""),
            RealDataFragment.newInstance("", "")
    };

    private TabLayout realTimeTab = null;
    private ViewPager realTimePage = null;

    private OnFragmentInteractionListener mListener;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            deviceParams = getArguments().getString(ARG_DEVICE_PARAM);
        }

        Log.i(TAG, "===onCreate===");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_real_time, container, false);

        realTimeTab = (TabLayout) rootView.findViewById(R.id.real_time_tab);
        realTimePage = (ViewPager) rootView.findViewById(R.id.real_time_page);

        realTimePage.setAdapter(new RealTimeFragmentAdapter(getChildFragmentManager(),
                realTimeTabItemFragmentArray, realTimeIndicatorTitleArray, getContext()));
        realTimeTab.setupWithViewPager(realTimePage);
        realTimeTab.addOnTabSelectedListener(this);

        Log.i(TAG, "===onCreateView===");

        return rootView;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        Log.i(TAG, "===onAttachw===");

//        if (context instanceof OnFragmentInteractionListener) {
//            mListener = (OnFragmentInteractionListener) context;
//        } else {
//            throw new RuntimeException(context.toString()
//                    + " must implement OnFragmentInteractionListener");
//        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;

        Log.i(TAG, "===onDetach===");
    }

    @Override
    public void onStart() {
        super.onStart();

        Log.i(TAG, "===onStart===");
    }

    @Override
    public void onResume() {
        super.onResume();

        Log.i(TAG, "===onResume===");
    }

    @Override
    public void onPause() {
        super.onPause();

        Log.i(TAG, "===onPause===");
    }

    @Override
    public void onStop() {
        super.onStop();

        Log.i(TAG, "===onStop===");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        Log.i(TAG, "===onDestroyView===");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        Log.i(TAG, "===onDestroy===");
    }

    @Override
    public void onTabSelected(TabLayout.Tab tab) {
        realTimePage.setCurrentItem(tab.getPosition());
    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {

    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {

    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in_popup_menu this fragment to be communicated
     * to the activity and potentially other fragments contained in_popup_menu that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }
}
