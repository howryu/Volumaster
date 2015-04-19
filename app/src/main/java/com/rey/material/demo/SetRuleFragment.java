package com.rey.material.demo;

import android.annotation.TargetApi;
import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.rey.material.app.DatePickerDialog;
import com.rey.material.app.Dialog;
import com.rey.material.app.DialogFragment;
import com.rey.material.app.TimePickerDialog;
import com.rey.material.widget.Button;
import com.rey.material.widget.EditText;
import com.rey.material.widget.FloatingActionButton;
import com.rey.material.widget.Slider;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by howryu on 4/13/15.
 */
public class SetRuleFragment extends Fragment implements View.OnClickListener{
    private TextView startTimeText;
    private TextView endTimeText;
    private TextView tv_discrete;
    private TextView dateText;

    private Button bt_start_time_picker;
    private Button bt_end_time_picker;
    private Button bt_sumbit;
    private Button bt_date_picker;
    private Button bt_cancel;

    private Activity main;
    private MyDB myDB;

    private Rule rule;
    private static long ruleId;

    public static SetRuleFragment newInstance(){
        ruleId = -1;
        SetRuleFragment fragment = new SetRuleFragment();
        return fragment;
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.d("update", "ruleId when first created " + String.valueOf(ruleId));

        View v = inflater.inflate(R.layout.fragment_setrule, container, false);
        bt_start_time_picker = (Button)v.findViewById(R.id.button_start_time_picker);
        bt_end_time_picker = (Button)v.findViewById(R.id.button_end_time_picker);
        bt_sumbit = (Button)v.findViewById(R.id.button_submit);
        bt_date_picker = (Button)v.findViewById(R.id.button_date_picker);
        bt_cancel = (Button)v.findViewById(R.id.button_cancel);

        startTimeText = (TextView)v.findViewById(R.id.start_time_text);
        endTimeText = (TextView)v.findViewById(R.id.end_time_text);
        dateText = (TextView)v.findViewById(R.id.date_text);

        Date date = new Date();
        bt_start_time_picker.setText(DateFormat.getTimeInstance(DateFormat.SHORT).format(date));
        bt_end_time_picker.setText(DateFormat.getTimeInstance(DateFormat.SHORT).format(date));
        bt_date_picker.setText(SimpleDateFormat.getDateInstance().format(date));

        final EditText editText = (EditText)v.findViewById(R.id.textfield_with_label);
        // editText.clearComposingText();

        main = this.getActivity();

        myDB = MyDB.getInstance(container.getContext());

        bt_start_time_picker.setOnClickListener(this);
        bt_end_time_picker.setOnClickListener(this);
        bt_date_picker.setOnClickListener(this);

        // set default rule values
        DateFormat timeFormat = new SimpleDateFormat("HH:mm");
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");


        Slider sl_discrete = (Slider)v.findViewById(R.id.volume_slider);
        tv_discrete = (TextView)v.findViewById(R.id.slider_volume_text);
        //tv_discrete.setText(String.format("volume   %d", sl_discrete.getValue()));

        sl_discrete.setOnPositionChangeListener(new Slider.OnPositionChangeListener() {
            @Override
            public void onPositionChanged(Slider view, float oldPos, float newPos, int oldValue, int newValue) {
                rule.setVolume(Integer.toString(newValue));
                //tv_discrete.setText(String.format("volume   %d", newValue));
            }
        });


        if (ruleId == -1){
            bt_cancel.setVisibility(View.GONE);
            editText.getText().clear();

            Log.d("TextTitle", editText.getText().toString());

            rule = new Rule("Rule", dateFormat.format(date), timeFormat.format(date), timeFormat.format(date), "8");
            bt_sumbit.setText("submit");
            bt_sumbit.setOnClickListener(new View.OnClickListener(){
                public void onClick(View view){
//                Log.d("submit", "editText" + editText.getText().toString());
//                if (editText.getText().toString() != "")
                    rule.setTitle(editText.getText().toString());
                    myDB.insert(rule);
                    Log.d("submit", rule.getTitle());
                    Log.d("submit", rule.getDate());
                    Log.d("submit", rule.getStart_time());
                    Log.d("submit", rule.getEnd_time());
                    Log.d("submit", rule.getVolume());
                    Toast.makeText(main, "Rule Submitted", Toast.LENGTH_SHORT).show();
                }
            });

            sl_discrete.setValue(8, true);

        }
        else{
            bt_cancel.setVisibility(View.VISIBLE);
            rule = myDB.selectById(ruleId);
            editText.setText(rule.getTitle());
            Log.d("TextTitle", editText.getText().toString());


            String tmp;
            // TODO
            tmp = rule.getStart_time();
            try {
                date = timeFormat.parse(tmp);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            bt_start_time_picker.setText(DateFormat.getTimeInstance(DateFormat.SHORT).format(date));
            tmp = rule.getEnd_time();
            try {
                date = timeFormat.parse(tmp);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            bt_end_time_picker.setText(DateFormat.getTimeInstance(DateFormat.SHORT).format(date));

            tmp = rule.getDate();
            try {
                date = dateFormat.parse(tmp);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            DateFormat dateFormatShown = new SimpleDateFormat("MMM dd, yyyy");
            bt_date_picker.setText(dateFormatShown.format(date));


            bt_sumbit.setText("update");
            final long passinId = ruleId;
            bt_sumbit.setOnClickListener(new View.OnClickListener(){
                public void onClick(View view){
                    rule.setTitle( editText.getText().toString());
                    myDB.updateById(passinId, rule.getTitle(), rule.getStart_time(), rule.getEnd_time(), rule.getVolume());
                    Toast.makeText(main, "Rule Updated", Toast.LENGTH_SHORT).show();
                }
            });
            sl_discrete.setValue(Integer.parseInt(rule.getVolume()), true);
            Log.d("TextTitle", rule.getVolume().toString());

            ruleId = -1;
        }
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
                        String message = dialog.getFormattedTime(DateFormat.getTimeInstance(DateFormat.SHORT));
                        String hour;
                        String min;
                        if (dialog.getHour() < 10)
                            hour = "0" + dialog.getHour();
                        else hour = String.valueOf(dialog.getHour());
                        if (dialog.getMinute() < 10)
                            min = "0" + dialog.getMinute();
                        else min = String.valueOf(dialog.getMinute());
                        rule.setStart_time(hour + ":" + min);
                        bt_start_time_picker.setText(message);
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
                        String message = dialog.getFormattedTime(DateFormat.getTimeInstance(DateFormat.SHORT));
                        String hour;
                        String min;
                        if (dialog.getHour() < 10)
                            hour = "0" + dialog.getHour();
                        else hour = String.valueOf(dialog.getHour());
                        if (dialog.getMinute() < 10)
                            min = "0" + dialog.getMinute();
                        else min = String.valueOf(dialog.getMinute());
                        rule.setEnd_time(hour + ":" + min);
                        bt_end_time_picker.setText(message);
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
                        bt_date_picker.setText(date);

                        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                        rule.setDate(dateFormat.format(dialog.getDate()));
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

    public void setUpdateId(long id){

        this.ruleId = id;
        Log.d("update", "set " + String.valueOf(this.ruleId));
    }


}
