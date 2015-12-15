package com.music.lu.utils;



import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

public class SensorManagerUtil implements SensorEventListener {

	private Context context;
	private SensorManager sensorManager;
	
	private int value = 14;
	
	private static SensorManagerUtil sensorManagerUtil=null;
	private boolean isRegister=false;
	public int getValue() {
		return value;
	}

	public void setValue(int value) {
		this.value = value;
	}
	private SensorChangedListener s;
	
	private SensorManagerUtil(Context context) {
		this.context = context;

		sensorManager = (SensorManager) context
				.getSystemService(Context.SENSOR_SERVICE);

	}
	public static SensorManagerUtil getInstance(Context context){
		if(sensorManagerUtil==null){
			sensorManagerUtil=new SensorManagerUtil(context);
		}
		return sensorManagerUtil;
	}
	public void registerListener(){
		
		if(!isRegister){
			sensorManager.registerListener(this,
					sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
					SensorManager.SENSOR_DELAY_NORMAL);
			
		}
		isRegister=true;
		
	}
	@Override
	public void onAccuracyChanged(Sensor sensor, int accuracy) {

	}
	public void unRegisterListener(){
		sensorManager.unregisterListener(this);
		isRegister=false;
	}
	

	@Override
	public void onSensorChanged(SensorEvent event) {
		int sensorType = event.sensor.getType();
		float[] values = event.values;

		if (sensorType == Sensor.TYPE_ACCELEROMETER) {

			
			 if (values[0] > value || values[1] > value
					|| values[2] > value) {
				 if(s!=null){
					 s.onSensorChanged();
				 }
			}

		}

	}
	public SensorChangedListener getS() {
		return s;
	}

	public void setS(SensorChangedListener s) {
		this.s = s;
	}
	public boolean isRegister() {
		return isRegister;
	}

	public void setRegister(boolean isRegister) {
		this.isRegister = isRegister;
	}
	public interface SensorChangedListener{
		void onSensorChanged();
	}

}
