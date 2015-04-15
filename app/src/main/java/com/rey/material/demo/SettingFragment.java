package com.rey.material.demo;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;

import com.rey.material.widget.Button;
/**
 * Created by howryu on 4/13/15.
 */
public class SettingFragment extends Fragment {

    public static SettingFragment newInstance(){
        SettingFragment fragment = new SettingFragment();

        return fragment;
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_setting, container, false);

        Button sync= (Button)v.findViewById(R.id.button_sync);
        final Activity mainAc = this.getActivity();
        sync.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(mainAc, UpcomingEventsActivity.class);
                getActivity().startActivityForResult(intent, MainActivity.SYNC_CODE);
            }
        });
        return v;
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

}
