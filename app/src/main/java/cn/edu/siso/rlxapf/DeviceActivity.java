package cn.edu.siso.rlxapf;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class DeviceActivity extends AppCompatActivity {

    private Button devicePrefOk = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_device);

        devicePrefOk = (Button) findViewById(R.id.device_pref_ok);
        devicePrefOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DeviceActivity.this, MainActivity.class);
                startActivity(intent);
                DeviceActivity.this.finish();
            }
        });

        getSupportFragmentManager().beginTransaction().replace(
                R.id.device_pref, new DevicePrefFragment()).commit();
    }
}
