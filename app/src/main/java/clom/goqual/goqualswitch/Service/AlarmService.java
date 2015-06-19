package clom.goqual.goqualswitch.Service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import clom.goqual.goqualswitch.R;

/**
 * Created by admin on 2015. 6. 19..
 */
public class AlarmService extends Service {
    private static final String TAG = "SERVICE_ALARM";

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.e(TAG, "GOT Service");
        String action = intent.getAction();
        if(action.equals(getString(R.string.ACTION_ALARM))) {
            String ringtoneURI = intent.getStringExtra(getString(R.string.key_alarm_ringtone));
            Log.e(TAG, "SERVICE RINGTONE : " + ringtoneURI);
            Intent klaxonIntent = new Intent(this,AlarmKlaxonService.class);
            klaxonIntent.setAction(getString(R.string.ACTION_ALARM_KLAXON));
            klaxonIntent.putExtra(getString(R.string.key_alarm_ringtone), ringtoneURI);
            startService(klaxonIntent);
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
