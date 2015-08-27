package clom.goqual.goqualswitch.ViewHolder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import clom.goqual.goqualswitch.R;

/**
 * Created by ladmusician on 15. 8. 26..
 */
public class BluetoothDeviceViewHolder extends RecyclerView.ViewHolder {
    public TextView mTxtBLEName;
    public TextView mTxtBLEMacAddress;

    public BluetoothDeviceViewHolder(View itemView) {
        super(itemView);

        mTxtBLEMacAddress = (TextView) itemView.findViewById(R.id.card_bt_txt_mac);
        mTxtBLEName = (TextView) itemView.findViewById(R.id.card_bt_txt_name);
    }
}
