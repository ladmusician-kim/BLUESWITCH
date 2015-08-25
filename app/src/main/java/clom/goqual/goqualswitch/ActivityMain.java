package clom.goqual.goqualswitch;

import android.app.Activity;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.widget.Button;
import android.widget.Toast;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import clom.goqual.goqualswitch.Alarm.ActivityCreateAlarm;
import clom.goqual.goqualswitch.SharedPreference.InfoSharedPreference;


public class ActivityMain extends Activity {
    private static final String TAG = "ACTIVITY_MAIN";
    private Context mContext;
    private InfoSharedPreference mDeviceInfo;
    private NotificationManager mNotiMng = null;

    @InjectView(R.id.btn_alarm) Button mBtnAlarm;
    @InjectView(R.id.btn_noti) Button mBtnNoti;
    @OnClick({ R.id.btn_alarm, R.id.btn_noti })
    void onClickButton (Button btn) {
        switch(btn.getId()) {
            case R.id.btn_alarm:
                Intent alarmIntent = new Intent(getApplicationContext(), ActivityCreateAlarm.class);
                startActivity(alarmIntent);
                break;
            case R.id.btn_noti:
                Intent notiIntent = new Intent(getString(R.string.ACTION_NOTI));
                notiIntent.setAction(getString(R.string.ACTION_NOTI));
                notiIntent.putExtra(getString(R.string.key_noti_switch_turn_on), false);
                startService(notiIntent);

                Toast.makeText(ActivityMain.this, "Notification Registered.", Toast.LENGTH_SHORT).show();
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
        mContext = getApplicationContext();
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
