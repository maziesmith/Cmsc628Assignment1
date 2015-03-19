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
    /**
     * This class defines several constants that are used to represent error codes.
     */
    private static class errors {
        /** Indicates that no errors were detected. */
        public static int OK=0;
        /** Indicates that no SensorManager was created. */
        public static int NO_SENSORS=1;
        /** Indicates that no accelerometer was detected. */
        public static int NO_ACCELEROMETER=2;
        /** Indicates that no gyroscope was detected. */
        public static int NO_GYROSCOPE=3;
    }
    /** Stores any errors that occur during setup. */
    private int errortype = errors.OK;

    private Handler _handler = null;
    private SensorManager _sensorManager = null;
    /** The accelerometer sensor. */
    private Sensor _accelerometer = null;
    /** The gyroscope sensor. */
    private Sensor _gyroscope = null;
    /** How long to wait between displaying warning messages. Currently 6 seconds. */
    private final long lWarningInterval = 6000; // 6 seconds
    /** How long to go between taking readings. Currently 120 seconds. */
    private final long lInterval = 20000; //120 seconds

    /** When the current interval started. */
    private long lTimeStart;
    /** When the most recent warning message was displayed. */
    private long lWarningTimeStart;
    /** The 'current' time; updated every time sensors change. */
    private long lTimeEnd;

    // So, the problem is that 1) the gyroscope has no idea which way is up, 2) the accelerometer
    // can feel acceleration due to gravity, but has no way to differentiate gravity vs. movement,
    // and 3) the user might be moving or have the phone in a weird position when they start using
    // the app. Proposed solution: Assume the user is not moving and is holding the device upright
    // when  they start the app. Then, set and maintain these values.
    private float xAxis;
    private float yAxis;
    private float zAxis;

    /**
     * This is called once during setup. It handles initialization.
     * @param savedInstanceState If this is resuming from a previous run, load previous state.
     */
    protected void onCreate(Bundle savedInstanceState) {
        // Stuff inherited from ActionBarActivity.
        super.onCreate(savedInstanceState);
        // Display the main activity.
        this.setContentView(R.layout.activity_main);
        _handler = new Handler();
        // Record the start time.
        lTimeStart = System.currentTimeMillis();
        // If there are any warnings, display them after the waiting interval.
        lWarningTimeStart = lTimeStart;

        // Attempt to create the sensor manager.
        _sensorManager = (SensorManager)getSystemService(Context.SENSOR_SERVICE);
        // First, check if a SensorManager was successfully created.
        if (_sensorManager==null) {
            errortype = errors.NO_SENSORS;
        } else /* if it was created OK, */ {
            // Try to create the two sensors.
            _accelerometer = _sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
            _gyroscope     = _sensorManager.getDefaultSensor(Sensor.TYPE_GRAVITY);
            // Then check if they were created OK.
            /*---*/if (_accelerometer==null){
                errortype = errors.NO_ACCELEROMETER;
            } else if (_gyroscope==null) {
                errortype = errors.NO_GYROSCOPE;
            }
        }
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        // Update the current time.
        lTimeEnd = System.currentTimeMillis();

        // If enough time between warnings is done, display a new warning (if there are any).
        if (lTimeEnd - lWarningTimeStart >= lWarningInterval) {
            //periodically show warning messages on a short interval
            /*---*/if (errortype == errors.NO_GYROSCOPE) {
                Toast.makeText(getApplicationContext(), "WARNING: Missing Gyroscope!", Toast.LENGTH_SHORT).show();
            } else if (errortype == errors.NO_ACCELEROMETER) {
                Toast.makeText(getApplicationContext(), "WARNING: Missing Accelerometer!", Toast.LENGTH_SHORT).show();
            } else if (errortype == errors.NO_SENSORS) {
                Toast.makeText(getApplicationContext(), "WARNING: Missing Required Sensors!", Toast.LENGTH_SHORT).show();
            }
            // Update the time that the most recent warning was displayed.
            lWarningTimeStart = lTimeEnd;
        }
        // If enough time has passed for a new reading to be taken, take a new reading.
        if (lTimeEnd - lTimeStart >= lInterval) { // TODO: I removed the 'else' from this line because it would cause the code to skip making a new entry sometimes.
            // TODO: create a new entry
            EntryItem item = new EntryItem("?", lTimeStart, lTimeEnd);
            // TODO figure out how to add EntryItems to the list
            //android.widget.RelativeLayout temptest = ((android.widget.RelativeLayout)(findViewById(R.id.listviewfrag)));
            // Toast.makeText(getApplicationContext(), "INTERVAL: "+(new Long(((lTimeEnd - lTimeStart)/1000))).toString(), Toast.LENGTH_SHORT).show();
            lTimeStart = lTimeEnd;
        }
        // TODO: there was no reason for this code to only be executed sometimes. Removed 'else'.
        // read data
        final float xval = event.values[0];
        final float yval = event.values[1];
        final float zval = event.values[2];

        // Accelerometer data
        if(event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            _handler.post(new Runnable(){
                @Override
                public void run() {
                    String message = "Accelerometer: " + xval + ", " + yval + ", " + zval;
                    Toast.makeText(getApplicationContext(),message,Toast.LENGTH_SHORT).show();
                }});}

        // Gyroscope data
        else if(event.sensor.getType() == Sensor.TYPE_GYROSCOPE) {
            _handler.post(new Runnable(){
                @Override
                public void run() {
                    String message = "Gyroscope: "+xval+", "+yval+", "+zval;
                    //Toast.makeText(getApplicationContext(),message,Toast.LENGTH_SHORT).show();
                }});}
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
