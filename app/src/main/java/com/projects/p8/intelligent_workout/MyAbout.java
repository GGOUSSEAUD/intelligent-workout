package com.projects.p8.intelligent_workout;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

public class MyAbout extends Activity
{
    private IntelligentWorkoutAbout mIntelligentWorkoutAbout;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
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
