package cn.edu.siso.rlxapf;

import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import lecho.lib.hellocharts.gesture.ContainerScrollType;
import lecho.lib.hellocharts.gesture.ZoomType;
import lecho.lib.hellocharts.model.Axis;
import lecho.lib.hellocharts.model.AxisValue;
import lecho.lib.hellocharts.model.Line;
import lecho.lib.hellocharts.model.LineChartData;
import lecho.lib.hellocharts.model.PointValue;
import lecho.lib.hellocharts.model.ValueShape;
import lecho.lib.hellocharts.view.LineChartView;

public class RealCurveFragment extends Fragment {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    private String TAG = "===RealCurveFragment===";

    private OnFragmentInteractionListener mListener;

    private LineChartView voltageChart = null;
    private LineChartView ampereChart = null;
    private LineChartView loadChart = null;
    private LineChartView compensateChart = null;

    private List<AxisValue> xAxisLabels = null;
    private List<AxisValue> yAxisLabels = null;
    private List<List<PointValue>> voltageDatas = null;
    private List<List<PointValue>> ampereDatas = null;
    private List<List<PointValue>> loadDatas = null;
    private List<List<PointValue>> compensateDatas = null;
    private List<Line> voltageLines = null;
    private List<Line> ampereLines = null;
    private List<Line> loadLines = null;
    private List<Line> compensateLines = null;

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

        // 系统电压图表
        voltageChart = (LineChartView) rootView.findViewById(R.id.voltage_chart);
        // 系统电流图表
        ampereChart = (LineChartView) rootView.findViewById(R.id.ampere_chart);
        // 负载电流图表
        loadChart = (LineChartView) rootView.findViewById(R.id.load_chart);
        // 补偿电流图表
        compensateChart = (LineChartView) rootView.findViewById(R.id.compensate_chart);

        // 初始化数据和标题
        xAxisLabels = new ArrayList<AxisValue>();
        yAxisLabels = new ArrayList<AxisValue>();

        voltageDatas = new ArrayList<List<PointValue>>();
        voltageDatas.add(new ArrayList<PointValue>());
        voltageDatas.add(new ArrayList<PointValue>());
        voltageDatas.add(new ArrayList<PointValue>());

        ampereDatas = new ArrayList<List<PointValue>>();
        ampereDatas.add(new ArrayList<PointValue>());
        ampereDatas.add(new ArrayList<PointValue>());
        ampereDatas.add(new ArrayList<PointValue>());

        loadDatas = new ArrayList<List<PointValue>>();
        loadDatas.add(new ArrayList<PointValue>());
        loadDatas.add(new ArrayList<PointValue>());
        loadDatas.add(new ArrayList<PointValue>());

        compensateDatas = new ArrayList<List<PointValue>>();
        compensateDatas.add(new ArrayList<PointValue>());
        compensateDatas.add(new ArrayList<PointValue>());
        compensateDatas.add(new ArrayList<PointValue>());

        String[] xLabels = getResources().getStringArray(R.array.curve_x_axis_labels);
        String[] yLabels = getResources().getStringArray(R.array.curve_y_axis_labels);
        for (int i = 0; i < xLabels.length; i++) {
            xAxisLabels.add(new AxisValue(i).setLabel(xLabels[i]));
            yAxisLabels.add(new AxisValue(i).setLabel(yLabels[i]));

            voltageDatas.get(0).add(new PointValue(i, (new Random().nextInt(10) + 1)));
            voltageDatas.get(1).add(new PointValue(i, (new Random().nextInt(10) + 1)));
            voltageDatas.get(2).add(new PointValue(i, (new Random().nextInt(10) + 1)));

            ampereDatas.get(0).add(new PointValue(i, (new Random().nextInt(10) + 1)));
            ampereDatas.get(1).add(new PointValue(i, (new Random().nextInt(10) + 1)));
            ampereDatas.get(2).add(new PointValue(i, (new Random().nextInt(10) + 1)));

            loadDatas.get(0).add(new PointValue(i, (new Random().nextInt(10) + 1)));
            loadDatas.get(1).add(new PointValue(i, (new Random().nextInt(10) + 1)));
            loadDatas.get(2).add(new PointValue(i, (new Random().nextInt(10) + 1)));

            compensateDatas.get(0).add(new PointValue(i, (new Random().nextInt(10) + 1)));
            compensateDatas.get(1).add(new PointValue(i, (new Random().nextInt(10) + 1)));
            compensateDatas.get(2).add(new PointValue(i, (new Random().nextInt(10) + 1)));
        }

