package clom.goqual.goqualswitch.Service;

import android.app.NotificationManager;
import android.app.Service;
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
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        String action = intent.getAction();
        Intent notiIntent = null;
        if (action.equals(getString(R.string.ACTION_NOTI))) {
            boolean isTurnOn = intent.getBooleanExtra(getString(R.string.key_noti_switch_turn_on), false);
            Log.e(TAG, "GOT SERVICE");
            Log.d(TAG, "SWITCH STATE : " + isTurnOn);
            if (isTurnOn) {
                notiIntent = new Intent(getString(R.string.ACTION_NOTI_TURN_OFF));
                startService(notiIntent);
            } else {
                notiIntent = new Intent(getString(R.string.ACTION_NOTI_TURN_ON));
                startService(notiIntent);
            }
        }

        return super.onStartCommand(intent, flags, startId);
    }



    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
