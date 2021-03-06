package com.chunyan.bluetoothtest2.broadcastreceiver;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.chunyan.bluetoothtest2.callback.ClientCallBack;

import java.lang.reflect.Method;

public class BlueReceiver extends BroadcastReceiver {
    private String pin = "0000";  //此处为你要连接的蓝牙设备的初始密钥，一般为1234或0000
    private static final String TAG = "mcy_Receiver";
    private ClientCallBack callBack;

    public BlueReceiver(ClientCallBack callBack) {
        this.callBack = callBack;
    }

    public void setCallBack(ClientCallBack callBack){
        this.callBack=callBack;
    }

    //广播接收器，当远程蓝牙设备被发现时，回调函数onReceiver()会被执行
    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        Log.d(TAG, "action:" + action);
        BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
        if(callBack==null){
            return;
        }
        switch (action) {
            case BluetoothAdapter.ACTION_DISCOVERY_STARTED:
                callBack.onScanStarted();
                break;
            case BluetoothAdapter.ACTION_DISCOVERY_FINISHED:
                callBack.onScanFinished();
                break;
            case BluetoothDevice.ACTION_FOUND:
                callBack.onScanning(device);
                break;
            case BluetoothDevice.ACTION_PAIRING_REQUEST:
                    callBack.onBondRequest();
                break;
            case BluetoothDevice.ACTION_BOND_STATE_CHANGED:
                switch (device.getBondState()) {
                    case BluetoothDevice.BOND_NONE:
                        callBack.onBondFail(device);
                        break;
                    case BluetoothDevice.BOND_BONDING:
                        callBack.onBonding(device);
                        break;
                    case BluetoothDevice.BOND_BONDED:
                        callBack.onBondSuccess(device);
                        break;
                }
                break;
        }
    }

}
