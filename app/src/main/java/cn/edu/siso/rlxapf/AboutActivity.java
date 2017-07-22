package cn.edu.siso.rlxapf;

import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

public class AboutActivity extends AppCompatActivity implements
        AboutFragment.OnFragmentInteractionListener {

    public static String TAG = "AboutActivity";

    private ImageButton toolbarBack = null;
    private TextView toolbarTitle = null;

    private String currentFragmentTag = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        toolbarBack = (ImageButton) findViewById(R.id.toolbar_back);
        toolbarTitle = (TextView) findViewById(R.id.toolbar_title);

        toolbarBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (currentFragmentTag.equals(AboutFragment.TAG)) {
                    finish();
                }
                if (currentFragmentTag.equals(DescriptionFragment.TAG)) {
                    toolbarTitle.setText(R.string.title_activity_about);
                    currentFragmentTag = AboutFragment.TAG;
                    getSupportFragmentManager().popBackStack();
                }
            }
        });

        getSupportFragmentManager().beginTransaction().replace(
                R.id.about_content, AboutFragment.newInstance()).commit();
        currentFragmentTag = AboutFragment.TAG;
    }

    @Override
    public void onFragmentInteraction(Uri uri) {
        Log.i(TAG, uri.getScheme() + " " + uri.getAuthority() + " " + uri.getQueryParameter("action") + " " + uri.getQueryParameter("data"));

        if (uri.getQueryParameter(UriCommunication.Action).equals(UriCommunication.ActionParams.Click)) {
            int id = Integer.parseInt(uri.getQueryParameter(UriCommunication.Data));

            switch (id) {
                case R.id.about_description:
                    toolbarTitle.setText(R.string.about_label_description);
                    getSupportFragmentManager().beginTransaction().replace(
                            R.id.about_content, DescriptionFragment.newInstance()).addToBackStack(null).commit();
                    currentFragmentTag = DescriptionFragment.TAG;
                    Log.i(TAG, "===about_description===");
                    break;
            }
        }
    }
}
