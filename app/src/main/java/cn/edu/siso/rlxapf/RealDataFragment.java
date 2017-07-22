package cn.edu.siso.rlxapf;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class RealDataFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    private RecyclerView realDataRecyclerView = null;
    private DataSectionRecyclerAdapter adapter = null;

    private List<List<Map<String, String>>> realData = null;

    public RealDataFragment() {
        // Required empty public constructor
    }

    public static RealDataFragment newInstance(String param1, String param2) {
        RealDataFragment fragment = new RealDataFragment();
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

        // 数据列表
        this.realData = new ArrayList<List<Map<String,String>>>();

        // 添加电压、电流数据
        List<Map<String, String>> voltageData = new ArrayList<Map<String, String>>();
        String[] voltageTitles = getResources().getStringArray(R.array.real_data_voltage_titles);
        for (int i = 0; i < voltageTitles.length; i++) {
            Map<String, String> itemData = new HashMap<String, String>();
            itemData.put(DataSectionRecyclerAdapter.TITLE_KEY, voltageTitles[i]);
            itemData.put(DataSectionRecyclerAdapter.DATA_KEY, "0");
            voltageData.add(itemData);
        }
        realData.add(voltageData);

        // 添加功率数据
        List<Map<String, String>>  powerData = new ArrayList<Map<String, String>>();
        String[] powerTitles = getResources().getStringArray(R.array.real_data_power_titles);
        for (int i = 0; i < powerTitles.length; i++) {
            Map<String, String> itemData = new HashMap<String, String>();
            itemData.put(DataSectionRecyclerAdapter.TITLE_KEY, powerTitles[i]);
            itemData.put(DataSectionRecyclerAdapter.DATA_KEY, "0");
            powerData.add(itemData);
        }
        realData.add(powerData);

        // 添加thd数据
        List<Map<String, String>> thdData = new ArrayList<Map<String, String>>();
        String[] thdTitles = getResources().getStringArray(R.array.real_data_thd_titles);
        for (int i = 0; i < thdTitles.length; i++) {
            Map<String, String> itemData = new HashMap<String, String>();
            itemData.put(DataSectionRecyclerAdapter.TITLE_KEY, thdTitles[i]);
            itemData.put(DataSectionRecyclerAdapter.DATA_KEY, "0");
            thdData.add(itemData);
        }
        realData.add(thdData);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_real_data, container, false);

        realDataRecyclerView = (RecyclerView) rootView.findViewById(R.id.real_data_view);

        GridLayoutManager layoutManager = new GridLayoutManager(getContext(), 2);
        adapter = new DataSectionRecyclerAdapter(getContext(), realData);
        RealDataSpanSizeLookup lookup = new RealDataSpanSizeLookup(adapter, layoutManager);
        layoutManager.setSpanSizeLookup(lookup);
        realDataRecyclerView.setLayoutManager(layoutManager);
        realDataRecyclerView.setAdapter(adapter);

        return rootView;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
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
