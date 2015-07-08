package clom.goqual.goqualswitch.Service;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import clom.goqual.goqualswitch.R;

/**
 * Created by ladmusician on 15. 7. 8..
 */
public class NotificationTurnOnService extends Service {
    private static final String TAG = "SERVICE_NOTI_TURNON";
    private NotificationManager mNotiMng;
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
            String action = intent.getAction();
            if (action.equals(getString(R.string.ACTION_NOTI_TURN_ON))) {
                Log.e(TAG, "GOT TURN ON SERVICE");
                Intent notiIntent= new Intent(this, NotificationService.class);
                notiIntent.setAction(getString(R.string.ACTION_NOTI));
                notiIntent.putExtra(getString(R.string.key_noti_switch_turn_on), true);
                PendingIntent pendingIntent = PendingIntent.getService(this, 0, notiIntent, PendingIntent.FLAG_UPDATE_CURRENT);
                Notification notification = new Notification(android.R.drawable.ic_lock_lock, getString(R.string.noti_ticket),System.currentTimeMillis());
                notification.flags = Notification.FLAG_AUTO_CANCEL | Notification.FLAG_ONGOING_EVENT;
                notification.setLatestEventInfo(this,getString(R.string.noti_title), getString(R.string.noti_turn_off), pendingIntent);
                mNotiMng = getNotiMng();

            mNotiMng.notify(12345, notification);
        }
        return super.onStartCommand(intent, flags, startId);
    }

    private NotificationManager getNotiMng() {
        if (mNotiMng != null) {
            return mNotiMng;
        }
        return (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }
}
