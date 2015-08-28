package clom.goqual.goqualswitch.BLE;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import clom.goqual.goqualswitch.Adapter.BluetoothDeviceViewAdapter;
import clom.goqual.goqualswitch.BLE.Service.BluetoothLeService;
import clom.goqual.goqualswitch.R;

/**
 * Created by ladmusician on 15. 8. 26..
 */
public class BluetoothLibrary extends Fragment {
    private static final String TAG = "FRAGMENT_BLUETOOTHLIB";
    private Activity mParentActivity;
    private Context mContext;
    private BluetoothManager mBluetoothMng;
    private BluetoothAdapter mBluetoothAdapter;
    private BluetoothDeviceViewAdapter mBluetoothDeviceViewAdapter;
    private BluetoothLeService mBluetoothLeService;

    private ConnectionState mConnectionState = ConnectionState.isNull;

    private static final long SCAN_PERIOD = 10000;

    public void onCreateProcess() {
        if(!initiate())
        {
            Toast.makeText(mContext, R.string.error_bluetooth_not_supported,
                    Toast.LENGTH_SHORT).show();
            ((Activity) mContext).finish();
        }

        Intent gattServiceIntent = new Intent(mContext, BluetoothLeService.class);
        mContext.bindService(gattServiceIntent, mServiceConnection, Context.BIND_AUTO_CREATE);
    }

    public boolean initiate() {
        // Use this check to determine whether BLE is supported on the device.
        if (!mContext.getPackageManager().hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE))
            return false;

        // Initializes a Bluetooth adapter.
        mBluetoothMng = getBluetoothMng();
        mBluetoothAdapter = getBluetoothApt();

        if (mBluetoothAdapter == null) {
            return false;
        }
        return true;
    }

    // Code to manage Service lifecycle.
    private final ServiceConnection mServiceConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName componentName, IBinder service) {
            System.out.println("mServiceConnection onServiceConnected");
            mBluetoothLeService = ((BluetoothLeService.LocalBinder) service).getService();
            if (!mBluetoothLeService.initialize()) {
                Log.e(TAG, "Unable to initialize Bluetooth");
                ((Activity) mContext).finish();
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            System.out.println("mServiceConnection onServiceDisconnected");
            mBluetoothLeService = null;
        }
    };





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
    /*
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = getActivity().getApplicationContext();
    }
    */

    @Override
    public void onAttach(Activity activity) {
        mParentActivity = activity;
        super.onAttach(activity);
    }
}
