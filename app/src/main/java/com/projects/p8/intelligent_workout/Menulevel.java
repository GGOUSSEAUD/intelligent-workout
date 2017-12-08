package com.projects.p8.intelligent_workout;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;

import java.util.StringTokenizer;

public class Menulevel extends MyTypeOfActivity
{
    private IntelligentWorkoutMenuLvl mIntelligentWorkoutMenuLvl;
    SharedPreferences sharedpref;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        this.level_music = "wii_music.mp3";
        setContentView(R.layout.mainlvl);

        sharedpref = getSharedPreferences(getString(R.string.sharedpref), MODE_PRIVATE);
        final SharedPreferences.Editor editor = sharedpref.edit();

        final Intent myIntent = new Intent(Menulevel.this, Inlevel.class);

        mIntelligentWorkoutMenuLvl = findViewById(R.id.IntelligentWorkoutMenuLvl);
        mIntelligentWorkoutMenuLvl.setVisibility(View.VISIBLE);

        mIntelligentWorkoutMenuLvl.setEventListener(new IntelligentWorkoutMenuLvl.IMyEventListener() {

            @Override
            public void onEventAccured() {
                    myService.setCurrent_level(mIntelligentWorkoutMenuLvl.lvl);
                    Log.i("MenuLevel","lvl value is set to" + myService.getCurrent_level());

                    Menulevel.this.startActivity(myIntent);
            }

            @Override
            public void getLvldone(){


                String savedString = sharedpref.getString("level_done", "0,0,0,0,0,0,0,0,0,0,0");
                StringTokenizer st = new StringTokenizer(savedString, ",");
                int[] savedList = new int[mIntelligentWorkoutMenuLvl.nblevel];
                for (int i = 0; i < mIntelligentWorkoutMenuLvl.nblevel; i++) {
                    savedList[i] = Integer.parseInt(st.nextToken());
                }

                mIntelligentWorkoutMenuLvl.leveldone = savedList;

                myService.setLeveldones(mIntelligentWorkoutMenuLvl.leveldone);
            }

        });
    }
}
