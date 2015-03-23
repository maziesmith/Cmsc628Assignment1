package com.coolcoders.cmsc628assignment1;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;
import android.content.Context;

public class MainActivity extends ActionBarActivity
{
    /** Allows static referencing of the application context. */
    private static Context context;

    /**
     * This is called once during setup. It handles initialization.
     * @param savedInstanceState If this is resuming from a previous run, load previous state.
     */
    protected void onCreate(Bundle savedInstanceState) {
        // Stuff inherited from ActionBarActivity.
        super.onCreate(savedInstanceState);

        // Enable static referencing of the application context.
        context = this.getApplicationContext();

        // Display the main activity.
        this.setContentView(R.layout.activity_main);

        if (savedInstanceState == null){
           // Toast.makeText(context, "Nothing was saved. :D", Toast.LENGTH_SHORT).show();
            SensorFragment sensorFragment = new SensorFragment();
            sensorFragment.setArguments(getIntent().getExtras());
            getFragmentManager().beginTransaction().replace(R.id.mainLayoutID, sensorFragment, "the_entry_fragment" ).commit();
        }
        /*else {
            Toast.makeText(context, "Oh hey, you saved stuff.", Toast.LENGTH_SHORT).show();
        SensorFragment sensorFragment = (SensorFragment) getFragmentManager().findFragmentByTag("the_entry_fragment");
        }*/
    }

    /**
     * Enables static accessing of application context.
     * @return the application context
     */
    public static Context GetApplicationContext(){
        return context;
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
}
