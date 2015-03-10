package com.example.services;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;

public class CountService extends Service {
    // Binder given to clients
    private final IBinder binder = new LocalBinder();
    // Random number generator

    /**
     * Class used for the client Binder.  Because we know this service always
     * runs in the same process as its clients, we don't need to deal with IPC.
     */
    public class LocalBinder extends Binder {
        public CountService getService() {
            // Return this instance of LocalService so clients can call public methods
            return CountService.this;
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
    	return binder;
    }

    /** method for clients */
    public void countTo(Integer max) {
      for (int i = 0; i < max ; i++) ;
    }
}