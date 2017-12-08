package com.projects.p8.intelligent_workout;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

public class MySettings extends MyTypeOfActivity
{
    private IntelligentWorkoutSettings mIntelligentWorkoutSettings;
    Context shared_pref_context;
    SharedPreferences sharedpref;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        this.level_music = "elev_music3.mp3";
        setContentView(R.layout.mainsettings);

        sharedpref = getSharedPreferences(getString(R.string.sharedpref), MODE_PRIVATE);
        final SharedPreferences.Editor editor = sharedpref.edit();

        mIntelligentWorkoutSettings = findViewById(R.id.IntelligentWorkoutSettings);

        System.out.println("lock music is " + mIntelligentWorkoutSettings.lock_music+ "and lock sound is "+mIntelligentWorkoutSettings.lock_sound + "Oncreate before pref");

        mIntelligentWorkoutSettings.lock_music = !sharedpref.getBoolean(getString(R.string.music_allowance),true);
        mIntelligentWorkoutSettings.lock_sound = !sharedpref.getBoolean(getString(R.string.sound_allowance),true);

        System.out.println("lock music is " + mIntelligentWorkoutSettings.lock_music+ "and lock sound is "+mIntelligentWorkoutSettings.lock_sound + "Oncreate after pref");

        mIntelligentWorkoutSettings.setVisibility(View.VISIBLE);



        mIntelligentWorkoutSettings.setEventListener(new IntelligentWorkoutSettings.IMyEventListener() {
            public void onSoundPressed(){
                myService.setAllow_sound(mIntelligentWorkoutSettings.lock_sound);

            }

            @Override
            public void onMenuPressed() {
                onBackPressed();
            }

            public void onMusicPressed(){
                myService.setAllow_music(mIntelligentWorkoutSettings.lock_music);
                if(!mIntelligentWorkoutSettings.lock_music) {
                    myService.playMusic();
                    editor.putBoolean(getString(R.string.music_allowance),!mIntelligentWorkoutSettings.lock_music);
                    editor.apply();
                }
                else {
                    myService.pauseMusic();
                    editor.putBoolean(getString(R.string.music_allowance),!mIntelligentWorkoutSettings.lock_music);
                    editor.apply();
                }
            }

        });
    }

    @Override
    protected void onStart() {
        super.onStart();

        sharedpref = getSharedPreferences(getString(R.string.sharedpref), MODE_PRIVATE);

        System.out.println("lock music is " + mIntelligentWorkoutSettings.lock_music+ "and lock sound is "+mIntelligentWorkoutSettings.lock_sound + "OnStart before pref");

        mIntelligentWorkoutSettings.lock_music = !sharedpref.getBoolean(getString(R.string.music_allowance),true);
        mIntelligentWorkoutSettings.lock_sound = !sharedpref.getBoolean(getString(R.string.sound_allowance),true);

        System.out.println("lock music is " + mIntelligentWorkoutSettings.lock_music+ "and lock sound is "+mIntelligentWorkoutSettings.lock_sound + "OnState after pref");

    }
}
