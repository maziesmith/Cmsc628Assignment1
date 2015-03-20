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
public class EntryFragment extends ListFragment implements SensorEventListener {

    public static List<EntryItem> _items;
    private EntryAdapter _adapter; /** new items are displayed from this */
    private SensorManager _sensorManager = null;
    private Sensor _accelerometer = null; /** accelerometer sensor. */
    private Sensor _gyroscope = null; /** gyroscope sensor. */
    /** Intervals between reading sensor data, displaying warnings, and recording data */
    private final long lSensorInterval = 5000; // 0.5 seconds
    private final long lWarningInterval = 10000; // 10 seconds
    private final long lRecordInterval = 20000; // 120 seconds

    /** Track times between reading sensor data, showing warnings, and recording data */
    private Handler _handler = null;
    private long lSensorTimeStart;
    private long lWarningTimeStart;
    private long lRecordTimeStart;
    /** The 'current' time; updated every time sensors change. */
    private long lTimeEnd;

    /** Gets message from Warnings class depending on status of sensors detected. */
    private int errorCode = Warnings.OK;

    // So, the problem is that 1) the gyroscope has no idea which way is up, 2) the accelerometer
    // can feel acceleration due to gravity, but has no way to differentiate gravity vs. movement,
    // and 3) the user might be moving or have the phone in a weird position when they start using
    // the app. Proposed solution: Assume the user is not moving and is holding the device upright
    // when  they start the app. Then, set and maintain these values.
    private float xAxis;
    private float yAxis;
    private float zAxis;

    /** Store cumulative accelerator x,y,z data from the sensors */
    private float[] _data_accelerometer;


    // TODO: populate list from MainActivity
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        _items = new ArrayList<EntryItem>();
        _data_accelerometer = new float[3]; /* x, y, z */
        lRecordTimeStart = System.currentTimeMillis();
        lSensorTimeStart = System.currentTimeMillis() - 250; // offset .25s to avoid overlap
        lWarningTimeStart = System.currentTimeMillis() - 4000; // offset 4s to avoid overlap
        // Attempt to create the sensor manager.
        _sensorManager = (SensorManager)getActivity().getSystemService(Context.SENSOR_SERVICE);
        // First, check if a SensorManager was successfully created.
        _handler = new Handler();
        if (_sensorManager==null) {
            errorCode = Warnings.NO_SENSORS;
        } else /* if it was created OK, */ {
            // Try to create the two sensors.
            _accelerometer = _sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
            _gyroscope     = _sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);
            // Then check if they were created OK.
            if (_accelerometer==null){
                errorCode = Warnings.NO_ACCELEROMETER;
            } else if (_gyroscope==null) {
                errorCode = Warnings.NO_GYROSCOPE;
            }
        }

        _items.add(new EntryItem("START", lRecordTimeStart,lSensorTimeStart));
        // send data to the adapter for displaying
        _adapter = new EntryAdapter(getActivity(), _items);
        setListAdapter(_adapter);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState){
        super.onViewCreated(view, savedInstanceState);
        int[] colors = { 0xA8A8A8A8, 0xFFFFFFFF, 0xA8A8A8A8 };
        getListView().setDivider(new GradientDrawable(GradientDrawable.Orientation.RIGHT_LEFT, colors));
        getListView().setDividerHeight(1);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        // Update the current time.
        lTimeEnd = System.currentTimeMillis();
        if (lTimeEnd - lSensorTimeStart >= lSensorInterval) {

            // TODO: read sensor data
            // Accelerometer data
            final float xval = event.values[0];
            final float yval = event.values[1];
            final float zval = event.values[2];
            // Accelerometer data
            if(event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
                _handler.post(new Runnable(){
                    @Override
                    public void run() {
                        // records the amount of movement, direction doesn't matter.
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
        // If enough time between warnings is done, display a new warning (if there are any).
        if (lTimeEnd - lWarningTimeStart >= lWarningInterval) {

            Toast.makeText(getActivity().getApplicationContext(), Warnings.Message(errorCode), Toast.LENGTH_SHORT).show();
            // Update the time that the most recent warning was displayed.
            lWarningTimeStart = lTimeEnd;
        }
        // If enough time has passed to save / calculate data, do that.
        if (lTimeEnd - lRecordTimeStart >= lRecordInterval) {
            // put the data into a new item, then add to the displayed list
            String message = "accelerometer data: " + _data_accelerometer[0] + ", " + _data_accelerometer[1] +
                    ", " + _data_accelerometer[2] + ".";
            EntryItem item = new EntryItem(message, lRecordTimeStart, lTimeEnd);
            _items.add(item);
            _adapter.notifyDataSetChanged();

            //Toast.makeText(getActivity().getApplicationContext(), message, Toast.LENGTH_SHORT).show();
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
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}
