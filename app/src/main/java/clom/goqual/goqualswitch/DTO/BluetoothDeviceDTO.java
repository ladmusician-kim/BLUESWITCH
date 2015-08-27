package clom.goqual.goqualswitch.DTO;

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
}
