package com.projects.p8.intelligent_workout;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

public class Menulevel extends MyTypeOfActivity
{
    private IntelligentWorkoutMenuLvl mIntelligentWorkoutMenuLvl;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        this.level_music = "wii_music.mp3";
        setContentView(R.layout.mainlvl);

        final Intent myIntent = new Intent(Menulevel.this, Inlevel.class);

        mIntelligentWorkoutMenuLvl = findViewById(R.id.IntelligentWorkoutMenuLvl);
        mIntelligentWorkoutMenuLvl.setVisibility(View.VISIBLE);

        mIntelligentWorkoutMenuLvl.setEventListener(new IntelligentWorkoutMenuLvl.IMyEventListener() {

            @Override
            public void onEventAccured() {
                    Menulevel.this.startActivity(myIntent);
            }
        });
    }
}
