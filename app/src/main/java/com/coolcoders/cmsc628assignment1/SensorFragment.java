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
import android.os.Environment;
import android.os.Handler;
import android.view.View;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
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
    private final long lSensorInterval = 100; // 0.1 seconds
    private final long lWarningInterval = 10000; // 10 seconds
    private final long lWriteInterval = 12000; // 120 seconds
    public final int iMaxEntries = 10; // remove stored data on 11th or older entries

    /** Track times between reading sensor data, showing warnings, and recording data */
    public static Handler _sensorHandler;
    public static Handler _warningHandler;
    public static Handler _writeHandler;

    public static long lSensorTimeStart;
    public static long lWriteTimeStart;

    public static int iGuessSitting;
    public static int iGuessProne;
    public static int iGuessUpright;

    /** Gets message from Warnings class depending on status of sensors detected. */
    public static int errorCode = Warnings.OK;

    private Runnable _sensorRunnable = new Runnable(){
        @Override
        public void run(){
            onResume(); // force sensor to record if/when app is not in focus.
            _sensorHandler.postDelayed(_sensorRunnable, lSensorInterval);
        }
    };

    private Runnable _warningRunnable = new Runnable(){
        @Override
        public void run(){
            //Toast.makeText(getActivity().getApplicationContext(), Warnings.Message(errorCode), Toast.LENGTH_SHORT).show();
            _warningHandler.postDelayed(_warningRunnable, lWarningInterval);
        }
    };

    private Runnable _writeRunnable = new Runnable(){
        @Override
        public void run(){
            String  message = "blank";
            if (iGuessUpright > iGuessProne && iGuessUpright > iGuessSitting) {
                message = "Walking/Standing";
            }
            else if (iGuessSitting > iGuessUpright && iGuessSitting > iGuessProne) {
                message = "Sitting";
            } else {
               message = "Lying Down";
            }
            /* put the data into a new item, then add to the displayed list */
            EntryItem item = new EntryItem(message, lWriteTimeStart, System.currentTimeMillis());
            _items.add(item);
            if (_items.size()>iMaxEntries) { /* remove entries that are too old */
                _items.remove(0);
            }
            SaveToFile(_items, "list_items.xml");
            /* add the new entry, and then reset the sensor data */
            _adapter.notifyDataSetChanged();
            iGuessSitting = iGuessProne = iGuessUpright = 0;
            lWriteTimeStart = System.currentTimeMillis();
            _writeHandler.postDelayed(_writeRunnable,lWriteInterval);
        }
    };

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState==null) {
            _items = new ArrayList<EntryItem>();
            _items.add(new EntryItem("Start",System.currentTimeMillis(),System.currentTimeMillis()));
            lSensorTimeStart = System.currentTimeMillis();
            lWriteTimeStart = lSensorTimeStart + 100; // offset to to avoid overlap
            _sensorHandler = new Handler();
            _sensorHandler.postDelayed(_sensorRunnable, lSensorInterval);
            _warningHandler = new Handler();
            _warningHandler.postDelayed(_warningRunnable, lWarningInterval);
            _writeHandler = new Handler();
            _writeHandler.postDelayed(_writeRunnable, lWriteInterval);
        }
        /* Attempt to get the sensor manager. */
        _sensorManager = (SensorManager) getActivity().getSystemService(Context.SENSOR_SERVICE);
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
        if (System.currentTimeMillis() - lSensorTimeStart >= lSensorInterval) {
            // Accelerometer data
            final float xval = Math.abs(event.values[0]);
            final float yval = Math.abs(event.values[1]);
            final float zval = Math.abs(event.values[2]);
            if (yval > zval && yval > xval) {
                /* y-axis = up, likely standing or walking */
                iGuessUpright++;
            } else if (xval > yval && xval > zval) {
                /* x-axis = sideways, most like in a pocket when seated */
                iGuessSitting++;
            } else {
                iGuessProne++;
            }
            lSensorTimeStart = System.currentTimeMillis();
        }
    }

    public static void SaveToFile(List<EntryItem> items, String filename){
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
            //String filename = "list_data.xml";
            String strout = EntryItem.EntryListString(items);
            File file = new File(MainActivity.context.getFilesDir(), filename);
            FileOutputStream outputStream;
            try {
                outputStream = MainActivity.context.openFileOutput(filename, Context.MODE_PRIVATE);
                outputStream.write(strout.getBytes());
                outputStream.close();
                Toast.makeText(MainActivity.context, "Saved " + filename + " to SD", Toast.LENGTH_SHORT);
            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(MainActivity.context, "! Could not save " + filename + " to SD !", Toast.LENGTH_SHORT);
            }
        } else {
            Toast.makeText(MainActivity.context, "! Could not save " + filename + " to SD !", Toast.LENGTH_SHORT);
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
