package com.bluetooth;

import java.util.ArrayList;
import java.util.Set;
import java.util.UUID;

import com.example.androidmovimientos.R;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Toast;

public class BluetoothActivity extends Activity {
	private static final int REQUEST_ENABLE_BT = 1;
	public static final int DATA_RECEIVED = 2;
	public static final int SOCKET_CONNECTED = 3;
	public static final int SELECT_BLUETOOTH_DEVICE = 4;
	public static final UUID APP_UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
	protected BluetoothAdapter bluetoothAdapter = null;
	protected ConnectionThread bluetoothConnection = null;
	private ConnectThread connectThread;
	protected String data;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// Obtenemos el bluetooth del dispositivo.
		bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
		// vemos si el dispositivo tiene Bluetooth y si está habilitado.
		checkBluetoothEnable();
	}
	
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == REQUEST_ENABLE_BT && resultCode == RESULT_OK) {
			Toast.makeText(this, R.string.bluetooth_enable, Toast.LENGTH_SHORT).show();
		} else if (requestCode == SELECT_BLUETOOTH_DEVICE	&& resultCode == RESULT_OK) {
			BluetoothDevice device = data.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
			connectToBluetoothServer(device.getAddress());
		}
	}
	

	@Override
	protected void onDestroy() {
		super.onDestroy();
		if(connectThread != null){
			bluetoothAdapter.cancelDiscovery();
			connectThread.cancel();
			connectThread = null;
			bluetoothConnection = null;
		}
	}
	
	/**
	 * Chequeamos que el dispositivo tiene Bluetooth y que está activado. 
	 * Si no está activado, preguntamos si desea activarlo.
	 */
	private void checkBluetoothEnable(){
		if (bluetoothAdapter == null) {
		    Toast.makeText(this, "El dispositivo no soporta Bluetooth", Toast.LENGTH_LONG).show();
		}
		if(!bluetoothAdapter.isEnabled()){
			Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
			startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
		}
	}
	
	
	/**
	 * Genera una lista de los dispositivos vinculados y se la pasa a un Intent para que la muestre y maneje la selección.
	 * @param view
	 */
	public void selectBluetooth(View view){
		Set<BluetoothDevice> pairedDevices = bluetoothAdapter.getBondedDevices();;
		ArrayList<String> pairedDeviceStrings = new ArrayList<String>();
		
		if (pairedDevices.size() > 0) {
		    for (BluetoothDevice device : pairedDevices) {
		    	pairedDeviceStrings.add(device.getName() + "\n" + device.getAddress());
		    }
		}
		Intent showDevicesIntent = new Intent(this, ShowDevices.class);
		showDevicesIntent.putStringArrayListExtra("devices", pairedDeviceStrings);
		startActivityForResult(showDevicesIntent, SELECT_BLUETOOTH_DEVICE);
	}
	
	
	private void connectToBluetoothServer(String id) {
		connectThread = new ConnectThread(id, handler);
		connectThread.start();
	}
	
	public Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case SOCKET_CONNECTED: {
				bluetoothConnection = (ConnectionThread) msg.obj;
				break;
			}
			case DATA_RECEIVED: {
				data = (String) msg.obj;
			}
			default:
				break;
			}
		}
	};
	

	
	
}
