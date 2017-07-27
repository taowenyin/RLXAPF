package cn.edu.siso.rlxapf.data.viewholder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import cn.edu.siso.rlxapf.R;

/**
 * Created by taowenyin on 17-7-25.
 */

public class CurveSectionViewHolder extends RecyclerView.ViewHolder {

    private TextView curveGroupSection = null;
    private TextView curveASection = null;
    private TextView curveBSection = null;
    private TextView curveCSection = null;

    public CurveSectionViewHolder(View itemView) {
        super(itemView);

        curveGroupSection = (TextView) itemView.findViewById(R.id.curve_group_section);
        curveASection = (TextView) itemView.findViewById(R.id.curve_a_section);
        curveBSection = (TextView) itemView.findViewById(R.id.curve_b_section);
        curveCSection = (TextView) itemView.findViewById(R.id.curve_c_section);
    }

    public void setCurveGroupSection(String groupSection) {
        curveGroupSection.setText(groupSection);
    }

    public void setCurveASection(String aSection) {
        curveASection.setText(aSection);
    }

    public void setCurveBSection(String bSection) {
        curveBSection.setText(bSection);
    }

    public void setCurveCSection(String cSection) {
        curveCSection.setText(cSection);
    }
}
