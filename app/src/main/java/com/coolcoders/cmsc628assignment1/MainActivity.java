package com.coolcoders.cmsc628assignment1;

import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.os.Handler;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.widget.Toast;

public class MainActivity extends ActionBarActivity
{
    /**
     * This is called once during setup. It handles initialization.
     * @param savedInstanceState If this is resuming from a previous run, load previous state.
     */
    protected void onCreate(Bundle savedInstanceState) {
        // Stuff inherited from ActionBarActivity.
        super.onCreate(savedInstanceState);
        // Display the main activity.
        this.setContentView(R.layout.activity_main);

        if (savedInstanceState == null){
            Toast.makeText(this.getApplicationContext(), "Nothing was saved. :D", Toast.LENGTH_SHORT).show();
            EntryFragment entryFragment = new EntryFragment();
            entryFragment.setArguments(getIntent().getExtras());
            getFragmentManager().beginTransaction().replace(R.id.mainLayoutID, entryFragment, "the_entry_fragment" ).commit();
        } else {
            Toast.makeText(this.getApplicationContext(), "Oh hey, you saved stuff.", Toast.LENGTH_SHORT).show();
            EntryFragment entryFragment = (EntryFragment) getFragmentManager().findFragmentByTag("the_entry_fragment");
        }
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
