package com.projects.p8.intelligent_workout;

import android.app.Activity;
import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

/**
 * Created by gaetan on 05/12/17.
 */

public class MyTypeOfActivity extends Activity implements ServiceConnection{

    ServiceManager myService;
    boolean mServiceBound = false;
    Intent intent;
    String level_music;

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
        mServiceBound = false;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.e("MyTypeOfActivity","Beginning onStart()");
        intent = new Intent(this, ServiceManager.class);
        Log.e("MyTypeOfActivity","Starting service");
        startService(intent);
        Log.e("MyTypeOfActivity","Service started & Binding service");
        bindService(intent,mServiceConn , Context.BIND_AUTO_CREATE);
        Log.e("MyTypeOfActivity","boundService");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.e("MyTypeOfActivity Life","onStop");
        if (mServiceBound) {
            //myService.pauseMusic();
            unbindService(mServiceConn);
            mServiceBound = false;
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.e("MyTypeOfActivity Life","onDestroy");
    }

    @Override
    protected void onResume() {
        super.onResume();
        /*Log.e("MyTypeOfActivity Life","onResume");
        if(mServiceBound){
            Log.e("MyTypeOfActivity Life","onResume(reloading music)");
            myService.reloadmusic();
        }*/
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.e("MyTypeOfActivity Life","onPause");
        if(mServiceBound) {
            Log.e("MyTypeOfActivity Life","onPause(Pausing music)");
            myService.pauseMusic();
        }
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

            myService.setNewMusic(level_music);
            myService.playMusic();
        }
    };

}
