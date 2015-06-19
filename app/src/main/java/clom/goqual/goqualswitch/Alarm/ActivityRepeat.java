package clom.goqual.goqualswitch.Alarm;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.InjectViews;
import butterknife.OnClick;
import clom.goqual.goqualswitch.R;
import clom.goqual.goqualswitch.SharedPreference.InfoSharedPreference;

/**
 * Created by admin on 2015. 6. 18..
 */
public class ActivityRepeat extends Activity {
    private static final String TAG = "ACTIVITY_REPEAT";
    private Context mContext;
    private InfoSharedPreference mDeviceInfo;

    private boolean[] mArrBoolIsRepeat = new boolean[7];

    private RelativeLayout.LayoutParams mParamCheckImg;

    @InjectViews({ R.id.alarmrepeat_img_sun, R.id.alarmrepeat_img_mon, R.id.alarmrepeat_img_tue,
            R.id.alarmrepeat_img_wed, R.id.alarmrepeat_img_thur, R.id.alarmrepeat_img_fri, R.id.alarmrepeat_img_sat }) List<ImageView> mArrImgDayOfWeekCheck;

    @InjectView(R.id.alarmrepeat_btn_back) Button mBtnBack;
    @OnClick({ R.id.alarmrepeat_btn_back, R.id.alarmrepeat_rl_wed, R.id.alarmrepeat_rl_thur, R.id.alarmrepeat_rl_fri,
            R.id.alarmrepeat_rl_sat, R.id.alarmrepeat_rl_sun, R.id.alarmrepeat_rl_mon, R.id.alarmrepeat_rl_tue })
    void onClickButton(View view) {
        switch(view.getId()) {
            case R.id.alarmrepeat_btn_back:
                Intent intent = new Intent();
                intent.putExtra(getString(R.string.key_repeat_sun), mArrBoolIsRepeat[0]);
                intent.putExtra(getString(R.string.key_repeat_mon), mArrBoolIsRepeat[1]);
                intent.putExtra(getString(R.string.key_repeat_tue), mArrBoolIsRepeat[2]);
                intent.putExtra(getString(R.string.key_repeat_wed), mArrBoolIsRepeat[3]);
                intent.putExtra(getString(R.string.key_repeat_thur), mArrBoolIsRepeat[4]);
                intent.putExtra(getString(R.string.key_repeat_fri), mArrBoolIsRepeat[5]);
                intent.putExtra(getString(R.string.key_repeat_sat), mArrBoolIsRepeat[6]);
                setResult(1, intent);
                finish();
                break;
            case R.id.alarmrepeat_rl_sun:
                handleChecking(0);
                break;
            case R.id.alarmrepeat_rl_mon:
                handleChecking(1);
                break;
            case R.id.alarmrepeat_rl_tue:
                handleChecking(2);
                break;
            case R.id.alarmrepeat_rl_wed:
                handleChecking(3);
                break;
            case R.id.alarmrepeat_rl_thur:
                handleChecking(4);
                break;
            case R.id.alarmrepeat_rl_fri:
                handleChecking(5);
                break;
            case R.id.alarmrepeat_rl_sat:
                handleChecking(6);
                break;
            default:
                break;
        }
        }

    void handleChecking (int idx) {
        if (mArrBoolIsRepeat[idx]) {
            mArrBoolIsRepeat[idx] = false;
            mArrImgDayOfWeekCheck.get(idx).setVisibility(View.GONE);
        } else {
            mArrBoolIsRepeat[idx] = true;
            mArrImgDayOfWeekCheck.get(idx).setVisibility(View.VISIBLE);
        }
    }

    void handleInitState(Intent data) {
        mArrBoolIsRepeat[0] = data.getBooleanExtra(getString(R.string.key_repeat_sun), false);
        mArrBoolIsRepeat[1] = data.getBooleanExtra(getString(R.string.key_repeat_mon), false);
        mArrBoolIsRepeat[2] = data.getBooleanExtra(getString(R.string.key_repeat_tue), false);
        mArrBoolIsRepeat[3] = data.getBooleanExtra(getString(R.string.key_repeat_wed), false);
        mArrBoolIsRepeat[4] = data.getBooleanExtra(getString(R.string.key_repeat_thur), false);
        mArrBoolIsRepeat[5] = data.getBooleanExtra(getString(R.string.key_repeat_fri), false);
        mArrBoolIsRepeat[6] = data.getBooleanExtra(getString(R.string.key_repeat_sat), false);

        for(int i = 0; i < 7; i ++) {
            if (mArrBoolIsRepeat[i]) {
                mArrImgDayOfWeekCheck.get(i).setVisibility(View.VISIBLE);
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm_repeat);
        ButterKnife.inject(this);

        mContext = this;

        handleInitState(getIntent());
        setLayoutParam();
    }

    void setLayoutParam() {
        int panelSize = getDeviceInfo().getValue(getString(R.string.key_panelwidth), -1);
        int checkImgSize = panelSize / 15;

        Log.e(TAG, "PANEL SIZE: " + getDeviceInfo().getValue(getString(R.string.key_panelwidth), -1));
        Log.e(TAG, "SIZE: " + checkImgSize);

        mParamCheckImg = new RelativeLayout.LayoutParams(checkImgSize, checkImgSize);
        mParamCheckImg.addRule(RelativeLayout.CENTER_VERTICAL, RelativeLayout.TRUE);
        mParamCheckImg.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, RelativeLayout.TRUE);
        mParamCheckImg.setMargins(0, 0, 30, 0);

        for(int i = 0; i < 7; i ++) {
            mArrImgDayOfWeekCheck.get(i).setLayoutParams(mParamCheckImg);
        }
    }

    @Override
    protected void onDestroy() {
        ButterKnife.reset(this);
        super.onDestroy();
    }

    public InfoSharedPreference getDeviceInfo() {
        if (mDeviceInfo == null) {
            mDeviceInfo = new InfoSharedPreference(mContext);
        }

        return mDeviceInfo;
    }
}
