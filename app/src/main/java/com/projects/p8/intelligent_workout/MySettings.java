package com.projects.p8.intelligent_workout;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

public class MySettings extends MyTypeOfActivity
{
    private IntelligentWorkoutSettings mIntelligentWorkoutSettings;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        this.level_music = "elev_music3.mp3";
        setContentView(R.layout.mainsettings);

        mIntelligentWorkoutSettings = findViewById(R.id.IntelligentWorkoutSettings);
        mIntelligentWorkoutSettings.setVisibility(View.VISIBLE);

        mIntelligentWorkoutSettings.setEventListener(new IntelligentWorkoutSettings.IMyEventListener() {
            @Override
            public void onMenuPressed() {
                onBackPressed();
            }

            public void onSoundPressed(boolean music_state){

            }

            public void onMusicPressed(boolean music_state){

            }

        });
    }
}
