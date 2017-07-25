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

import cn.edu.siso.rlxapf.bean.DataBean;
import cn.edu.siso.rlxapf.bean.DataGroupBean;

public class SpectrumFragment extends Fragment {
    private static final String ARG_DEVICE_PARAM = "device_param";

    private String deviceParams;

    private OnFragmentInteractionListener mListener;

    private RecyclerView spectrumRecyclerView = null;
    private DataSectionRecyclerAdapter adapter = null;

    private List<DataGroupBean> spectrumData = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            deviceParams = getArguments().getString(ARG_DEVICE_PARAM);
        }

        // 初始化谐波数据
        spectrumData = new ArrayList<DataGroupBean>();

        String[] spectrumSectionData = getResources().getStringArray(R.array.real_data_sections);
        for (int i = 0; i < spectrumSectionData.length; i++) {

            List<DataBean> data = new ArrayList<DataBean>();
            for (int j = 0; j < 25; j++) {
                List<Map<String, Integer>> item = new ArrayList<Map<String, Integer>>();

                Map<String, Integer> value = new HashMap<String, Integer>();
                value.put(DataBean.DATA_KEY, 0);
                item.add(value);

                DataBean itemData = null;
                if (j == 0) {
                    switch (j) {
                        case 0:
                            itemData = new DataBean("A相谐波电流", item);
                            break;
                        case 1:
                            itemData = new DataBean("B相谐波电流", item);
                            break;
                        case 2:
                            itemData = new DataBean("C相谐波电流", item);
                            break;
                    }
                } else {
                    itemData = new DataBean(j + 1 + "次", item);
                }

                data.add(itemData);
            }

            DataGroupBean spectrumGroupData = new DataGroupBean(spectrumSectionData[0], data);
            spectrumData.add(spectrumGroupData);
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
