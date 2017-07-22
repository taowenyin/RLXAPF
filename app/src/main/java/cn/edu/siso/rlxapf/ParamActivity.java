package cn.edu.siso.rlxapf;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

public class ParamActivity extends AppCompatActivity {

    private ImageButton toolbarNavBack = null;

    private String currentFragmentTag = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_param);

        toolbarNavBack = (ImageButton) findViewById(R.id.toolbar_back);

        toolbarNavBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (currentFragmentTag.equals(ParamPrefFragment.TAG)) {
                    finish();
                }
            }
        });

        getSupportFragmentManager().beginTransaction().replace(
                R.id.param_content, ParamPrefFragment.newInstance()).commit();
        currentFragmentTag = ParamPrefFragment.TAG;
    }
}
