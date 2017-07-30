package cn.edu.siso.rlxapf;

import android.content.Context;
import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.truizlop.sectionedrecyclerview.SectionedRecyclerViewAdapter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

import cn.edu.siso.rlxapf.bean.DataBean;
import cn.edu.siso.rlxapf.bean.DataGroupBean;
import cn.edu.siso.rlxapf.data.viewholder.CurveFooterViewHolder;
import cn.edu.siso.rlxapf.data.viewholder.CurveItemViewHolder;
import cn.edu.siso.rlxapf.data.viewholder.CurveSectionViewHolder;
import lecho.lib.hellocharts.gesture.ContainerScrollType;
import lecho.lib.hellocharts.gesture.ZoomType;
import lecho.lib.hellocharts.model.Axis;
import lecho.lib.hellocharts.model.AxisValue;
import lecho.lib.hellocharts.model.Line;
import lecho.lib.hellocharts.model.LineChartData;
import lecho.lib.hellocharts.model.PointValue;
import lecho.lib.hellocharts.model.ValueShape;
import lecho.lib.hellocharts.model.Viewport;
import lecho.lib.hellocharts.view.LineChartView;


public class DataCurveRecycleAdapter extends SectionedRecyclerViewAdapter<
        CurveSectionViewHolder, CurveItemViewHolder, CurveFooterViewHolder> {

    private List<DataGroupBean> data = null;
    private Context context = null;
    private Axis axisX = null, axisY = null;

    private String[] lineColor = null;

    public static final String TAG = "DataCurveRecycleAdapter";

    public DataCurveRecycleAdapter(Context context, List<DataGroupBean> data) {
        super();

        this.context = context;
        this.data = data;
    }

    @Override
    protected int getSectionCount() {
        return data.size();
    }

    @Override
    protected int getItemCountForSection(int section) {
        return 1;
    }

    @Override
    protected boolean hasFooterInSection(int section) {
        return true;
    }

    @Override
    protected CurveSectionViewHolder onCreateSectionHeaderViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.curve_section_layout, parent, false);
        return new CurveSectionViewHolder(view);
    }

    @Override
    protected CurveFooterViewHolder onCreateSectionFooterViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.curve_footer_layout, parent, false);
        return new CurveFooterViewHolder(view);
    }

    @Override
    protected CurveItemViewHolder onCreateItemViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.curve_item_layout, parent, false);

        LineChartView curveChart = (LineChartView) view.findViewById(R.id.curve_chart);

        // 初始化标题
        List<AxisValue> xAxisLabels = new ArrayList<AxisValue>();
        List<AxisValue> yAxisLabels = new ArrayList<AxisValue>();

        String[] xLabels = context.getResources().getStringArray(R.array.curve_x_axis_labels);
        String[] yLabels = context.getResources().getStringArray(R.array.curve_y_axis_labels);
        for (String label : xLabels) {
            String labelValue = label.substring(0, label.length() - 1);
            xAxisLabels.add(new AxisValue(Integer.valueOf(labelValue) - 1).setLabel(label));
        }
        for (String label : yLabels) {
            yAxisLabels.add(new AxisValue(Integer.valueOf(label)).setLabel(label));
        }

        // 初始化X轴样式
        axisX = new Axis(); //X轴
        axisX.setHasTiltedLabels(false); //X坐标轴字体是斜的显示还是直的，true是斜的显示
        axisX.setTextColor(Color.parseColor("#6C6C6C"));  //设置字体颜色
//        axisX.setName("时间");  //表格名称 ?
        axisX.setTextSize(10); // 设置字体大小
        axisX.setMaxLabelChars(xAxisLabels.size()); // 最多几个X轴坐标，意思就是你的缩放让X轴上数据的个数7<=x<=mAxisXValues.length ?
        axisX.setValues(xAxisLabels); // 填充X轴的坐标名称
        axisX.setHasLines(true); // x 轴分割线 ?

        // 初始化Y轴样式
        axisY = new Axis(); //Y轴
        axisY.setHasTiltedLabels(false); //X坐标轴字体是斜的显示还是直的，true是斜的显示
        axisY.setTextColor(Color.parseColor("#6C6C6C"));  //设置字体颜色
