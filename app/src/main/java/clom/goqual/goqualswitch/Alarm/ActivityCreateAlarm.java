package clom.goqual.goqualswitch.Alarm;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.TimePicker;

import java.util.Arrays;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import clom.goqual.blelibrary.CalculateTriggerTime;
import clom.goqual.goqualswitch.DTO.AlarmDTO;
import clom.goqual.goqualswitch.R;
import clom.goqual.goqualswitch.Service.AlarmService;

/**
 * Created by admin on 2015. 6. 18..
 */
public class ActivityCreateAlarm extends Activity {
    private static final String TAG = "ACTIVITY_CREATE_ALARM";
    private Context mContext = this;

    private boolean mIsValidateSound = false;
    private boolean mIsValidateRepeat = false;

    private String mRingtoneUri = "";
    private String mRingtoneTitle = "";
    private boolean[] mArrBoolIsRepeat = new boolean[7];
    private AlarmManager mAlarmManger;

    @InjectView(R.id.alarmcreate_txt_ringtone_title) TextView mTxtRingtoneTitle;
    @InjectView(R.id.alarmcreate_txt_repeat_result) TextView mTxtRepeatResult;
    @InjectView(R.id.alarmcreate_tp_trigger_time) TimePicker mTPTriggerTime;

    @InjectView(R.id.alarmcreate_btn_cancel) Button mBtnCancel;
    @OnClick({ R.id.alarmcreate_btn_cancel, R.id.alarmcreate_rl_ringtone, R.id.alarmcreate_rl_repeat, R.id.alarmcreate_btn_save })
    void onClickButton (View view) {
    switch(view.getId()) {
        case R.id.alarmcreate_rl_repeat:
            setRepeat();
            break;
        case R.id.alarmcreate_rl_ringtone:
            showRingtonePickerDialog();
            break;
        case R.id.alarmcreate_btn_save:
            long returnVal = createAlarmInDB();
            if (returnVal != -1) {
                setAlarm(returnVal);
            }
            break;
        case R.id.alarmcreate_btn_cancel:
            finish();
            break;
        default:
            break;


    }
}
    // 알람 등록 IN DB
    long createAlarmInDB() {
        int hour = mTPTriggerTime.getCurrentHour();
        int min = mTPTriggerTime.getCurrentMinute();

        Log.e(TAG, "INSERT DB RINGTONE : " + mRingtoneUri);
        AlarmDTO alarm = new AlarmDTO(hour, min, mRingtoneUri, mArrBoolIsRepeat);
        alarm.save();

        return alarm.getId();
    }
    void setAlarm(long id) {
        AlarmDTO alarm = AlarmDTO.findById(AlarmDTO.class, id);
        CalculateTriggerTime calculateTriggerTime = new CalculateTriggerTime(alarm.getHour(), alarm.getMin());
        long triggerTime = calculateTriggerTime.calSetTimerSecond();

        Log.e(TAG, "INTENT RINGTONE : " + alarm.getRingtone());

        Intent alarmIntent = new Intent(mContext, AlarmService.class);
        alarmIntent.setAction(getString(R.string.ACTION_ALARM));
        alarmIntent.putExtra(getString(R.string.key_alarm_id), id);
        alarmIntent.putExtra(getString(R.string.key_alarm_ringtone), alarm.getRingtone());
        PendingIntent pi = PendingIntent.getService(mContext, 0, alarmIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        getAlarmManager().setRepeating(AlarmManager.RTC_WAKEUP, triggerTime, AlarmManager.INTERVAL_DAY, pi);
        Log.e(TAG, "SET ALARM");
    }

    // 반복
    void setRepeat() {
        Intent intent = new Intent(mContext, ActivityRepeat.class);
        intent.putExtra(getString(R.string.key_repeat_sun), mArrBoolIsRepeat[0]);
        intent.putExtra(getString(R.string.key_repeat_mon), mArrBoolIsRepeat[1]);
        intent.putExtra(getString(R.string.key_repeat_tue), mArrBoolIsRepeat[2]);
        intent.putExtra(getString(R.string.key_repeat_wed), mArrBoolIsRepeat[3]);
        intent.putExtra(getString(R.string.key_repeat_thur), mArrBoolIsRepeat[4]);
        intent.putExtra(getString(R.string.key_repeat_fri), mArrBoolIsRepeat[5]);
        intent.putExtra(getString(R.string.key_repeat_sat), mArrBoolIsRepeat[6]);

        startActivityForResult(intent, 1);
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
            case 1:     // repeat
                dealRepeat(data);
                break;
            default:
                break;
        }
    }

    void dealRepeat(Intent data) {
        String repeatString = "";
        boolean isAllChecked = true;
        mArrBoolIsRepeat[0] = data.getBooleanExtra(getString(R.string.key_repeat_sun), false);
        mArrBoolIsRepeat[1] = data.getBooleanExtra(getString(R.string.key_repeat_mon), false);
        mArrBoolIsRepeat[2] = data.getBooleanExtra(getString(R.string.key_repeat_tue), false);
        mArrBoolIsRepeat[3] = data.getBooleanExtra(getString(R.string.key_repeat_wed), false);
        mArrBoolIsRepeat[4] = data.getBooleanExtra(getString(R.string.key_repeat_thur), false);
        mArrBoolIsRepeat[5] = data.getBooleanExtra(getString(R.string.key_repeat_fri), false);
        mArrBoolIsRepeat[6] = data.getBooleanExtra(getString(R.string.key_repeat_sat), false);

        for(int i = 0; i < 7; i ++) {
            if (mArrBoolIsRepeat[i]) {
                switch(i) {
                    case 0:
                        repeatString += "Sun. ";
                        break;
                    case 1:
                        repeatString += "Mon. ";
                        break;
                    case 2:
                        repeatString += "Tues. ";
                        break;
                    case 3:
                        repeatString += "Wed. ";
                        break;
                    case 4:
                        repeatString += "Thurs. ";
                        break;
                    case 5:
                        repeatString += "Fri. ";
                        break;
                    case 6:
                        repeatString += "Sat. ";
                        break;
                    default:
                        break;
                }
            } else {
                isAllChecked = false;
            }
        }

        if (isAllChecked) {
            repeatString = "Everyday";
        }

        if (repeatString.equals("")) {
            mIsValidateRepeat = false;
        } else {
            mIsValidateRepeat = true;
        }
        mTxtRepeatResult.setText(repeatString);

    }
    void dealRingtone(Intent data) {
        Uri uri = data.getParcelableExtra(RingtoneManager.EXTRA_RINGTONE_PICKED_URI);
        mRingtoneUri = uri.toString();
        if (uri != null) {
            Ringtone ringtone = RingtoneManager.getRingtone(this, uri);
            mRingtoneTitle = ringtone.getTitle(this);
            mTxtRingtoneTitle.setText(mRingtoneTitle);
            mIsValidateSound = true;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm_create);
        ButterKnife.inject(this);

        mTxtRingtoneTitle.setText(getString(R.string.BASIC_RINGTONE));
        mRingtoneUri = getString(R.string.BASIC_RINGTONE);

        // 최기화
        Arrays.fill(mArrBoolIsRepeat, false);
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

    private AlarmManager getAlarmManager() {
        if (mAlarmManger == null) {
            mAlarmManger = (AlarmManager)getApplicationContext().getSystemService(Context.ALARM_SERVICE);
        }
        return mAlarmManger;
    }
}
