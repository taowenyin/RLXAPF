package cn.edu.siso.rlxapf.data.viewholder;

import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

import cn.edu.siso.rlxapf.R;
import lecho.lib.hellocharts.gesture.ContainerScrollType;
import lecho.lib.hellocharts.gesture.ZoomType;
import lecho.lib.hellocharts.model.Axis;
import lecho.lib.hellocharts.model.AxisValue;
import lecho.lib.hellocharts.model.Line;
import lecho.lib.hellocharts.model.LineChartData;
import lecho.lib.hellocharts.model.PointValue;
import lecho.lib.hellocharts.model.ValueShape;
import lecho.lib.hellocharts.view.LineChartView;

public class CurveItemViewHolder extends RecyclerView.ViewHolder {

    private LineChartView curveChart = null;

    public CurveItemViewHolder(View itemView) {
        super(itemView);

        curveChart = (LineChartView) itemView.findViewById(R.id.curve_chart);
    }

    public LineChartView getCurveChart() {
        return curveChart;
    }
}
