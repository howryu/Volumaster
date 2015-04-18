package com.rey.material.demo;

import android.annotation.TargetApi;
import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.rey.material.app.DatePickerDialog;
import com.rey.material.app.Dialog;
import com.rey.material.app.DialogFragment;
import com.rey.material.app.TimePickerDialog;
import com.rey.material.widget.Button;
import com.rey.material.widget.FloatingActionButton;
import com.rey.material.widget.Slider;

import java.text.SimpleDateFormat;
/**
 * Created by howryu on 4/13/15.
 */
public class SetRuleFragment extends Fragment implements View.OnClickListener{
    private TextView startTimeText;
    private TextView endTimeText;
    private TextView tv_discrete;
    private TextView dateText;

    private Activity main;
    private MyDB myDB;

    public static SetRuleFragment newInstance(){
        SetRuleFragment fragment = new SetRuleFragment();
        return fragment;
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_setrule, container, false);
        Button bt_start_time_picker = (Button)v.findViewById(R.id.button_start_time_picker);
        Button bt_end_time_picker = (Button)v.findViewById(R.id.button_end_time_picker);
        Button bt_sumbit = (Button)v.findViewById(R.id.button_submit);
        Button bt_date_picker = (Button)v.findViewById(R.id.button_date_picker);

        startTimeText = (TextView)v.findViewById(R.id.start_time_text);
        endTimeText = (TextView)v.findViewById(R.id.end_time_text);
        dateText = (TextView)v.findViewById(R.id.date_text);

        main = this.getActivity();

        myDB = MyDB.getInstance(container.getContext());

        bt_start_time_picker.setOnClickListener(this);
        bt_end_time_picker.setOnClickListener(this);
        bt_date_picker.setOnClickListener(this);

        bt_sumbit.setOnClickListener(new OnClickListener(){
            public void onClick(View view){
                //Rule r = new Rule("06:00", "07:00", "3");
                Log.d("DB", "Before Insert rule in setting " + myDB.select().size());
                //myDB.insert(r);
                Log.d("DB", "After Insert rule in setting " + myDB.select().size());
                Toast.makeText(main, "Rule Submitted", Toast.LENGTH_SHORT).show();
            }
        });


        Slider sl_discrete = (Slider)v.findViewById(R.id.volume_slider);
        tv_discrete = (TextView)v.findViewById(R.id.slider_volume_text);
        tv_discrete.setText(String.format("value=%d", sl_discrete.getValue()));
        sl_discrete.setOnPositionChangeListener(new Slider.OnPositionChangeListener() {
            @Override
            public void onPositionChanged(Slider view, float oldPos, float newPos, int oldValue, int newValue) {
                tv_discrete.setText(String.format("value=%d", newValue));
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

    @Override
    public void onClick(View v){
        if (v instanceof FloatingActionButton){
            FloatingActionButton bt = (FloatingActionButton)v;
            bt.setLineMorphingState((bt.getLineMorphingState() + 1) % 2, true);
        }

        Dialog.Builder builder = null;
        switch (v.getId()){
            case R.id.button_start_time_picker:
                builder = new TimePickerDialog.Builder(6, 00){
                    @Override
                    public void onPositiveActionClicked(DialogFragment fragment) {
                        TimePickerDialog dialog = (TimePickerDialog)fragment.getDialog();
                        String message = dialog.getFormattedTime(SimpleDateFormat.getTimeInstance());
                        //Toast.makeText(fragment.getDialog().getContext(), "Time is " + dialog.getFormattedTime(SimpleDateFormat.getTimeInstance()), Toast.LENGTH_SHORT).show();
                        startTimeText.setText(message);
                        super.onPositiveActionClicked(fragment);
                    }

                    @Override
                    public void onNegativeActionClicked(DialogFragment fragment) {
                        Toast.makeText(fragment.getDialog().getContext(), "Cancelled" , Toast.LENGTH_SHORT).show();
                        super.onNegativeActionClicked(fragment);
                    }
                };

                builder.positiveAction("OK")
                        .negativeAction("CANCEL");
                break;
            case R.id.button_end_time_picker:
                builder = new TimePickerDialog.Builder(6, 00){
                    @Override
                    public void onPositiveActionClicked(DialogFragment fragment) {
                        TimePickerDialog dialog = (TimePickerDialog)fragment.getDialog();
                        String message = dialog.getFormattedTime(SimpleDateFormat.getTimeInstance());
                        //Toast.makeText(fragment.getDialog().getContext(), "Time is " + dialog.getFormattedTime(SimpleDateFormat.getTimeInstance()), Toast.LENGTH_SHORT).show();
                        endTimeText.setText(message);
                        super.onPositiveActionClicked(fragment);
                    }

                    @Override
                    public void onNegativeActionClicked(DialogFragment fragment) {
                        Toast.makeText(fragment.getDialog().getContext(), "Cancelled" , Toast.LENGTH_SHORT).show();
                        super.onNegativeActionClicked(fragment);
                    }
                };

                builder.positiveAction("OK")
                        .negativeAction("CANCEL");
                break;
            case R.id.button_date_picker:
                builder = new DatePickerDialog.Builder(){
                    @Override
                    public void onPositiveActionClicked(DialogFragment fragment) {
                        DatePickerDialog dialog = (DatePickerDialog)fragment.getDialog();
                        String date = dialog.getFormattedDate(SimpleDateFormat.getDateInstance());
                        //Toast.makeText(fragment.getDialog().getContext(), "Date is " + date, Toast.LENGTH_SHORT).show();
                        dateText.setText(date);
                        super.onPositiveActionClicked(fragment);
                    }

                    @Override
                    public void onNegativeActionClicked(DialogFragment fragment) {
                        Toast.makeText(fragment.getDialog().getContext(), "Cancelled" , Toast.LENGTH_SHORT).show();
                        super.onNegativeActionClicked(fragment);
                    }
                };

                builder.positiveAction("OK")
                        .negativeAction("CANCEL");
                break;
        }
        DialogFragment fragment = DialogFragment.newInstance(builder);
        fragment.show(getFragmentManager(), null);
    }


}
