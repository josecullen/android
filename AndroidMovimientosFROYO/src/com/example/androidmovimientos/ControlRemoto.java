package com.example.androidmovimientos;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Set;
import java.util.UUID;

import com.bluetooth.BluetoothActivity;
import com.bluetooth.ConnectThread;
import com.bluetooth.ConnectionThread;
import com.bluetooth.ShowDevices;
import com.sensor.Action;
import com.sensor.SensorHelper;





import android.annotation.SuppressLint;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

public class ControlRemoto extends BluetoothActivity{

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

	SensorHelper sensorHelper;
	SeekBar seekVisualizacion;
	
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

		txtReposoX.setText("version 0.1.2");
		
		txtMillis = (EditText)findViewById(R.id.txtMillis);
		
		arrowForward = (ImageButton)findViewById(R.id.arrowForward);
		arrowLeft = (ImageButton)findViewById(R.id.arrowLeft);
		arrowRight = (ImageButton)findViewById(R.id.arrowRight);		
		
		seekVisualizacion = (SeekBar)findViewById(R.id.seekVisualizacion);		
		seekVisualizacion.setProgress(50);

		setSensor();	
	}
	
	public void filter(View view){	sensorHelper.filter.filter();	}
	public void stop(View view)	 {	sensorHelper.stop();	}
	public void play(View view)  {	sensorHelper.play();	}
	
	public void addMillis(View view)	 {	sensorHelper.setMillis(sensorHelper.getMillis() + 5);		}	
	public void minusMillis(View view)	 {	sensorHelper.setMillis(sensorHelper.getMillis() -5);		}
	public void addThreshold(View view)	 {	sensorHelper.setThreshold(sensorHelper.getThreshold() + 1);	}
	public void minusThreshold(View view){	sensorHelper.setThreshold(sensorHelper.getThreshold() -1);	}

	/**
	 * Esta parte es muy importante, ya que setea el sensor de Acelerómetro que luego se comunicará con 
	 * el dispositivo bluetooth para mandarle instrucciones.
	 */
	private void setSensor(){
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
					if(mBluetoothConnection != null){
						String temp = "x"+df.format(sensorHelper.getX())+","+
								"y"+df.format(sensorHelper.getY())+","+
								"z"+df.format(sensorHelper.getZ())+"_"	;
						mBluetoothConnection.write(temp.getBytes());
					}

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
	
	
}
