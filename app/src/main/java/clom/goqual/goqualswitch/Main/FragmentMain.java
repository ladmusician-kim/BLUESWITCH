package clom.goqual.goqualswitch.Main;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.RelativeLayout;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import clom.goqual.goqualswitch.Common.BaseFragment;
import clom.goqual.goqualswitch.R;

/**
 * Created by ladmusician on 15. 8. 27..
 */
public class FragmentMain extends BaseFragment {
    private static final String TAG = "FRAGMENT_MAIN";
    public static final String FLAG_FRAGMENT_MAIN = "FLAG_FRAGMENT_MAIN";
    public static final int ARG_FRAGMENT_MAIN = 0;

    private RelativeLayout.LayoutParams mParamBtnFindSwitch;

    @InjectView(R.id.tutorial_btn_findswitch)
    ImageButton mBtnFindSwitch;
    @OnClick({ R.id.tutorial_btn_findswitch})
    void onClickButton (ImageButton btn) {
        switch (btn.getId()) {
            case R.id.tutorial_btn_findswitch:
                Log.e(TAG, "check");
                break;
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main, container, false);
        ButterKnife.inject(this, view);
        handleSharedPreference();

        setLayout();

        return view;
    }

    public void setLayout() {
        int panelWidth = mDeviceInfo.getValue(getString(R.string.key_panelwidth), -1);
        int panelHeight = mDeviceInfo.getValue(getString(R.string.key_panelheight), -1);

        // find switch btn 크기 조정;
        int btnFindSwitchHeight = (panelHeight) * 130 / 1920;
        int btnFindSwitchWidth = (int) (btnFindSwitchHeight * 4.84);
        mParamBtnFindSwitch = new RelativeLayout.LayoutParams(btnFindSwitchWidth, btnFindSwitchHeight);
        mParamBtnFindSwitch.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, RelativeLayout.TRUE);
        mParamBtnFindSwitch.setMargins(0, 0, 0, 50);
        mParamBtnFindSwitch.addRule(RelativeLayout.CENTER_HORIZONTAL, RelativeLayout.TRUE);

        mBtnFindSwitch.setLayoutParams(mParamBtnFindSwitch);
    }
}
