package com.sensor;

import java.io.FilterReader;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

public class SensorHelper implements SensorEventListener{
	private SensorManager senSensorManager;
	private Sensor sen;
	
	int type = 1;
	long millis = 0;
	double shakeThreshold = 0;
	
	public Filter filter = new Filter();
	public float 
		x = 0,
		y = 0,
		z = 0,
		lastX = 0,
		lastY = 0,
		lastZ = 0; 
	
	long 
		lastCurrentTime = 0,
		currentTime = 0, 
		diffTime = 0;
	
	Action action;	
	
	public SensorHelper(SensorManager sensorManager) {
		senSensorManager = sensorManager;
	}	
	public SensorHelper setSensor(int TYPE){
		type = TYPE;
		sen = senSensorManager.getDefaultSensor(type);
		return this;
	}	
	public SensorHelper play(){
		senSensorManager.registerListener(this, sen,SensorManager.SENSOR_DELAY_NORMAL);
		return this;
	}	
	public SensorHelper stop(){
		senSensorManager.unregisterListener(this, sen);
		return this;
	}
	
	@Override
	public void onSensorChanged(SensorEvent sensorEvent) {
		Sensor mySensor = sensorEvent.sensor;
		if (mySensor.getType() == Sensor.TYPE_ACCELEROMETER) {	
			if(passTheTime()){				
				x = sensorEvent.values[0];
				y = sensorEvent.values[1];
				z = sensorEvent.values[2];
				
				filter.filtering(x, y, z);
				
				diffTime = currentTime - lastCurrentTime;
				
				float speed = Math.abs(x + y + z - lastX - lastY - lastZ) / diffTime * 10000;
				
				if(speed > shakeThreshold || lastCurrentTime == 0 || currentTime == 0){
					lastCurrentTime = currentTime;
					action.action();
				}	
				lastX = x;	lastY = y; 	lastZ = z;
			}
			
		}	
	}
		
	public float getX(){	return x - filter.filterX;	}
	public float getY(){	return y - filter.filterY;	}
	public float getZ(){	return z - filter.filterZ;	}
	
	public SensorHelper setAction(Action action){
		this.action = action;
		return this;
	}	
	public SensorHelper setMillis(long millis){
		this.millis = millis;
		return this;
	}
	public SensorHelper setThreshold(double threshold){
		this.shakeThreshold = threshold;
		return this;
	}
	private boolean passTheTime(){
		currentTime = System.currentTimeMillis();
		if((currentTime - lastCurrentTime) > millis)	return true;
		else return false;
	}
	
	public long getMillis(){
		return millis;
	}
	public double getThreshold(){
		return shakeThreshold; 
	}	

	@Override
	public String toString(){
		StringBuilder s = new StringBuilder();
		s
			.append("SENSOR HELPER: ").append("\t")
			.append("millis = ").append(millis).append("\t")
			.append("threshold = ").append(shakeThreshold).append("\t");
		return s.toString();			
	}
	
	@Override public void onAccuracyChanged(Sensor sensor, int accuracy) {}
}
