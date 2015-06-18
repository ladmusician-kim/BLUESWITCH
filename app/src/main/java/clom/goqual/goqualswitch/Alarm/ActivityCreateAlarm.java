package clom.goqual.goqualswitch.Alarm;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.Button;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import clom.goqual.goqualswitch.R;

/**
 * Created by admin on 2015. 6. 18..
 */
public class ActivityCreateAlarm extends FragmentActivity {
    private static final String TAG = "ACTIVITY_CREATE_ALARM";
    private Context mContext;

    @InjectView(R.id.alarmcreate_btn_cancel) Button mBtnCancel;
    @OnClick({ R.id.alarmcreate_btn_cancel })
    void onClickButton (View view) {
        switch(view.getId()) {
            case R.id.alarmcreate_rl_repeat:
                break;
            case R.id.alarmcreate_rl_ringtone:
                break;
            case R.id.alarmcreate_btn_cancel:
                finish();
                break;
            default:
                break;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm_create);
        ButterKnife.inject(this);
    }
    @Override
    protected void onDestroy() {
        ButterKnife.reset(this);
        super.onDestroy();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }
}
