package clom.goqual.goqualswitch.DTO;

import android.bluetooth.BluetoothDevice;

/**
 * Created by ladmusician on 15. 8. 26..
 */
public class BluetoothDeviceDTO {
    private String mMacAddress;
    private String mBLEName;

    public String getBLEName () { return this.mBLEName; }
    public String getMacAddress () { return this.mMacAddress; }

    public BluetoothDeviceDTO(String address, String name) {
        this.mBLEName = name;
        this.mMacAddress = address;
    }

    public BluetoothDeviceDTO(BluetoothDevice device) {
        this.mBLEName = device.getName();
        this.mMacAddress = device.getAddress();
    }
}
