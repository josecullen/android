package com.example.androidmovimientos;

import java.io.IOException;
import java.io.OutputStream;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Set;
import java.util.UUID;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnClickListener;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.ParcelUuid;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import com.sensor.Action;
import com.sensor.SensorHelper;

public class MainActivity extends Activity {
	private static final int REQUEST_ENABLE_BT = 1;
	SensorHelper sensorHelper;
	SeekBar seekVisualizacion;
	
	
	ArrayAdapter<String> mArrayAdapter;
	BluetoothAdapter mBluetoothAdapter;
	BluetoothSocket btSocket;
	ArrayList<BluetoothDevice> btDeviceArray = new ArrayList<BluetoothDevice>();
	Set<BluetoothDevice> pairedDevices;
	ConnectAsyncTask connectAsyncTask;
	
	float 
		leftArrowThreshold = -0.5f, 
		rightArrowThreshold = 0.5f, 
		forwardArrowThreshold = 0.5f; 
	TextView 
		txtReposo, 
		txtActividad,
		txtReposoX,
		txtReposoY,
		txtReposoZ,
		txtMovimientoX,
		txtMovimientoY,
		txtMovimientoZ,
		txtVelocity;
	EditText 
		txtThreshold, 
		txtMillis;
	
	ImageButton 
		arrowLeft,
		arrowForward,
		arrowRight;
	int count = 1;

