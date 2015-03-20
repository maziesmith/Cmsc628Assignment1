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
    private Handler _handler = null;
    private SensorManager _sensorManager = null;
    private Sensor _accelerometer = null; /** accelerometer sensor. */
    private Sensor _gyroscope = null; /** gyroscope sensor. */

    /** Intervals between reading sensor data, displaying warnings, and recording data */
    private final long lSensorInterval = 5000; // 0.5 seconds
    private final long lWarningInterval = 120000; // 120 seconds
    private final long lRecordInterval = 12000; // 12 seconds

    /** Track times between reading sensor data, showing warnings, and recording data */
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


    /**
     * This is called once during setup. It handles initialization.
     * @param savedInstanceState If this is resuming from a previous run, load previous state.
     */
    protected void onCreate(Bundle savedInstanceState) {
        // Stuff inherited from ActionBarActivity.
        super.onCreate(savedInstanceState);
        // Display the main activity.
        this.setContentView(R.layout.activity_main);
        _data_accelerometer = new float[3];
        _handler = new Handler();
        // Record the start time.
        lRecordTimeStart = System.currentTimeMillis();
        lSensorTimeStart = lRecordTimeStart;
        lWarningTimeStart = lRecordTimeStart;
        // Attempt to create the sensor manager.
        _sensorManager = (SensorManager)getSystemService(Context.SENSOR_SERVICE);
        // First, check if a SensorManager was successfully created.
        if (_sensorManager==null) {
            errorCode = Warnings.NO_SENSORS;
        } else /* if it was created OK, */ {
            // Try to create the two sensors.
            _accelerometer = _sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
            _gyroscope     = _sensorManager.getDefaultSensor(Sensor.TYPE_GRAVITY);
            // Then check if they were created OK.
            if (_accelerometer==null){
                errorCode = Warnings.NO_ACCELEROMETER;
            } else if (_gyroscope==null) {
                errorCode = Warnings.NO_GYROSCOPE;
            }
        }
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
            Toast.makeText(getApplicationContext(), Warnings.Message(errorCode), Toast.LENGTH_SHORT).show();
            // Update the time that the most recent warning was displayed.
            lWarningTimeStart = lTimeEnd;
        }
        // If enough time has passed to save / calculate data, do that.
        if (lTimeEnd - lRecordTimeStart >= lRecordInterval) {
            // TODO: create a new entry
            //EntryItem item = new EntryItem("?", lRecordTimeStart, lTimeEnd);
            // TODO figure out how to add EntryItems to the list
            //android.widget.RelativeLayout temptest = ((android.widget.RelativeLayout)(findViewById(R.id.listviewfrag)));
            String message = "" + _data_accelerometer[0] + ", " + _data_accelerometer[1] +
                    ", " + _data_accelerometer[2] + ".";
            Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
            _data_accelerometer[0] = _data_accelerometer[1] = _data_accelerometer[2] = 0;
            lRecordTimeStart = lTimeEnd;
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
