package com.projects.p8.intelligent_workout;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

public class Inlevel extends Activity
{
    private IntelligentWorkout mIntelligentWorkout;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        final Intent myIntent = new Intent(Inlevel.this, Menulevel.class);

        mIntelligentWorkout = findViewById(R.id.IntelligentWorkout);
        mIntelligentWorkout.setVisibility(View.VISIBLE);

        mIntelligentWorkout.setEventListener(new IntelligentWorkoutMenuLvl.IMyEventListener() {

            @Override
            public void onEventAccured() {
                // TODO Auto-generated method stub
                Inlevel.this.startActivity(myIntent);
            }
        });
    }
}
