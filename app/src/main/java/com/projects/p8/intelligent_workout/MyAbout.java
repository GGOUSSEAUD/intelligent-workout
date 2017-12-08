package com.projects.p8.intelligent_workout;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

public class MyAbout extends MyTypeOfActivity
{
    private IntelligentWorkoutAbout mIntelligentWorkoutAbout;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        this.level_music = "ure_welcome.mp3";
        setContentView(R.layout.mainabout);

        mIntelligentWorkoutAbout = findViewById(R.id.IntelligentWorkoutAbout);
        mIntelligentWorkoutAbout.setVisibility(View.VISIBLE);

        mIntelligentWorkoutAbout.setEventListener(new IntelligentWorkoutAbout.IMyEventListener() {

            @Override
            public void onMenuPressed() {
                onBackPressed();
            }
        });
    }
}
