//package com.bluetooth;
//
//import java.io.IOException;
//import java.io.OutputStream;
//import java.util.ArrayList;
//import java.util.Set;
//import java.util.UUID;
//
//import android.app.Activity;
//import android.app.AlertDialog;
//import android.bluetooth.BluetoothAdapter;
//import android.bluetooth.BluetoothDevice;
//import android.bluetooth.BluetoothSocket;
//import android.content.Context;
//import android.content.DialogInterface;
//import android.content.Intent;
//import android.content.DialogInterface.OnClickListener;
//import android.os.AsyncTask;
//import android.widget.ArrayAdapter;
//import android.widget.Toast;
//
//
//public class BluetoothHelper{
//	private static final int REQUEST_ENABLE_BT = 0;
//	private ArrayAdapter<String> mArrayAdapter;
//	private BluetoothAdapter mBluetoothAdapter;
//	private BluetoothSocket btSocket;
//	private ArrayList<BluetoothDevice> btDeviceArray = new ArrayList<BluetoothDevice>();
//	Set<BluetoothDevice> pairedDevices;
//	Context context;
////	ConnectAsyncTask connectAsyncTask;
//	
//	public BluetoothHelper(Context context) {
//		this.context = context;
//		mArrayAdapter = new ArrayAdapter<String>(context, android.R.layout.simple_list_item_1);
//		mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
//		connectAsyncTask = new ConnectAsyncTask();
//
//		if (mBluetoothAdapter == null) {
//		    Toast.makeText(context, "El dispositivo no soporta Bluetooth", Toast.LENGTH_LONG).show();
//		}else{
//			if(!mBluetoothAdapter.isEnabled()){
//				Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
//				((Activity)context).startActivityForResult(enableBtIntent, 1);
//			}		
//			
//			pairedDevices = mBluetoothAdapter.getBondedDevices();
//			if (pairedDevices.size() > 0) {
//			    // Loop a través de los dispositivos vinculados.
//			    for (BluetoothDevice device : pairedDevices) {
//			        // Agrega el nombre y la dirección a un ArrayAdapter para luego mostrarlo en una ListView.
//			        mArrayAdapter.add(device.getName() + "\n" + device.getAddress());
//			        btDeviceArray.add(device);
//			    }
//			}
//		}		
//	}
//	
//	public AlertDialog.Builder getBluetoothDialog(){
//		AlertDialog.Builder dialog = new AlertDialog.Builder(context);
//		dialog.setAdapter(mArrayAdapter, new OnClickListener() {
//			
//			@Override
//			public void onClick(DialogInterface dialog, int which) {
//				BluetoothDevice device = btDeviceArray.get(which);
//				connectAsyncTask.execute(device);
//			}
//		});
//		
//		return dialog;
//	}
//	
//	public boolean sendMessage(String message){
//		System.out.println("log - sendMessage : "+message);
//		OutputStream mmOutStream = null;
//		
//		try {
//			if(btSocket == null){
//				System.out.println("log - socket null");
//				return false;
//			}
//			
//			if(btSocket.getRemoteDevice() != null){
//				mmOutStream = btSocket.getOutputStream();
//				mmOutStream.write(message.getBytes());
//				return true;
//			}else{
//				System.out.println("log - remoteDevice null");
//			}
//			
//		} catch (IOException e) { 
//			System.out.println("log - sendMessage ERROR : "+e.getMessage());				
//		}
//		return false;
//	}
//	
//	private class ConnectAsyncTask extends AsyncTask<BluetoothDevice, Integer, BluetoothSocket>{
//
//		private BluetoothSocket mmSocket;
//		private BluetoothDevice mmDevice;
//		
//		@Override
//		protected BluetoothSocket doInBackground(BluetoothDevice... device) {
//							
//			mmDevice = device[0];
//			
//			try {				
//				String mmUUID = "00001101-0000-1000-8000-00805F9B34FB";
//				mmSocket = mmDevice.createRfcommSocketToServiceRecord(UUID.fromString(mmUUID));
//				mmSocket.connect();
//				
//			} catch (Exception e) {
//				System.out.println(e.getMessage());				
//			}
//			
//			return mmSocket;
//		}
//		
//		@Override
//		protected void onPostExecute(BluetoothSocket result) {			
//			btSocket = result;
//		}
//		
//	}
//
//}
