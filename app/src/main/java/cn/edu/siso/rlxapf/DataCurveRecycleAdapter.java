package cn.edu.siso.rlxapf;

import android.content.Context;
import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.truizlop.sectionedrecyclerview.SectionedRecyclerViewAdapter;

import java.util.ArrayList;
import java.util.List;
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


public class DataCurveRecycleAdapter extends SectionedRecyclerViewAdapter<
        CurveSectionViewHolder, CurveItemViewHolder, CurveFooterViewHolder> {

    private List<DataGroupBean> data = null;
    private Context context = null;

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
        DataGroupBean groupBean = data.get(section);

        // 初始化数据和标题
        List<AxisValue> xAxisLabels = new ArrayList<AxisValue>();
        List<AxisValue> yAxisLabels = new ArrayList<AxisValue>();
        List<List<PointValue>> pointValueDatas = new ArrayList<List<PointValue>>();

        String[] xLabels = context.getResources().getStringArray(R.array.curve_x_axis_labels);
        String[] yLabels = context.getResources().getStringArray(R.array.curve_y_axis_labels);
        for (int i = 0; i < xLabels.length; i++) {
            xAxisLabels.add(new AxisValue(i).setLabel(xLabels[i]));
            yAxisLabels.add(new AxisValue(i).setLabel(yLabels[i]));
        }
        for (int i = 0; i < groupBean.getDataSize(); i++) {
            List<PointValue> pointItemData = new ArrayList<PointValue>();
            for (int j = 0; j < groupBean.getIndexData(i).getDataSize(); j++) {
                Float value = (Float) groupBean.getIndexData(i).getIndexData(j).get(DataBean.DATA_KEY);
                pointItemData.add(new PointValue(j, value));
            }
            pointValueDatas.add(pointItemData);
        }

        // 初始化曲线样式
        List<Line> dataLines = new ArrayList<Line>();
        String[] dataLinesColors = new String[]{"#0C85BF", "#1DBA11", "#5406C2"};
        for (int i = 0; i < dataLinesColors.length; i++) {
            Line line = new Line(pointValueDatas.get(i));

//            line.setColor(ContextCompat.getColor(context, dataLinesColors[i]));
            line.setColor(Color.parseColor(dataLinesColors[i]));
            line.setShape(ValueShape.CIRCLE); // 折线图上每个数据点的形状  这里是圆形
            line.setCubic(true); // 曲线是否平滑，即是曲线还是折线
            line.setFilled(false); // 是否填充曲线的面积 ?
            line.setHasLabels(true); // 曲线的数据坐标是否加上备注
            // voltageLine.setHasLabelsOnlyForSelected(true);// 点击数据坐标提示数据（设置了这个line.setHasLabels(true);就无效）
            line.setHasLines(true); // 是否用线显示。如果为false 则没有曲线只有点显示
            line.setHasPoints(true); // 是否显示圆点 如果为false 则没有原点只有点显示（每个数据点都是个大的圆点）

            dataLines.add(line);
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

        LineChartData charData = new LineChartData();
        charData.setLines(dataLines);
        charData.setAxisXBottom(axisX); // x轴在底部
        charData.setAxisYLeft(axisY); //Y轴设置在左边

        //设置行为属性，支持缩放、滑动以及平移
        holder.getCurveChart().setInteractive(false);
        holder.getCurveChart().setZoomType(ZoomType.HORIZONTAL);
        holder.getCurveChart().setMaxZoom((float) 2);//最大方法比例
        holder.getCurveChart().setContainerScrollEnabled(false, ContainerScrollType.HORIZONTAL);
        holder.getCurveChart().setLineChartData(charData);
        holder.getCurveChart().setVisibility(View.VISIBLE);

    }
}
