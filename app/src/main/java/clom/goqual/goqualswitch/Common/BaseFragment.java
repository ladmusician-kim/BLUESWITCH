package clom.goqual.goqualswitch.Common;

import android.app.Fragment;
import android.util.DisplayMetrics;

import clom.goqual.goqualswitch.R;
import clom.goqual.goqualswitch.SharedPreference.InfoSharedPreference;

/**
 * Created by ladmusician on 15. 8. 27..
 */
public class BaseFragment extends Fragment {
    public InfoSharedPreference mDeviceInfo;

    public void handleSharedPreference() {
        // sharedPreference
        if (getDeviceInfo().getValue(getString(R.string.key_panelwidth), -1) == -1
                || getDeviceInfo().getValue(getString(R.string.key_panelheight), -1) == -1) {
            DisplayMetrics metrics = new DisplayMetrics();
            getActivity().getWindowManager().getDefaultDisplay().getMetrics(metrics);

            getDeviceInfo().put(getString(R.string.key_panelwidth),(metrics.widthPixels));
            getDeviceInfo().put(getString(R.string.key_panelheight),(metrics.heightPixels));
        }
    }

    public InfoSharedPreference getDeviceInfo() {
        if (mDeviceInfo == null) {
            mDeviceInfo = new InfoSharedPreference(getActivity().getApplicationContext());
        }

        return mDeviceInfo;
    }
}
