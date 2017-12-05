package com.projects.p8.intelligent_workout;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

public class Menulevel extends Activity
{
    private IntelligentWorkoutMenuLvl mIntelligentWorkoutMenuLvl;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        final Intent myIntent = new Intent(Menulevel.this, Inlevel.class);

        mIntelligentWorkoutMenuLvl = findViewById(R.id.IntelligentWorkoutLvl);
        mIntelligentWorkoutMenuLvl.setVisibility(View.VISIBLE);

        mIntelligentWorkoutMenuLvl.setEventListener(new IntelligentWorkoutMenuLvl.IMyEventListener() {

            @Override
            public void onEventAccured() {
                // TODO Auto-generated method stub
                Menulevel.this.startActivity(myIntent);
            }
        });
    }
}