	DecimalFormat df = new DecimalFormat("##.#");
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.second_activity);	
		
		mArrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1);
		mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
		connectAsyncTask = new ConnectAsyncTask();
		
		if (mBluetoothAdapter == null) { 
		    Toast.makeText(this, "El dispositivo no soporta Bluetooth", Toast.LENGTH_LONG).show();
		}else{
			if(!mBluetoothAdapter.isEnabled()){
				Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
				startActivityForResult(enableBtIntent, 1);
			}		
			
			pairedDevices = mBluetoothAdapter.getBondedDevices();
			if (pairedDevices.size() > 0) {
			    // Loop a través de los dispositivos vinculados.
			    for (BluetoothDevice device : pairedDevices) {
			        // Agrega el nombre y la dirección a un ArrayAdapter para luego mostrarlo en una ListView.
			        mArrayAdapter.add(device.getName() + "\n" + device.getAddress());
			        btDeviceArray.add(device);
			    }
			}
		}		
		
		txtReposoX = (TextView)findViewById(R.id.txtReposoX);
		txtReposoY = (TextView)findViewById(R.id.txtReposoY);
		txtReposoZ = (TextView)findViewById(R.id.txtReposoZ);
		txtMovimientoX = (TextView)findViewById(R.id.txtMovimientoX);
		txtMovimientoY = (TextView)findViewById(R.id.txtMovimientoY);
		txtMovimientoZ =  (TextView)findViewById(R.id.txtMovimientoZ);
		txtVelocity =  (TextView)findViewById(R.id.txtVelocity);

		txtReposoX.setText("version 0.0.5");
		
		txtMillis = (EditText)findViewById(R.id.txtMillis);
		
		arrowForward = (ImageButton)findViewById(R.id.arrowForward);
		arrowLeft = (ImageButton)findViewById(R.id.arrowLeft);
		arrowRight = (ImageButton)findViewById(R.id.arrowRight);		
		
		seekVisualizacion = (SeekBar)findViewById(R.id.seekVisualizacion);		
		seekVisualizacion.setProgress(50);

		sensorHelper = new SensorHelper((SensorManager) getSystemService(Context.SENSOR_SERVICE));
		sensorHelper
			.setMillis(200)
			.setSensor(Sensor.TYPE_ACCELEROMETER)
			.setAction(new Action() {				
				@Override
				public void action() {
					txtMovimientoX.setText(df.format(sensorHelper.getX()));
					txtMovimientoY.setText(df.format(sensorHelper.getY()));
					txtMovimientoZ.setText(df.format(sensorHelper.getZ()));
				
					seekVisualizacion.setProgress((int) (sensorHelper.getY() * 10 + 50));
					
					float velocity = sensorHelper.getX() * -10;
					if(velocity < 0 && velocity > - 10){
						velocity = 0.0f;
					}else{
						if(velocity < 0){
							velocity = velocity  + 10;
						}
						txtVelocity.setText(df.format(velocity));
					}			
					
					txtReposoX.setText(df.format(sensorHelper.filter.filterX));
					txtReposoY.setText(df.format(sensorHelper.filter.filterY));
					txtReposoZ.setText(df.format(sensorHelper.filter.filterZ));
							
					setArrows();
					sendMessage(
							"x"+df.format(sensorHelper.getX())+","+
							"y"+df.format(sensorHelper.getY())+","+
							"z"+df.format(sensorHelper.getZ())+"_"											
					);

				}
				
				@SuppressWarnings("deprecation") @SuppressLint("NewApi") 
				void setArrows(){
					if(Build.VERSION.SDK_INT == Build.VERSION_CODES.FROYO){
						if(sensorHelper.getY() < leftArrowThreshold)arrowLeft.setAlpha(255);
						else arrowLeft.setAlpha(100);
						if(sensorHelper.getY() > rightArrowThreshold) arrowRight.setAlpha(255);
						else arrowRight.setAlpha(100);
						if(sensorHelper.getX() < forwardArrowThreshold) arrowForward.setAlpha(255);
						else arrowForward.setAlpha(100);
					}else{
						if(sensorHelper.getY() < leftArrowThreshold)arrowLeft.setAlpha(1.0f);
						else arrowLeft.setAlpha(.5f);
						if(sensorHelper.getY() < rightArrowThreshold) arrowRight.setAlpha(1.0f);
						else arrowRight.setAlpha(.5f);
						if(sensorHelper.getX() < forwardArrowThreshold) arrowForward.setAlpha(1.0f);
						else arrowForward.setAlpha(.5f);
					}	
				}
				
			});	
		
		
	}
	
	private final BroadcastReceiver mReceiver = new BroadcastReceiver() {
        public void onReceive (Context context, Intent intent) {
            String action = intent.getAction();

            if (BluetoothAdapter.ACTION_STATE_CHANGED.equals(action)) {
                if(intent.getIntExtra(BluetoothAdapter.EXTRA_STATE, -1) == BluetoothAdapter.STATE_OFF){
//                 	Bluetooth is disconnected
                	System.out.println("DESCONECTADO");
                }              	
                	
            }
        }
	};
	
	@Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
    	switch (resultCode) {
		case RESULT_OK:
			pairedDevices = mBluetoothAdapter.getBondedDevices();
			if (pairedDevices.size() > 0) {
			    for (BluetoothDevice device : pairedDevices) {
			        mArrayAdapter.add(device.getName() + "\n" + device.getAddress());
			    }
			}
			break;
		case RESULT_CANCELED:
			AlertDialog.Builder dialog = new AlertDialog.Builder(this);
			dialog.setMessage(R.string.dialog_bluetooth_error);
			dialog.show();
			break;
		
		default:
			break;
		}
    }
	
	public void selectBluetooth(View view) {
		if(mmSocket != null){
			try {
				mmSocket.close();				
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		AlertDialog.Builder dialog = getBluetoothDialog();
		dialog.show();
	}

	public void filter(View view){	sensorHelper.filter.filter();	}
	public void stop(View view)	 {	sensorHelper.stop();	}
	public void play(View view)  {	sensorHelper.play();	}
	
	public void addMillis(View view)	 {	sensorHelper.setMillis(sensorHelper.getMillis() + 5);		}	
	public void minusMillis(View view)	 {	sensorHelper.setMillis(sensorHelper.getMillis() -5);		}
	public void addThreshold(View view)	 {	sensorHelper.setThreshold(sensorHelper.getThreshold() + 1);	}
	public void minusThreshold(View view){	sensorHelper.setThreshold(sensorHelper.getThreshold() -1);	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	
	public AlertDialog.Builder getBluetoothDialog(){
		AlertDialog.Builder dialog = new AlertDialog.Builder(this);
		dialog.setAdapter(mArrayAdapter, new OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				BluetoothDevice device = btDeviceArray.get(which);
				connectAsyncTask = new ConnectAsyncTask();
				connectAsyncTask.execute(device);
			}
		});		
		return dialog;
	}
	
	public boolean sendMessage(String message){
		System.out.println("log - sendMessage : "+message);
		
		try {
			if(btSocket == null){
				System.out.println("log - socket null");
				return false;
			}			
			if(btSocket.getRemoteDevice() != null){
				mmOutStream = btSocket.getOutputStream();
				mmOutStream.write(message.getBytes());
				return true;
			}else{
				System.out.println("log - remoteDevice null");
			}
			
		} catch (IOException e) { 
			System.out.println("log - sendMessage ERROR : "+e.getMessage());
			Toast.makeText(this, R.string.lost_connection, Toast.LENGTH_SHORT).show();

		}
		return false;
	}
	OutputStream mmOutStream = null;

	private BluetoothSocket mmSocket;
	private BluetoothDevice mmDevice;
	class ConnectAsyncTask extends AsyncTask<BluetoothDevice, Integer, BluetoothSocket>{

		
		@Override
		protected BluetoothSocket doInBackground(BluetoothDevice... device) {
							
			mmDevice = device[0];			
			try {				
				if(mmSocket != null){
					mmOutStream.close();
					mmOutStream = null;
					mmSocket.close();
					mmSocket = null;
				}
					String mmUUID = "00001101-0000-1000-8000-00805F9B34FB";
					mmSocket = mmDevice.createRfcommSocketToServiceRecord(UUID.fromString(mmUUID));
				
				
				
				mmSocket.connect();
				
			} catch (Exception e) {
				System.out.println(e.getMessage());		
				try {
					mmSocket.close();
				} catch (IOException e1) {
					System.out.println(e1.getMessage());		
				}
			}			
			return mmSocket;
		}
		
		@Override
		protected void onPostExecute(BluetoothSocket result) {			
			btSocket = result;
		}		
	}

}




