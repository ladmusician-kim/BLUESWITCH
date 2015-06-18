package clom.goqual.goqualswitch.Alarm;

import android.content.Context;
import android.content.Intent;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

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

    private String mRingtoneUri = "";
    private String mRingtoneTitle = "";
    private boolean mIsValidateSound = false;

    @InjectView(R.id.alarmcreate_txt_ringtone_title) TextView mTxtRingtoneTitle;

    @InjectView(R.id.alarmcreate_btn_cancel) Button mBtnCancel;
    @OnClick({ R.id.alarmcreate_btn_cancel, R.id.alarmcreate_rl_ringtone })
    void onClickButton (View view) {
        switch(view.getId()) {
            case R.id.alarmcreate_rl_repeat:
                break;
            case R.id.alarmcreate_rl_ringtone:
                showRingtonePickerDialog();
                break;
            case R.id.alarmcreate_btn_cancel:
                finish();
                break;
            default:
                break;
        }
    }

    // 벨소리 선택 dialog
    private void showRingtonePickerDialog() {
        Intent intent = new Intent(RingtoneManager.ACTION_RINGTONE_PICKER);
        intent.putExtra(RingtoneManager.EXTRA_RINGTONE_TITLE, "SELECT RINGTONE:");
        intent.putExtra(RingtoneManager.EXTRA_RINGTONE_SHOW_DEFAULT, true);
        intent.putExtra(RingtoneManager.EXTRA_RINGTONE_TYPE,RingtoneManager.TYPE_ALL);

        startActivityForResult(intent, 0);
    }

    // startActivity 결과값 받아오기;
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (resultCode) {
            case -1:	// ringtone
                dealRingtone(data);
                break;
            default:
                break;
        }
    }

    void dealRingtone(Intent data) {
        Uri uri = data.getParcelableExtra(RingtoneManager.EXTRA_RINGTONE_PICKED_URI);
        mRingtoneUri = uri.toString();
        Log.e(TAG, "RINGTONE URL: " + mRingtoneUri);
        if (uri != null) {
            Ringtone ringtone = RingtoneManager.getRingtone(this, uri);
            mRingtoneTitle = ringtone.getTitle(this);
            Log.e(TAG, "RINGTONE TITLE: " + mRingtoneTitle);
            mTxtRingtoneTitle.setText(mRingtoneTitle);
            mIsValidateSound = true;
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
