package cn.edu.siso.rlxapf;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.truizlop.sectionedrecyclerview.SectionedSpanSizeLookup;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SpectrumFragment extends Fragment {
    private static final String ARG_DEVICE_PARAM = "device_param";

    private String deviceParams;

    private OnFragmentInteractionListener mListener;

    private RecyclerView spectrumRecyclerView = null;
    private DataSectionRecyclerAdapter adapter = null;

    private List<List<Map<String, String>>> spectrumData = null;

    private static final int SPECTRUM_SECTIION_COUNT = 3;

    private static final int SPECTRUM_DATA_COUNT = 25;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            deviceParams = getArguments().getString(ARG_DEVICE_PARAM);
        }

        // 初始化谐波数据
        this.spectrumData = new ArrayList<List<Map<String,String>>>();
        for (int i = 0; i < SPECTRUM_SECTIION_COUNT; i++) {
            List<Map<String, String>> dataItem = new ArrayList<Map<String, String>>();

            for (int j = 0; j < SPECTRUM_DATA_COUNT; j++) {
                Map<String, String> item = new HashMap<String, String>();

                if (j == 0) {
                    switch (i) {
                        case 0:
                            item.put(DataSectionRecyclerAdapter.TITLE_KEY, "A相谐波电流");
                            break;
                        case 1:
                            item.put(DataSectionRecyclerAdapter.TITLE_KEY, "B相谐波电流");
                            break;
                        case 2:
                            item.put(DataSectionRecyclerAdapter.TITLE_KEY, "C相谐波电流");
                            break;
                    }
                } else {
                    item.put(DataSectionRecyclerAdapter.TITLE_KEY, j + 1 + "次");
                }
                item.put(DataSectionRecyclerAdapter.DATA_KEY, "0");

                dataItem.add(item);
            }

            spectrumData.add(dataItem);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_spectrum, container, false);

        spectrumRecyclerView = (RecyclerView) rootView.findViewById(R.id.spectrum_view);

        GridLayoutManager layoutManager = new GridLayoutManager(getContext(), 2);
        adapter = new DataSectionRecyclerAdapter(getContext(), spectrumData);
        SectionedSpanSizeLookup lookup = new SectionedSpanSizeLookup(adapter, layoutManager);
        layoutManager.setSpanSizeLookup(lookup);
        spectrumRecyclerView.setLayoutManager(layoutManager);
        spectrumRecyclerView.setAdapter(adapter);

        return rootView;
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
