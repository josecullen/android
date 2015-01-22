package com.sensor;

public class Filter {
	int 
		bufferLength = 10, 
		countBuffer = 10;
	public float 
		filterX = 0, 
		filterY = 0, 
		filterZ = 0;
	float[][] buffer = new float[3][bufferLength];
	
	
	public void filtering(float x, float y, float z){
		if(countBuffer >= bufferLength){
			return;
		}else{
			if(countBuffer >= 0 && countBuffer < bufferLength){
				buffer[0][countBuffer] = x;
				buffer[1][countBuffer] = y;
				buffer[2][countBuffer] = z;
			}
			countBuffer++;	

			for(float f : buffer[0])	filterX += f;			
			for(float f : buffer[1])	filterY += f;
			for(float f : buffer[2])	filterZ += f;
			
			filterX = filterX / bufferLength;
			filterY = filterY / bufferLength;
			filterZ = filterZ / bufferLength;
		}		
	}

	public void filter(){
		filterX = 0;
		filterY = 0;
		filterZ = 0;
		countBuffer = -5;
	}
	
	public void setFilterBufferLength(int newLength){
		bufferLength = newLength;
		buffer = new float[3][bufferLength];
		countBuffer = newLength;
	}
	
}
