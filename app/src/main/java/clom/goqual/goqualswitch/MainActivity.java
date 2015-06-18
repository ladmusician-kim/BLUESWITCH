package clom.goqual.goqualswitch;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.widget.Button;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import clom.goqual.goqualswitch.Alarm.ActivityCreateAlarm;


public class MainActivity extends FragmentActivity {

    @InjectView(R.id.btn_alarm) Button mBtnAlarm;
    @OnClick({ R.id.btn_alarm })
    void onClickButton (Button btn) {
        switch(btn.getId()) {
            case R.id.btn_alarm:
                Intent alarmIntent = new Intent(getApplicationContext(), ActivityCreateAlarm.class);
                startActivity(alarmIntent);
                break;
            default:
                break;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.inject(this);
    }

    @Override
    protected void onDestroy() {
        ButterKnife.reset(this);
        super.onDestroy();
    }
}
