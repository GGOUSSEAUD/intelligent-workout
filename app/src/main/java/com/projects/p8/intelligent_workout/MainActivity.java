package com.projects.p8.intelligent_workout;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

public class MainActivity extends MyTypeOfActivity
{
    private IntelligentWorkoutMenu mIntelligentWorkoutMenu;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        this.level_music = "elev_music.mp3";
        setContentView(R.layout.mainmenu);

        final Intent menulevelIntent = new Intent(MainActivity.this, Menulevel.class);
        final Intent settingsIntent = new Intent(MainActivity.this, MySettings.class);
        final Intent aboutIntent = new Intent(MainActivity.this, MyAbout.class);

        mIntelligentWorkoutMenu = findViewById(R.id.IntelligentWorkoutMenu);
        mIntelligentWorkoutMenu.setVisibility(View.VISIBLE);

        mIntelligentWorkoutMenu.setEventListener(new IntelligentWorkoutMenu.IMyEventListener() {
            @Override
            public void onEventStart()
            {
                MainActivity.this.startActivity(menulevelIntent);
            }

            @Override
            public void onEventSettings()
            {
                MainActivity.this.startActivity(settingsIntent);
            }

            @Override
            public void onEventAbout()
            {
                MainActivity.this.startActivity(aboutIntent);
            }
        });
    }


    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
        /*if(mServiceBound)
            myService.pauseMusic();
    */
    }

    @Override
    protected void onStart() {
        super.onStart();
        //System.out.println("Onstart mservicebound is" + mServiceBound);

    }

    @Override
    protected void onResume() {
        super.onResume();
        /*if(mServiceBound){
            System.out.println("Im supposed to do my job but I don't");
            myService.setNewMusic("elev_music.mp3");
            myService.playMusic();
        }*/
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
