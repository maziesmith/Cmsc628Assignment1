package com.coolcoders.cmsc628assignment1;

import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.os.Handler;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Bitmap;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.IBinder;
import android.provider.MediaStore;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.Calendar;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends ActionBarActivity implements SensorEventListener {

    private Handler _handler;
    private Calendar _calendar;
    private SensorManager _sensorManager;
    private Sensor _accelerometer, _gyroscope;
    private final int iInterval = 120;

    private int iTimeStart, iTimeEnd;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        _handler = new Handler();
        _calendar = Calendar.getInstance();
        iTimeStart = _calendar.get(Calendar.SECOND);
        _sensorManager = (SensorManager)getSystemService(Context.SENSOR_SERVICE);
        _accelerometer = _sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        _gyroscope = _sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);
    }


    public void onSensorChanged(SensorEvent event) {
        iTimeEnd = _calendar.get(Calendar.SECOND);
        if (iTimeEnd - iTimeStart >= iInterval)
        {
            // briefly postpone inputting data so info can be processed
            onPause();
            // TODO: interpret sensor data
            // TODO: save data to SD
            String sText = "Sleeping";
            /*
             Whichever count for activity type has the majority probably
             estimates the actual activity performed during the period
              */
            if (true) {
                sText = "Sitting";
            }
            else if (true) {
                sText = "Walking";
            }

            ListItem item = new ListItem(sText, iTimeStart, iTimeEnd);

            //TODO: display list item

            iTimeStart = iTimeEnd;
            // Data is processed/saved... resume recording
            onResume();
        }
        else if (event.sensor == _accelerometer)
        {
            // TODO: get sensor data, record somehow
        }
        else if (event.sensor == _gyroscope)
        {

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
