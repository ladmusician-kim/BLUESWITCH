package clom.goqual.goqualswitch;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.DisplayMetrics;
import android.widget.Button;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import clom.goqual.goqualswitch.Alarm.ActivityCreateAlarm;
import clom.goqual.goqualswitch.SharedPreference.InfoSharedPreference;


public class MainActivity extends FragmentActivity {
    private static final String TAG = "ACTIVITY_MAIN";
    private InfoSharedPreference mDeviceInfo;

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

    public void handleSharedPreference() {
        // sharedPreference
        if (getDeviceInfo().getValue(getString(R.string.key_panelwidth), -1) == -1
                || getDeviceInfo().getValue(getString(R.string.key_panelheight), -1) == -1) {
            DisplayMetrics metrics = new DisplayMetrics();
            getWindowManager().getDefaultDisplay().getMetrics(metrics);

            getDeviceInfo().put(getString(R.string.key_panelwidth),(metrics.widthPixels));
            getDeviceInfo().put(getString(R.string.key_panelheight),(metrics.heightPixels));
        }
    }

    public InfoSharedPreference getDeviceInfo() {
        if (mDeviceInfo == null) {
            mDeviceInfo = new InfoSharedPreference(this);
        }

        return mDeviceInfo;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.inject(this);
        handleSharedPreference();
    }

    @Override
    protected void onDestroy() {
        ButterKnife.reset(this);
        super.onDestroy();
    }
}
