package clom.goqual.goqualswitch.Adapter;

import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import clom.goqual.goqualswitch.DTO.BluetoothDeviceDTO;
import clom.goqual.goqualswitch.R;
import clom.goqual.goqualswitch.ViewHolder.BluetoothDeviceViewHolder;

/**
 * Created by ladmusician on 15. 8. 26..
 */
public class BluetoothDeviceViewAdapter extends RecyclerView.Adapter<BluetoothDeviceViewHolder>{
    private Context mContext;
    private ArrayList<BluetoothDeviceDTO> mListBLEDevice;

    public void addDevice(BluetoothDevice device) {
        boolean isExistDevice = false;
        for(BluetoothDeviceDTO each : mListBLEDevice) {
            if(each.getMacAddress().equals(device.getAddress()))
                isExistDevice = true;
        }

        if (!isExistDevice) {
            mListBLEDevice.add(new BluetoothDeviceDTO(device.getAddress(), device.getName()));
        }
    }

    @Override
    public BluetoothDeviceViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View card_view = LayoutInflater.from(mContext).inflate(R.layout.card_bluetooth, viewGroup, false);
        return new BluetoothDeviceViewHolder(card_view);
    }

    @Override
    public void onBindViewHolder(BluetoothDeviceViewHolder viewHolder, int position) {
        BluetoothDeviceDTO item = mListBLEDevice.get(position);
        viewHolder.mTxtBLEName.setText(item.getBLEName());
        viewHolder.mTxtBLEMacAddress.setText(item.getMacAddress());
    }

    @Override
    public int getItemCount() {return mListBLEDevice.size();}

    // constructer
    public BluetoothDeviceViewAdapter(Context ctx) {
        mContext = ctx;
        mListBLEDevice = new ArrayList<>();
    }
}
