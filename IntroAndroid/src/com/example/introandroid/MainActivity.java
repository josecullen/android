package com.example.introandroid;

import android.support.v7.app.ActionBarActivity;
import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;


public class MainActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    
    public void showLifecycleActivity(View view)  {	
    	Intent intent = new Intent(this, LifecycleActivity.class);
    	startActivity(intent);
    }

    public void showFragmentsActivity(View view)  {	
    	Intent intent = new Intent(this, FragmentsActivity.class);
    	startActivity(intent);
    }
    
    public void showServiceActivity(View view)  {	
    	Intent intent = new Intent(this, ServiceActivity.class);
    	startActivity(intent);
    }
    
}
