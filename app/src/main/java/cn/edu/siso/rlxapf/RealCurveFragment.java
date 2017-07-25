package cn.edu.siso.rlxapf;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import cn.edu.siso.rlxapf.bean.DataBean;
import cn.edu.siso.rlxapf.bean.DataGroupBean;

public class RealCurveFragment extends Fragment {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    private String TAG = "===RealCurveFragment===";

    private OnFragmentInteractionListener mListener;

    private RecyclerView curveDataRecyclerView = null;

    private List<DataGroupBean> data = null;

    private Context context = null;

    public RealCurveFragment() {
        // Required empty public constructor
    }

    public static RealCurveFragment newInstance(String param1, String param2) {
        RealCurveFragment fragment = new RealCurveFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

        Log.i(TAG, "===onCreate===");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.i(TAG, "===onCreateView===");

        View rootView = inflater.inflate(R.layout.fragment_real_curve, container, false);
        curveDataRecyclerView = (RecyclerView) rootView.findViewById(R.id.curve_data_view);

        List<Map<String, Float>> data11 = new ArrayList<Map<String, Float>>();
        List<Map<String, Float>> data12 = new ArrayList<Map<String, Float>>();
        List<Map<String, Float>> data13 = new ArrayList<Map<String, Float>>();
        for (int j = 0; j < 10; j++) {
            Map<String, Float> value1 = new HashMap<String, Float>();
            value1.put(DataBean.DATA_KEY, Float.valueOf(String.valueOf((new Random().nextInt(10) + 1))));
            data11.add(value1);

            Map<String, Float> value2 = new HashMap<String, Float>();
            value2.put(DataBean.DATA_KEY, Float.valueOf(String.valueOf((new Random().nextInt(10) + 1))));
            data12.add(value2);

            Map<String, Float> value3 = new HashMap<String, Float>();
            value3.put(DataBean.DATA_KEY, Float.valueOf(String.valueOf((new Random().nextInt(10) + 1))));
            data13.add(value3);
        }
        List<DataBean> dataBeen1 = new ArrayList<DataBean>();
        DataBean dataBean11 = new DataBean("系统A相电压", data11);
        DataBean dataBean12 = new DataBean("系统B相电压", data12);
        DataBean dataBean13 = new DataBean("系统C相电压", data13);
        dataBeen1.add(dataBean11);
        dataBeen1.add(dataBean12);
        dataBeen1.add(dataBean13);
        DataGroupBean groupBean1 = new DataGroupBean("系统电压", dataBeen1);

        List<Map<String, Float>> data21 = new ArrayList<Map<String, Float>>();
        List<Map<String, Float>> data22 = new ArrayList<Map<String, Float>>();
        List<Map<String, Float>> data23 = new ArrayList<Map<String, Float>>();
        for (int j = 0; j < 10; j++) {
            Map<String, Float> value1 = new HashMap<String, Float>();
            value1.put(DataBean.DATA_KEY, Float.valueOf(String.valueOf((new Random().nextInt(10) + 1))));
            data21.add(value1);

            Map<String, Float> value2 = new HashMap<String, Float>();
            value2.put(DataBean.DATA_KEY, Float.valueOf(String.valueOf((new Random().nextInt(10) + 1))));
            data22.add(value2);

            Map<String, Float> value3 = new HashMap<String, Float>();
            value3.put(DataBean.DATA_KEY, Float.valueOf(String.valueOf((new Random().nextInt(10) + 1))));
            data23.add(value3);
        }
        List<DataBean> dataBeen2 = new ArrayList<DataBean>();
        DataBean dataBean21 = new DataBean("系统A相电流", data21);
        DataBean dataBean22 = new DataBean("系统B相电流", data22);
        DataBean dataBean23 = new DataBean("系统C相电流", data23);
        dataBeen2.add(dataBean21);
        dataBeen2.add(dataBean22);
        dataBeen2.add(dataBean23);
        DataGroupBean groupBean2 = new DataGroupBean("系统电流", dataBeen2);

        List<Map<String, Float>> data31 = new ArrayList<Map<String, Float>>();
        List<Map<String, Float>> data32 = new ArrayList<Map<String, Float>>();
        List<Map<String, Float>> data33 = new ArrayList<Map<String, Float>>();
        for (int j = 0; j < 10; j++) {
            Map<String, Float> value1 = new HashMap<String, Float>();
            value1.put(DataBean.DATA_KEY, Float.valueOf(String.valueOf((new Random().nextInt(10) + 1))));
            data31.add(value1);

            Map<String, Float> value2 = new HashMap<String, Float>();
            value2.put(DataBean.DATA_KEY, Float.valueOf(String.valueOf((new Random().nextInt(10) + 1))));
            data32.add(value2);

            Map<String, Float> value3 = new HashMap<String, Float>();
            value3.put(DataBean.DATA_KEY, Float.valueOf(String.valueOf((new Random().nextInt(10) + 1))));
            data33.add(value3);
        }
        List<DataBean> dataBeen3 = new ArrayList<DataBean>();
        DataBean dataBean31 = new DataBean("负载A相电流", data31);
        DataBean dataBean32 = new DataBean("负载B相电流", data32);
        DataBean dataBean33 = new DataBean("负载C相电流", data33);
        dataBeen3.add(dataBean31);
        dataBeen3.add(dataBean32);
        dataBeen3.add(dataBean33);
        DataGroupBean groupBean3 = new DataGroupBean("负载电流", dataBeen3);

        List<Map<String, Float>> data41 = new ArrayList<Map<String, Float>>();
        List<Map<String, Float>> data42 = new ArrayList<Map<String, Float>>();
        List<Map<String, Float>> data43 = new ArrayList<Map<String, Float>>();
        for (int j = 0; j < 10; j++) {
            Map<String, Float> value1 = new HashMap<String, Float>();
            value1.put(DataBean.DATA_KEY, Float.valueOf(String.valueOf((new Random().nextInt(10) + 1))));
            data41.add(value1);

            Map<String, Float> value2 = new HashMap<String, Float>();
            value2.put(DataBean.DATA_KEY, Float.valueOf(String.valueOf((new Random().nextInt(10) + 1))));
            data42.add(value2);

            Map<String, Float> value3 = new HashMap<String, Float>();
            value3.put(DataBean.DATA_KEY, Float.valueOf(String.valueOf((new Random().nextInt(10) + 1))));
            data43.add(value3);
        }
        List<DataBean> dataBeen4 = new ArrayList<DataBean>();
        DataBean dataBean41 = new DataBean("补偿A相电流", data41);
        DataBean dataBean42 = new DataBean("补偿B相电流", data42);
        DataBean dataBean43 = new DataBean("补偿C相电流", data43);
        dataBeen4.add(dataBean41);
        dataBeen4.add(dataBean42);
        dataBeen4.add(dataBean43);
        DataGroupBean groupBean4 = new DataGroupBean("补偿电流", dataBeen4);

        data = new ArrayList<DataGroupBean>();
        data.add(groupBean1);
        data.add(groupBean2);
        data.add(groupBean3);
        data.add(groupBean4);

        DataCurveRecycleAdapter adapter = new DataCurveRecycleAdapter(context, data);
        LinearLayoutManager layoutManager = new LinearLayoutManager(context);
        curveDataRecyclerView.setLayoutManager(layoutManager);
        curveDataRecyclerView.setAdapter(adapter);

        return rootView;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        this.context = context;

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

        Log.i(TAG, "===onDetach===");

        mListener = null;
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
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
