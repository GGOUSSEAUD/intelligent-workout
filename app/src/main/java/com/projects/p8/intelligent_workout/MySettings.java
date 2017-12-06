package com.projects.p8.intelligent_workout;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

public class MySettings extends Activity
{
    private IntelligentWorkoutSettings mIntelligentWorkoutSettings;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mainsettings);

        mIntelligentWorkoutSettings = findViewById(R.id.IntelligentWorkoutSettings);
        mIntelligentWorkoutSettings.setVisibility(View.VISIBLE);

        mIntelligentWorkoutSettings.setEventListener(new IntelligentWorkoutSettings.IMyEventListener() {
            @Override
            public void onMenuPressed() {
                onBackPressed();
            }
        });
    }
}
