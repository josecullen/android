package com.example.androidmovimientos;

import java.text.DecimalFormat;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
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
	
	BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
	
	
	DecimalFormat df = new DecimalFormat("##.#");
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.second_activity);
		
		
		
		txtReposoX = (TextView)findViewById(R.id.txtReposoX);
		txtReposoY = (TextView)findViewById(R.id.txtReposoY);
		txtReposoZ = (TextView)findViewById(R.id.txtReposoZ);
		txtMovimientoX = (TextView)findViewById(R.id.txtMovimientoX);
		txtMovimientoY = (TextView)findViewById(R.id.txtMovimientoY);
		txtMovimientoZ =  (TextView)findViewById(R.id.txtMovimientoZ);
		txtVelocity =  (TextView)findViewById(R.id.txtVelocity);

		txtMillis = (EditText)findViewById(R.id.txtMillis);
		
		arrowForward = (ImageButton)findViewById(R.id.arrowForward);
		arrowLeft = (ImageButton)findViewById(R.id.arrowLeft);
		arrowRight = (ImageButton)findViewById(R.id.arrowRight);
		
		
		seekVisualizacion = (SeekBar)findViewById(R.id.seekVisualizacion);		
		seekVisualizacion.setProgress(50);

		
		sensorHelper = new SensorHelper((SensorManager) getSystemService(Context.SENSOR_SERVICE));
		sensorHelper
			.setMillis(100)
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
				}
				
				@SuppressLint("NewApi") void setArrows(){
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
			}).play();
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
//		this.invalidateOptionsMenu();
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

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (mBluetoothAdapter == null) {
			System.out.println("NO BLUETOOTH");
		}else{
			if (!mBluetoothAdapter.isEnabled()) {
				System.out.println("YES BLUETOOTH");

			    Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
			    startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
			}
		}
	}
	

}
