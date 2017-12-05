package com.projects.p8.intelligent_workout;

import android.app.Service;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.IBinder;
import android.provider.MediaStore;
import android.util.Log;

import java.io.IOException;

public class ServiceManager extends Service {
    private IBinder mBinder = new BinderInterface();
    int musicID = getResources().getIdentifier("elev_music.mp3","raw", getPackageName());
    MediaPlayer player;

    public ServiceManager() {
    }

    @Override
    public void onCreate() {
        super.onCreate();
        try {
            AssetFileDescriptor afd = getAssets().openFd("elev_music.mp3");
        } catch (IOException e) {
            e.printStackTrace();
        }
        player = MediaPlayer.create(this,musicID);

        //player.setDataSource(afd.getFileDescriptor(),afd.getStartOffset(),afd.getLength());

        try {
            player.prepare();
        } catch (IOException e) {
            e.printStackTrace();
        }
            player.start();
        }


    @Override
    public void onDestroy() {
        super.onDestroy();
        player.release();
    }

    @Override
    public IBinder onBind(Intent intent) {
        Log.v("Binder_Test", "in onBind");
        return mBinder;
    }


@Override
    public boolean onUnbind(Intent intent) {
    Log.v("Binder_Test", "in onUnbind");
    return true;
    }


    public class BinderInterface extends Binder

    {
        ServiceManager getService() {
            return ServiceManager.this;
        }
    }
}
