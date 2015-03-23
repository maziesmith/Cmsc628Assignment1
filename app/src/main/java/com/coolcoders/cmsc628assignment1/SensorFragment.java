package com.coolcoders.cmsc628assignment1;

import android.app.Activity;
import android.app.ListFragment;
import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

/**
 * List UI that contains entries
 */
public class SensorFragment extends ListFragment implements SensorEventListener {

    public static List<EntryItem> _items;
    public static EntryAdapter _adapter; /** new items are displayed from this */
    public static SensorManager _sensorManager = null;
    public static Sensor _accelerometer = null; /** accelerometer sensor. */
    public static Sensor _gyroscope = null; /** gyroscope sensor. */
    /** Intervals between reading sensor data, displaying warnings, and recording data */
    private final long lSensorInterval = 250; // 0.25 seconds
    private final long lWarningInterval = 120000; // 10 seconds
    private final long lRecordInterval = 7000; // 120 seconds
    public final int iMaxRecordedEntries = 10; // remove stored data on 11th or older entries

    /** Track times between reading sensor data, showing warnings, and recording data */
    public static Handler _handler = null;
    public static long lSensorTimeStart;
    public static long lWarningTimeStart;
    public static long lRecordTimeStart;
    /** The 'current' time; updated every time sensors change. */
    public static long lTimeEnd;

    /** Gets message from Warnings class depending on status of sensors detected. */
    public static int errorCode = Warnings.OK;

   /* // So, the problem is that 1) the gyroscope has no idea which way is up, 2) the accelerometer
    // can feel acceleration due to gravity, but has no way to differentiate gravity vs. movement,
    // and 3) the user might be moving or have the phone in a weird position when they start using
    // the app. Proposed solution: Assume the user is not moving and is holding the device upright
    // when  they start the app. Then, set and maintain these values.
    private float xAxis;
    private float yAxis;
    private float zAxis; */

    /** Store cumulative accelerator x,y,z data from the sensors */
    public static float[] _data_accelerometer;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState==null) {
            _items = new ArrayList<EntryItem>();
            _data_accelerometer = new float[3]; /* x, y, z */
            lRecordTimeStart = System.currentTimeMillis();
            lSensorTimeStart = System.currentTimeMillis() - 250; // offset .25s to avoid overlap
            lWarningTimeStart = System.currentTimeMillis() - 4000; // offset 4s to avoid overlap
            /* Attempt to create the sensor manager. */
            _sensorManager = (SensorManager) getActivity().getSystemService(Context.SENSOR_SERVICE);
            /* First, check if a SensorManager was successfully created. */
            _handler = new Handler();
        }
        if (_sensorManager==null) {
            errorCode = Warnings.NO_SENSORS;
        } else {
            /* if manager was found OK, try to get the required sensors. */
            _accelerometer = _sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
            _gyroscope     = _sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);
            /* Then check if they were found OK too. */
            if (_accelerometer==null){
                errorCode = Warnings.NO_ACCELEROMETER;
            } else if (_gyroscope==null) {
                errorCode = Warnings.NO_GYROSCOPE;
            }
        }
        /* send data to the adapter for displaying */
        if (_adapter==null) {
            _adapter = new EntryAdapter(getActivity(), _items);
        }
        setListAdapter(_adapter);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        // Update the current time.
        lTimeEnd = System.currentTimeMillis();
        if (lTimeEnd - lSensorTimeStart >= lSensorInterval)
        {
            // Accelerometer data
            final float xval = event.values[0];
            final float yval = event.values[1];
            final float zval = event.values[2];
            if(event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
                _handler.post(new Runnable(){
                    @Override
                    public void run() {
                        _data_accelerometer[0] += Math.abs(xval);
                        _data_accelerometer[1] += Math.abs(yval);
                        _data_accelerometer[2] += Math.abs(zval);
                    }});}
            // Gyroscope data
            else if(event.sensor.getType() == Sensor.TYPE_GYROSCOPE) {
                _handler.post(new Runnable(){
                    @Override
                    public void run() {
                        // TODO: test on a device with a functional gyroscope
                        /** WE'RE HAVING SOME TROUBLE WITH THE GYROSCOPE SIR */
                    }});}
            lSensorTimeStart = lTimeEnd;
        }
        /* If enough time between warnings is done, display a new warning (if there are any). */
        if (lTimeEnd - lWarningTimeStart >= lWarningInterval)
        {
            Toast.makeText(getActivity().getApplicationContext(), Warnings.Message(errorCode), Toast.LENGTH_SHORT).show();
            /*Update the time that the most recent warning was displayed. */
            lWarningTimeStart = lTimeEnd;
        }
        /* If enough time has passed to save / calculate data, do that. */
        if (lTimeEnd - lRecordTimeStart >= lRecordInterval)
        {
            String message = "<unknown>";
            if (_data_accelerometer[1] > _data_accelerometer[0] && _data_accelerometer[1] > _data_accelerometer[2])
            {
                /* majority of force along y-axis implies device is oriented upright. */
                if (_data_accelerometer[0]+_data_accelerometer[2]>20) {
                    /* a lot of movement on xz-plane generally implies walking */
                    message = "Walking";
                } else {
                    /* otherwise assume non-moving */
                    message = "Sitting";
                }
            }
            else {
                /* majority of force along x- or z-axis implies device is oriented sideways. */
                message = "Lying Down";
            }
            /* put the data into a new item, then add to the displayed list */
            EntryItem item = new EntryItem(message, lRecordTimeStart, lTimeEnd);
            _items.add(item);
            if (_items.size()>iMaxRecordedEntries) { /* remove entries that are too old */
                _items.remove(0);
            }
            /* add the new entry, and then reset the sensor data */
            _adapter.notifyDataSetChanged();
            _data_accelerometer[0] = _data_accelerometer[1] = _data_accelerometer[2] = 0;
            lRecordTimeStart = lTimeEnd;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        _sensorManager.registerListener(this, _accelerometer, SensorManager.SENSOR_DELAY_NORMAL);
        _sensorManager.registerListener(this, _gyroscope, SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    public void onPause() {
        super.onPause();
        _sensorManager.unregisterListener(this);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState){
        super.onViewCreated(view, savedInstanceState);
        int[] colors = { 0xA8A8A8A8, 0xFFFFFFFF, 0xA8A8A8A8 };
        getListView().setDivider(new GradientDrawable(GradientDrawable.Orientation.RIGHT_LEFT, colors));
        getListView().setDividerHeight(1);
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        // required for implementing SensorEventListener... nothing else here
    }
}
