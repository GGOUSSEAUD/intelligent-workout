package com.projects.p8.intelligent_workout;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import java.util.StringTokenizer;

public class Inlevel extends MyTypeOfActivity
{
    private IntelligentWorkout mIntelligentWorkout;
    SharedPreferences sharedpref;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        this.level_music = "elev_music2.mp3";
        setContentView(R.layout.main);
        sharedpref = getSharedPreferences(getString(R.string.sharedpref), MODE_PRIVATE);
        final SharedPreferences.Editor editor = sharedpref.edit();
        //Log.i("Inlevel","lvl before service "+ mIntelligentWorkout.lvl);
        //mIntelligentWorkout.lvl = myService.getCurrent_level();
        //Log.i("Inlevel","lvl after service "+ mIntelligentWorkout.lvl);
        mIntelligentWorkout = findViewById(R.id.IntelligentWorkout);
        mIntelligentWorkout.setVisibility(View.VISIBLE);

        mIntelligentWorkout.setEventListener(new IntelligentWorkout.IMyEventListener() {

            @Override
            public void onMenuPressed() {
                onBackPressed(); // end activity
            }

            @Override
            public void onLvlInstancePressed() {
                Log.i("InLevel","onLvlInstancePressed : lvl is" + myService.getCurrent_level());
                mIntelligentWorkout.lvl = myService.getCurrent_level(); // end activity
            }

            @Override
            public void onRetryPressed() {
                Intent intent = getIntent();
                mIntelligentWorkout.in = false;
                finish();
                startActivity(intent);
            }

            @Override
            public void onContinuePressed() {
                //reste a incrementer la variable lvl ici (j'espere que le service peut gerer sa ?)
                int val = myService.getCurrent_level() + 1;
                if(val > mIntelligentWorkout.nblevel)
                    myService.setCurrent_level(0);
                else
                    myService.setCurrent_level(val);
                Intent intent = getIntent();
                mIntelligentWorkout.in = false;
                finish();
                startActivity(intent);
            }

            @Override
            public void onLevelWon(){

                StringBuilder str = new StringBuilder();
                for (int i = 0; i < mIntelligentWorkout.leveldone.length; i++) {
                    str.append(mIntelligentWorkout.leveldone[i]).append(",");
                }
                editor.putString("level_done", str.toString());
                editor.apply();

            }

            @Override
            public void soundHappening(){
                if(myService.getAllow_sound()){
                    myService.playsound();
                }
            }

            @Override
            public void getLevelDone(){
                mIntelligentWorkout.leveldone = myService.getLeveldones();
            }

        });
    }
}
