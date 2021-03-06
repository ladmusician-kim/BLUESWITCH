package clom.goqual.goqualswitch.Common;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.content.res.Configuration;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.RelativeLayout;

import butterknife.ButterKnife;
import butterknife.OnClick;
import clom.goqual.goqualswitch.Main.FragmentMain;
import clom.goqual.goqualswitch.R;
import clom.goqual.goqualswitch.SharedPreference.InfoSharedPreference;

/**
 * Created by ladmusician on 15. 8. 27..
 */
public class BaseActivity extends ActionBarActivity {
    private static final String TAG = "ACTIVITY_BASE";
    public Context mContext;
    public InfoSharedPreference mDeviceInfo;


    /*
    @InjectView(R.id.btn_alarm) Button mBtnAlarm;
    @InjectView(R.id.btn_noti) Button mBtnNoti;
    @OnClick({ R.id.btn_alarm, R.id.btn_noti })
    void onClickButton (Button btn) {
        switch(btn.getId()) {
            case R.id.btn_alarm:
                Intent alarmIntent = new Intent(getApplicationContext(), ActivityCreateAlarm.class);
                startActivity(alarmIntent);
                break;
            case R.id.btn_noti:
                Intent notiIntent = new Intent(getString(R.string.ACTION_SERVICE_NOTI));
                notiIntent.putExtra(getString(R.string.key_noti_switch_turn_on), false);

                startService(notiIntent);

                Toast.makeText(ActivityMain.this, "Notification Registered.", Toast.LENGTH_SHORT).show();
                break;
            default:
                break;
        }
    }
    */

    private Toolbar mToolbar;
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mDrawerToggle;

    @OnClick({ R.id.menu_rl_aboutus, R.id.menu_rl_help, R.id.menu_rl_newswitch, R.id.menu_rl_none, R.id.menu_rl_versioninfo})
    void onClickButton (RelativeLayout rl) {
        switch(rl.getId()) {
            case R.id.menu_rl_newswitch:
                mDrawerLayout.closeDrawers();
                break;
            case R.id.menu_rl_help:
                mDrawerLayout.closeDrawers();
                break;
            case R.id.menu_rl_aboutus:
                mDrawerLayout.closeDrawers();
                break;
            case R.id.menu_rl_versioninfo:
                mDrawerLayout.closeDrawers();
                break;
            case R.id.menu_rl_none:
                mDrawerLayout.closeDrawers();
                break;
            default:
                break;
        }
    }
    public void handleSharedPreference() {
        // sharedPreference
        if (getDeviceInfo().getValue(getString(R.string.key_panelwidth), -1) == -1
                || getDeviceInfo().getValue(getString(R.string.key_panelheight), -1) == -1) {
            DisplayMetrics metrics = new DisplayMetrics();
            getWindowManager().getDefaultDisplay().getMetrics(metrics);

            getDeviceInfo().put(getString(R.string.key_panelwidth),(metrics.widthPixels));
            getDeviceInfo().put(getString(R.string.key_panelheight),(metrics.heightPixels));
        }
    }

    public InfoSharedPreference getDeviceInfo() {
        if (mDeviceInfo == null) {
            mDeviceInfo = new InfoSharedPreference(this);
        }

        return mDeviceInfo;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = getApplicationContext();
        setContentView(R.layout.activity_base);
        ButterKnife.inject(this);
        handleSharedPreference();

        handle_menu();

        Fragment fragment = new FragmentMain();

        Bundle args = new Bundle();
        args.putInt(FragmentMain.FLAG_FRAGMENT_MAIN, FragmentMain.ARG_FRAGMENT_MAIN);
        fragment.setArguments(args);

        FragmentManager fragmentManager = getFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.container, fragment).commit();
    }

    void handle_menu () {
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);

        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, R.string.app_name, R.string.app_name);
        mDrawerLayout.setDrawerListener(mDrawerToggle);
    }

    public static boolean isContainedInNotificationListeners(Context ctx) {
        String enabledListeners = Settings.Secure.getString(ctx.getContentResolver(), "enabled_notification_listeners");
        return !TextUtils.isEmpty(enabledListeners) && enabledListeners.contains(ctx.getPackageName());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        ButterKnife.reset(this);
        super.onDestroy();
    }
}
