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

import clom.goqual.goqualswitch.Adapter.LeDeviceListAdapter;
import clom.goqual.goqualswitch.BLE.Service.BluetoothLeService;
import clom.goqual.goqualswitch.DTO.BluetoothDeviceDTO;
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
    private BluetoothLeService mBluetoothLeService;
    private AlertDialog mDiaglogBloothLeDevice;
    private LeDeviceListAdapter mLeDeviceListAdapter = null;

    // state
    private boolean mScanning = false;

    // blueswitch
    private String mBSName;
    private String mBSAddress;

    private ConnectionState mConnectionState = ConnectionState.isNull;

    private static final long SCAN_PERIOD = 10000;

    public void onCreateProcess() {
        if (!initiate()) {
            Toast.makeText(mContext, R.string.error_bluetooth_not_supported,
                    Toast.LENGTH_SHORT).show();
            ((Activity) mContext).finish();
        }

        Intent gattServiceIntent = new Intent(mContext, BluetoothLeService.class);
        mContext.bindService(gattServiceIntent, mServiceConnection, Context.BIND_AUTO_CREATE);

        handle_dialog();
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
    public final ServiceConnection mServiceConnection = new ServiceConnection() {
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

    public void scanLeDevice(final boolean enable) {
        if (enable) {
            if (mLeDeviceListAdapter != null) {
                mLeDeviceListAdapter.clear();
                mLeDeviceListAdapter.notifyDataSetChanged();
            }
            if(!mScanning) {
                mScanning = true;
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mBluetoothAdapter.stopLeScan(mLeScanCallback);
                        mScanning = false;
                    }
                }, SCAN_PERIOD);


                mBluetoothAdapter.startLeScan(mLeScanCallback);
            }

        } else {
            if(mScanning)
            {
                mScanning = false;
                mBluetoothAdapter.stopLeScan(mLeScanCallback);
            }
        }

    }

    // Device scan callback.
    private BluetoothAdapter.LeScanCallback mLeScanCallback = new BluetoothAdapter.LeScanCallback() {
        @Override
        public void onLeScan(final BluetoothDevice device, int rssi, byte[] scanRecord) {
            ((Activity) mContext).runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    mLeDeviceListAdapter.addDevice(device);
                    mLeDeviceListAdapter.notifyDataSetChanged();
                }
            });
        }
    };

    private void handle_dialog() {
        // Initializes and show the scan Device Dialog
        mLeDeviceListAdapter = new LeDeviceListAdapter(mContext);
        mDiaglogBloothLeDevice = new AlertDialog.Builder(mContext)
                .setTitle(getString(R.string.DIALOG_FIND_BLUESWITCH))
                .setAdapter(mLeDeviceListAdapter, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int position) {
                        handle_dialog_item_click(position);
                    }
                })
                .setOnCancelListener(new DialogInterface.OnCancelListener() {

                    @Override
                    public void onCancel(DialogInterface arg0) {
                        mConnectionState = ConnectionState.isToScan;
                        //onConectionStateChange(mConnectionState);
                        mDiaglogBloothLeDevice.dismiss();

                        scanLeDevice(false);
                    }
                }).create();
    }

    private void handle_dialog_item_click (int position) {
        final BluetoothDeviceDTO device = mLeDeviceListAdapter.getDevice(position);
        if (device == null) return;

        scanLeDevice(false);

        mBSName = device.getBLEName();
        mBSAddress = device.getMacAddress();

        if (mBSName.equals("No Device Available") && mBSAddress.equals("No Address Available")) {
            mConnectionState = ConnectionState.isToScan;
            //onConectionStateChange(mConnectionState);
        } else {
            if (mBluetoothLeService.connect(mBSAddress)) {
                Log.e(TAG, "Connect request success");
                mConnectionState = ConnectionState.isConnecting;
                //onConectionStateChange(mConnectionState);
                new Handler().postDelayed(mConnectingOverTimeRunnable, 10000);
            } else {
                Log.d(TAG, "Connect request fail");
                mConnectionState = ConnectionState.isToScan;
                //onConectionStateChange(mConnectionState);
            }
        }
    }
    private Runnable mConnectingOverTimeRunnable=new Runnable(){
        @Override
        public void run() {
            if(mConnectionState==ConnectionState.isConnecting)
                mConnectionState=ConnectionState.isToScan;
            //onConectionStateChange(mConnectionState);
            mBluetoothLeService.close();
        }};

    private Runnable mDisonnectingOverTimeRunnable=new Runnable(){
        @Override
        public void run() {
            if(mConnectionState==ConnectionState.isDisconnecting)
                mConnectionState=ConnectionState.isToScan;
            //onConectionStateChange(mConnectionState);
            mBluetoothLeService.close();
        }};


    @Override
    public void onAttach(Activity activity) {
        mParentActivity = activity;
        super.onAttach(activity);
    }
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
}
