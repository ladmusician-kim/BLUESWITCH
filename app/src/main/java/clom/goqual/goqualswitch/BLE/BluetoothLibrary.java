package clom.goqual.goqualswitch.BLE;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;

import clom.goqual.goqualswitch.Adapter.BluetoothDeviceViewAdapter;

/**
 * Created by ladmusician on 15. 8. 26..
 */
public class BluetoothLibrary extends Activity {
    private Context mContext;
    private BluetoothManager mBluetoothMng;
    private BluetoothAdapter mBluetoothAdapter;
    private BluetoothDeviceViewAdapter mBluetoothDeviceViewAdapter;

    private ConnectionState mConnectionState = ConnectionState.isNull;

    private static final long SCAN_PERIOD = 10000;

    public boolean initiate() {
        // Use this check to determine whether BLE is supported on the device.
        if (!mContext.getPackageManager().hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE)) {
            return false;
        }

        // Initializes a Bluetooth adapter.
        mBluetoothMng = getBluetoothMng();
        mBluetoothAdapter = getBluetoothApt();

        if (mBluetoothAdapter == null) {
            return false;
        }
        return true;
    }

    public void scanLeDevice() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                mBluetoothAdapter.stopLeScan(mLeScanCallback);
            }
        }, SCAN_PERIOD);

        mBluetoothAdapter.startLeScan(mLeScanCallback);
    }

    // Device scan callback.
    private BluetoothAdapter.LeScanCallback mLeScanCallback = new BluetoothAdapter.LeScanCallback() {
        @Override
        public void onLeScan(final BluetoothDevice device, int rssi, byte[] scanRecord) {
            mBluetoothDeviceViewAdapter = getBluetoothDevicViewAdapter();
            ((Activity) mContext).runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    mBluetoothDeviceViewAdapter.addDevice(device);
                    mBluetoothDeviceViewAdapter.notifyDataSetChanged();
                }
            });
        }
    };

    private BluetoothManager getBluetoothMng() {
        if (mBluetoothAdapter != null) {
            return mBluetoothMng;
        }

        return (BluetoothManager) mContext.getSystemService(Context.BLUETOOTH_SERVICE);
    }
    private BluetoothAdapter getBluetoothApt() {
        if (mBluetoothAdapter != null) {
            return mBluetoothAdapter;
        }

        return mBluetoothMng.getAdapter();
    }
    private BluetoothDeviceViewAdapter getBluetoothDevicViewAdapter() {
        if (mBluetoothDeviceViewAdapter != null) {
            return mBluetoothDeviceViewAdapter;
        }

        return new BluetoothDeviceViewAdapter(mContext);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = getApplicationContext();
    }
}
