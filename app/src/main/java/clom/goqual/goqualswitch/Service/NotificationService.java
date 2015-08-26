package clom.goqual.goqualswitch.Service;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import clom.goqual.goqualswitch.R;

/**
 * Created by ladmusician on 15. 7. 6..
 */
public class NotificationService extends Service {
    private static final String TAG = "SERVICE_NOTIFICATION";
    private NotificationManager mNotiMng = null;
    private Context mContext = null;
    private int mNotiId = 123;
    private boolean isTurnOn = false;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        mContext = getApplicationContext();
        String action = intent.getAction();

        if (action.equals(getString(R.string.ACTION_SERVICE_NOTI))) {
            isTurnOn = intent.getBooleanExtra(getString(R.string.key_noti_switch_turn_on), false);

            Log.e(TAG, "GOT SERVICE");

            makeNotification();
        }

        return super.onStartCommand(intent, flags, startId);
    }

    private void makeNotification() {
        mNotiMng = getNotiMng();

        Intent passIntent = new Intent(this, NotificationService.class);
        passIntent.setAction(getString(R.string.ACTION_SERVICE_NOTI));
        passIntent.putExtra(getString(R.string.key_noti_switch_turn_on), !isTurnOn);
        PendingIntent pendingIntent = PendingIntent.getService(this, 0, passIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        Notification.Builder mBuilder = new Notification.Builder(this);
        mBuilder.setSmallIcon(R.drawable.icon_blueswitch);
        mBuilder.setWhen(System.currentTimeMillis());


        mBuilder.setTicker(getString(R.string.noti_ticket));
        mBuilder.setContentTitle(getString(R.string.noti_title));

        if (isTurnOn) {
            mBuilder.setContentText(getString(R.string.noti_turn_off));
        } else {
            mBuilder.setContentText(getString(R.string.noti_turn_on));
        }

        //mBuilder.setNumber(10);
        //mBuilder.setDefaults(Notification.DEFAULT_SOUND | Notification.DEFAULT_VIBRATE);
        mBuilder.setContentIntent(pendingIntent);
        mBuilder.setAutoCancel(false);
        mBuilder.setOngoing(true);

        mBuilder.setPriority(Notification.PRIORITY_MAX);

        mNotiMng.notify(mNotiId, mBuilder.build());
    }

    private NotificationManager getNotiMng() {
        if (mNotiMng != null) {
            return mNotiMng;
        }
        return (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
