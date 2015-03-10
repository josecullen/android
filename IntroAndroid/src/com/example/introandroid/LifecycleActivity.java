package com.example.introandroid;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.TextView;

public class LifecycleActivity extends ActionBarActivity {
	   TextView text;
	   
	   /** Called when the activity is first created. */
	   @Override
	   public void onCreate(Bundle savedInstanceState) {
	      super.onCreate(savedInstanceState);
	      setContentView(R.layout.activity_lifecycle);
	      text = (TextView) findViewById(R.id.methods);
	      text.append("onCreate() \n");
	   }

	   /** Called when the activity is about to become visible. */
	   @Override
	   protected void onStart() {
	      super.onStart();
	      text.append("onStart() \n");
	   }

	   /** Called when the activity has become visible. */
	   @Override
	   protected void onResume() {
	      super.onResume();
	      text.append("onResume() \n");
	   }

	   /** Called when another activity is taking focus. */
	   @Override
	   protected void onPause() {
	      super.onPause();
	      text.append("onPause() \n");
	   }

	   /** Called when the activity is no longer visible. */
	   @Override
	   protected void onStop() {
	      super.onStop();
	      text.append("onStop() \n");
	   }

	   /** Called just before the activity is destroyed. */
	   @Override
	   public void onDestroy() {
	      super.onDestroy();
	      text.append("onDestroy() \n");
	   }
	   
	    public void goBack(View view)  {	
	    	Intent intent = new Intent(this, MainActivity.class);
	    	startActivity(intent);
	    }
	}