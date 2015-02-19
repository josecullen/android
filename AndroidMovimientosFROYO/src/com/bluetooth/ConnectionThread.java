package com.bluetooth;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import com.example.androidmovimientos.ControlRemoto;

import android.bluetooth.BluetoothSocket;
import android.os.Handler;

public class ConnectionThread extends Thread {
	BluetoothSocket bluetoothSocket;
	private final Handler handler;
	private InputStream inStream;
	private OutputStream outStream;

	ConnectionThread(BluetoothSocket socket, Handler handler){
		super();
		bluetoothSocket = socket;
		this.handler = handler;
		try {
			inStream = bluetoothSocket.getInputStream();
			outStream = bluetoothSocket.getOutputStream();
		} catch (IOException e) {
		}
	}

	@Override
	public void run() {
			byte[] buffer = new byte[1024];
			int bytes;
		while (true) {
			try {
				bytes = inStream.read(buffer);
				String data = new String(buffer, 0, bytes);
				handler.obtainMessage(
						ControlRemoto.DATA_RECEIVED,
						data).sendToTarget();
			} catch (IOException e) {
				break;
			}
		}
	}

	public void write(byte[] bytes) {
		try {
			outStream.write(bytes);
		} catch (IOException e) {
			
		}
	}
}
