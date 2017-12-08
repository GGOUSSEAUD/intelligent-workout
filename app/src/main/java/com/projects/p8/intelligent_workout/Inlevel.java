package com.projects.p8.intelligent_workout;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

public class Inlevel extends MyTypeOfActivity
{
    private IntelligentWorkout mIntelligentWorkout;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        this.level_music = "elev_music2.mp3";
        setContentView(R.layout.main);

        //final Intent myIntent = new Intent(Inlevel.this, Win.class);

        mIntelligentWorkout = findViewById(R.id.IntelligentWorkout);
        mIntelligentWorkout.setVisibility(View.VISIBLE);

        mIntelligentWorkout.setEventListener(new IntelligentWorkout.IMyEventListener() {

            @Override
            public void onMenuPressed() {
                onBackPressed(); // end activity
            }

            @Override
            public void onRetryPressed() {
                Intent intent = getIntent();
                finish();
                startActivity(intent);
            }

            @Override
            public void onContinuePressed() {
                //reste a incrementer la variable lvl ici (j'espere que le service peut gerer sa ?)
                Intent intent = getIntent();
                finish();
                startActivity(intent);
            }
        });
    }
}
