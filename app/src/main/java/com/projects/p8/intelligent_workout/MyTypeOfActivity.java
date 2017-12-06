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
    boolean mServiceBound = false;
    Intent intent;

    @Override
    public void onServiceConnected(ComponentName name, IBinder service) {
        //TODO: Classe abstraite à définir
        Log.d("ServiceConnection","connected");
        ServiceManager.BinderInterface myBinder = (ServiceManager.BinderInterface) service;
        myService = myBinder.getService();
        mServiceBound = true;
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
        Log.e("MyTypeOfActivity","Beginning onStart()");
        intent = new Intent(this, ServiceManager.class);
        startService(intent);
        bindService(intent,mServiceConn , Context.BIND_AUTO_CREATE);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mServiceBound) {
            myService.stopMusic();
            unbindService(mServiceConn);
            mServiceBound = false;
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        myService.onDestroy();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(mServiceBound){
            myService.reloadmusic();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        myService.pauseMusic();
    }

    private ServiceConnection mServiceConn = new ServiceConnection() {

        @Override
        public void onServiceDisconnected(ComponentName name) {
            Log.e("Service connection","Im disconnected activity");
            mServiceBound = false;
        }

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            Log.e("Service connection","Im connected through activity");
            ServiceManager.BinderInterface myBinder = (ServiceManager.BinderInterface) service;
            myService = myBinder.getService();
            mServiceBound = true;
        }
    };

}
