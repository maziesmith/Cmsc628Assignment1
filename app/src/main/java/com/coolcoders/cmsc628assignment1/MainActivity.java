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

import java.util.Calendar;

public class MainActivity extends ActionBarActivity implements SensorEventListener
{
    private Handler _handler = null;
    private SensorManager _sensorManager = null;
    private Sensor _accelerometer = null, _gyroscope = null;
    private final int iInterval = 10000; //120000;//milliseconds

    private long lTimeStart, lTimeEnd;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        _handler = new Handler();
        lTimeStart = System.currentTimeMillis();
        _sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        if (_sensorManager != null) {
            _accelerometer = _sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
            _gyroscope = _sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);
        }

    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        lTimeEnd = System.currentTimeMillis();
        if (lTimeEnd - lTimeStart >= iInterval) {
//            onPause();
//            // TODO: interpret sensor data
//            // TODO: save data to SD


            String sText = "Sleeping";
//            /*            // briefly postpone inputting data so info can be processed
//
//             Whichever count for activity type has the majority probably
//             estimates the actual activity performed during the period
//              */
//            if (true) {
//                sText = "Sitting";
//            }
//            else if (true) {
//                sText = "Walking";
//            }
//
//            EntryItem item = new EntryItem(sText, iTimeStart, iTimeEnd);
            String message = "Recorded new times: ";
//            //TODO: display list item
//
            //Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
            lTimeStart = lTimeEnd;
//            // Data is processed/saved... resume recording
//            onResume();
//        }
//        else if (event.sensor == _accelerometer)
//        {
//            // TODO: get sensor data, record somehow
//        }
//        else if (event.sensor == _gyroscope)
//        {
//          }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        int a;
        if (_accelerometer!=null)
            _sensorManager.registerListener(this, _accelerometer, SensorManager.SENSOR_DELAY_NORMAL);
        if (_gyroscope!=null)
            _sensorManager.registerListener(this, _gyroscope, SensorManager.SENSOR_DELAY_NORMAL);

    }

    @Override
    protected void onPause() {
        super.onPause();
        if (_sensorManager!=null)
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
