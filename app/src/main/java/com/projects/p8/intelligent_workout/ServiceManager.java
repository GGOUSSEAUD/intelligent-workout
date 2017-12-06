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
    MediaPlayer player;
    int musicpos = 0;

    public ServiceManager() {
    }

    @Override
    public void onCreate() {
        super.onCreate();
        player = new MediaPlayer();
        try {
            AssetFileDescriptor afd = getAssets().openFd("elev_music.mp3");
            player.setDataSource(afd.getFileDescriptor(),afd.getStartOffset(),afd.getLength());
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            player.prepare();
        } catch (IOException e) {
            e.printStackTrace();
        }
            player.start();
        }

    public void setNewMusic(String musicname){
        try {
            AssetFileDescriptor afd = getAssets().openFd(musicname);
            player.setDataSource(afd.getFileDescriptor(),afd.getStartOffset(),afd.getLength());

        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            player.prepare();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void playMusic(){
        player.start();
    }

    public void pauseMusic(){
        player.pause();
        musicpos = player.getCurrentPosition();
    }

    public void reloadmusic(){
        player.seekTo(musicpos);
        player.start();
    }

    public void stopMusic(){
        player.release();
    }

    @Override
    public void onDestroy() {
        player.stop();
        player.release();
        super.onDestroy();
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