        // 初始化曲线样式
        voltageLines = new ArrayList<Line>();
        ampereLines = new ArrayList<Line>();
        loadLines = new ArrayList<Line>();
        compensateLines = new ArrayList<Line>();
        // int[] cureLineColors = getResources().getIntArray(R.array.cure_line_colors);
        String[] cureLineColors = new String[]{"#0C85BF", "#1DBA11", "#5406C2"};
        for (int i = 0; i < cureLineColors.length; i++) {
            Line voltageLine = new Line(voltageDatas.get(i));
            Line ampereLine = new Line(ampereDatas.get(i));
            Line loadLine = new Line(loadDatas.get(i));
            Line compensateLine = new Line(compensateDatas.get(i));

            // voltageLine.setColor(ContextCompat.getColor(context, cureLineColors[i]));
            voltageLine.setColor(Color.parseColor(cureLineColors[i]));
            voltageLine.setShape(ValueShape.CIRCLE); // 折线图上每个数据点的形状  这里是圆形
            voltageLine.setCubic(true); // 曲线是否平滑，即是曲线还是折线
            voltageLine.setFilled(false); // 是否填充曲线的面积 ?
            voltageLine.setHasLabels(true); // 曲线的数据坐标是否加上备注
            // voltageLine.setHasLabelsOnlyForSelected(true);// 点击数据坐标提示数据（设置了这个line.setHasLabels(true);就无效）
            voltageLine.setHasLines(true); // 是否用线显示。如果为false 则没有曲线只有点显示
            voltageLine.setHasPoints(true); // 是否显示圆点 如果为false 则没有原点只有点显示（每个数据点都是个大的圆点）

            // ampereLine.setColor(ContextCompat.getColor(context, cureLineColors[i]));
            ampereLine.setColor(Color.parseColor(cureLineColors[i]));
            ampereLine.setShape(ValueShape.CIRCLE); // 折线图上每个数据点的形状  这里是圆形
            ampereLine.setCubic(true); // 曲线是否平滑，即是曲线还是折线
            ampereLine.setFilled(false); // 是否填充曲线的面积 ?
            ampereLine.setHasLabels(true); // 曲线的数据坐标是否加上备注
            // ampereLine.setHasLabelsOnlyForSelected(true);// 点击数据坐标提示数据（设置了这个line.setHasLabels(true);就无效）
            ampereLine.setHasLines(true); // 是否用线显示。如果为false 则没有曲线只有点显示
            ampereLine.setHasPoints(true); // 是否显示圆点 如果为false 则没有原点只有点显示（每个数据点都是个大的圆点）

            // loadLine.setColor(ContextCompat.getColor(context, cureLineColors[i]));
            loadLine.setColor(Color.parseColor(cureLineColors[i]));
            loadLine.setShape(ValueShape.CIRCLE); // 折线图上每个数据点的形状  这里是圆形
            loadLine.setCubic(true); // 曲线是否平滑，即是曲线还是折线
            loadLine.setFilled(false); // 是否填充曲线的面积 ?
            loadLine.setHasLabels(true); // 曲线的数据坐标是否加上备注
            // loadLine.setHasLabelsOnlyForSelected(true);// 点击数据坐标提示数据（设置了这个line.setHasLabels(true);就无效）
            loadLine.setHasLines(true); // 是否用线显示。如果为false 则没有曲线只有点显示
            loadLine.setHasPoints(true); // 是否显示圆点 如果为false 则没有原点只有点显示（每个数据点都是个大的圆点）

            // compensateLine.setColor(ContextCompat.getColor(context, cureLineColors[i]));
            compensateLine.setColor(Color.parseColor(cureLineColors[i]));
            compensateLine.setShape(ValueShape.CIRCLE); // 折线图上每个数据点的形状  这里是圆形
            compensateLine.setCubic(true); // 曲线是否平滑，即是曲线还是折线
            compensateLine.setFilled(false); // 是否填充曲线的面积 ?
            compensateLine.setHasLabels(true); // 曲线的数据坐标是否加上备注
            // compensateLine.setHasLabelsOnlyForSelected(true);// 点击数据坐标提示数据（设置了这个line.setHasLabels(true);就无效）
            compensateLine.setHasLines(true); // 是否用线显示。如果为false 则没有曲线只有点显示
            compensateLine.setHasPoints(true); // 是否显示圆点 如果为false 则没有原点只有点显示（每个数据点都是个大的圆点）

            voltageLines.add(voltageLine);
            ampereLines.add(ampereLine);
            loadLines.add(loadLine);
            compensateLines.add(compensateLine);
        }

