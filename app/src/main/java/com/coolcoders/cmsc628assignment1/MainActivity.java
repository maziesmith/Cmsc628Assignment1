package com.coolcoders.cmsc628assignment1;

import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.os.Handler;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.widget.Toast;

public class MainActivity extends ActionBarActivity implements SensorEventListener
{
    private static class errors {
        public static int OK=0,NO_SENSORS=1,NO_ACCELEROMETER=2,NO_GYROSCOPE=3;
    }
    private int errortype = errors.OK;

    private Handler _handler = null;
    private SensorManager _sensorManager = null;
    private Sensor _accelerometer = null, _gyroscope = null;
    private final long lWarningInterval = 6000; // 6 seconds
    private final long lInterval = 20000; //120 seconds

    private long lTimeStart, lWarningTimeStart, lTimeEnd;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        _handler = new Handler();
        lTimeStart = System.currentTimeMillis();
        lWarningTimeStart = lTimeStart;
        _sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        _accelerometer = _sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        _gyroscope = _sensorManager.getDefaultSensor(Sensor.TYPE_GRAVITY);
        if (_sensorManager==null) {
            errortype = errors.NO_SENSORS;
        } else if (_accelerometer==null){
            errortype = errors.NO_ACCELEROMETER;
        } else if (_gyroscope==null){
            errortype = errors.NO_GYROSCOPE;
        }
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        lTimeEnd = System.currentTimeMillis();

        if (lTimeEnd - lWarningTimeStart >= lWarningInterval) {
            //periodically show warning messages on a short interval
            if (errortype == errors.NO_GYROSCOPE) {
                Toast.makeText(getApplicationContext(), "WARNING: Missing Gyroscope!", Toast.LENGTH_SHORT).show();
            } else if (errortype == errors.NO_ACCELEROMETER) {
                Toast.makeText(getApplicationContext(), "WARNING: Missing Accelerometer!", Toast.LENGTH_SHORT).show();
            } else if (errortype == errors.NO_SENSORS) {
                Toast.makeText(getApplicationContext(), "WARNING: Missing Required Sensors!", Toast.LENGTH_SHORT).show();
            }
            lWarningTimeStart = lTimeEnd;
        }
        else if (lTimeEnd - lTimeStart >= lInterval) {
            // TODO: create a new entry
            //   EntryItem item = new EntryItem("?", lTimeStart, lTimeEnd);
        //    Toast.makeText(getApplicationContext(), "INTERVAL: "+(new Long(((lTimeEnd - lTimeStart)/1000))).toString(), Toast.LENGTH_SHORT).show();
            lTimeStart = lTimeEnd;
        } else {
            // read data
            final float xval = event.values[0];
            final float yval = event.values[1];
            final float zval = event.values[2];
            if(event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
                _handler.post(new Runnable(){
                    @Override
                    public void run() {
                        //String message = "Accelerometer: "+xval+", "+yval+", "+zval;
                        //Toast.makeText(getApplicationContext(),message,Toast.LENGTH_SHORT).show();
                    }
                });
            } else
            if(event.sensor.getType() == Sensor.TYPE_GYROSCOPE) {
                _handler.post(new Runnable(){
                    @Override
                    public void run() {
                        //String message = "Gyroscope: "+xval+", "+yval+", "+zval;
                        //Toast.makeText(getApplicationContext(),message,Toast.LENGTH_SHORT).show();
                    }
                });
            }

        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        _sensorManager.registerListener(this, _accelerometer, SensorManager.SENSOR_DELAY_NORMAL);
        _sensorManager.registerListener(this, _gyroscope, SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    protected void onPause() {
        super.onPause();
        _sensorManager.unregisterListener(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    // required for SensorEventListener
    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }
}
