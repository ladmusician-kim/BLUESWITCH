package clom.goqual.goqualswitch.Service;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.os.IBinder;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import clom.goqual.goqualswitch.R;
import clom.goqual.goqualswitch.SharedPreference.InfoSharedPreference;

/**
 * Created by admin on 2015. 6. 19..
 */
public class AlarmViewService extends Service {
    private static final String TAG = "SERVICE_ALARM_VIEW";
    private TextView mTxtMain;
    private ImageButton mImgBtnCancel;
    private InfoSharedPreference mDeviceInfo;
    private WindowManager mWindowManager;
    private int panelWidth;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        String action = intent.getAction();
        if(action.equals(getString(R.string.ACTION_ALARM_VIEW))) {
            panelWidth = getDeviceInfo().getValue(getString(R.string.key_panelwidth), -1);
            mTxtMain = new TextView(this);
            mTxtMain.setBackgroundResource(R.drawable.common_img_splash);
            mTxtMain.setWidth(panelWidth);

            WindowManager.LayoutParams mainImageParams = new WindowManager.LayoutParams(
                    WindowManager.LayoutParams.MATCH_PARENT,
                    WindowManager.LayoutParams.MATCH_PARENT,
                    WindowManager.LayoutParams.TYPE_PHONE,
                    WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                    PixelFormat.TRANSLUCENT);

            mainImageParams.x = 0;
            mainImageParams.y = 0;

            mWindowManager = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
            mWindowManager.addView(mTxtMain, mainImageParams);

            mImgBtnCancel = new ImageButton(this);
            mImgBtnCancel.setImageResource(R.drawable.common_img_check);
            mImgBtnCancel.setBackgroundColor(Color.TRANSPARENT);
            mImgBtnCancel.setScaleType(ImageView.ScaleType.FIT_CENTER);
            mImgBtnCancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.e(TAG, "CLICK");
                    Intent klaxonIntent = new Intent(AlarmViewService.this, AlarmKlaxonService.class);
                    stopService(klaxonIntent);

                    Intent viewIntent = new Intent(AlarmViewService.this, AlarmViewService.class);
                    stopService(viewIntent);
                    stopSelf();
                }
            });

            WindowManager.LayoutParams cancelBtnParams = new WindowManager.LayoutParams(
                    WindowManager.LayoutParams.WRAP_CONTENT,
                    WindowManager.LayoutParams.WRAP_CONTENT,
                    WindowManager.LayoutParams.TYPE_PHONE,
                    WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                    PixelFormat.TRANSLUCENT);
            cancelBtnParams.gravity = Gravity.LEFT | Gravity.TOP;
            cancelBtnParams.width = panelWidth / 6;
            cancelBtnParams.height = panelWidth / 6;
            cancelBtnParams.x = 0;
            cancelBtnParams.y = 0;

            mWindowManager.addView(mImgBtnCancel, cancelBtnParams);
        }
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public void onDestroy() {
        if(mTxtMain != null) mWindowManager.removeView(mTxtMain);
        if(mImgBtnCancel != null) mWindowManager.removeView(mImgBtnCancel);
        super.onDestroy();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    public InfoSharedPreference getDeviceInfo() {
        if (mDeviceInfo == null) {
            mDeviceInfo = new InfoSharedPreference(this);
        }

        return mDeviceInfo;
    }
}
