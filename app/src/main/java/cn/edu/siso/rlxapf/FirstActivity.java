package cn.edu.siso.rlxapf;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import java.util.Timer;
import java.util.TimerTask;

public class FirstActivity extends Activity {

    private TimerTask startMainActivity = new TimerTask() {
        @Override
        public void run() {
            Intent intent = new Intent(FirstActivity.this, LoginActivity.class);
            startActivity(intent);
            FirstActivity.this.finish();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first);

        new Timer().schedule(startMainActivity, 3000);
    }
}
