package com.projects.p8.intelligent_workout;

import android.app.Activity;
import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.util.Log;

/**
 * Created by gaetan on 05/12/17.
 */

public class MyTypeOfActivity extends Activity implements ServiceConnection{

    ServiceManager myService;
    ServiceConnection mServiceConn;

    @Override
    public void onServiceConnected(ComponentName name, IBinder service) {
        //TODO: Classe abstraite à définir
        Log.d("ServiceConnection","connected");
        //myService = service;
    }

    @Override
    public void onServiceDisconnected(ComponentName name) {
        //TODO: Classe abstraite à définir
        Log.d("ServiceConnection","disconnected");
        myService = null;
    }

    @Override
    protected void onStart() {
        super.onStart();
        bindService(new Intent("com.projects.p8.intelligent_workout.ServiceManager"),mServiceConn , Context.BIND_AUTO_CREATE);
    }

    @Override
    protected void onResume() {

        super.onResume();
    }

    @Override
    protected void onPause() {

        super.onPause();
    }

}