        // 初始化X轴样式
        Axis axisX = new Axis(); //X轴
        axisX.setHasTiltedLabels(false); //X坐标轴字体是斜的显示还是直的，true是斜的显示
//        axisX.setTextColor(ContextCompat.getColor(context, R.color.label_color));  //设置字体颜色
        axisX.setTextColor(Color.parseColor("#6C6C6C"));  //设置字体颜色
//        axisX.setName("时间");  //表格名称 ?
        axisX.setTextSize(10); // 设置字体大小
        axisX.setMaxLabelChars(10); // 最多几个X轴坐标，意思就是你的缩放让X轴上数据的个数7<=x<=mAxisXValues.length ?
        axisX.setValues(xAxisLabels); // 填充X轴的坐标名称
        axisX.setHasLines(true); // x 轴分割线 ?

        // 初始化Y轴样式
        Axis axisY = new Axis(); //Y轴
        axisY.setHasTiltedLabels(false); //X坐标轴字体是斜的显示还是直的，true是斜的显示
        axisY.setTextColor(Color.parseColor("#6C6C6C"));  //设置字体颜色
//        axisY.setName("数值"); //y轴标注
        axisY.setTextSize(10); //设置字体大小
        axisY.setMaxLabelChars(10);//max label length, for example 60
        // Y轴固定
        axisY.setValues(yAxisLabels);
        axisY.setHasLines(true);

        LineChartData voltageCharData = new LineChartData();
        voltageCharData.setLines(voltageLines);
        voltageCharData.setAxisXBottom(axisX); // x轴在底部
        voltageCharData.setAxisYLeft(axisY); //Y轴设置在左边

        LineChartData ampereCharData = new LineChartData();
        ampereCharData.setLines(voltageLines);
        ampereCharData.setAxisXBottom(axisX); // x轴在底部
        ampereCharData.setAxisYLeft(axisY); //Y轴设置在左边

        LineChartData loadCharData = new LineChartData();
        loadCharData.setLines(loadLines);
        loadCharData.setAxisXBottom(axisX); // x轴在底部
        loadCharData.setAxisYLeft(axisY); //Y轴设置在左边

        LineChartData compensateCharData = new LineChartData();
        compensateCharData.setLines(compensateLines);
        compensateCharData.setAxisXBottom(axisX); // x轴在底部
        compensateCharData.setAxisYLeft(axisY); //Y轴设置在左边

        //设置行为属性，支持缩放、滑动以及平移
        voltageChart.setInteractive(false);
        voltageChart.setZoomType(ZoomType.HORIZONTAL);
        voltageChart.setMaxZoom((float) 2);//最大方法比例
        voltageChart.setContainerScrollEnabled(false, ContainerScrollType.HORIZONTAL);
        voltageChart.setLineChartData(voltageCharData);
        voltageChart.setVisibility(View.VISIBLE);

        ampereChart.setInteractive(false);
        ampereChart.setZoomType(ZoomType.HORIZONTAL);
        ampereChart.setMaxZoom((float) 2);//最大方法比例
        ampereChart.setContainerScrollEnabled(false, ContainerScrollType.HORIZONTAL);
        ampereChart.setLineChartData(ampereCharData);
        ampereChart.setVisibility(View.VISIBLE);

        loadChart.setInteractive(false);
        loadChart.setZoomType(ZoomType.HORIZONTAL);
        loadChart.setMaxZoom((float) 2);//最大方法比例
        loadChart.setContainerScrollEnabled(false, ContainerScrollType.HORIZONTAL);
        loadChart.setLineChartData(loadCharData);
        loadChart.setVisibility(View.VISIBLE);

        compensateChart.setInteractive(false);
        compensateChart.setZoomType(ZoomType.HORIZONTAL);
        compensateChart.setMaxZoom((float) 2);//最大方法比例
        compensateChart.setContainerScrollEnabled(false, ContainerScrollType.HORIZONTAL);
        compensateChart.setLineChartData(compensateCharData);
        compensateChart.setVisibility(View.VISIBLE);

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
