package com.rey.material.demo;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.util.Log;
import android.widget.Toast;

/**
 * Created by zhaoheri on 3/5/15.
 */
public class AlarmReceiver extends BroadcastReceiver {


    private Context mContext;
    public static final String VOLUME = "Vol";


    @Override
    public void onReceive(Context context, Intent intent)
    {
        // TODO Auto-generated method stub


        // here you can start an activity or service depending on your need
        // for ex you can start an activity to vibrate phone or to ring the phone

        // Show the toast  like in above screen shot
        Toast.makeText(context, "Alarm Triggered", Toast.LENGTH_LONG).show();
        mContext = context;
        int vol = intent.getIntExtra(VOLUME, 0);
        Log.d("alarm receive", "receive alarm vol = " + vol);
        AudioManager am;
        am = (AudioManager) mContext.getSystemService(Context.AUDIO_SERVICE);
        am.setStreamVolume(AudioManager.STREAM_RING, vol, 0);
    }

}
