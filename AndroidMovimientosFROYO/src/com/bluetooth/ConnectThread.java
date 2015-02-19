package com.bluetooth;

import java.io.IOException;

import com.example.androidmovimientos.ControlRemoto;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.os.Handler;

public class ConnectThread extends Thread {
	private BluetoothSocket bluetoothSocket;
	private final BluetoothDevice bluetoothDevice;
	private final BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
	private final Handler handler;

	public ConnectThread(String deviceID, Handler handler) {
		bluetoothDevice = bluetoothAdapter.getRemoteDevice(deviceID);
		this.handler = handler;
		try {
			bluetoothSocket = bluetoothDevice.createRfcommSocketToServiceRecord(BluetoothActivity.APP_UUID);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void run() {
		bluetoothAdapter.cancelDiscovery();
		try {
			bluetoothSocket.connect();
			manageConnectedSocket();
		} catch (IOException connectException) {
			try {
				bluetoothSocket.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	private void manageConnectedSocket() {
		ConnectionThread conn = new ConnectionThread(bluetoothSocket, handler);
		handler.obtainMessage(BluetoothActivity.SOCKET_CONNECTED, conn).sendToTarget();
		conn.start();
	}
	
	public void cancel() {
		try {
			bluetoothSocket.close();
		} catch (IOException e) {
		}
	}

}
