package clom.goqual.goqualswitch.BLE;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import butterknife.ButterKnife;
import butterknife.InjectView;
import clom.goqual.goqualswitch.Adapter.BluetoothDeviceViewAdapter;
import clom.goqual.goqualswitch.R;

public class DialogBluetoothLeDevice extends Activity {
    private static final String TAG = "DIALOG_BLE_DEVICE";
    private Context mContext;
    private BluetoothDeviceViewAdapter mBluetoothDeviceViewAdapter;

    @InjectView(R.id.dialog_dle_device_list)
    RecyclerView mListDevice;

    void setLayout() {
        mBluetoothDeviceViewAdapter = new BluetoothDeviceViewAdapter(mContext);
        mListDevice.setLayoutManager(new LinearLayoutManager(mContext));
        mListDevice.setAdapter(mBluetoothDeviceViewAdapter);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.inject(this);
        setContentView(R.layout.dialog_ble_device);
        mContext = getApplicationContext();

        setLayout();
    }

    @Override
    protected void onDestroy() {
        ButterKnife.reset(this);
        super.onDestroy();
    }
}