//        axisY.setName("数值"); //y轴标注
        axisY.setTextSize(10); //设置字体大小
        axisY.setMaxLabelChars(yAxisLabels.size());//max label length, for example 60
        axisY.setValues(yAxisLabels);
        axisY.setHasLines(true);

        // 为曲线控件设置视口
        Viewport v = new Viewport(curveChart.getMaximumViewport());
        v.bottom = -30;
        v.top = 120;
        v.left = 0;
        v.right = 10 - 1;
        curveChart.setMaximumViewport(v);
        curveChart.setCurrentViewport(v);

        curveChart.setCurrentViewport(v);
        curveChart.setViewportCalculationEnabled(false);

        //设置行为属性，支持缩放、滑动以及平移
        curveChart.setInteractive(false);
        curveChart.setZoomType(ZoomType.HORIZONTAL);
        curveChart.setMaxZoom(2);//最大方法比例
        curveChart.setContainerScrollEnabled(false, ContainerScrollType.HORIZONTAL);
        curveChart.setVisibility(View.VISIBLE);
        curveChart.setValueTouchEnabled(true);

        lineColor = context.getResources().getStringArray(R.array.real_curve_line_color);

        return new CurveItemViewHolder(view);
    }

    @Override
    protected void onBindSectionHeaderViewHolder(CurveSectionViewHolder holder, int section) {
        DataGroupBean groupBean = data.get(section);
        holder.setCurveGroupSection(groupBean.getGroupSection());

        holder.setCurveASection(groupBean.getData().get(0).getSection());
        holder.setCurveBSection(groupBean.getData().get(1).getSection());
        holder.setCurveCSection(groupBean.getData().get(2).getSection());
    }

    @Override
    protected void onBindSectionFooterViewHolder(CurveFooterViewHolder holder, int section) {

    }

    @Override
    protected void onBindItemViewHolder(CurveItemViewHolder holder, int section, int position) {
        // 获取每一组的数据
        DataGroupBean groupBean = data.get(section);

        // 初始化曲线集对象
        List<Line> realLines = new ArrayList<Line>();
        // 初始化曲线数据集对象
        List<List<PointValue>> realLinesPointData = new ArrayList<List<PointValue>>();
        // 初始化数据
        for (int i = 0; i < groupBean.getDataSize(); i++) {
            // 初始化每一条曲线的数据对象
            List<PointValue> itemPointData = new ArrayList<PointValue>();
            // 获取每一条曲线的数据值
            DataBean<Float> itemData = groupBean.getIndexData(i);
            for (int j = 0; j < itemData.getDataSize(); j++) {
                // 获取数据
                Map<String, Float> dataValue = itemData.getIndexData(j);
                // 把输入加入新创建的曲线数据对象
                itemPointData.add(new PointValue(j, dataValue.get(DataBean.DATA_KEY)));
            }

            // 创建一条曲线，并初始化数据
            Line line = new Line(itemPointData);
            // 设置这条曲线的颜色
            line.setColor(Color.parseColor(lineColor[i]));
            // 设置曲线圆点的大小
            line.setPointRadius(5);
            // 设置这条曲线的宽度
            line.setStrokeWidth(2);
            // 折线图上每个数据点的形状  这里是圆形
            line.setShape(ValueShape.CIRCLE);
            // 曲线是否平滑，即是曲线还是折线
            line.setCubic(true);
            // 是否填充曲线的面积
            line.setFilled(false);
            // 曲线的数据坐标是否加上备注
            line.setHasLabels(false);
            // 点击数据坐标提示数据（设置了这个line.setHasLabels(true);就无效）
            line.setHasLabelsOnlyForSelected(true);
            // 是否用线显示。如果为false 则没有曲线只有点显示
            line.setHasLines(true);
            // 是否显示圆点 如果为false 则没有原点只有点显示（每个数据点都是个大的圆点）
            line.setHasPoints(true);

            // 把所有数据放到曲线数据集对象
            realLinesPointData.add(itemPointData);
            // 把曲线放到曲线数据集对象
            realLines.add(line);
        }

        // 初始化曲线数据对象
        LineChartData charData = new LineChartData(realLines);
        // 设置曲线的X轴
        charData.setAxisXBottom(axisX);
        // 设置曲线的Y轴
        charData.setAxisYLeft(axisY);

        // 得到当前的图表对象
        LineChartView curveChart = holder.getCurveChart();
        // 把曲线数据对象设置到曲线控件中
        curveChart.setLineChartData(charData);
    }
}
