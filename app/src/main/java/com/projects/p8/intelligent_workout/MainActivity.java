package com.projects.p8.intelligent_workout;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends Activity
{
    private IntelligentWorkout mIntelligentWorkout;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        mIntelligentWorkout = findViewById(R.id.IntelligentWorkout);
        mIntelligentWorkout.setVisibility(View.VISIBLE);
    }
}
