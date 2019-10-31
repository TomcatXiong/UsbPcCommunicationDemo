package com.yx.usb2pc;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.hardware.usb.UsbAccessory;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbManager;
import android.util.Log;

/**
 * UsbConnectReceiver
 * 主要监听ACTION_USB_STATE,启动关闭socket服务
 *
 * @author yx
 * @date 2019/10/30 20:39
 */
public class UsbConnectReceiver extends BroadcastReceiver {
    private static final String TAG = "UsbConnectReceiver";

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.hasExtra(UsbManager.EXTRA_PERMISSION_GRANTED)) {
            boolean permissionGranted =
                    intent.getBooleanExtra(UsbManager.EXTRA_PERMISSION_GRANTED, false);
            Log.v(TAG, "permissionGranted : " + permissionGranted);
        }
        String action = intent.getAction();
        Log.v(TAG, "action:" + action);
        switch (action) {
            case UsbManager.ACTION_USB_ACCESSORY_ATTACHED:
            case UsbManager.ACTION_USB_ACCESSORY_DETACHED:
                UsbAccessory accessory = intent.getParcelableExtra(UsbManager.EXTRA_ACCESSORY);
                Log.v(TAG, accessory.toString());
                break;
            case UsbManager.ACTION_USB_DEVICE_ATTACHED:
                Log.v(TAG, UsbManager.ACTION_USB_DEVICE_ATTACHED);
                break;
            case UsbManager.ACTION_USB_DEVICE_DETACHED:
                UsbDevice device = intent.getParcelableExtra(UsbManager.EXTRA_DEVICE);
                Log.v(TAG, device.toString());
                break;
            case UsbManager.ACTION_USB_STATE:
                boolean connected = intent.getBooleanExtra(UsbManager.USB_CONNECTED, false);
                boolean functionAdb = intent.getBooleanExtra(UsbManager.USB_FUNCTION_ADB, false);
                Log.v(TAG, "connected:" + connected + " function_adb : " + functionAdb);
                // 断开停止服务
                if (!connected) {
                    SocketServer.getSocketServerInstant().stopServer();
                } else {
                    // 开启adb调试功能，开启服务
                    if (functionAdb) {
                        SocketServer.getSocketServerInstant().startServer();
                    }
                }
                break;
            default:
                break;
        }
    }
}
