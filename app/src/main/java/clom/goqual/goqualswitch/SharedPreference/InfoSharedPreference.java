package clom.goqual.goqualswitch.SharedPreference;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by admin on 2015. 6. 18..
 */
public class InfoSharedPreference {
    private final String PREF_NAME = "com.goqual.blueswitchinfo";
    private Context mContext;

    public InfoSharedPreference (Context context) {
        mContext = context;
    }

    public void put(String key, int value) {
        SharedPreferences pref = mContext.getSharedPreferences(PREF_NAME, Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();

        editor.putInt(key, value);
        editor.commit();
    }

    public int getValue(String key, int dftInt) {
        SharedPreferences pref = mContext.getSharedPreferences(PREF_NAME, Activity.MODE_PRIVATE);

        try {
            return pref.getInt(key, dftInt);
        }
        catch(Exception e) {
            return dftInt;
        }
    }
}
