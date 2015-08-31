package clom.goqual.goqualswitch.Adapter;

import android.app.Activity;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import clom.goqual.goqualswitch.DTO.BluetoothDeviceDTO;
import clom.goqual.goqualswitch.R;
import clom.goqual.goqualswitch.ViewHolder.BluetoothDeviceViewHolder;

/**
 * Created by ladmusician on 15. 8. 31..
 */
public class LeDeviceListAdapter extends BaseAdapter {
    private ArrayList<BluetoothDeviceDTO> mLeDevices;
    private LayoutInflater mInflator;
    private Context mContext;

    public LeDeviceListAdapter(Context ctx) {
        super();
        mLeDevices = new ArrayList<BluetoothDeviceDTO>();
        mInflator =  ((Activity) mContext).getLayoutInflater();
    }

    public void addDevice(BluetoothDevice device) {
        BluetoothDeviceDTO dto = new BluetoothDeviceDTO(device);

        if (!mLeDevices.contains(dto)) {
            mLeDevices.add(dto);
        }
    }

    public BluetoothDeviceDTO getDevice(int position) {
        return mLeDevices.get(position);
    }

    public void clear() {
        mLeDevices.clear();
    }

    @Override
    public int getCount() {
        return mLeDevices.size();
    }

    @Override
    public Object getItem(int i) {
        return mLeDevices.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View convertView, ViewGroup viewGroup) {
        BluetoothDeviceViewHolder viewHolder;

        if (convertView == null) {
            convertView = mInflator.inflate(R.layout.list_le_device, null);
            viewHolder = new BluetoothDeviceViewHolder();

            viewHolder.mTxtBLEMacAddress = (TextView) convertView.findViewById(R.id.list_bt_txt_mac);
            viewHolder.mTxtBLEName= (TextView) convertView.findViewById(R.id.list_bt_txt_name);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (BluetoothDeviceViewHolder) convertView.getTag();
        }

        BluetoothDeviceDTO device = mLeDevices.get(i);

        final String deviceName = device.getBLEName();

        if (deviceName != null && deviceName.length() > 0)
            viewHolder.mTxtBLEName.setText(deviceName);
        else
            viewHolder.mTxtBLEName.setText(R.string.UNKNOWN_DEVICE);

        viewHolder.mTxtBLEMacAddress.setText(device.getMacAddress());

        return convertView;
    }
}
